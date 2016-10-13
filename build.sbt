name := "scalongo"

version := "2.0"

lazy val `scalongo` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

routesGenerator := InjectedRoutesGenerator

libraryDependencies ++= Seq(jdbc, cache, ws, filters, specs2 % Test)

unmanagedResourceDirectories in Test <+= baseDirectory(_ / "target/web/public/test")

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

libraryDependencies ++= Seq(
  "org.mongodb.scala" %% "mongo-scala-driver" % "1.1.1",
  "org.scala-lang.modules" %% "scala-async" % "0.9.5",
  "com.github.t3hnar" % "scala-bcrypt_2.11" % "2.5",
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.0" % "test"
)
