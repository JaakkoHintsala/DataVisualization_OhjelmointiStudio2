package OS2

import javafx.collections.ListChangeListener
import scalafx.collections._
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
import scalafx.geometry.Insets

object ChartNewSeries {
  def popUpScene(originalScene: Scene, numberChart: NumberChart) = {
    val stage = new Stage()
    stage.width = 400d
    stage.height = 500d
    stage.alwaysOnTop = true
    val scenePane = new ScrollPane()
    val newScene = new Scene(scenePane)
    scenePane.fitToWidth = true
    val hboxParent = new HBox()
    val endButton = new Button("Make chart")
    val realParent = new VBox()
    realParent.spacing = 10d
    endButton.prefWidth <== scenePane.width / 3
    endButton.prefHeight = 40d
    realParent.alignment = scalafx.geometry.Pos.TopCenter
    realParent.children = List(hboxParent, endButton)
    scenePane.content = realParent
    val vbox1 = new VBox()
    val vbox2 = new VBox()
    hboxParent.children.addAll(vbox1, vbox2)
    hboxParent.spacing = 20d
    vbox1.spacing = 10d
    vbox2.spacing = 10d
    vbox1.fillWidth = true
    vbox1.alignment = scalafx.geometry.Pos.TopCenter
    vbox2.alignment = scalafx.geometry.Pos.TopCenter

    val placeHolder1 = new Label("Nothing selected")
    val placeHolder2 = new Label("Nothing selected")
    val listA = new ListView[String]()
    val listB = new ListView[String]()
    val buttonA = new Button("Confirm selection")
    val buttonB = new Button("Confirm selection")
    var XaxisVals = Vector[javafx.scene.control.TablePosition[GenericRow, String]]()
    var YaxisVals = Vector[javafx.scene.control.TablePosition[GenericRow, String]]()
    listA.placeholder = placeHolder1
    listB.placeholder = placeHolder2
    listA.prefWidth <== scenePane.width / 2
    listB.prefWidth <== scenePane.width / 2
    listA.prefHeight <== scenePane.height - 195d
    listB.prefHeight <== scenePane.height - 195d
    val textXAxis = new TextField()
    val textYAxis = new TextField()
    val textSeriesname = new TextField()


    textXAxis.prefWidth <== listA.width
    textYAxis.prefWidth <== listB.width

    textSeriesname.prefHeight = 35d
    textXAxis.promptText = "Replace X axis name"
    textYAxis.promptText = "Replace Y axis name"
    textSeriesname.promptText = "Name of data"
    vbox1.children = List(listA, textXAxis, buttonA)
    vbox2.children = List(listB, textYAxis, buttonB)
    val sep = Separator(Orientation.Horizontal)
    realParent.children = List(hboxParent, sep, textSeriesname, endButton)


    var selections = new ObjectProperty(this, "bruh", null: javafx.scene.control.TableView.TableViewSelectionModel[GenericRow])
    var positions = ObservableBuffer[javafx.scene.control.TablePosition[_, _]]()
    positions.onChange({
      if (!buttonA.disabled.value) {
        listA.items = positions.map(x => x.getTableColumn.getCellData(x.getRow).asInstanceOf[String])
      }
      else if (!buttonB.disabled.value) {
        listB.items = positions.map(x => x.getTableColumn.getCellData(x.getRow).asInstanceOf[String])
      }

    })


    buttonA.onAction = (e: ActionEvent) => {
      textXAxis.disable = true
      buttonA.disable = true
      // println(positions.asInstanceOf[ObservableBuffer[javafx.scene.control.TablePosition[GenericRow, String]]])
      XaxisVals = positions.asInstanceOf[ObservableBuffer[javafx.scene.control.TablePosition[GenericRow, String]]].toVector
      // println("x: " + XaxisVals)
      //println("y: " + YaxisVals)
      scenePane.requestFocus()
    }
    buttonB.onAction = (e: ActionEvent) => {
      textYAxis.disable = true
      buttonB.disable = true
      //  println(positions.asInstanceOf[ObservableBuffer[javafx.scene.control.TablePosition[GenericRow, String]]])
      YaxisVals = positions.asInstanceOf[ObservableBuffer[javafx.scene.control.TablePosition[GenericRow, String]]].toVector

      //println("x: " + XaxisVals)
      //println("y: " + YaxisVals)
      scenePane.requestFocus()
    }
    endButton.onAction = (e: ActionEvent) => {
      val numberChartObject = new NumberChartObject(XaxisVals.toVector, YaxisVals.toVector)
      numberChartObject.dataSeries.name = textSeriesname.text.value
      numberChartObject.Xpositions.setAll(XaxisVals: _*)
      numberChartObject.Ypositions.setAll(YaxisVals: _*)

      if (textXAxis.text.value != "")
        numberChartObject.XAxisName.value = textXAxis.text.value

      if (textYAxis.text.value != "")
        numberChartObject.YAxisName.value = textYAxis.text.value

      numberChart.objects.add(numberChartObject)

      stage.hide()
    }


    if (originalScene.getFocusOwner != null && originalScene.getFocusOwner.getClass.getSimpleName == "TableView") {
      positions.setAll(originalScene.getFocusOwner.asInstanceOf[javafx.scene.control.TableView[GenericRow]].getSelectionModel.getSelectedCells.toVector: _*)
      selections = originalScene.getFocusOwner.asInstanceOf[javafx.scene.control.TableView[GenericRow]].selectionModel

      selections.value.getSelectedCells.onChange({

        val c = ObservableBuffer(selections.value.getSelectedCells.toVector)
        val diff = c -- positions
        val b = positions.retainAll(c.toVector)
        val d = positions.addAll(diff)

      })
    }

    originalScene.focusOwnerProperty().onChange((obs, oldV, newV) => {
      if (newV != null) {
        newV.getClass.getSimpleName match {
          case "TableView" => {

            val t = newV.asInstanceOf[javafx.scene.control.TableView[GenericRow]]
            selections = t.selectionModel


            val cc = ObservableBuffer(t.getSelectionModel.getSelectedCells.toVector)

            val diff = cc -- positions
            val bb = positions.retainAll(cc.toVector)
            val d = positions.addAll(diff)


            t.getSelectionModel.getSelectedCells.onChange({

              val ccc = ObservableBuffer(t.getSelectionModel.getSelectedCells.toVector)
              val difff = ccc -- positions
              val bbb = positions.retainAll(ccc.toVector)
              val dd = positions.addAll(difff)

            })


          }
          case _ => {}
        }
      }
    })


    stage.scene = newScene
    stage.show()


  }

}
