package gui

import FlockSimulationApp.{simPanel, simWorld}
import util.Parameters._
import logic.{Obstacle, SimulationVector, Vehicle}

import scala.math.{cos, sin, toRadians}
import scala.swing.{Action, BoxPanel, Color, Orientation, Separator, TextArea}
import java.awt.{Color, Dimension}

// The object responsible for the menu bar and its contents.
object MenuBar {

  // An instance of a scala menu bar with contents.
  // Each submenu uses methods of internal objects inside this MenuBar object.
  val menu: scala.swing.MenuBar = new scala.swing.MenuBar {

    // File menu.
    contents += new scala.swing.Menu("File") {
      contents += new scala.swing.MenuItem(Action("Reset") {
        FileMenu.reset()
      })
      contents += new Separator
      contents += new scala.swing.MenuItem(Action("Save") {
        FileMenu.save()
      })
      contents += new scala.swing.MenuItem(Action("Load") {
        FileMenu.load()
      })
      contents += new Separator
      contents += new scala.swing.MenuItem(Action("Exit") {
        FileMenu.exit()
      })
    }

    // Controls menu.
    contents += new scala.swing.Menu("Controls") {
      contents += new scala.swing.MenuItem(Action("Display obstacle controls") {
        ControlsMenu.showObstacleControls()
      })
      contents += new scala.swing.MenuItem(Action("Hide obstacle controls") {
        ControlsMenu.hideObstacleControls()
      })
    }

    // Help menu.
    contents += new scala.swing.Menu("Help") {
      contents += new scala.swing.MenuItem(Action("How to Use") {
        HelpMenu.howToUse()
      })
      contents += new scala.swing.MenuItem(Action("Loading file format") {
        HelpMenu.loadingFormat()
      })
    }

    // Presets menu.
    contents += new scala.swing.Menu("Presets") {
      contents += new scala.swing.MenuItem(Action("Classic") {
        PresetsMenu.classic()
      })
      contents += new scala.swing.MenuItem(Action("Hundred Random") {
        PresetsMenu.hundred()
      })
      contents += new scala.swing.MenuItem(Action("Limit") {
        PresetsMenu.limit()
      })
      contents += new scala.swing.MenuItem(Action("Wall on Wall") {
        PresetsMenu.wallOnWall()
      })
      contents += new scala.swing.MenuItem(Action("Obstacle Course") {
        PresetsMenu.obstacleCourse()
      })
      contents += new scala.swing.MenuItem(Action("Obstacle Bound") {
        PresetsMenu.obstacleBound()
      })
    }

    // Themes menu.
    contents += new scala.swing.Menu("Themes") {
      contents += new scala.swing.MenuItem(Action("Classic") {
        ThemesMenu.classicTheme()
      })
      contents += new scala.swing.MenuItem(Action("Light") {
        ThemesMenu.lightTheme()
      })
      contents += new scala.swing.MenuItem(Action("Neon") {
        ThemesMenu.neonTheme()
      })
    }
  }

  // The object that stores methods for the File submenu.
  object FileMenu {

    // Quits the app.
    def exit(): Unit = sys.exit(0)

    // Uses object IO to load a simulation state from a .txt file.
    def load(): Unit = IO.loadFile()

    // Uses object IO to save the simulation state to a .txt file.
    def save(): Unit = IO.saveFile()

    // Resets simulation to default parameters.
    def reset(): Unit = {
      simWorld.removeVehicles()
      simWorld.removeObstacles()

      FlockSimulationApp.controlsSuperPanel.detectionSlider.value = 50
      FlockSimulationApp.controlsSuperPanel.speedSlider.value = 3
      FlockSimulationApp.controlsSuperPanel.separationSlider.value = 5
      FlockSimulationApp.controlsSuperPanel.cohesionSlider.value = 4
      FlockSimulationApp.controlsSuperPanel.alignmentSlider.value = 6

      centerPull = 0
      FlockSimulationApp.controlsSuperPanel.gravityToggle.text = "Gravity: off"

      if (!FlockSimulationApp.controlsSuperPanel.obstacleControlsHidden) {
        FlockSimulationApp.controlsSuperPanel.seeAheadSlider.value = 90
        FlockSimulationApp.controlsSuperPanel.avoidanceSlider.value = 85
      }
    }
  }

