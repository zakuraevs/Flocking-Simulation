package gui

import util.Parameters._
import logic.SimulationWorld

import java.awt._
import scala.collection.parallel.CollectionConverters.ArrayIsParallelizable

// Represents the field where vehicles are displayed and move.
class SimulationPanel(world: SimulationWorld) extends scala.swing.Panel {
  
  // Color of the panel.
  background = backgroundColor
  
  // Displaying vehicles and obstacles using Java's awt.
  override def paintComponent(g : Graphics2D): Unit = {

    // Color of the vehicles.
    g.setColor(VehicleColor)
    
    // 2 Arrays that represent x and y coordinates of the 4 corners of the polygon model of each vehicle.
    // Uses vehicle's position & velocity.
    // Utilising Scala's parallel collections here instead of an Executor.
    for(v <- this.world.vehicles.par) {
      val xCoords: Array[Int] = Array(
        v.position.xValue + vehiclePolygonFrontOffset * v.velocity.normalize.xValue,
        v.position.xValue + vehiclePolygonSideOffset * v.velocity.normalize.yValue,
        v.position.xValue - vehiclePolygonBackOffset * v.velocity.normalize.xValue,
        v.position.xValue - vehiclePolygonSideOffset * v.velocity.normalize.yValue
      ).map(_.toInt)

      val yCoords: Array[Int] = Array(
        v.position.yValue + vehiclePolygonFrontOffset * (v.velocity.normalize.yValue),
        v.position.yValue - vehiclePolygonSideOffset * (v.velocity.normalize.xValue),
        v.position.yValue - vehiclePolygonBackOffset * (v.velocity.normalize.yValue),
        v.position.yValue + vehiclePolygonSideOffset * (v.velocity.normalize.xValue)
      ).map(_.toInt)

      // Draw the vehicles.
      g.fillPolygon(xCoords, yCoords, xCoords.length)
    }
    
    // Draw the obstacles.
    for(o <- this.world.obstacles.par) {
      g.fillOval(
        o.position.xValue.toInt-o.radius,
        o.position.yValue.toInt-o.radius,
        o.radius * 2,
        o.radius * 2
      )
    }
  }
}
