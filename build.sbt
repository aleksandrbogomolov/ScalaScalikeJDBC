name := "ScalaSample"

version := "1.0"

scalaVersion := "2.12.1"

libraryDependencies ++= Seq(
  "org.postgresql" % "postgresql" % "42.0.0",
  "org.scala-lang.modules" % "scala-xml_2.12" % "1.0.6",
  "org.scalikejdbc" % "scalikejdbc_2.12" % "2.5.1",
  "org.scalikejdbc" % "scalikejdbc-config_2.12" % "2.5.1"
)
