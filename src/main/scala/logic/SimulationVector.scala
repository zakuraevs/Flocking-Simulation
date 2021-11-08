package logic

import scala.math._

// Represents a mathematical vector in 2D, witha  number of useful methods.
class SimulationVector(private var x: Double, private var y: Double) {
  
  // X and Y coordinates of the vector.
  def xValue: Double = this.x
  def yValue: Double = this.y

  // Sets the X and Y coordinates of a vector.
  def setX(n: Double): Unit = this.x = n
  def setY(n: Double): Unit = this.y = n

  // Calculates the magnitude of a vector.
  def magnitude: Double = sqrt(pow(this.xValue, 2) + pow(this.yValue, 2))

  // Normalzies the vector, ie preserves the direction, but scales magnitude to 1.
  def normalize: SimulationVector = {
    if(this.magnitude != 0) {
      new SimulationVector(((this.xValue)/(this.magnitude)), ((this.yValue)/(this.magnitude)))
    } else {
      new SimulationVector(0, 0)
    }
  }

  // Adds two vectos.
  def +(other: SimulationVector): SimulationVector = {
    new SimulationVector((this.xValue + other.xValue), (this.yValue + other.yValue))
  }

  // Subtracts two vectors.
  def -(other: SimulationVector): SimulationVector = {
    new SimulationVector((this.xValue - other.xValue), (this.yValue - other.yValue))
  }

  // Multiplies a vector by a scalar (scales it :) ).
  def *(scalar: Double): SimulationVector = {
    new SimulationVector((this.xValue * scalar), (this.yValue * scalar))
  }

  // Calculates distance from the 'tip' of one vector to 'tip' of another one.
  def distance(otherPos: SimulationVector): Double = {
    sqrt( pow((this.xValue - otherPos.xValue), 2) + pow((this.yValue - otherPos.yValue), 2) )
  }

  // Checks if a vector is the zero vector.
  def isZero: Boolean = this.xValue == 0 && this.yValue == 0
  
  override def toString: String = {
    "x: " + this.xValue + ", y: " + this.yValue + ", magnitude: " + this.magnitude + ", angle from x-axis: " + atan2(this.yValue, this.xValue).toDegrees
  }

}

// TODO clarify.
// Helper object with useful functionality.
object SimulationVector {
  
  // Calculates the average position of 'tips' of several vectors.
  def averagePosition(list: Array[SimulationVector]): SimulationVector = {
    if(list.length < 1) {
      new SimulationVector(0, 0)
    } else {
    
      var xSum = 0.0
      var ySum = 0.0
    
      for(i <- list) {
        xSum = xSum + i.xValue
        ySum = ySum + i.yValue
      }
      
      var xAverage = xSum/list.length
      var yAverage = ySum/list.length
      
      new SimulationVector(xAverage, yAverage)
    }  
  }

  // Sums up a number of vectos. Shortcut for + method.
  def sumUp(sequence: Array[SimulationVector]): SimulationVector = {
    var sum = new SimulationVector(0,0)
    for(i <- sequence) {
      sum = sum + i
    }
    sum
  }
  
}