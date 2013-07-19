name := "geo"

organization in ThisBuild := "com.bionicspirit"

version in ThisBuild := "0.1"

scalaVersion in ThisBuild := "2.10.2"

compileOrder in ThisBuild := CompileOrder.JavaThenScala

scalacOptions in ThisBuild ++= Seq(
  "-unchecked", "-deprecation", "-feature", "-target:jvm-1.6"
)

licenses in ThisBuild := Seq("MIT" -> url("http://opensource.org/licenses/MIT"))

homepage in ThisBuild := Some(url("http://github.com/bionicspirit/geo"))

resolvers ++= Seq(
  "Sonatype Groups" at "https://oss.sonatype.org/content/groups/public/",
  "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases/",
  "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases"
)

libraryDependencies ++= Seq(
  "com.maxmind.geoip" % "geoip-api" % "1.2.10",
  "org.scalatest" %% "scalatest" % "1.9.1" % "test",
  "junit" % "junit" % "4.10" % "test"
)

pomExtra in ThisBuild := (
  <scm>
    <url>git@github.com:bionicspirit/geo.git</url>
    <connection>scm:git:git@github.com:bionicspirit/geo.git</connection>
  </scm>
  <developers>
    <developer>
      <id>alex_ndc</id>
      <name>Alexandru Nedelcu</name>
      <url>http://bionicspirit.com</url>
    </developer>
  </developers>)
