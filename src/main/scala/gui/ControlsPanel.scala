package gui

import gui.FlockSimulationApp.{simPanel, simWorld}
import logic.Parameters._
import logic.{Obstacle, SimulationVector, Vehicle}

import scala.swing._
import scala.swing.event._

// Represnts the controls bar at the bottom of the GUI.
class ControlsPanel(orientation: scala.swing.Orientation.Value) extends scala.swing.BoxPanel(orientation) {
  
  minimumSize = new Dimension(width,controlsHeight)
  maximumSize = new Dimension(width,controlsHeight)
  
  var obstacleControlsHidden = true
  
  // Supercontainer for controls.
  val verticalSuperPanel = new BoxPanel(Orientation.Vertical){
    preferredSize  = new Dimension(width,150)
  }
  
  // Sliders to control the simulation.
  val slidersPanel = new BoxPanel(Orientation.Horizontal) { 
    background = controlsColor
    preferredSize  = new Dimension(width-50,60)
  }
  
  // Labels to display parameter values.
  val labelsPanel = new GridPanel(1,4) {
    background = controlsColor
    preferredSize  = new Dimension(width,60)
  }
  
  // Buttons for actions.
  val buttonsPanel = new GridPanel(2,2) {
    background = controlsColor
  }
  
  // Labels that display the values of parameters.
  val numOfVehiclesLabel = new scala.swing.Label { text = simWorld.numOfVehicles + " out of " + vehicleLimit }
  val detectionLabel = new scala.swing.Label { text = "Detection radius: " + detectionRadius.toString }
  val speedLabel = new scala.swing.Label { text = "Speed: " + topSpeed.toString }
  val separationLabel = new scala.swing.Label { text = "Separation: " + separationWeight.toString }
  val cohesiobLabel = new scala.swing.Label { text = "Cohesion: " + cohesionWeight.toString }
  val alignmentLabel = new scala.swing.Label { text = "Alignment: " + alignmentWeight.toString }
  val seeAheadLabel = new scala.swing.Label { text = "See-ahead: " + seeAhead.toString }
  val avoidanceLabel = new scala.swing.Label { text = "Avoidance: " + avoidanceForce.toString }

  // TODO refactor all of the below to simplify it somehow.
  // Sliders that will go inside controlsPanel.
  val detectionSlider = new Slider {
    min = 1
    max = 400
    value = detectionRadius
  }
  
  val speedSlider = new Slider {
    min = 1
    max = 10
    value = topSpeed.toInt
  }
  
  val separationSlider = new Slider {
    min = 0
    max = 10
    value = separationWeight.toInt
  }
  
  val cohesionSlider = new Slider {
    min = 0
    max = 10
    value = cohesionWeight.toInt
  }
  
  val alignmentSlider = new Slider {
    min = 0
    max = 10
    value = alignmentWeight.toInt
  }
  
  val seeAheadSlider = new Slider {
    min = 0
    max = 200
     value = seeAhead.toInt
  }
  
  val avoidanceSlider = new Slider {
    min = 0
    max = 100
    value = avoidanceForce.toInt
  }
  
  // Buttons that go inside buttonsPanel.
  val gravityToggle = new scala.swing.Button {
    text = "Gravity: on"
  }
  
  val clearVehiclesButton = new scala.swing.Button {
    text = "clear"
  }
  
  val obstacleVehicleButton = new scala.swing.Button {
    text = "adding: vehicles"
  }
  
  // Listening to controls.
  FlockSimulationApp.listenTo(detectionSlider)
  FlockSimulationApp.listenTo(speedSlider)
  FlockSimulationApp.listenTo(separationSlider)
  FlockSimulationApp.listenTo(cohesionSlider)
  FlockSimulationApp.listenTo(alignmentSlider)
  FlockSimulationApp.listenTo(seeAheadSlider)
  FlockSimulationApp.listenTo(avoidanceSlider)
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
      avoidanceForce = v
      avoidanceLabel.text = "Avoidance: " + avoidanceSlider.value
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
          gravityToggle.text = "Gravity: off"
          centerPull = 0
        } else {
          centerPull = 1
          gravityToggle.text = "Gravity: on"
        }
    }
    
    case ButtonClicked(`clearVehiclesButton`) => {
      simWorld.removeVehicles() 
      simWorld.removeObstacles()
    }
    
    case ButtonClicked(`obstacleVehicleButton`) => {
      addingVehicles = !addingVehicles
      if(addingVehicles) obstacleVehicleButton.text = "adding: vehicles"
      else obstacleVehicleButton.text = "adding: obstacles"
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
  
  labelsPanel.contents += detectionLabel
  labelsPanel.contents += speedLabel
  labelsPanel.contents += separationLabel
  labelsPanel.contents += cohesiobLabel
  labelsPanel.contents += alignmentLabel

  FlockSimulationApp.view.contents += simPanel
  FlockSimulationApp.view.contents += this
}