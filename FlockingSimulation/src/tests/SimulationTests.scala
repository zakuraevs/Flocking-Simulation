package tests

import logic._
import org.junit.Test
import org.junit.Assert._
import scala.math._
import logic.SimulationVector
import logic.Parameters._
import logic.SimulationWorld


class SimulationTests {
  
  
  //Testing adding and clearing vehicles
  @Test def clearTest() = {
    
    var w = new SimulationWorld
    
    //vehciles have been added
    for(x <- 0 to 100 ) {
      w.addVehicle(new Vehicle(Vehicle.randomPosition, Vehicle.randomVelocity, w))
    }
    assertEquals("a vehicle is missing from the panel", w.vehicles.size, 101, 0)
    
    //vehicles have been removed
    w.removeVehicles()
    assertEquals("a vehicle has not been removed from the panel", w.vehicles.size, 0, 0)
    
    //vehcile added beyond boundaries
    try {
      w.addVehicle(new Vehicle(new SimulationVector(width * 2, height * 2), Vehicle.randomVelocity, w))
    } catch {
      case e: Exception => assertEquals(e.getMessage, "Vehicle added outside boundaries of simulation: " + new SimulationVector(width * 2, height * 2))
      case _: Throwable => fail
    }
    w.vehicles.foreach(_.updateVelocity())
    w.vehicles.foreach(_.move())
    assertEquals("a vehicle has been added outside the panel", w.vehicles.size, 1, 0)
    
    
  }
  
  
  //Testing nearby vehciles
  @Test def nearbyVehiclesTest() = {
    
    var w = new SimulationWorld
    var zero = new SimulationVector(0, 0)
    var y = 500
    
    for(x <- 0 to 1000 by detectionRadius - 1) {
      w.addVehicle(new Vehicle(new SimulationVector(x, y), Vehicle.randomVelocity, w))
    }
    
   w.vehicles.foreach(_.updateVelocity())
   
   assertTrue("a velocity turned out to be zero", w.vehicles.forall( !_.velocity.isZero) )
   
  }
  
  
  //Testing vehicle movement in a straight line
  @Test def movementTest() = {
    centerPull = 0
    
    var w = new SimulationWorld
    var v = new Vehicle(new SimulationVector(width/2, height/2), new SimulationVector(5, 0), w)
    
    w.addVehicle(v)
    
    var initialPositions = v.position
    
    for(i <- 0 to 1000) {
      v.updateVelocity()
      v.move()
      assertTrue("vehicle did not move: " + v.position, v.position != initialPositions )
      assertTrue("vehicle moved across y axis: " + v.position, v.position.yValue == height/2)
    }   
  }
  
  
  //Testing separation
  @Test def separationTest() = {
    
    separationWeight = 100
    cohesionWeight = 0
    alignmentWeight = 0
    
    var w = new SimulationWorld
    
    var vehicle1 = new Vehicle(new SimulationVector(width/2 - 5, height/2), Vehicle.randomVelocity, w)
    var vehicle2 =new Vehicle(new SimulationVector(width/2 + 5, height/2), Vehicle.randomVelocity, w)
    
    w.addVehicle(vehicle1)
    w.addVehicle(vehicle2)
    
    var initialDistance = vehicle1.position.distance(vehicle2.position)
    
    for(i <- 0 to 10) {
      vehicle1.updateVelocity()
      vehicle2.updateVelocity()
    
      vehicle1.move()
      vehicle2.move()
    }  
    
    var finalDistance = vehicle1.position.distance(vehicle2.position)
    
    assertTrue("vehicles did not move apart. vehicle1 position " + vehicle1.position + ", vehicle2 position: " + vehicle2.position, initialDistance < finalDistance)
    
  }
  
