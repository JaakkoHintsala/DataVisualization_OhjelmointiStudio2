package OS2

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

object Appi extends JFXApp {
  stage = new JFXApp.PrimaryStage {

    title.value = "DATA"
    width = 1000
    height = 700
  }
   val roott = new BorderPane()
  val scenee = new Scene(roott)


    roott.top = new MenuBar {
      val fileMenu = new Menu("File")
      val settings = new Menu("Settings")
      val viewMenu = new Menu("View")

      val fileNew = new Menu("New")
      val fileNewTable = new MenuItem("Table")
      val fileNewChatter = new MenuItem("Chatterchart")
      fileNew.items = List(fileNewTable, fileNewChatter)
      fileNewTable.onAction = (e: ActionEvent) => {
        val newTable = GenericTaulu(Vector(), Vector()).table
        newTable.prefWidth <== (scroll.width / 3) - 30d
        newTable.maxHeight = 100
        flowPane.children.add(newTable)
/*        var curBox = vbox1
        for(box <- vboxes)
          {
            if(box.children.size() < curBox.children.size()){
              curBox = box
            }
          }
        curBox.children.add(GenericTaulu(Vector(), Vector()).table)*/
      }
      fileNewChatter.onAction = (e: ActionEvent) => {
        ChartValueChooser.popUpScene(scenee)

 /*       println("bruh")
        val popup = new Popup()
        popup.setX(300)
        popup.setY(300)
        popup.content.add(GenericTaulu(Vector(), Vector()).table)
        popup.show(stage)*/
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

    val vbox1  = new VBox()
    val vbox2  = new VBox()
    val vbox3  = new VBox()
    val vboxes = List(vbox1,vbox2,vbox3)
    val hbox = new HBox()
    hbox.children = vboxes
    val flowPane = new FlowPane(Orientation.Horizontal, 10d, 10d)
    scroll.content = flowPane
    roott.center = scroll




  stage.scene = scenee


}