  // An object that stores methods for the Controls menu.
  object ControlsMenu {

    // Makes controls of obstacle avoidance behavior visible.
    def showObstacleControls() = {
      if (FlockSimulationApp.controlsSuperPanel.obstacleControlsHidden) {
        FlockSimulationApp.controlsSuperPanel.obstacleControlsHidden = false
        FlockSimulationApp.controlsSuperPanel.slidersPanel.contents += FlockSimulationApp.controlsSuperPanel.seeAheadSlider
        FlockSimulationApp.controlsSuperPanel.slidersPanel.contents += FlockSimulationApp.controlsSuperPanel.avoidanceSlider
        FlockSimulationApp.controlsSuperPanel.labelsPanel.contents += FlockSimulationApp.controlsSuperPanel.seeAheadLabel
        FlockSimulationApp.controlsSuperPanel.labelsPanel.contents += FlockSimulationApp.controlsSuperPanel.avoidanceLabel
        FlockSimulationApp.controlsSuperPanel.buttonsPanel.contents += FlockSimulationApp.controlsSuperPanel.obstacleVehicleButton

        FlockSimulationApp.controlsSuperPanel.revalidate()
      }
    }

    // Hides controls of obstacle avoidance behavior.
    def hideObstacleControls() = {
      if (!FlockSimulationApp.controlsSuperPanel.obstacleControlsHidden) {
        FlockSimulationApp.controlsSuperPanel.obstacleControlsHidden = true
        FlockSimulationApp.controlsSuperPanel.slidersPanel.contents -= FlockSimulationApp.controlsSuperPanel.seeAheadSlider
        FlockSimulationApp.controlsSuperPanel.slidersPanel.contents -= FlockSimulationApp.controlsSuperPanel.avoidanceSlider
        FlockSimulationApp.controlsSuperPanel.labelsPanel.contents -= FlockSimulationApp.controlsSuperPanel.seeAheadLabel
        FlockSimulationApp.controlsSuperPanel.labelsPanel.contents -= FlockSimulationApp.controlsSuperPanel.avoidanceLabel
        FlockSimulationApp.controlsSuperPanel.buttonsPanel.contents -= FlockSimulationApp.controlsSuperPanel.obstacleVehicleButton

        FlockSimulationApp.controlsSuperPanel.revalidate()
      }
    }
  }

  // The object that stores methods for the Help menu.
  object HelpMenu {

    // Creates a new window with use instructions.
    // Does not stop the simulations.
    def howToUse() = {
      val helpWindow: scala.swing.Frame = new scala.swing.Frame() {
        resizable = false
        background = new Color(0, 0, 0)
        minimumSize = new Dimension(200, 400)

        val text: TextArea = new scala.swing.TextArea {
          text = helpText
        }
        val container: BoxPanel = new BoxPanel(Orientation.Vertical)
        container.contents += text
        contents = container
      }
      helpWindow.visible = true
    }

    // Creates a new window with I/O instructions.
    // Does not stop the simulations.
    def loadingFormat() = {
      val formatWindow: scala.swing.Frame = new scala.swing.Frame() {
        resizable = false
        background = new Color(0, 0, 0)
        minimumSize = new Dimension(200, 400)

        val text: TextArea = new scala.swing.TextArea {
          text = formatText
        }
        val container: BoxPanel = new BoxPanel(Orientation.Vertical)
        container.contents += text
        contents = container
      }
      formatWindow.visible = true
    }
  }

  // The object that stores methods for the Presets menu.
  // Contains presets.
  object PresetsMenu {

    // Classic preset with 3 vehicles moving in random directions.
    def classic(): Unit = {
      FileMenu.reset()

      simWorld.addVehicle(new Vehicle(Vehicle.randomPosition, Vehicle.randomVelocity, simWorld))
      simWorld.addVehicle(new Vehicle(Vehicle.randomPosition, Vehicle.randomVelocity, simWorld))
      simWorld.addVehicle(new Vehicle(Vehicle.randomPosition, Vehicle.randomVelocity, simWorld))
    }

    // Preset that creates 100 vehicles in random positions.
    def hundred(): Unit = {
      FileMenu.reset()

      for (i <- 0 to 99) {
        simWorld.addVehicle(new Vehicle(Vehicle.randomPosition, Vehicle.randomVelocity, simWorld))
      }
    }

