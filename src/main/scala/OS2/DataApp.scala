package OS2

import OS2.DataChoosers.ChartValueChooser
import OS2.File.GenericTableFile
import OS2.GUIElements.{AverageCard, GenericTaulu, MaxCard, MinCard, StandardDeviationCard, SumCard}
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.beans.property.DoubleProperty
import scalafx.collections.ObservableBuffer
import scalafx.event.ActionEvent
import scalafx.geometry.Orientation
import scalafx.scene._
import scalafx.scene.control._
import scalafx.scene.input.{Dragboard, MouseDragEvent}
import scalafx.scene.layout._
import scalafx.stage._

import java.nio.file.Paths
import java.io.File

object Appi extends JFXApp {
  stage = new JFXApp.PrimaryStage {

    title.value = "DATA"
    width = 1000
    height = 700
  }
  val flowPane = new FlowPane(Orientation.Horizontal, 10d, 10d)
  val roott = new BorderPane()
  val scenee = new Scene(roott)


  roott.top = new MenuBar {
    val fileMenu = new Menu("File")
    val settings = new Menu("Settings")
    val viewMenu = new Menu("View")

    val fileNew = new Menu("New")
    val fileNewTable = new MenuItem("Table")
    val fileNewChatter = new MenuItem("Chatterchart")
    val fileNewLine = new MenuItem("Linechart")
    val fileNewBar = new MenuItem("Barchart")
    val fileNewPie = new MenuItem("Piechart")

    val fileCard = new Menu("Card")
    val SCard = new MenuItem("Sum")
    val minCard = new MenuItem("Min")
    val maxCard = new MenuItem("Max")
    val averageCard = new MenuItem("Average")
    val stdDevCard = new MenuItem("Std deviation")
    fileCard.items = List(SCard, minCard, maxCard, averageCard, stdDevCard)

    SCard.onAction = (ae: ActionEvent) => {
      ChartValueChooser.popUpSceneCard(classOf[SumCard],  scenee, flowPane)
    }
    minCard.onAction = (ae: ActionEvent) => {
      ChartValueChooser.popUpSceneCard(classOf[MinCard],scenee, flowPane)
    }
    maxCard.onAction = (ae: ActionEvent) => {
      ChartValueChooser.popUpSceneCard(classOf[MaxCard],scenee, flowPane)
    }
    averageCard.onAction = (ae: ActionEvent) => {
      ChartValueChooser.popUpSceneCard(classOf[AverageCard],scenee, flowPane)
    }
    stdDevCard.onAction = (ae: ActionEvent) => {
      ChartValueChooser.popUpSceneCard(classOf[StandardDeviationCard],scenee, flowPane)
    }

    fileNew.items = List(fileNewTable, fileNewChatter, fileNewLine, fileNewBar, fileNewPie, fileCard)

    fileNewTable.onAction = (e: ActionEvent) => {
      val taulu = GenericTaulu(Vector(), Vector())
      val newTable = taulu.table
      val titled = new TitledPane()
      titled.content = newTable
      val deletesYeetus = new MenuItem("Delete")
      deletesYeetus.onAction = ((ae: ActionEvent) => {
        val a = flowPane.children.removeAll(titled)
      })

      taulu.cmenu.items.add(deletesYeetus)
      flowPane.children.add(titled)

    }
    fileNewChatter.onAction = (e: ActionEvent) => {
      ChartValueChooser.popUpSceneScatter(scenee, flowPane)
    }
    fileNewLine.onAction = (e: ActionEvent) => {
      ChartValueChooser.popUpSceneLine(scenee, flowPane)
    }
    fileNewBar.onAction = (e: ActionEvent) => {
      ChartValueChooser.popUpSceneBar(scenee, flowPane)
    }
    fileNewPie.onAction = (e: ActionEvent) => {
      ChartValueChooser.popUpScenePie(scenee, flowPane)
    }
    val fileOpen = new MenuItem("Open")
    fileOpen.onAction = (e: ActionEvent) => {
      val ch = new FileChooser
      val selected = ch.showOpenDialog(stage)

      val curr = new Tab()
      curr.text = "New tabb"
      curr.content = GenericTableFile.fromFile(selected.getAbsolutePath).table


      println(selected.toString)
    }
    val fileDelete = new MenuItem("Delete")

    val fileSave = new MenuItem("Save")

    fileSave.onAction = (e: ActionEvent) => {
      val ch = new FileChooser
      val currentPath = Paths.get(".").toAbsolutePath.normalize().toString
      ch.setInitialDirectory(new File(currentPath))
      val f = new File(currentPath)
      val filter = new FileChooser.ExtensionFilter("Dashboard", "*.dashb")
      ch.extensionFilters.addAll(filter)

      val selected = ch.showSaveDialog(stage)
       GenericTableFile.toFile(selected.getAbsolutePath, flowPane.children(0).asInstanceOf[javafx.scene.control.TitledPane].content.value.getUserData.asInstanceOf[GenericTaulu])
      println(selected.toString)
    }
    val button = new MenuItem("print selected")
    button.onAction = (e: ActionEvent) => {
      println("scene: " + scene.value.focusOwnerProperty())
    }
    fileMenu.items = List(
      fileNew,
      fileOpen,
      fileSave,
      fileDelete,
      button
    )


    menus = List(
      fileMenu,
      viewMenu,
      settings

    )
  }
  val scroll = new ScrollPane()
  scroll.fitToHeight = true
  scroll.fitToWidth = true
  //ScrollPane.ScrollBarPolicy.Never

  val vbox1 = new VBox()
  val vbox2 = new VBox()
  val vbox3 = new VBox()
  val vboxes = List(vbox1, vbox2, vbox3)
  val hbox = new HBox()
  hbox.children = vboxes

  scroll.content = flowPane
  roott.center = scroll


  stage.scene = scenee


}