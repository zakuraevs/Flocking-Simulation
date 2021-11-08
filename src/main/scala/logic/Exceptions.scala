package logic

import Parameters.errorText

import scala.swing.{BoxPanel, Orientation}

import java.awt.{Color, Dimension}

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
