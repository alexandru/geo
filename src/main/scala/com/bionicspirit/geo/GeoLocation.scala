package com.bionicspirit.geo

trait GeoLocation {
  def countryAlpha2: String
  def countryAlpha3: String
  def city: Option[String]
  def countryName: Option[String]
  def latitude: Option[Float]
  def longitude: Option[Float]
  def areaCode: Option[Int]
  def postalCode: Option[String]
  def region: Option[String]
  def dmaCode: Option[Int]
}

trait GeoIPLocation extends GeoLocation {
  def address: String
  def isIPv4: Boolean
  def isIPv6: Boolean
}

case class GeoIPv4Location(
  address: String,
  countryAlpha2: String,
  countryAlpha3: String,
  city: Option[String],
  countryName: Option[String],
  latitude: Option[Float],
  longitude: Option[Float],
  areaCode: Option[Int],
  postalCode: Option[String],
  region: Option[String],
  dmaCode: Option[Int]
) extends GeoIPLocation {
  val isIPv4 = true
  val isIPv6 = false
}

case class GeoIPv6Location(
  address: String,
  countryAlpha2: String,
  countryAlpha3: String,
  city: Option[String],
  countryName: Option[String],
  latitude: Option[Float],
  longitude: Option[Float],
  areaCode: Option[Int],
  postalCode: Option[String],
  region: Option[String],
  dmaCode: Option[Int]
) extends GeoIPLocation {
  val isIPv4 = false
  val isIPv6 = true
}