    // Preset that creates 1000 vehicles in random positions.
    def limit(): Unit = {
      FileMenu.reset()

      for (i <- 0 to vehicleLimit) {
        simWorld.addVehicle(new Vehicle(Vehicle.randomPosition, Vehicle.randomVelocity, simWorld))
      }
    }

    // Preset that has 2 groups of vehicles moving at each other.
    def wallOnWall(): Unit = {
      FileMenu.reset()
      FlockSimulationApp.controlsSuperPanel.speedSlider.value = 6

      for (i <- 0 to height by 60) {
        simWorld.addVehicle(new Vehicle(new SimulationVector(120, i), new SimulationVector(10, 0), simWorld))
      }
      for (i <- 0 to height by 60) {
        simWorld.addVehicle(new Vehicle(new SimulationVector(width - 120, i), new SimulationVector(-10, 0), simWorld))
      }
    }

    // Preset that contains a number of preset obstacles.
    def obstacleCourse(): Unit = {
      FileMenu.reset()
      FlockSimulationApp.controlsSuperPanel.speedSlider.value = 3

      simWorld.addObstacle(new Obstacle(new SimulationVector(width / 5, height / 2 - controlsHeight / 2), 70))
      simWorld.addObstacle(new Obstacle(new SimulationVector((width / 5) * 4, height / 2 - controlsHeight / 2), 70))
      simWorld.addObstacle(new Obstacle(new SimulationVector(width / 2, height / 5 - controlsHeight / 2), 70))
      simWorld.addObstacle(new Obstacle(new SimulationVector(width / 2, (height / 5 * 4 - controlsHeight / 2)), 70))

      simWorld.addVehicle(new Vehicle(new SimulationVector(width / 2, height / 2), Vehicle.randomVelocity, simWorld))
      simWorld.addVehicle(new Vehicle(new SimulationVector(width, height), Vehicle.randomVelocity, simWorld))
      simWorld.addVehicle(new Vehicle(new SimulationVector(width, 0), Vehicle.randomVelocity, simWorld))
    }

    // Preset with a circle of bounds surrounding a vehicle.
    def obstacleBound(): Unit = {
      FileMenu.reset()

      for (theta <- 0 to 365 by 3) {
        simWorld.addObstacle(new Obstacle(new SimulationVector((width / 2) + 250 * cos(toRadians(theta)), (height / 2 - controlsHeight / 2) + 250 * sin(toRadians(theta))), 10))
      }

      simWorld.addVehicle(new Vehicle(new SimulationVector(width / 2, height / 2 - controlsHeight / 2), Vehicle.randomVelocity, simWorld))
    }
  }

  // The object that stores methods for the Themes menu.
  // Contains themes that change appearance of the GUI.
  object ThemesMenu {

    def classicTheme(): Unit = {
      VehicleColor = Color.white
      backgroundColor = Color.black
      controlsColor = Color.white
      simPanel.background = backgroundColor
      FlockSimulationApp.controlsSuperPanel.slidersPanel.background = controlsColor
      FlockSimulationApp.controlsSuperPanel.labelsPanel.background = controlsColor
      FlockSimulationApp.controlsSuperPanel.buttonsPanel.background = controlsColor
    }

    def lightTheme(): Unit = {
      VehicleColor = Color.gray
      backgroundColor = Color.white
      controlsColor = new Color(230, 230, 230)
      simPanel.background = backgroundColor
      FlockSimulationApp.controlsSuperPanel.slidersPanel.background = controlsColor
      FlockSimulationApp.controlsSuperPanel.labelsPanel.background = controlsColor
      FlockSimulationApp.controlsSuperPanel.buttonsPanel.background = controlsColor
    }

    def neonTheme(): Unit = {
      VehicleColor = Color.green
      backgroundColor = Color.black
      controlsColor = Color.green
      simPanel.background = backgroundColor
      FlockSimulationApp.controlsSuperPanel.slidersPanel.background = controlsColor
      FlockSimulationApp.controlsSuperPanel.labelsPanel.background = controlsColor
      FlockSimulationApp.controlsSuperPanel.buttonsPanel.background = controlsColor
    }
  }

}
