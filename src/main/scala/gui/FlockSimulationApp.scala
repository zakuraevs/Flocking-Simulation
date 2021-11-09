package gui

import logic.Parameters.{height, vehicleLimit, width}
import logic.SimulationWorld

import scala.swing.{BoxPanel, Color, Dimension, MainFrame, MenuBar, Orientation, SimpleSwingApplication}
import scala.collection.parallel.CollectionConverters._
import java.awt.event.ActionListener
import java.util.concurrent.Executors

// The app itself:
// Creates instances of SimulationWorld, SimulationPanel, MenuBar and ControlsPanel.
// Also creates and starts the execution threads.
object FlockSimulationApp extends SimpleSwingApplication {

  // MainFrame
  val simWindow = new MainFrame {
    // Basic parameters for the MainFrame.
    title = "Flocking Simualtion"
    resizable = true
    background = new Color(0, 0, 0)
    minimumSize = new Dimension(600, 400)
    maximumSize = new Dimension(3000, 2000)

    // Menu bar on top of the simulation.
    menuBar = gui.MenuBar.menu
  }

  // An instance of the SimulationWorld.
  val simWorld = new SimulationWorld

  // Main container. Every GUI element is inside it.
  val view = new BoxPanel(Orientation.Vertical)

  // Elements within the main container.
  // The simulation field:
  val simPanel = new SimulationPanel(simWorld) {
    preferredSize = new Dimension(width, height + 200)
  }
  // The controls panel:
  val controlsSuperPanel = new ControlsPanel(Orientation.Horizontal)

  // Adding main container to MainFrame.
  simWindow.contents = view

  // Making simWindow the main view of the app.
  def top: MainFrame = this.simWindow

  // TODO put this into a separate file?
  class VehicleTask(vehicle: logic.Vehicle) extends Runnable {
    override def run() {
      vehicle.updateVelocity()
      vehicle.move()
    }
  }

  val cores: Int = Runtime.getRuntime.availableProcessors
  val pool: java.util.concurrent.ExecutorService = Executors.newFixedThreadPool(cores)

  new javax.swing.Timer(10, new ActionListener {
        def actionPerformed(e: java.awt.event.ActionEvent) {
          simWorld.vehicles.foreach(vehicle => {
            pool.submit(new VehicleTask(vehicle))
          })

          controlsSuperPanel.numOfVehiclesLabel.text =
            simWorld.vehicles.size + " out of " + vehicleLimit
          width = simWindow.size.width
          height = simWindow.size.height

          simPanel.repaint()
        }
   }).start
}
