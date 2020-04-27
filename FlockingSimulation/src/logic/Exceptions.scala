package logic

import java.awt._
import scala.swing._
import logic.Parameters._

object Exceptions {
  case class InvalidLoadingException(description: String = "", data: String) extends java.lang.Exception(description)
  
  def errorWindow() = {
    val errorWindow = new scala.swing.Frame() {
      resizable = false
      background = new Color(0, 0, 0)
      minimumSize = new Dimension(200,400)
            
      val text = new scala.swing.TextArea {text = errorText}
      val container = new BoxPanel(Orientation.Vertical) 
      container.contents += text
      contents = container
    }
    errorWindow.visible = true
  }
}
