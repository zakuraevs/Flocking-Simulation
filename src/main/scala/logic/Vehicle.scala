package logic

import logic.Parameters._

import scala.util.Random

// A Class that represents a single vehicle (booid, fish, bird).
class Vehicle(var position: SimulationVector, initialVelocity: SimulationVector, panel: SimulationWorld) {
  
  // Veloctiy of the vehicle, ie it's direction and speed.
  var velocity = initialVelocity

  // Collection of vehicle within a radius of this one, gets updated every 10ms.
  private var nearbyVehicles = Array[Vehicle]()
   
  // Update the collection of nearby vehicles.
  private def updateNearbyVehicles(): Unit = {
    for(i <- this.panel.vehicles) {
      if(this.position.distance(i.position) < detectionRadius) {
        nearbyVehicles = nearbyVehicles :+ i
      }
    }
    nearbyVehicles = nearbyVehicles.filter( _ != this)
  }
  
  // Moves the vehcile by adding its velocity to the position. Also handles crossing of simualtion's boundaries.
  def move() = {
    if(this.position.xValue > width) {
      this.position.setX(this.position.xValue % width)
    } else if(this.position.yValue > height-controlsHeight) { 
      this.position.setY(this.position.yValue % (height-controlsHeight))
    } else if(this.position.xValue < 0) {
      this.position.setX(this.position.xValue + width)
    } else if(this.position.yValue < 0) {
       this.position.setY(this.position.yValue + (height-controlsHeight))
    } else if(this.position.yValue < 0 && this.position.xValue > width) {
      this.position.setX(this.position.xValue + width)
      this.position.setY(this.position.yValue + (height-controlsHeight))
    }
    else {this.position = new SimulationVector((this.position.xValue + this.velocity.xValue), (this.position.yValue + this.velocity.yValue))
    }
  }
  
  // Limits the top speed of a vehicle.
  private def limitSpeed: SimulationVector = this.velocity.normalize * topSpeed
  
  // Calculates vector that repulses this vehicle from nearby ones.
  private def calculateSeparation: SimulationVector = {
    var repulsionVector = new SimulationVector(0,0)
    for(i <- nearbyVehicles) {
      repulsionVector = repulsionVector + ((this.position - i.position).normalize)
    }
    repulsionVector.normalize
  }
  
  // Calculates vector that attracts this vehicle to the 'center of mass' of nearby ones.
  private def calculateCohesion: SimulationVector = {
    if(this.nearbyVehicles.size == 0) return new SimulationVector(0,0)
    var positions = Array[SimulationVector]()
    for(i <- this.nearbyVehicles) {
      positions = positions :+ i.position
    }
    var centerOfMass = SimulationVector.averagePosition(positions)
    var cohesionVector = centerOfMass - this.position
    cohesionVector.normalize
  }
  
  // Calculates vector of alignment with other vehicles.
  private def calculateAlignment: SimulationVector = {
    var velocities = Array[SimulationVector]()
    for(i <- nearbyVehicles) {
      velocities = velocities :+ i.velocity
    }
    var averageVelocity = SimulationVector.averagePosition(velocities)
    averageVelocity.normalize
  }
  
  // Calculates vector that pulls the vehicle towards the center of simulation.
  // Arguably makes simulation more natural.
  private def vectorToCenter: SimulationVector = {
    val center = new SimulationVector(width/2,height/2)
    (center - this.position).normalize
  }
  
  // Calculates vector that repulses this vehicle from obstacles.
  private def calculateAvoidance: SimulationVector = {
    val ahead = this.position + (this.velocity.normalize * seeAhead)
    val aheadHalf = ahead * 0.5

    def vectorIntersectsObstacle(obstacle: Obstacle): Boolean = {
      ahead.distance(obstacle.position) <= obstacle.radius *1.2 || aheadHalf.distance(obstacle.position) <= obstacle.radius *1.2 || this.position.distance(obstacle.position) <= obstacle.radius *1.2
    }
    
    def findMostThreatening: Option[Obstacle] = {
      var collidableObstacles = this.panel.obstacles.filter( o => vectorIntersectsObstacle(o)).sortBy(o => this.position.distance(o.position))
      var mostThreatening = {
        if(collidableObstacles.size > 0) Some(collidableObstacles.head)
        else None
      }
      mostThreatening
    }
    
    val threat = findMostThreatening
    
    threat match {
      case Some(obstacle) => (ahead - obstacle.position).normalize  
      case None => new SimulationVector(0, 0)
    }
    
  }

  // Updates this vehicle's velocity combining the functions defined above. Updated every 10ms.
  def updateVelocity(): Unit = {
    this.nearbyVehicles = Array[Vehicle]()
    this.updateNearbyVehicles()
    var actingVectors = Array[SimulationVector]((this.velocity),(this.calculateSeparation * (0.1*separationWeight)), 
        (this.calculateCohesion * (0.1*cohesionWeight)),(this.calculateAlignment * (0.1*alignmentWeight)), 
        this.vectorToCenter * 0.1 * (centerPull), this.calculateAvoidance * (0.1* avoidanceForce)) 
    this.velocity = SimulationVector.sumUp(actingVectors).normalize * topSpeed
  }
}

// Helper object for creating random positions and velocities for new vehicles.
object Vehicle {
  private val r = Random
  
  def randomVelocity: SimulationVector = new SimulationVector(scala.math.cos(r.nextInt(360).toRadians), scala.math.sin(r.nextInt(360).toRadians))
  
  def randomPosition: SimulationVector = new SimulationVector(r.nextInt(width), r.nextInt(height))
  
  def randomVehicle(p: SimulationWorld): Vehicle = new Vehicle(randomVelocity, randomPosition, p)
}