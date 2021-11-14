package gui

import gui.FlockSimulationApp.{simPanel, simWorld}
import util.Parameters._
import logic.{Obstacle, SimulationVector, Vehicle}

import scala.swing._
import scala.swing.event._

// Represnts the controls bar at the bottom of the GUI.
class ControlsPanel(orientation: scala.swing.Orientation.Value) extends scala.swing.BoxPanel(orientation) {
  
  minimumSize = new Dimension(width,controlsHeight)
  maximumSize = new Dimension(width,controlsHeight)
  
  var obstacleControlsHidden: Boolean = true
  
  // Supercontainer for controls.
  val verticalSuperPanel: BoxPanel = new BoxPanel(Orientation.Vertical){
    preferredSize  = new Dimension(width,150)
  }
  
  // Sliders to control the simulation.
  val slidersPanel: BoxPanel = new BoxPanel(Orientation.Horizontal) {
    background = controlsColor
    preferredSize = new Dimension(width-50,60)
  }
  
  // Labels to display parameter values.
  val labelsPanel: GridPanel = new GridPanel(1,4) {
    background = controlsColor
    preferredSize = new Dimension(width,60)
  }
  
  // Buttons for actions.
  val buttonsPanel: GridPanel = new GridPanel(2,2) {
    background = controlsColor
  }
  
  // Labels that display the values of parameters.
  val numOfVehiclesLabel: Label  = new scala.swing.Label { text = simWorld.numOfVehicles + " out of " + vehicleLimit }
  val detectionLabel: Label      = new scala.swing.Label { text = "Detection radius: " + detectionRadius.toString }
  val speedLabel: Label          = new scala.swing.Label { text = "Speed: " + topSpeed.toString }
  val separationLabel: Label     = new scala.swing.Label { text = "Separation: " + separationWeight.toString }
  val cohesiobLabel: Label       = new scala.swing.Label { text = "Cohesion: " + cohesionWeight.toString }
  val alignmentLabel: Label      = new scala.swing.Label { text = "Alignment: " + alignmentWeight.toString }
  val seeAheadLabel: Label       = new scala.swing.Label { text = "See-ahead: " + seeAhead.toString }
  val avoidanceLabel: Label      = new scala.swing.Label { text = "Avoidance: " + avoidanceWeight.toString }
  val ruleMultiplierLabel: Label = new scala.swing.Label { text = "Rule Multiplier: " + (ruleMultiplier * 10).toInt.toString }

  // Sliders that will go inside controlsPanel.
  val detectionSlider: Slider = new Slider {
    min = sliderParameters("detectionSlider")._1
    max = sliderParameters("detectionSlider")._2
    value = detectionRadius
  }
  
  val speedSlider: Slider = new Slider {
    min = sliderParameters("speedSlider")._1
    max = sliderParameters("speedSlider")._2
    value = topSpeed
  }
  
  val separationSlider: Slider = new Slider {
    min = sliderParameters("separationSlider")._1
    max = sliderParameters("separationSlider")._2
    value = separationWeight
  }
  
  val cohesionSlider: Slider = new Slider {
    min = sliderParameters("cohesionSlider")._1
    max = sliderParameters("cohesionSlider")._2
    value = cohesionWeight
  }
  
  val alignmentSlider: Slider = new Slider {
    min = sliderParameters("alignmentSlider")._1
    max = sliderParameters("alignmentSlider")._2
    value = alignmentWeight
  }
  
  val seeAheadSlider: Slider = new Slider {
    min = sliderParameters("seeAheadSlider")._1
    max = sliderParameters("seeAheadSlider")._2
    value = seeAhead.toInt
  }
  
  val avoidanceSlider: Slider = new Slider {
    min = sliderParameters("avoidanceSlider")._1
    max = sliderParameters("avoidanceSlider")._2
    value = avoidanceWeight
  }

  val ruleMultiplierSlider: Slider = new Slider {
    min = sliderParameters("ruleMultiplierSlider")._1
    max = sliderParameters("ruleMultiplierSlider")._2
    value = (ruleMultiplier * 10).toInt
  }
  
  // Buttons that go inside buttonsPanel.
  val gravityToggle: Button = new scala.swing.Button {
    text = gravityToggleOnText
  }
  
  val clearVehiclesButton: Button = new scala.swing.Button {
    text = clearVehiclesButtonText
  }
  
  val obstacleVehicleButton: Button = new scala.swing.Button {
    text = obstacleVehicleButtonTextVehicles
  }
  
