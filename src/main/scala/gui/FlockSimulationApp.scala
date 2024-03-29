package gui

import util.Parameters.{height, vehicleLimit, width, refreshRate}
import logic.SimulationWorld

import scala.swing.{BoxPanel, Color, Dimension, MainFrame, Orientation, SimpleSwingApplication}

import java.awt.event.ActionListener
import java.util.concurrent.Executors

// The app itself.
// Creates instances of SimulationWorld, SimulationPanel, MenuBar and ControlsPanel.
// Crates an executor thread pull and a timer feeding it with tasks.
object FlockSimulationApp extends SimpleSwingApplication {

  // An instance of the SimulationWorld.
  val simWorld: SimulationWorld = new SimulationWorld

  // The topmost window
  val simWindow: MainFrame = new MainFrame {
    // Basic parameters for the MainFrame.
    title = "Flocking Simualtion"
    resizable = true
    background = new Color(0, 0, 0)
    minimumSize = new Dimension(600, 400)
    maximumSize = new Dimension(3000, 2000)

    // Menu bar on top of the simulation.
    menuBar = gui.MenuBar.menu
  }

  // Main container. Every GUI element is inside it.
  val view: BoxPanel = new BoxPanel(Orientation.Vertical)

  // Elements within the main container.
  // The simulation field:
  val simPanel: SimulationPanel = new SimulationPanel(simWorld) {
    preferredSize = new Dimension(width, height + 200)
  }
  // The controls panel:
  val controlsSuperPanel: ControlsPanel = new ControlsPanel(Orientation.Horizontal)

  // Adding the main container to the MainFrame.
  simWindow.contents = view

  // Making simWindow the main view of the app.
  def top: MainFrame = this.simWindow

  // A class encapsulating a single task on a single vehicle.
  // Instances of this class are used as arguments for the thread pool.
  class VehicleTask(vehicle: logic.Vehicle) extends Runnable {
    override def run() {
      vehicle.updateVelocity()
      vehicle.move()
    }
  }

  // Setting up the thread pool.
  val cores: Int = Runtime.getRuntime.availableProcessors
  val pool: java.util.concurrent.ExecutorService = Executors.newFixedThreadPool(cores)

  // Setting up a timer to repeatedly update the velocities and positions of all vehicles,
  // and repainting the simulation panel.
  new javax.swing.Timer(refreshRate, new ActionListener {
        def actionPerformed(e: java.awt.event.ActionEvent) {
          simWorld.vehicles.foreach(vehicle => {
            pool.submit(new VehicleTask(vehicle))
          })

          controlsSuperPanel.numOfVehiclesLabel.text = simWorld.vehicles.size + " out of " + vehicleLimit
          width = simWindow.size.width
          height = simWindow.size.height

          simPanel.repaint()
        }
   }).start
}
