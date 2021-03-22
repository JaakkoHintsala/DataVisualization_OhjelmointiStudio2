package OS2

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.beans.property.DoubleProperty
import scalafx.collections.ObservableBuffer
import scalafx.event.ActionEvent
import scalafx.scene._
import scalafx.scene.control._
import scalafx.scene.layout._
import scalafx.stage.FileChooser

object Appi extends JFXApp {
  stage = new JFXApp.PrimaryStage {
    title.value = "DATA"
    width = 1000
    height = 700
  }

  val root = new BorderPane {
    top = new MenuBar {
      val fileMenu = new Menu("File")
      val settings = new Menu("Settings")
      val viewMenu = new Menu("View")

      val fileNew = new MenuItem("New")
      fileNew.onAction = (e: ActionEvent) => {
        tabit.tabs += new Tab
      }
      val fileOpen = new MenuItem("Open")
      fileOpen.onAction = (e: ActionEvent) => {
        val ch = new FileChooser
        val selected = ch.showOpenDialog(stage)
        println(selected.toString)
      }
      val fileDelete = new MenuItem("Delete")

      fileMenu.items = List(
        fileNew,
        fileOpen,
        fileDelete
      )

      menus = List(
        fileMenu,
        viewMenu,
        settings
      )
    }
    val tabit = new TabPane
    val tab1 = new Tab
    val tab2 = new Tab
    val tab3 = new Tab
    tab1.text = "table"
    tab2.text = "chatter"
    tab3.text = "line"

    val s1 = Vector(10,11,12,13,14)
      .zip(Vector(4,7,9,11,12))
      .map(x => IntCell(x._1,x._2))

    val t1 = Int2x("vuosi", "määrä", s1)
    t1.table.editable = true
    tab1.content = t1.table

    val ch = t1.chatter
    val lin = t1.line
    tab2.content = ch
    tab3.content = lin

    tabit.tabs = List(tab1, tab2, tab3)


    val scrolli = new ScrollPane


    center = tabit

  }


  val scene = new Scene(root)
  stage.scene = scene

}