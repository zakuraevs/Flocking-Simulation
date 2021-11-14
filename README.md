# Flocking-Simulation
A 2D simulation of flocking behavior that allows users to add autonomous characters & obstacles, resize the simulation and control parameters such as speed and gravitational pull in real time.

This program is a Scala implementation of Craig Reynold's "Steering Behaviors For Autonomous Characters" (Reynolds, 1997). 

![Flocking simulation](https://i.imgur.com/pvNSIVA.gif)

### Contents of the repository:

- src/main/scala/gui: the GUI part of the program
- src/main/scala/logic: the domain and logic part of the program
- src/main/scala/util: util storage
- src/test/scala: unit tests

The rest of the files are mostly a part of the sbt project structure and can be left untouched.

## How to run this program

**Requirements:**

- Java SE Development Kit 8 (JDK8) or later (https://www.oracle.com/java/technologies/javase-jdk8-downloads.html)  
- IntelliJ IDE: (https://www.jetbrains.com/idea/download/) 

**Importing:**
The program can be imported into IntelliJ directly:

1. Clone the repo locally
2. Open the root directory of the repo through Intellij

**Running:**
The app can be launched by running the FlockSimulationApp.scala object inside the gui package as a Scala Application.
Just left-click on the big green triangle next to the object.

## Features

- The simulation window can be resized by dragging the window edges. This resized the edges of the simulation too.
- New vehicles can be added by left-clicking the black area.
- The toolbar at the bottom of the screen allows adjusting simulation parameters:
  - Detection radius: how far each vehicle can 'see' other vehicles around itself.
  - Speed: the speed of vehicles.
  - Separation: the strength of behavior causing vehicles to a void each other.
  - Cohesion: the strength of behavior causing vehicles to be attracted to each other.
  - Allignment: the strength of behavior causing vehicles to face the same direction.
  - See-ahead: the distance vehicles can see obstacles in front of themselves.
  - Avoidance: The strength of behavior causing vehicles to avoid obstacles.
  - Gravity: a button that toggles gravitational pull towards the center of the simulation.
  - Clear: A button that removes all vehicles and obstacles.
  - Adding vehicles/obstacles: Chooses whether vehicles or obstacles are being added by left clicks.
 - The top menu bar has the following functionality:
  - File: Saving the simulation state into a text file and laoding it from one. Resetting the simulation.
  - Controls: Showing or hiding obstacle-related controls.
  - Help: Offers pop-up windows with instructions.
  - Presets: Offers simulation presets with vehicles and obstacles in specific locations.
  - Themes: Offers different choices for the visual appearance of the program.
  
## Known issues

- The simulation is known to not display properly on some Linux distributions: vehicles leave traces.

## References

REYNOLDS, C., 1997. Steering Behaviors For Autonomous vehicles [online]. Available from http://www.red3d.com/cwr/steer/
