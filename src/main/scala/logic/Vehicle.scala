package logic

import util.Parameters._

import scala.collection.mutable.ArrayBuffer
import scala.util.Random

// A Class that represents a single vehicle.
class Vehicle(var position: SimulationVector, initialVelocity: SimulationVector, world: SimulationWorld) {

  // Veloctiy of the vehicle, i.e.m it's direction and speed.
  var velocity: SimulationVector = initialVelocity

  // Collection of vehicle within a radius of this one, gets updated periodically.
  private var nearbyVehicles: ArrayBuffer[Vehicle] = ArrayBuffer[Vehicle]()

  // Update the collection of nearby vehicles.
  private def updateNearbyVehicles(): Unit = {
    for(vehicle <- this.world.vehicles) {
      if(this.position.distance(vehicle.position) < detectionRadius) {
        nearbyVehicles += vehicle
      }
    }
    nearbyVehicles = nearbyVehicles.filter( _ != this)
  }

  // Moves the vehcile by adding its velocity to the position. Also handles crossing of simualtion's boundaries.
  def move() = {
    // Cross left bound.
    if(this.position.xValue > width) {
      this.position.setX(this.position.xValue % width)
    }
    // Cross bottom bound.
    else if(this.position.yValue > height-controlsHeight) {
      this.position.setY(this.position.yValue % (height-controlsHeight))
    }
    // Cross left bound.
    else if(this.position.xValue < 0) {
      this.position.setX(this.position.xValue + width)
    }
    // Cross top bound.
    else if(this.position.yValue < 0) {
       this.position.setY(this.position.yValue + (height-controlsHeight))
    }
    // Default
    else {
      this.position = new SimulationVector(
        (this.position.xValue + this.velocity.xValue),
        (this.position.yValue + this.velocity.yValue)
      )
    }
  }

  // Limits the top speed of a vehicle.
  private def limitSpeed: SimulationVector = this.velocity.normalize * topSpeed

  // Calculates vector that repulses this vehicle from nearby ones.
  private def calculateSeparation: SimulationVector = {

    var repulsionVector: SimulationVector = new SimulationVector(0,0)
    for(i <- nearbyVehicles) {
      repulsionVector += ((this.position - i.position).normalize)
    }

    repulsionVector.normalize
  }

  // Calculates the vector that attracts this vehicle to the 'center of mass' of nearby ones.
  private def calculateCohesion: SimulationVector = {

    if (this.nearbyVehicles.size == 0) {
      return new SimulationVector(0,0)
    }

    val positions: ArrayBuffer[SimulationVector] = ArrayBuffer[SimulationVector]()
    for(i <- this.nearbyVehicles) {
      positions += i.position
    }
    val centerOfMass: SimulationVector = SimulationVector.averagePosition(positions)
    val cohesionVector: SimulationVector = centerOfMass - this.position

    cohesionVector.normalize
  }

  // Calculates the vector of alignment with other vehicles.
  private def calculateAlignment: SimulationVector = {

    val velocities: ArrayBuffer[SimulationVector] = ArrayBuffer[SimulationVector]()
    for(i <- nearbyVehicles) {
      velocities += i.velocity
    }
    val averageVelocity: SimulationVector = SimulationVector.averagePosition(velocities)

    averageVelocity.normalize
  }

  // Calculates the vector that pulls the vehicle towards the center of simulation.
  // Arguably makes simulation more natural.
  private def vectorToCenter: SimulationVector = {
    val center: SimulationVector = new SimulationVector( width / 2, height / 2)
    (center - this.position).normalize
  }

  // Calculates the vector that repulses this vehicle from obstacles.
  private def calculateObstacleAvoidance: SimulationVector = {
    val ahead: SimulationVector = this.position + (this.velocity.normalize * seeAhead)
    val aheadHalf: SimulationVector = ahead * 0.5

    def vectorIntersectsObstacle(obstacle: Obstacle): Boolean = {
      ahead.distance(obstacle.position) <= obstacle.radius * obstacleRadiusMultiplier ||
      aheadHalf.distance(obstacle.position) <= obstacle.radius * obstacleRadiusMultiplier ||
      this.position.distance(obstacle.position) <= obstacle.radius * obstacleRadiusMultiplier
    }

    def findMostThreatening: Option[Obstacle] = {
      var collidableObstacles = this.world.obstacles.filter(o => vectorIntersectsObstacle(o)).sortBy(o => this.position.distance(o.position))
      var mostThreatening = collidableObstacles.headOption
      mostThreatening
    }
    
    val threat: Option[Obstacle] = findMostThreatening
    
    threat match {
      case Some(obstacle) => (ahead - obstacle.position).normalize  
      case None => new SimulationVector(0, 0)
    }
  }

  // Updates this vehicle's velocity combining the functions defined above. Updated every 10ms.
  def updateVelocity(): Unit = {

    this.nearbyVehicles = ArrayBuffer[Vehicle]()
    this.updateNearbyVehicles()

    val actingVectors: ArrayBuffer[SimulationVector] = ArrayBuffer[SimulationVector](
      this.velocity,
      this.calculateSeparation * ruleMultiplier * separationWeight,
      this.calculateCohesion * ruleMultiplier * cohesionWeight,
      this.calculateAlignment * ruleMultiplier * alignmentWeight,
      this.vectorToCenter * ruleMultiplier * centerPull
    )

    if (!this.world.obstacles.isEmpty) {
      actingVectors += this.calculateObstacleAvoidance * ruleMultiplier * avoidanceWeight
    }

    this.velocity = SimulationVector.sumUp(actingVectors).normalize * topSpeed
  }
}

// Helper object for creating random positions and velocities for new vehicles.
object Vehicle {
  private val r: Random.type = Random
  
  def randomVelocity: SimulationVector = new SimulationVector(
    scala.math.cos(r.nextInt(360).toRadians),
    scala.math.sin(r.nextInt(360).toRadians)
  )
  
  def randomPosition: SimulationVector = new SimulationVector(r.nextInt(width), r.nextInt(height))
  
  def randomVehicle(p: SimulationWorld): Vehicle = new Vehicle(randomVelocity, randomPosition, p)
}