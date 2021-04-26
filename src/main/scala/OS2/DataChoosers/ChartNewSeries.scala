package OS2.DataChoosers

import OS2.GUIElements.{Bar, GenericRow, NumberChart, NumberChartObject, StringNumberChartObject, TablePosVector}
import OS2._
import scalafx.beans.property.ObjectProperty
import scalafx.collections.ObservableBuffer
import scalafx.event.ActionEvent
import scalafx.geometry.Orientation
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.layout.{HBox, VBox}
import scalafx.stage.Stage
import scalafx.Includes._

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
    textXAxis.text = numberChart.chart.getYAxis.label.value
    textYAxis.text = numberChart.chart.getXAxis.label.value
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
      val numberChartObject = new NumberChartObject(TablePosVector(XaxisVals.toVector), TablePosVector(YaxisVals.toVector))
      numberChartObject.dataSeries.name = textSeriesname.text.value
      numberChartObject.Xpositions.setAll(XaxisVals: _*)
      numberChartObject.Ypositions.setAll(YaxisVals: _*)
      numberChartObject.XAxisName.value = textXAxis.text.value
      numberChartObject.YAxisName.value = textYAxis.text.value
      numberChart.objects.foreach(x => {
        if (x.XAxisName.value != numberChartObject.XAxisName.value) {
          println("set series X")
          x.XAxisName.value = numberChartObject.XAxisName.value
        }
      })
      numberChart.objects.foreach(x => {
        println("set series Y")
        if (x.YAxisName.value != numberChartObject.YAxisName.value) {
          x.YAxisName.value = numberChartObject.YAxisName.value
        }
      })

      numberChartObject.XAxisName.onChange({
        println("update series")
        numberChart.objects.foreach(x => {
          if (x.XAxisName.value != numberChartObject.XAxisName.value) {
            x.XAxisName.value = numberChartObject.XAxisName.value
          }
        })
      })
      numberChartObject.YAxisName.onChange({
        numberChart.objects.foreach(x => {
          if (x.YAxisName.value != numberChartObject.YAxisName.value) {
            x.YAxisName.value = numberChartObject.YAxisName.value
          }
        })
      })


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

  def popUpSceneBar(originalScene: Scene, bar: Bar) = {
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
    textXAxis.text = bar.chart.getYAxis.label.value
    textYAxis.text = bar.chart.getXAxis.label.value
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
      println(positions.asInstanceOf[ObservableBuffer[javafx.scene.control.TablePosition[GenericRow, String]]])
      YaxisVals = positions.asInstanceOf[ObservableBuffer[javafx.scene.control.TablePosition[GenericRow, String]]].toVector
      scenePane.requestFocus()
    }
    endButton.onAction = (e: ActionEvent) => {
      val stringNumberChartObject = new StringNumberChartObject(TablePosVector(XaxisVals.toVector), TablePosVector(YaxisVals.toVector))
      stringNumberChartObject.dataSeries.name = textSeriesname.text.value
      stringNumberChartObject.Xpositions.setAll(XaxisVals: _*)
      stringNumberChartObject.Ypositions.setAll(YaxisVals: _*)
      stringNumberChartObject.XAxisName.value = textXAxis.text.value
      stringNumberChartObject.YAxisName.value = textYAxis.text.value
      bar.objects.foreach(x => {
        if (x.XAxisName.value != stringNumberChartObject.XAxisName.value) {
          println("set series X")
          x.XAxisName.value = stringNumberChartObject.XAxisName.value
        }
      })
      bar.objects.foreach(x => {
        println("set series Y")
        if (x.YAxisName.value != stringNumberChartObject.YAxisName.value) {
          x.YAxisName.value = stringNumberChartObject.YAxisName.value
        }
      })

      stringNumberChartObject.XAxisName.onChange({
        println("update series")
        bar.objects.foreach(x => {
          if (x.XAxisName.value != stringNumberChartObject.XAxisName.value) {
            x.XAxisName.value = stringNumberChartObject.XAxisName.value
          }
        })
      })
      stringNumberChartObject.YAxisName.onChange({
        bar.objects.foreach(x => {
          if (x.YAxisName.value != stringNumberChartObject.YAxisName.value) {
            x.YAxisName.value = stringNumberChartObject.YAxisName.value
          }
        })
      })

      bar.objects.add(stringNumberChartObject)

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
