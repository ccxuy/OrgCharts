import com.github.play2war.plugin._

name := """OrgChart"""

version := "1.0"

Play2WarPlugin.play2WarSettings

Play2WarKeys.servletVersion := "3.1"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  "be.objectify"  %% "deadbolt-java"     % "2.3.2",
  "com.feth"      %% "play-authenticate" % "0.6.8",
  // "joda-time" %% "joda-time" % "2.7",
  javaJdbc,
  javaEbean,
  cache,
  javaWs,
  javaJpa,
  // filters,
  // "org.webjars" %% "webjars-play" % "2.3.0",
  // "org.webjars" % "bootstrap" % "3.2.0",
  "com.fasterxml.jackson.core" % "jackson-core"        % "2.5.0",
  "com.fasterxml.jackson.core" % "jackson-databind"    % "2.5.0",
  "com.fasterxml.jackson.core" % "jackson-annotations" % "2.5.0",
  "org.perf4j" % "perf4j" % "0.9.16",
  "com.google.code.gson" % "gson" % "2.3.1",
  "org.hibernate" % "hibernate-entitymanager" % "4.3.0.Final",
  "org.hibernate" % "hibernate-c3p0" % "4.3.8.Final",
  "com.mchange" % "c3p0" % "0.9.2.1",
  "com.mchange" % "mchange-commons-java" % "0.2.9"
)

// resolvers += Resolver.url("Objectify Play Repository", url("http://deadbolt.ws/releases/"))(Resolver.ivyStylePatterns)