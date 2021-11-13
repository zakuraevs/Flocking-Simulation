package util

import util.Parameters.errorText

import java.awt.{Color, Dimension}
import scala.swing.{BoxPanel, Orientation}

object Exceptions {

  case class InvalidLoadingException(description: String = "", data: String) extends java.lang.Exception(description)

  def errorWindow() = {
    val errorWindow = new scala.swing.Frame() {
      resizable = false
      background = new Color(0, 0, 0)
      minimumSize = new Dimension(200, 400)

      val text = new scala.swing.TextArea {
        text = errorText
      }
      val container = new BoxPanel(Orientation.Vertical)
      container.contents += text
      contents = container
    }
    errorWindow.visible = true
  }
}
