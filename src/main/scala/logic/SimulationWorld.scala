package logic

import util.Parameters._

class SimulationWorld {

  // Vehicles and obstacles in the world.
  var vehicles: Array[Vehicle] = Array[Vehicle]()
  var obstacles: Array[Obstacle] = Array[Obstacle]()
  
  // Number of vehicles in the world.
  def numOfVehicles: Int = this.vehicles.length
  
  // Adding & removing vehicles and obstacles.
  def addVehicle(v: Vehicle): Unit = {
    if (this.numOfVehicles < vehicleLimit) {
      if (v.position.xValue <= width && v.position.xValue >= 0 && v.position.yValue <= height && v.position.yValue >= 0) {
        this.vehicles = this.vehicles :+ v
      } else {
        this.vehicles = this.vehicles :+ new Vehicle(Vehicle.randomPosition, v.velocity, this)
      } 
    }
  }

  def removeVehicles(): Unit = vehicles = Array[Vehicle]()
  
  def addObstacle(o: Obstacle): Unit = {
    this.obstacles = this.obstacles :+ o
  }
  
  def removeObstacles(): Unit = obstacles = Array[Obstacle]()
}