  //Testing cohesion
  @Test def cohesionTest() = {
    
    detectionRadius = 300
    separationWeight = 0
    cohesionWeight = 100
    alignmentWeight = 0
    
    var w = new SimulationWorld
    
    var vehicle1 = new Vehicle(new SimulationVector(width/2 - 100, height/2), Vehicle.randomVelocity, w)
    var vehicle2 =new Vehicle(new SimulationVector(width/2 + 100, height/2), Vehicle.randomVelocity, w)
    
    w.addVehicle(vehicle1)
    w.addVehicle(vehicle2)
    
    var initialDistance = vehicle1.position.distance(vehicle2.position)
    
    for(i <- 0 to 10) {
      vehicle1.updateVelocity()
      vehicle2.updateVelocity()
    
      vehicle1.move()
      vehicle2.move()
    }
    var finalDistance = vehicle1.position.distance(vehicle2.position)
    
    assertTrue("vehicles did not group together. distance between vehicles: " + vehicle1.position.distance(vehicle2.position), initialDistance > finalDistance)
    
  }
  
  //Testing alignment
  @Test def alignmentTest() = {
    
    detectionRadius = 500
    separationWeight = 0
    cohesionWeight = 0
    alignmentWeight = 100
    
    var w = new SimulationWorld
    
    var vehicle1 = new Vehicle(new SimulationVector(width/2 - 100, height/2), Vehicle.randomVelocity, w)
    var vehicle2 =new Vehicle(new SimulationVector(width/2 + 100, height/2), Vehicle.randomVelocity, w)
    
    w.addVehicle(vehicle1)
    w.addVehicle(vehicle2)
    
    
      vehicle1.updateVelocity()
      vehicle2.updateVelocity()
    
      
      val theta1 = atan2(vehicle1.velocity.yValue, vehicle1.velocity.xValue).toDegrees
      val theta2 = atan2(vehicle2.velocity.yValue, vehicle2.velocity.xValue).toDegrees
      
      assertEquals(" Vehicles' velocities faced too far in different directions: vehicle 1: " + theta1 + " vehicle 2: " + theta2, theta1, theta2, 15)
  }
  
  
  //Testing top speed
  @Test def topSpeedTest() = {
    
    topSpeed = 5
    
    var w = new SimulationWorld
    
    for(x <- 0 to 100 ) {
      w.addVehicle(new Vehicle(Vehicle.randomPosition, Vehicle.randomVelocity, w))
    }
    
    for(i <- 0 to 10) {
      w.vehicles.foreach(_.updateVelocity())
      w.vehicles.foreach(_.move())
    }
    
    for(vehicle <- w.vehicles) {
      assertTrue("a vehicle has exceeded top speed: " + vehicle.velocity.magnitude, vehicle.velocity.magnitude <= topSpeed + 0.0001)
    }

  }
  
  
  //Testing crossing boundaries
  @Test def boundariesTest() = {
    
    var w = new SimulationWorld
    
    var vehicle = new Vehicle(new SimulationVector(width - 10, height/2), new SimulationVector(5,0), w)
    w.addVehicle(vehicle)
    vehicle.updateVelocity()
    vehicle.move()
    assertTrue("vehicle did not teleport across x axis correctly - vehicle's position is: " + vehicle.position, vehicle.position.xValue < width)
    
    w.removeVehicles()
    vehicle = new Vehicle(new SimulationVector(10, height/2), new SimulationVector(-5,0), w)
    w.addVehicle(vehicle)
    vehicle.updateVelocity()
    vehicle.move()
    assertTrue("vehicle did not teleport across x axis correctly - vehicle's position is: " + vehicle.position, vehicle.position.xValue > 0)
    
    w.removeVehicles()
    vehicle = new Vehicle(new SimulationVector(width/2, height-10), new SimulationVector(0,5), w)
    w.addVehicle(vehicle)
    vehicle.updateVelocity()
    vehicle.move()
    assertTrue("vehicle did not teleport across y axis correctly - vehicle's position is: " + vehicle.position, vehicle.position.yValue < height)
   
    w.removeVehicles()
    vehicle = new Vehicle(new SimulationVector(width/2, 10), new SimulationVector(0,-5), w)
    w.addVehicle(vehicle)
    vehicle.updateVelocity()
    vehicle.move()
    assertTrue("vehicle did not teleport across y axis correctly - vehicle's position is: " + vehicle.position, vehicle.position.yValue < height)
  }
  
  
  
  
}