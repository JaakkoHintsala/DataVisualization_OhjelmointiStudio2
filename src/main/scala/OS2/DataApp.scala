package OS2

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.beans.property.DoubleProperty
import scalafx.collections.ObservableBuffer
import scalafx.event.ActionEvent
import scalafx.geometry.Orientation
import scalafx.scene._
import scalafx.scene.control._
import scalafx.scene.layout._
import scalafx.stage._

object Appi extends JFXApp {
  stage = new JFXApp.PrimaryStage {
    title.value = "DATA"
    width = 1000
    height = 700
  }

  val roott = new BorderPane {
    top = new MenuBar {
      val fileMenu = new Menu("File")
      val settings = new Menu("Settings")
      val viewMenu = new Menu("View")

      val fileNew = new Menu("New")
      val fileNewTable = new MenuItem("Table")
      val fileNewChatter = new MenuItem("Chatterchart")
      fileNew.items = List(fileNewTable, fileNewChatter)
      fileNewTable.onAction = (e: ActionEvent) => {
        flowPane.children.add(GenericTaulu(Vector(), Vector()).table)
      }
      fileNewChatter.onAction = (e: ActionEvent) => {
        println("bruh")
        val popup = new Popup()
        popup.setX(300)
        popup.setY(300)
        popup.content.add(GenericTaulu(Vector(), Vector()).table)
        popup.show(stage)
      }
      val fileOpen = new MenuItem("Open")
      fileOpen.onAction = (e: ActionEvent) => {
        val ch = new FileChooser
        val selected = ch.showOpenDialog(stage)

        val curr = new Tab()
        curr.text = "New tabb"
        curr.content = GenericTableConverter.fromFile(selected.getAbsolutePath).table


        println(selected.toString)
      }
      val fileDelete = new MenuItem("Delete")

      val fileSave = new MenuItem("Save")

      fileSave.onAction = (e: ActionEvent) => {
        val ch = new FileChooser
        val selected = ch.showOpenDialog(stage)
        // GenericTableConverter.toFile(selected.getAbsolutePath, t)
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

    val flowPane = new FlowPane(Orientation.Horizontal)
    scroll.content = flowPane
    center = scroll

  }

  val scene = new Scene(roott)
  stage.scene = scene

}