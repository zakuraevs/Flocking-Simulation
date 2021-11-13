package gui

import FlockSimulationApp.simWorld
import util.Exceptions.InvalidLoadingException
import util.Parameters._
import logic.{SimulationVector, Vehicle}
import util.Exceptions

import scala.io.Source
import scala.swing.FileChooser

// The object responsible for handling laoding and saving of files.
object IO {
  // Loading a simulation state from a .txt file.
  def loadFile(): Unit = {

    try {
      // File chooser in a new window to select file to be loaded.
      val chooser = new FileChooser
      if (chooser.showOpenDialog(null) == FileChooser.Result.Approve) {

        // Reading the file line by line, splitting each line at spaces.
        // The result is data, which is an array[each line] of arrays[word in each line].
        val source = Source.fromFile(chooser.selectedFile)
        val lines = source.getLines()
        val data = lines.map(line => line.split(" ")).toArray

        source.close()

        // Calls the method to reset the simulation to the default state.
        gui.MenuBar.FileMenu.reset()

        // Changing parameter values to loaded values.
        topSpeed = {
          if (data(0)(1).toInt < 1) {
            throw new InvalidLoadingException("Invalid speed inside file: ", data(0)(1).toString)
          }
          else data(0)(1).toInt
        }
        detectionRadius = {
          if (data(1)(1).toInt < 1) {
            throw new InvalidLoadingException("Invalid detection radius inside file: ", data(1)(1).toString)
          }
          else data(1)(1).toInt
        }
        centerPull = {
          if (data(2)(1).toInt < 0) {
            throw new InvalidLoadingException("Invalid (negative) center pull: ", data(2)(1).toString)
          }
          else data(2)(1).toInt
        }
        separationWeight = {
          if (data(3)(1).toInt < 0) {
            throw new InvalidLoadingException("Invalid (negative) separation weight: ", data(3)(1).toString)
          }
          else data(3)(1).toInt
        }
        cohesionWeight = {
          if (data(4)(1).toInt < 0) {
            throw new InvalidLoadingException("Invalid (negative) cohesion weight: ", data(4)(1).toString)
          }
          else data(4)(1).toInt
        }
        alignmentWeight = {
          if (data(5)(1).toInt < 0) {
            throw new InvalidLoadingException("Invalid (negative) alignment weight: ", data(5)(1).toString)
          }
          else data(5)(1).toInt
        }

        // Creating Array of pairs, each pair containing x and y coords for a loaded vehicle.
        val vehicleData = data(6).tail.map(_.filterNot(Set(',', ';', '(', ')')(_))).map(_.toDouble)
        val vehicleDataGrouped = vehicleData.grouped(2).toArray
        val vehicleCoords = vehicleDataGrouped.zipWithIndex.filter(_._2 % 2 == 0).map(_._1)
        val vehicleVelocities = vehicleDataGrouped.zipWithIndex.filter(_._2 % 2 != 0).map(_._1)

        // Changing labels to loaded values.
        FlockSimulationApp.controlsSuperPanel.detectionLabel.text = "Detection radius: " + detectionRadius
        FlockSimulationApp.controlsSuperPanel.speedLabel.text = "Speed: " + topSpeed
        FlockSimulationApp.controlsSuperPanel.separationLabel.text = "Separation: " + separationWeight
        FlockSimulationApp.controlsSuperPanel.cohesiobLabel.text = "Cohesion: " + cohesionWeight
        FlockSimulationApp.controlsSuperPanel.alignmentLabel.text = "Alignment: " + alignmentWeight

        if (centerPull > 0) {
          FlockSimulationApp.controlsSuperPanel.gravityToggle.text = "Gravity: on"
        } else if (centerPull == 0) {
          FlockSimulationApp.controlsSuperPanel.gravityToggle.text = "Gravity: off"
        }

        // Chaning slider and toggle values to loaded values.
        FlockSimulationApp.controlsSuperPanel.detectionSlider.value = detectionRadius
        FlockSimulationApp.controlsSuperPanel.speedSlider.value = topSpeed.toInt
        FlockSimulationApp.controlsSuperPanel.separationSlider.value = separationWeight.toInt
        FlockSimulationApp.controlsSuperPanel.cohesionSlider.value = cohesionWeight.toInt
        FlockSimulationApp.controlsSuperPanel.alignmentSlider.value = alignmentWeight.toInt

        for (i <- 0 to vehicleCoords.size - 1) {
          simWorld.addVehicle(new Vehicle(new SimulationVector(vehicleCoords(i)(0), vehicleCoords(i)(1)), new SimulationVector(vehicleVelocities(i)(0), vehicleVelocities(i)(1)), simWorld))
        }
      }

    } catch {
      case e: InvalidLoadingException => {
        println(e.description + e.data)
        println("""Incorrect file format. Refer to 'Help' for format details. ¯\_(ツ)_/¯""")
        Exceptions.errorWindow()
      }
      case _: Throwable => {
        println("""Incorrect file format. Refer to 'Help' for format details. ¯\_(ツ)_/¯""")
        Exceptions.errorWindow()
      }
    }
  }

  // Saving a simulation state to a .txt file.
  def saveFile(): Unit = {

    val chooser = new FileChooser

    if (chooser.showSaveDialog(null) == FileChooser.Result.Approve) {

      // Coordinates of vehicles in simulation put into arrays.
      val vehicleXes = simWorld.vehicles.map(_.position.xValue).toArray
      val vehicleYs = simWorld.vehicles.map(_.position.yValue).toArray
      val zippedCoords = vehicleXes.zip(vehicleYs)

      val vehicleVelocityXes = simWorld.vehicles.map(_.velocity.xValue).toArray
      val vehicleVelocityYss = simWorld.vehicles.map(_.velocity.yValue).toArray
      val zippedVelocities = vehicleVelocityXes.zip(vehicleVelocityYss)

      // Turning these into a string.
      var vehicleCoordsStr = ""
      for (i <- 0 to zippedCoords.size - 1) {
        vehicleCoordsStr = vehicleCoordsStr + "(" + zippedCoords(i)._1
        vehicleCoordsStr = vehicleCoordsStr + " "
        vehicleCoordsStr = vehicleCoordsStr + zippedCoords(i)._2
        vehicleCoordsStr = vehicleCoordsStr + ", "
        vehicleCoordsStr = vehicleCoordsStr + zippedVelocities(i)._2
        vehicleCoordsStr = vehicleCoordsStr + " "
        vehicleCoordsStr = vehicleCoordsStr + zippedVelocities(i)._1
        vehicleCoordsStr = vehicleCoordsStr + "); "
      }

      // Making the whole text of the file to be saved.
      val text = ("speed: " + topSpeed + "\n"
        + "detectionRadius: " + detectionRadius + "\n"
        + "centerPull: " + centerPull + "\n"
        + "separationWeight: " + separationWeight.toInt + "\n"
        + "cohesionWeight: " + cohesionWeight.toInt + "\n"
        + "alignmentWeight: " + alignmentWeight.toInt + "\n"
        + "Vehicles: " + vehicleCoordsStr)

      // Writing text to the file.
      val pw = new java.io.PrintWriter(chooser.selectedFile)
      pw.print(text)
      pw.close()
    }
  }
}
