package util

import java.awt.Color

// An object that stores values of various parameters affecting simulation and GUI.
object Parameters {

  // GUI paramters
  var width: Int = 1260
  var height: Int = 600
  var VehicleColor: Color = Color.white
  var backgroundColor: Color = Color.black
  var controlsColor: Color = Color.white
  var addingVehicles: Boolean = true
  var controlsHeight: Int = 195
  val refreshRate: Int = 10
  val vehiclePolygonFrontOffset: Int = 18
  val vehiclePolygonBackOffset: Int = 4
  val vehiclePolygonSideOffset: Int = 7

  // Sliders
  val sliderParameters = Map[String, Tuple3[Int, Int, Int]](
    "detectionSlider" -> (1, 400, detectionRadius),
    "speedSlider" -> (1, 10, topSpeed),
    "separationSlider" -> (1, 10, separationWeight),
    "cohesionSlider" -> (1, 10, cohesionWeight),
    "alignmentSlider" -> (1, 10, alignmentWeight),
    "seeAheadSlider" -> (1, 200, seeAhead.toInt),
    "avoidanceSlider" -> (1, 100, avoidanceWeight),
    "ruleMultiplierSlider" -> (1, 10, (ruleMultiplier * 10).toInt)
  )

  // Buttons
  var gravityToggleOnText = "Gravity: on"
  var gravityToggleOffText = "Gravity: off"
  var clearVehiclesButtonText = "clear"
  var obstacleVehicleButtonTextVehicles = "adding: vehicles"
  var obstacleVehicleButtonTextObstacles = "adding: obstacles"

  // Simulation parameters
  val vehicleLimit: Int = 1000
  val obstacleRadiusMultiplier = 1.2

  var ruleMultiplier: Double = 0.2
  var topSpeed: Int = 3
  var detectionRadius: Int = 50
  var centerPull: Int = 1
  var separationWeight: Int = 5
  var cohesionWeight: Int = 4
  var alignmentWeight: Int = 6
  var seeAhead: Double = 90
  var avoidanceWeight: Int = 85


  // Text for the help section
  val helpText =
    """
          \(•◡•)/

Welcome to Flocking Simulation

This app simulates behaviour of groups of vehicles such as
bird flocks and fish schools.

It is based on Craig Reynold's research:
'http://www.red3d.com/cwr/steer/gdc99/'

You can add new vehicles by left-clicking the black panel.
There is a limit to the maximum number of vehicles
that can be added.

Things you can adjust:
- Detection Radius: how far a vehicle can detect other vehicles
- Speed: the speed of vehicles
- Separation: how strongly vehicles repulse each other
- Cohesion: how strongly vehicles are attracted to each other
- Alignment: how strongly vehicles steer in the same direction
- Gravity: you can switch pull towards center of simulation on and off
- Clear: you can remove all vehicles

You can also use the top menu to save the current state of simulation
as a text file or load it from one using the top menu, change the color theme,
reset the simulation or show addiotional controls that affect obstacle avoidance
and allow you to palce obstacles.

Additional features under development.

- Sergey
"""

  // Text for IO assistance
  val formatText =
    """
         ⚆ _ ⚆

The load file must be a .txt file

The file has to be as in the following example:

'speed: 4
detectionRadius: 160
centerPull: 1
separationWeight: 38
cohesionWeight: 38
alignmentWeight: 50
Vehicles: (796 328, 3 2); (779 442, -1 -2);

This example sets the simulation's
- speed to 4
- vehicle detextion radius to 160
- center pull(gravity) to 1
- separation weight of vehicles to 38
- cohesion weight of vehicles to 38
- alignment weight of vehicles to 50

And adds 2 vehicles at coordinates
(796 328), (779 442), with velocities (3 2), (-1 -2).

You can only use integer values for all parameters.

You can only use positive values for
- speed
- detectionRadius

And non-negative values for
- centerPull
- separationWeight
- cohesionWeight
- alignmentWeight
"""

  // Text for incorrect IO format
  val errorText =
    """
         (▀̿Ĺ̯▀̿ ̿)

Looks like you used incorrect file format.

You can only use integer values for all parameters.

You can only use positive values for
- speed
- detectionRadius

And non-negative values for
- centerPull
- separationWeight
- cohesionWeight
- alignmentWeight

Please see 'Help' --> 'Loading Fiel format' for details :)
"""
}
