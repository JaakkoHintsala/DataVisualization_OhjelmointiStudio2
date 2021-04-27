package OS2.DataChoosers

import OS2.File.ChartFile
import OS2.Elements.{Bar, Card, CardDataObject, GenericRow, Line, NumberChartObject, Pie, Scatter, StringNumberChartObject, TablePosVector}
import scalafx.Includes._
import scalafx.beans.property.ObjectProperty
import scalafx.collections.ObservableBuffer
import scalafx.event.ActionEvent
import scalafx.geometry.{Insets, Orientation}
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.layout.{FlowPane, HBox, VBox}
import scalafx.stage.Stage

object ChartValueChooser {


  def popUpSceneScatter(originalScene: Scene, flowPane: FlowPane) = {
    val ch = new Chooser


    var selections = new ObjectProperty(this, "bruh", null: javafx.scene.control.TableView.TableViewSelectionModel[GenericRow])
    var positions = ObservableBuffer[javafx.scene.control.TablePosition[_, _]]()
    positions.onChange({
      if (!ch.buttonA.disabled.value) {
        ch.listA.items = positions.map(x => x.getTableColumn.getCellData(x.getRow).asInstanceOf[String])
      }
      else if (!ch.buttonB.disabled.value) {
        ch.listB.items = positions.map(x => x.getTableColumn.getCellData(x.getRow).asInstanceOf[String])
      }

    })


    ch.buttonA.onAction = (e: ActionEvent) => {
      ch.textXAxis.disable = true
      ch.buttonA.disable = true
      // println(positions.asInstanceOf[ObservableBuffer[javafx.scene.control.TablePosition[GenericRow, String]]])
      ch.XaxisVals = positions.asInstanceOf[ObservableBuffer[javafx.scene.control.TablePosition[GenericRow, String]]].toVector
      // println("x: " + XaxisVals)
      //println("y: " + YaxisVals)
      ch.scenePane.requestFocus()
    }
    ch.buttonB.onAction = (e: ActionEvent) => {
      ch.textYAxis.disable = true
      ch.buttonB.disable = true
      println(positions.asInstanceOf[ObservableBuffer[javafx.scene.control.TablePosition[GenericRow, String]]])
      ch.YaxisVals = positions.asInstanceOf[ObservableBuffer[javafx.scene.control.TablePosition[GenericRow, String]]].toVector

      //println("x: " + XaxisVals)
      //println("y: " + YaxisVals)
      ch.scenePane.requestFocus()
    }
    ch.endButton.onAction = (e: ActionEvent) => {

      val data = new NumberChartObject(TablePosVector(ch.XaxisVals.toVector), TablePosVector(ch.YaxisVals.toVector))
      data.dataSeries.name = ch.textSeriesname.text.value
      val S = new Scatter(data)
      S.titled.text = ch.textSeriesname.text.value
      S.xAxis.label = ch.textXAxis.text.value
      S.yAxis.label = ch.textYAxis.text.value
      data.YAxisName.value = ch.textXAxis.text.value
      data.YAxisName.value = ch.textYAxis.text.value

      S.contextMenu(flowPane, new Stage(originalScene.getWindow.asInstanceOf[javafx.stage.Stage]), originalScene)

      flowPane.children.add(S.titled)


      ch.stage.hide()
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

            println(t.getSelectionModel)

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


    ch.stage.scene = ch.newScene
    ch.stage.show()


  }

  def popUpSceneLine(originalScene: Scene, flowPane: FlowPane) = {
    val ch = new Chooser


    var selections = new ObjectProperty(this, "bruh", null: javafx.scene.control.TableView.TableViewSelectionModel[GenericRow])
    var positions = ObservableBuffer[javafx.scene.control.TablePosition[_, _]]()
    positions.onChange({
      if (!ch.buttonA.disabled.value) {
        ch.listA.items = positions.map(x => x.getTableColumn.getCellData(x.getRow).asInstanceOf[String])
      }
      else if (!ch.buttonB.disabled.value) {
        ch.listB.items = positions.map(x => x.getTableColumn.getCellData(x.getRow).asInstanceOf[String])
      }

    })


    ch.buttonA.onAction = (e: ActionEvent) => {
      ch.textXAxis.disable = true
      ch.buttonA.disable = true
      println(positions.asInstanceOf[ObservableBuffer[javafx.scene.control.TablePosition[GenericRow, String]]])
      ch.XaxisVals = positions.asInstanceOf[ObservableBuffer[javafx.scene.control.TablePosition[GenericRow, String]]].toVector
      // println("x: " + XaxisVals)
      //println("y: " + YaxisVals)
      ch.scenePane.requestFocus()
    }
    ch.buttonB.onAction = (e: ActionEvent) => {
      ch.textYAxis.disable = true
      ch.buttonB.disable = true
      println(positions.asInstanceOf[ObservableBuffer[javafx.scene.control.TablePosition[GenericRow, String]]])
      ch.YaxisVals = positions.asInstanceOf[ObservableBuffer[javafx.scene.control.TablePosition[GenericRow, String]]].toVector

      //println("x: " + XaxisVals)
      //println("y: " + YaxisVals)
      ch.scenePane.requestFocus()
    }
    ch.endButton.onAction = (e: ActionEvent) => {
      val data = new NumberChartObject(TablePosVector(ch.XaxisVals.toVector), TablePosVector(ch.YaxisVals.toVector))
      data.dataSeries.name = ch.textSeriesname.text.value
      val S = new Line(data)
      S.titled.text = ch.textSeriesname.text.value
      S.xAxis.label = ch.textXAxis.text.value
      S.yAxis.label = ch.textYAxis.text.value
      data.YAxisName.value = ch.textXAxis.text.value
      data.YAxisName.value = ch.textYAxis.text.value


    S.contextMenu(flowPane, new Stage(originalScene.getWindow.asInstanceOf[javafx.stage.Stage]), originalScene)


      flowPane.children.add(S.titled)


      ch.stage.hide()
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

            println(t.getSelectionModel)

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


    ch.stage.scene = ch.newScene
    ch.stage.show()


  }

  def popUpSceneBar(originalScene: Scene, flowPane: FlowPane) = {
    val ch = new Chooser


    var selections = new ObjectProperty(this, "bruh", null: javafx.scene.control.TableView.TableViewSelectionModel[GenericRow])
    var positions = ObservableBuffer[javafx.scene.control.TablePosition[_, _]]()
    positions.onChange({
      if (!ch.buttonA.disabled.value) {
        ch.listA.items = positions.map(x => x.getTableColumn.getCellData(x.getRow).asInstanceOf[String])
      }
      else if (!ch.buttonB.disabled.value) {
        ch.listB.items = positions.map(x => x.getTableColumn.getCellData(x.getRow).asInstanceOf[String])
      }

    })


    ch.buttonA.onAction = (e: ActionEvent) => {
      ch.textXAxis.disable = true
      ch.buttonA.disable = true
      println(positions.asInstanceOf[ObservableBuffer[javafx.scene.control.TablePosition[GenericRow, String]]])
      ch.XaxisVals = positions.asInstanceOf[ObservableBuffer[javafx.scene.control.TablePosition[GenericRow, String]]].toVector
      // println("x: " + XaxisVals)
      //println("y: " + YaxisVals)
      ch.scenePane.requestFocus()
    }
    ch.buttonB.onAction = (e: ActionEvent) => {
      ch.textYAxis.disable = true
      ch.buttonB.disable = true
      // println(positions.asInstanceOf[ObservableBuffer[javafx.scene.control.TablePosition[GenericRow, String]]])
      ch.YaxisVals = positions.asInstanceOf[ObservableBuffer[javafx.scene.control.TablePosition[GenericRow, String]]].toVector

      //println("x: " + XaxisVals)
      //println("y: " + YaxisVals)
      ch.scenePane.requestFocus()
    }
    ch.endButton.onAction = (e: ActionEvent) => {
      val data = new StringNumberChartObject(TablePosVector(ch.XaxisVals.toVector), TablePosVector(ch.YaxisVals.toVector))
      data.dataSeries.name = ch.textSeriesname.text.value
      val S = new Bar(data)
      S.titled.text = ch.textSeriesname.text.value
      S.xAxis.label = ch.textXAxis.text.value
      S.yAxis.label = ch.textYAxis.text.value
      data.YAxisName.value = ch.textXAxis.text.value
      data.YAxisName.value = ch.textYAxis.text.value

     S.contextMenu(flowPane, new Stage(originalScene.getWindow.asInstanceOf[javafx.stage.Stage]), originalScene)

      flowPane.children.add(S.titled)


      ch.stage.hide()
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

            println(t.getSelectionModel)

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


    ch.stage.scene = ch.newScene
    ch.stage.show()


  }

  def popUpScenePie(originalScene: Scene, flowPane: FlowPane) = {
    val ch = new Chooser


    var selections = new ObjectProperty(this, "bruh", null: javafx.scene.control.TableView.TableViewSelectionModel[GenericRow])
    var positions = ObservableBuffer[javafx.scene.control.TablePosition[_, _]]()
    positions.onChange({
      if (!ch.buttonA.disabled.value) {
        ch.listA.items = positions.map(x => x.getTableColumn.getCellData(x.getRow).asInstanceOf[String])
      }
      else if (!ch.buttonB.disabled.value) {
        ch.listB.items = positions.map(x => x.getTableColumn.getCellData(x.getRow).asInstanceOf[String])
      }

    })


    ch.buttonA.onAction = (e: ActionEvent) => {
      ch.textXAxis.disable = true
      ch.buttonA.disable = true
      println(positions.asInstanceOf[ObservableBuffer[javafx.scene.control.TablePosition[GenericRow, String]]])
      ch.XaxisVals = positions.asInstanceOf[ObservableBuffer[javafx.scene.control.TablePosition[GenericRow, String]]].toVector
      // println("x: " + XaxisVals)
      //println("y: " + YaxisVals)
      ch.scenePane.requestFocus()
    }
    ch.buttonB.onAction = (e: ActionEvent) => {
      ch.textYAxis.disable = true
      ch.buttonB.disable = true
      println(positions.asInstanceOf[ObservableBuffer[javafx.scene.control.TablePosition[GenericRow, String]]])
      ch.YaxisVals = positions.asInstanceOf[ObservableBuffer[javafx.scene.control.TablePosition[GenericRow, String]]].toVector

      //println("x: " + XaxisVals)
      //println("y: " + YaxisVals)
      ch.scenePane.requestFocus()
    }
    ch.endButton.onAction = (e: ActionEvent) => {
      val data = new StringNumberChartObject(TablePosVector(ch.XaxisVals.toVector), TablePosVector(ch.YaxisVals.toVector))
      data.dataSeries.name = ch.textSeriesname.text.value
      val S = new Pie(data)
      S.titled.text = ch.textSeriesname.text.value
      S.contextMenu(flowPane, new Stage(originalScene.getWindow.asInstanceOf[javafx.stage.Stage]), originalScene)
      flowPane.children.add(S.titled)


      ch.stage.hide()
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

            println(t.getSelectionModel)

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


    ch.stage.scene = ch.newScene
    ch.stage.show()


  }

  def popUpSceneCard[T <: Card](cardClass: Class[T], originalScene: Scene, flowPane: FlowPane): Unit = {

    val stage = new Stage()
    stage.width = 250d
    stage.height = 600d
    stage.alwaysOnTop = true
    val scenePane = new ScrollPane()
    val newScene = new Scene(scenePane)
    scenePane.fitToWidth = true
    val hboxParent = new HBox()
    val endButton = new Button("Make card")
    val realParent = new VBox()

    realParent.spacing = 10d
    endButton.prefWidth <== scenePane.width / 3
    endButton.prefHeight = 40d
    realParent.alignment = scalafx.geometry.Pos.TopCenter
    realParent.children = List(hboxParent, endButton)
    scenePane.content = realParent
    val vbox1 = new VBox()

    hboxParent.children.addAll(vbox1)
    hboxParent.spacing = 20d
    vbox1.spacing = 10d

    vbox1.fillWidth = true
    vbox1.alignment = scalafx.geometry.Pos.TopCenter


    val placeHolder1 = new Label("Nothing selected")
    val listA = new ListView[String]()
    listA.margin = Insets(20d)
    var XaxisVals = Vector[javafx.scene.control.TablePosition[GenericRow, String]]()
    listA.placeholder = placeHolder1
    listA.prefWidth <== scenePane.width

    listA.prefHeight <== scenePane.height - 150d


    val textSeriesname = new TextField()
    textSeriesname.prefHeight = 20d
    textSeriesname.margin = Insets(0d, 20d, 0, 20d)


    textSeriesname.promptText = "Name of data"
    vbox1.children = List(listA)

    val sep = Separator(Orientation.Horizontal)
    realParent.children = List(hboxParent, sep, textSeriesname, endButton)


    var selections = new ObjectProperty(this, "bruh", null: javafx.scene.control.TableView.TableViewSelectionModel[GenericRow])
    var positions = ObservableBuffer[javafx.scene.control.TablePosition[_, _]]()
    positions.onChange({

      listA.items = positions.map(x => x.getTableColumn.getCellData(x.getRow).asInstanceOf[String])

    })


    endButton.onAction = (e: ActionEvent) => {
      XaxisVals = positions.asInstanceOf[ObservableBuffer[javafx.scene.control.TablePosition[GenericRow, String]]].toVector
      val data = new CardDataObject(XaxisVals.toVector)
      data.dataName.value = textSeriesname.text.value


      val S: Card = cardClass.getConstructor(classOf[CardDataObject]).newInstance(data)

      S.titled.text = textSeriesname.text.value


      S.contextMenu(flowPane, new Stage(originalScene.getWindow.asInstanceOf[javafx.stage.Stage]), originalScene)

      flowPane.children.add(S.titled)


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

            println(t.getSelectionModel)

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