  // Listening to controls.
  FlockSimulationApp.listenTo(detectionSlider)
  FlockSimulationApp.listenTo(speedSlider)
  FlockSimulationApp.listenTo(separationSlider)
  FlockSimulationApp.listenTo(cohesionSlider)
  FlockSimulationApp.listenTo(alignmentSlider)
  FlockSimulationApp.listenTo(seeAheadSlider)
  FlockSimulationApp.listenTo(avoidanceSlider)
  FlockSimulationApp.listenTo(ruleMultiplierSlider)
  FlockSimulationApp.listenTo(simPanel.mouse.clicks)
  FlockSimulationApp.listenTo(gravityToggle)
  FlockSimulationApp.listenTo(clearVehiclesButton)
  FlockSimulationApp.listenTo(obstacleVehicleButton)

  // Reactions to events.
  FlockSimulationApp.reactions += {
    
    case ValueChanged(`detectionSlider`) => {
      detectionRadius = detectionSlider.value
      detectionLabel.text = "Detection radius: " + detectionSlider.value
    }

    case ValueChanged(`speedSlider`) => {
      topSpeed = speedSlider.value
      speedLabel.text = "Speed: " + speedSlider.value
    }

    case ValueChanged(`separationSlider`) => {
      val v = separationSlider.value 
      separationWeight = v
      separationLabel.text = "Separation: " + separationSlider.value
    }

    case ValueChanged(`cohesionSlider`) => {
      val v = cohesionSlider.value 
      cohesionWeight = v
      cohesiobLabel.text = "Cohesion: " + cohesionSlider.value
    }

    case ValueChanged(`alignmentSlider`) => {
      val v = alignmentSlider.value 
      alignmentWeight = v
      alignmentLabel.text = "Alignment: " + alignmentSlider.value
    }

    case ValueChanged(`seeAheadSlider`) => {
      val v = seeAheadSlider.value 
      seeAhead = v
      seeAheadLabel.text = "See-ahead: " + seeAheadSlider.value
    }

    case ValueChanged(`avoidanceSlider`) => {
      val v = avoidanceSlider.value
      avoidanceWeight = v
      avoidanceLabel.text = "Avoidance: " + avoidanceSlider.value
    }

     case ValueChanged(`ruleMultiplierSlider`) => {
      val v = ruleMultiplierSlider.value
      ruleMultiplier = v.toDouble / 10D
      ruleMultiplierLabel.text = "Rule Multiplier: " + ruleMultiplierSlider.value
    }

    case clickEvent: MousePressed => {
      // Making sure vehicles can only be added inside the simulation panel.
      if(clickEvent.point.x <= width && clickEvent.point.x >= 0 && clickEvent.point.y <= height && clickEvent.point.y >= 0 ) {
        if(addingVehicles) {
          simWorld.addVehicle(new Vehicle(new SimulationVector(clickEvent.point.x, clickEvent.point.y), Vehicle.randomVelocity, simWorld))  
        } else {
          simWorld.addObstacle(new Obstacle(new SimulationVector(clickEvent.point.x, clickEvent.point.y), 30) )  
        }
      }
    }
    
    case ButtonClicked(`gravityToggle`) => {
        if(centerPull == 1) {
          gravityToggle.text = gravityToggleOffText
          centerPull = 0
        } else {
          centerPull = 1
          gravityToggle.text = gravityToggleOnText
        }
    }
    
    case ButtonClicked(`clearVehiclesButton`) => {
      simWorld.removeVehicles() 
      simWorld.removeObstacles()
    }
    
    case ButtonClicked(`obstacleVehicleButton`) => {
      addingVehicles = !addingVehicles
      if(addingVehicles) obstacleVehicleButton.text = obstacleVehicleButtonTextVehicles
      else obstacleVehicleButton.text = obstacleVehicleButtonTextObstacles
    }
  }
  
  // Adding contents to all the containers (panels).
  verticalSuperPanel.contents += slidersPanel
  verticalSuperPanel.contents += labelsPanel
  
  this.contents += verticalSuperPanel
  this.contents += buttonsPanel
  
  buttonsPanel.contents += gravityToggle
  buttonsPanel.contents += clearVehiclesButton
  buttonsPanel.contents += numOfVehiclesLabel
  
  slidersPanel.contents += detectionSlider
  slidersPanel.contents += speedSlider
  slidersPanel.contents += separationSlider
  slidersPanel.contents += cohesionSlider
  slidersPanel.contents += alignmentSlider
  slidersPanel.contents += ruleMultiplierSlider
  
  labelsPanel.contents += detectionLabel
  labelsPanel.contents += speedLabel
  labelsPanel.contents += separationLabel
  labelsPanel.contents += cohesiobLabel
  labelsPanel.contents += alignmentLabel
  labelsPanel.contents += ruleMultiplierLabel

  FlockSimulationApp.view.contents += simPanel
  FlockSimulationApp.view.contents += this
}