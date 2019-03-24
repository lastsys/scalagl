package com.lastsys.playing

import org.lwjgl.Version
import org.lwjgl.glfw.Callbacks._
import org.lwjgl.glfw.GLFW._
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.{GL, GL11}
import org.lwjgl.system.MemoryStack._
import org.lwjgl.system.MemoryUtil._

import scala.util.Using
import scala.util.chaining._

case class Window(value: Long) extends AnyVal

object HelloWorld {
  def run(): Unit = {
    println(s"Hello LWJGL ${Version.getVersion}!")

    init() match {
      case Left(err) => System.err.println(err)
      case Right(w) =>
        loop(w)
        // Free the window callbacks and destroy the window.
        glfwFreeCallbacks(w.value)
        glfwDestroyWindow(w.value)
    }

    // Terminate GLFW and free the error callback. There is no problem in calling glfwTerminate if
    // initialization failed.
    glfwTerminate()
    glfwSetErrorCallback(null).free()
  }

  def init(): Either[ApplicationError, Window] = {
    // Setup an error callback. The default implementation prints the error message in System.err.
    GLFWErrorCallback.createPrint(System.err).set()

    if (!glfwInit()) return Left(InitializationError("Unable to initialize GLFW"))
    glfwDefaultWindowHints()
    glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
    glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE)

    // Create the window.
    val window = Window(glfwCreateWindow(300, 300, "Hello World!", NULL, NULL))
    if (window.value == NULL) return Left(InitializationError("Failed to create the GLFW window"))

    // Setup a key callback. It is called every time a key is pressed, repeated or released.
    glfwSetKeyCallback(window.value, (_, key, _, action, _) =>
      if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
        glfwSetWindowShouldClose(window.value, true)
      })

    // Get the thread stack and push a new frame.
    Using(stackPush) { stack =>
        val pWidth = stack.mallocInt(1)   // int*
        val pHeight = stack.mallocInt(1)  // int*

        // Get the window size passed to glfwCreateWindow.
        glfwGetWindowSize(window.value, pWidth, pHeight)

        // Get the resolution of the primary monitor.
        val videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor())

        // Center the window.
        glfwSetWindowPos(window.value,
          (videoMode.width() - pWidth.get(0)) / 2,
          (videoMode.height() - pHeight.get(0)) / 2)
      }

    // Make the OpenGL context current.
    glfwMakeContextCurrent(window.value)
    // Enable v-sync.
    glfwSwapInterval(1)
    // Make the window visible.
    glfwShowWindow(window.value)
    Right(window)
  }

  def loop(window: Window): Unit = {
    // This line is critical for LWJGL's interoperation with GLFW's
    // OpenGL context, or any context that is managed externally.
    // LWJGL detects the context that is current in the current thread,
    // creates the GLCapabilities instance and makes the OpenGL
    // bindings available for use.
    GL.createCapabilities()

    GL11.glClearColor(1.0f, 0.0f, 0.0f, 0.0f)

    // Run the rendering loop until the user has attempted to close the window
    // or has pressed the ESCAPE key.
    while (!glfwWindowShouldClose(window.value)) {
      GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT)
      glfwSwapBuffers(window.value)
      glfwPollEvents()
    }
  }
}
