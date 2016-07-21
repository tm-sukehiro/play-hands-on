name := """play-hands-on"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "anorm" % "2.4.0",
  evolutions,
  jdbc,
  cache,
  ws,
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test,
  "com.mohiva" %% "play-silhouette" % "3.0.0",
  "org.reactivemongo" %% "play2-reactivemongo" % "0.11.7.play24",
  specs2 % Test
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"
