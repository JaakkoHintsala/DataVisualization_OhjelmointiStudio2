package OS2

import scalafx.beans.property.ObjectProperty
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.beans.property.DoubleProperty
import scalafx.collections.ObservableBuffer
import scalafx.event.ActionEvent
import scalafx.geometry.Orientation
import scalafx.scene.{AccessibleRole, _}
import scalafx.scene.control._
import scalafx.scene.input.{Dragboard, MouseDragEvent}
import scalafx.scene.layout._
import scalafx.scene.control.TableView
import scalafx.stage.Stage


object ChartValueChooser {


  def popUpScene(originalScene: Scene) = {
    val scenePane = new Pane()
    scenePane.prefWidth = 200d
    scenePane.prefHeight = 200d
    val lab = new Label("Selected: ")
    scenePane.children = lab

    val newScene = new Scene(scenePane)
    var selections = new ObjectProperty(this, "bruh", null: javafx.scene.control.TableView.TableViewSelectionModel[GenericRow])
    val but = new Button("press")
    but.onAction = (e: ActionEvent) => {
      println(selections)
      if(selections.value != null) {
        println(selections.value.getSelectedCells)
      }
    }
    lab.graphic = but


    originalScene.focusOwnerProperty().onChange((obs, oldV, newV) => {

      newV.getClass.getSimpleName match {
        case "TableView" => {

          val t = newV.asInstanceOf[javafx.scene.control.TableView[GenericRow]]
          selections  = t.selectionModel
          println(t.getSelectionModel)
        }
        case _ => {}
      }
    })

    val stage = new Stage()
    stage.alwaysOnTop = true
    stage.scene = newScene
    stage.show()
  }

}
