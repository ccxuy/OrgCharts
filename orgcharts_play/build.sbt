import com.github.play2war.plugin._

name := """OrgCharts"""

version := "0.5"

Play2WarPlugin.play2WarSettings

Play2WarKeys.servletVersion := "3.0"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs,
  javaJpa,
  "com.google.code.gson" % "gson" % "2.3.1",
  "org.hibernate" % "hibernate-entitymanager" % "4.3.0.Final"
)
