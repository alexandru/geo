package com.bionicspirit.geo

import com.maxmind.geoip.LookupService
import java.net.{UnknownHostException, Inet4Address, Inet6Address, InetAddress}
import java.io._
import java.util.zip.GZIPInputStream

trait GeoIP {
  def searchIPv4(address: InetAddress): Option[GeoIPv4Location]
  def searchIPv4(ip: String): Option[GeoIPv4Location]
  def searchIPv6(address: InetAddress): Option[GeoIPv6Location]
  def searchIPv6(ip: String): Option[GeoIPv6Location]
  def searchIP(address: InetAddress): Option[GeoLocation]
  def searchIP(ip: String): Option[GeoLocation]
}

object GeoIP {
  class Implementation(ipv4Service: LookupService, ipv6Service: Option[LookupService]) extends GeoIP {
    def searchIPv4(address: InetAddress): Option[GeoIPv4Location] =
      if (address.isInstanceOf[Inet4Address])
        searchIPv4(address.getHostAddress)
      else
        throw new IllegalArgumentException("Invalid IPv4 address")

    def searchIPv4(ip: String): Option[GeoIPv4Location] = ip match {
      case IPv4Format() =>
        val locOpt = Option(ipv4Service.getLocation(ip))
        for (loc <- locOpt; alpha3 <- countries.alpha2ToAlpha3.get(loc.countryCode.toLowerCase)) yield
          GeoIPv4Location(
            ipv4 = ip,
            countryAlpha2 = loc.countryCode.toLowerCase,
            countryAlpha3 = alpha3.toLowerCase,
            countryName = Option(loc.countryName),
            city = Option(loc.city),
            latitude = Option(loc.latitude),
            longitude = Option(loc.longitude),
            areaCode = Option(loc.area_code),
            postalCode = Option(loc.postalCode),
            region = Option(loc.region),
            dmaCode = Option(loc.dma_code)
          )
      case _ =>
        throw new IllegalArgumentException("Invalid IPv4 argument: " + ip)
    }

    def searchIPv6(address: InetAddress): Option[GeoIPv6Location] =
      if (!address.isInstanceOf[Inet6Address])
        throw new IllegalArgumentException("Invalid IPv6 address")
      else
        searchIPv6(address.getHostAddress)

    def searchIPv6(ip: String): Option[GeoIPv6Location] =
      for (s <- ipv6Service; loc <- Option(s.getLocationV6(ip)); alpha3 <- countries.alpha2ToAlpha3.get(loc.countryCode.toLowerCase)) yield
        GeoIPv6Location(
          ipv6 = ip,
          countryAlpha2 = loc.countryCode.toLowerCase,
          countryAlpha3 = alpha3.toLowerCase,
          countryName = Option(loc.countryName),
          city = Option(loc.city),
          latitude = Option(loc.latitude),
          longitude = Option(loc.longitude),
          areaCode = Option(loc.area_code),
          postalCode = Option(loc.postalCode),
          region = Option(loc.region),
          dmaCode = Option(loc.dma_code)
        )

    def searchIP(address: InetAddress): Option[GeoLocation] =
      try
        if (address.isInstanceOf[Inet6Address])
          searchIPv6(address)
        else
          searchIPv4(address)
      catch {
        case ex: IllegalArgumentException =>
          None
      }

    def searchIP(ip: String): Option[GeoLocation] = ip match {
      case IPv4Format() =>
        searchIPv4(ip)
      case _ =>
        try
          searchIP(InetAddress.getByName(ip))
        catch {
          case _:UnknownHostException =>
            None
        }
    }

    private[this] val IPv4Format = """^\d{1,3}[.]\d{1,3}[.]\d{1,3}[.]\d{1,3}$""".r
  }

  def apply(databaseIPv4: File, databaseIPv6: Option[File]): GeoIP = {
    val ipv4Service = new LookupService(databaseIPv4, LookupService.GEOIP_INDEX_CACHE)
    val ipv6Service = databaseIPv6.map(file => new LookupService(file, LookupService.GEOIP_INDEX_CACHE))
    new Implementation(ipv4Service, ipv6Service)
  }

  def apply(): GeoIP = {
    def using[T <: Closeable, U](h: T)(cb: T => U) =
      try cb(h) finally h.close()

    def copyBytes(in: InputStream, out: OutputStream) {
      val buf = new Array[Byte](4000)
      var bytesRead = 0

      while (bytesRead >= 0) {
        bytesRead = in.read(buf)
        if (bytesRead > 0)
          out.write(buf, 0, bytesRead)
      }
    }

    using (new GZIPInputStream(getClass.getResourceAsStream("/com/bionicspirit/geo/GeoLiteCity.dat.gz"))) { inIPv4 =>
      using (new GZIPInputStream(getClass.getResourceAsStream("/com/bionicspirit/geo/GeoLiteCityv6.dat.gz"))) { inIPv6 =>
        val fileIPv4 = File.createTempFile("GeoLiteCity", ".dat.gz")
        val fileIPv6 = File.createTempFile("GeoLiteCityV6", ".dat.gz")
        fileIPv4.deleteOnExit()
        fileIPv6.deleteOnExit()

        using (new FileOutputStream(fileIPv4)) { out4 =>
          using (new FileOutputStream(fileIPv6)) { out6 =>
            copyBytes(inIPv4, out4)
            copyBytes(inIPv6, out6)
          }
        }

        apply(fileIPv4, Some(fileIPv6))
      }
    }
  }
}
