package OS2.Elements

import OS2.Appi.stage
import OS2.DataChoosers.ChartValueChooser
import OS2.File.UniversalFileOpener
import scalafx.application.JFXApp
import scalafx.event.ActionEvent
import scalafx.geometry.Orientation
import scalafx.scene.Scene
import scalafx.scene.control.{Menu, MenuBar, MenuItem, ScrollPane, TableView}
import scalafx.scene.layout.{BorderPane, FlowPane, HBox, VBox}
import scalafx.Includes._
import OS2.File._
import scalafx.stage.Stage

class GUI {

  val stage = new Stage
    stage.title.value = "DATA"
    stage.width = 1000
    stage.height = 700
  val flowPane = new FlowPane(Orientation.Horizontal, 10d, 10d)
  val roott = new BorderPane()
  val scenee = new Scene(roott)
  var tables = Vector[TableView[GenericRow]]()

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
      ChartValueChooser.popUpSceneCard(classOf[SumCard], scenee, flowPane)
    }
    minCard.onAction = (ae: ActionEvent) => {
      ChartValueChooser.popUpSceneCard(classOf[MinCard], scenee, flowPane)
    }
    maxCard.onAction = (ae: ActionEvent) => {
      ChartValueChooser.popUpSceneCard(classOf[MaxCard], scenee, flowPane)
    }
    averageCard.onAction = (ae: ActionEvent) => {
      ChartValueChooser.popUpSceneCard(classOf[AverageCard], scenee, flowPane)
    }
    stdDevCard.onAction = (ae: ActionEvent) => {
      ChartValueChooser.popUpSceneCard(classOf[StandardDeviationCard], scenee, flowPane)
    }

    fileNew.items = List(fileNewTable, fileNewChatter, fileNewLine, fileNewBar, fileNewPie, fileCard)

    fileNewTable.onAction = (e: ActionEvent) => {
      val taulu = GenericTaulu(Vector(), Vector())

      tables = tables :+ taulu.table
      val savemenu = new MenuItem("Save Table")
      savemenu.onAction = (ae: ActionEvent) => {
        OS2.File.GenericTableFile.toFile(stage, taulu)

      }
      taulu.cmenu.items.add(savemenu)
      val newTable = taulu.table

      val deletesYeetus = new MenuItem("Delete")
      deletesYeetus.onAction = ((ae: ActionEvent) => {
        val a = flowPane.children.removeAll(taulu.titled)
      })

      taulu.cmenu.items.add(deletesYeetus)
      flowPane.children.add(taulu.titled)

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
      UniversalFileOpener.openSesame(stage, flowPane, tables, scenee)
    }


    val fileDelete = new MenuItem("Delete")

    val fileSave = new MenuItem("Save dashcoard")

    fileSave.onAction = (e: ActionEvent) => {
      DashBoardFile.toFile(stage, flowPane.children.toVector.map(_.userData.asInstanceOf[Saveable]))


    }


    fileMenu.items = List(
      fileNew,
      fileOpen,
      fileSave,
      fileDelete
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
