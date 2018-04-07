name := "scalongo"

version := "3.0"

lazy val `scalongo` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.8"

routesGenerator := InjectedRoutesGenerator

libraryDependencies ++= Seq(
  guice,
  ws,
  filters,
  specs2 % Test
)

unmanagedResourceDirectories in Test <+= baseDirectory(_ / "target/web/public/test")

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

libraryDependencies ++= Seq(
  "org.mongodb.scala" %% "mongo-scala-driver" % "2.2.1",
  "org.scala-lang.modules" %% "scala-async" % "0.9.5",
  "com.github.t3hnar" % "scala-bcrypt_2.11" % "2.5"
)
