package OS2

import scalafx.Includes._
import scalafx.application.JFXApp
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
    tab1.text = "ykkönen"
    tab2.text = "kakkonen"
    tab3.text = "kolmonen"

    val s1 = IntSarake("numerot", ObservableBuffer(1, 2, 3, 4).map(x => IntCell(x)))

    val t1 = IntTaulukko("N", Vector(s1))
    tab1.content = t1.GUIElem

    tabit.tabs = List(tab1, tab2, tab3)


    val scrolli = new ScrollPane


    center = tabit

  }


  val scene = new Scene(root)
  stage.scene = scene

}