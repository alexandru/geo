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

case class GeoIPv4Location(
  ipv4: String,
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
) extends GeoLocation

case class GeoIPv6Location(
  ipv6: String,
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
) extends GeoLocation

