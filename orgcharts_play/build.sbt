import com.github.play2war.plugin._

name := """OrgCharts"""

version := "0.5"

Play2WarPlugin.play2WarSettings

Play2WarKeys.servletVersion := "3.0"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  "be.objectify"  %% "deadbolt-java"     % "2.3.2",
  "com.feth"      %% "play-authenticate" % "0.6.8",
  javaJdbc,
  javaEbean,
  cache,
  javaWs,
  javaJpa,
  // filters,
  // "org.webjars" %% "webjars-play" % "2.3.0",
  // "org.webjars" % "bootstrap" % "3.2.0",
  "com.google.code.gson" % "gson" % "2.3.1",
  "org.hibernate" % "hibernate-entitymanager" % "4.3.0.Final"
)

// resolvers += Resolver.url("Objectify Play Repository", url("http://deadbolt.ws/releases/"))(Resolver.ivyStylePatterns)