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
  "com.mohiva" %% "play-silhouette" % "4.0.0",
  "org.reactivemongo" %% "play2-reactivemongo" % "0.11.14",
  specs2 % Test,
  "net.ceedubs" %% "ficus" % "1.1.2",
  "net.codingwell" %% "scala-guice" % "4.0.0",
  "com.typesafe.play" %% "play-mailer" % "5.0.0",
  "org.webjars" %% "webjars-play" % "2.5.0",
  "com.adrianhurt" %% "play-bootstrap" % "1.0-P25-B3",
  "com.mohiva" %% "play-silhouette-testkit" % "3.0.0" % "test",
  filters,
  "com.mohiva" %% "play-silhouette-password-bcrypt" % "4.0.0",
  "com.mohiva" %% "play-silhouette-persistence" % "4.0.0"
)

resolvers += Resolver.jcenterRepo
resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"
resolvers += "Atlassian Releases" at "https://maven.atlassian.com/public/"

