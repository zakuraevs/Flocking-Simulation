package gui

import logic.Parameters._
import logic.SimulationWorld

import java.awt._

// Represents the field where vehicles are displayed and move.
class SimulationPanel(world: SimulationWorld) extends scala.swing.Panel {
  
  // Color of the panel.
  background = backgroundColor
  
  // Displaying vehicles and obstacles using Java's awt.
  override def paintComponent(g : Graphics2D): Unit = {
    
    // Color of vehicles.
    g.setColor(VehicleColor)
    
    // 2 Arrays that represent x and y coordinates of the 4 corners of the polygon model of each vehicle.
    // Uses vehicle's position & velocity.
    for(v <- this.world.vehicles) {
      val xCoords = Array(v.position.xValue+18*(v.velocity.normalize.xValue), v.position.xValue+7*(v.velocity.normalize.yValue), v.position.xValue-4*(v.velocity.normalize.xValue), v.position.xValue- 7*(v.velocity.normalize.yValue)).map(_.toInt)
      val yCoords = Array(v.position.yValue+18*(v.velocity.normalize.yValue), v.position.yValue-7*(v.velocity.normalize.xValue), v.position.yValue-4*(v.velocity.normalize.yValue), v.position.yValue+ 7*(v.velocity.normalize.xValue)).map(_.toInt)
      // Draw the vehicles.
      g.fillPolygon(xCoords, yCoords, xCoords.length)
    }
    
    // Draw the obstacles.
    for(o <- this.world.obstacles) {
      g.fillOval(o.position.xValue.toInt-o.radius, o.position.yValue.toInt-o.radius, o.radius * 2, o.radius * 2)
    }
  }
}