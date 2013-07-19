package com.bionicspirit.geo

import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class GeoLiteCitySuite extends FunSuite {
  test("lookup IPv4") {
    val geoip = GeoIP()
    val location = geoip.searchIP("188.26.33.60")

    assert(location.isDefined)

    val loc = location.get

    assert(loc.countryAlpha2 === "ro")
    assert(loc.countryAlpha3 === "rou")
    assert(loc.city === Some("Bucharest"))
    assert(loc.countryName === Some("Romania"))
  }

  test("lookup IPv6") {
    val geoip = GeoIP()
    val location = geoip.searchIP("2600:3c03::f03c:91ff:fe70:5b6f")

    assert(location.isDefined)
    val loc = location.get

    assert(loc.countryAlpha2 === "us")
    assert(loc.countryAlpha3 === "usa")
  }
}
