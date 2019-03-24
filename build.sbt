val lwjglVersion = "3.2.1"
val lwjglNatives = "natives-windows"
val scalaTestVersion = "3.0.5"

lazy val app = (project in file("."))
  .settings(
    name := "Playing Around",
    version := "0.1",
    scalaVersion := "2.13.0-M5",
    libraryDependencies ++= Seq(
      "org.lwjgl" % "lwjgl" % lwjglVersion,
      "org.lwjgl" % "lwjgl" % lwjglVersion classifier lwjglNatives,

      "org.lwjgl" % "lwjgl-assimp" % lwjglVersion,
      "org.lwjgl" % "lwjgl-assimp" % lwjglVersion classifier lwjglNatives,

      "org.lwjgl" % "lwjgl-glfw" % lwjglVersion,
      "org.lwjgl" % "lwjgl-glfw" % lwjglVersion classifier lwjglNatives,

      "org.lwjgl" % "lwjgl-openal" % lwjglVersion,
      "org.lwjgl" % "lwjgl-openal" % lwjglVersion classifier lwjglNatives,

      "org.lwjgl" % "lwjgl-opengl" % lwjglVersion,
      "org.lwjgl" % "lwjgl-opengl" % lwjglVersion classifier lwjglNatives,

      "org.lwjgl" % "lwjgl-stb" % lwjglVersion,
      "org.lwjgl" % "lwjgl-stb" % lwjglVersion classifier lwjglNatives

//      "org.scalatest" %% "scalatest" % scalaTestVersion % Test
    )
  )
