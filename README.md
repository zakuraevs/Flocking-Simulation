# Flocking-Simulation
2d simulation of autonomous vehicles. Written in Scala, launchable through Eclipse.

Written by Sergey Zakuraev as a part of Aalto University Programming 2: Studio course 

2d simulation where a user-selected number of vehicles (boids) move in a life-like flocking manner. 
Each vehicle has a number of parameters such as a position in the 2d world and a velocity, 
and each vehicle follows four rules to achieve realistic behavior. 

In order to run the program, the user needs to have Java SE Development Kit 8 (https://www.oracle.com/java/technologies/javase-jdk8-downloads.html) and Scala IDE for Eclipse (http://scala-ide.org/download/sdk.html) installed on their computer. The program can be imported into Eclipse as a git project:

1. Copy the clone URL on GitHub
2. File -> import -> Git -> Projects from Git -> next in Eclipse
3. Clone URI -> finish
4. Follow the recommended settings

The app can be launched by running the FlockSimulationApp.scala class inside the gui package as a Scala Application.

![Image description](https://i.imgur.com/pk0Zno3.png)
