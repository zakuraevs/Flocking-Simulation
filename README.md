# Flocking-Simulation
A 2D simulation of flocking behavior that allows users to add autonomous characters & obstacles, resize the simulation and control parameters such as speed and gravitational pull in real time.

This program is a Scala implementation of Craig Reynold's "Steering Behaviors For Autonomous Characters" (Reynolds, 1997). 

![Flocking simulation](https://i.imgur.com/pvNSIVA.gif)

### Contents of the repository:

- FlockingSimulation: root directory
- Project appendixes: Project documentation and I/O examples
- Resources: project dependencies
- src: source code
  - gui: the GUI part of the program
  - logic: the computational part of the program
  - tests: unit tests with JUnit

## How to run this program

**Requirements:**

- Java SE Development Kit 8 (JDK8) (https://www.oracle.com/java/technologies/javase-jdk8-downloads.html)  
- Eclipse Scala IDE: http://scala-ide.org/download/sdk.html) 

**Importing:**
The program can be imported into Eclipse as a git project:

1. Copy the GitHub URL (https://github.com/zakuraevs/Flocking-Simulation.git)
2. In Eclipse, File -> import -> Git -> Projects from Git -> next
3. Clone URI -> finish
4. Follow the recommended settings

**Running:**
The app can be launched by running the FlockSimulationApp.scala class inside the gui package as a Scala Application. Just right-click it and select run.

## Features

- The simulation window can be resized by dragging the window edges. This resized the edges of the simulation too.
- New characters can be added by left-clicking the black area.
- The toolbar at the bottom of the screen allows adjusting simulation parameters:
  - Detection radius: how far each character can 'see' other characters around itself.
  - Speed: the speed of characters.
  - Separation: the strength of behavior causing characters to a void each other.
  - Cohesion: the strength of behavior causing characters to be attracted to each other.
  - Allignment: the strength of behavior causing characters to face the same direction.
  - See-ahead: the distance characters can see obstacles in front of themselves.
  - Avoidance: The strength of behavior causing characters to avoid obstacles.
  - Gravity: a button that toggles gravitational pull towards the center of the simulation.
  - Clear: A button that removes all characters and obstacles.
  - Adding vehicles/obstacles: Chooses whether characters or obstacles are being added by left clicks.
 - The top menu bar has the following functionality:
  - File: Saving the simulation state into a text file and laoding it from one. Resetting the simulation.
  - Controls: Showing or hiding obstacle-related controls.
  - Help: Offers pop-up windows with instructions.
  - Presets: Offers simulation presets with characters and obstacles in specific locations.
  - Themes: Offers different choices for the visual appearance of the program.
  
## Known issues

- The simulation is known to not display properly on some Linux distributions: characters leave traces.

## Documentation

- Project files include a 'Project appendices' folder with detailed doccumentation of the project.

## References

REYNOLDS, C., 1997. Steering Behaviors For Autonomous Characters [online]. Available from http://www.red3d.com/cwr/steer/
