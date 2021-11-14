package vector_tests

import org.junit.Test
import org.junit.Assert._

import scala.math._
import logic.SimulationVector

import scala.collection.mutable.ArrayBuffer

class VectorTests {

  // Testing SimulationVector.

  // Testing coordinates.
   @Test def coordsTest() = {
    var vectors = Array[SimulationVector]()
    for(i <- 0 to 100) {
      vectors = vectors :+ new SimulationVector(i, i + 10)
    }

    for(i <- 0 to 100) {
      assertEquals("x Coordinates of a vector are incorrect", i, vectors(i).xValue, 0)
      assertEquals("y Coordinates of a vector are incorrect",i + 10, vectors(i).yValue, 0)
    }
   }

   // Testing setting coordinates.
   @Test def setCoordsTest() = {
     val a = new SimulationVector(0,0)
     a.setX(4.5)
     a.setY(9.6)

     assertEquals("x Coordinates of a vector are incorrect",4.5, a.xValue, 0)
     assertEquals("y Coordinates of a vector are incorrect",9.6, a.yValue, 0)
   }

   // Testing mganitudes and normalization.
   @Test def magnitudeNormalizationTest() = {
     var vectors = Array[SimulationVector]()
     var y = 100
     for(x <- -100 to 100) {
       vectors =  vectors :+ new SimulationVector(x,y)
       y -= 1
     }

     y = 100
     var i = 0

     for(x <- -100 to 100) {
       assertEquals("magnitude of a vector is not correct",hypot(x, y), vectors(i).magnitude, 0)

       vectors(i) = vectors(i).normalize

       assertEquals("direction of a vector is not correct", atan2(y, x), atan2(vectors(i).yValue, vectors(i).xValue), 0.000001)

       if(x == 0 && y == 0 ) {
         assertEquals("magnitude of a zero vector is not zero", 0, vectors(i).magnitude, 0.000001)
       } else {
         assertEquals("magnitude of a nonzero zero vector is not one", 1, vectors(i).magnitude, 0.000001)
       }

       y -= 1
       i += 1
     }

   }

   // Testing addition, subtraction and multiplication.
   @Test def addSubMultTest() = {

     var a = new SimulationVector(5.4, -5.6)
     var vectors = Array[SimulationVector]()
     var y = 100
     for(x <- -100 to 100) {
       vectors =  vectors :+ new SimulationVector(x,y)
       y -= 1
     }

     y = 100
     var i = 0

     for(x <- -100 to 100) {
       assertEquals("incorrect x value after addition", x + 5.4, (vectors(i) + a).xValue , 0)
       assertEquals("incorrect y value after addition",y + (-5.6), (vectors(i) + a).yValue , 0)
       assertEquals("incorrect x value after subtraction", x - 5.4, (vectors(i) - a).xValue , 0)
       assertEquals("incorrect y value after subtraction", y - (-5.6), (vectors(i) - a).yValue , 0)
       assertEquals("incorrect x value after multiplication", x * 5.4, (vectors(i) * 5.4).xValue , 0)
       assertEquals("incorrect y value after multiplication", y * (-5.6), (vectors(i) * (-5.6)).yValue , 0)

       y -= 1
       i += 1
     }
   }

   // Testing distance.
   @Test def distanceTest() = {
     var  aVectors = Array[SimulationVector]()
     var  bVectors = Array[SimulationVector]()

     var y1 = 100
     for(x <- -100 to 100) {
       aVectors =  aVectors :+ new SimulationVector(x,y1)
       y1 -= 1
     }

     var y2 = 150
     for(x <- -50 to 150) {
       bVectors =  bVectors :+ new SimulationVector(x,y2)
       y2 -= 1
     }

     y1 = 100
     y2 = 150
     var i = 0
     var x2 = -50

     for(x <- -100 to 100) {
       assertEquals("incorrect distance between vectors", hypot((aVectors(i)-bVectors(i)).xValue, (aVectors(i)-bVectors(i)).yValue), aVectors(i).distance(bVectors(i)) , 0)

       y1 -= 1
       y2 -= 1
       x2 += 1
       i += 1
     }

   }

   // Testing averagePosition.
   @Test def averagePositionTest() = {
     var vectors = ArrayBuffer[SimulationVector]()
     var y = 100
     for(x <- 0 to 100) {
       vectors += new SimulationVector(x,y)
       y -= 1
     }

     assertTrue("x value of average position of all vectors in I quadrant is negative", SimulationVector.averagePosition(vectors).xValue > 0)
     assertTrue("y value of average position of all vectors in I quadrant is negative", SimulationVector.averagePosition(vectors).yValue > 0)

     vectors = ArrayBuffer[SimulationVector]()
     y = 0

     for(x <- -100 to 0) {
       vectors += new SimulationVector(x,y)
       y -= 1
     }

     assertTrue("x value of average position of all vectors in III quadrant is positive", SimulationVector.averagePosition(vectors).xValue < 0)
     assertTrue("y value of average position of all vectors in III quadrant is positive", SimulationVector.averagePosition(vectors).yValue < 0)

   }

   // Testing sumUp.
   @Test def sumUpTest() = {
     var vectors = ArrayBuffer[SimulationVector]()
     var y = 100
     for(x <- 0 to 100) {
       vectors += new SimulationVector(x,y)
       y -= 1
     }

     var result = SimulationVector.sumUp(vectors).magnitude
     var corrrectSolution = vectors.reduceLeft(_ + _).magnitude

     assertEquals("sum of vectors is incorrect", corrrectSolution, result, 0)
   }

   // Testing isZero.
   @Test def isZero() = {

     var z = new SimulationVector(0, 0)
     var notZ = new SimulationVector(1, -1)

     assertTrue("a zero vector appears as non zero", z.isZero)
     assertTrue("a non zero vector appears as zero", !notZ.isZero)
   }
}
