package OS2.DataChoosers

import OS2.GUIElements.GenericRow
import scalafx.geometry.Orientation
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label, ListView, ScrollPane, Separator, TextField}
import scalafx.scene.layout.{HBox, VBox}
import scalafx.stage.Stage

class Chooser {

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
    textXAxis.promptText = "X axis name"
    textYAxis.promptText = "Y axis name"
    textSeriesname.promptText = "Name of data"
    vbox1.children = List(listA, textXAxis, buttonA)
    vbox2.children = List(listB, textYAxis, buttonB)
    val sep = Separator(Orientation.Horizontal)
    realParent.children = List(hboxParent, sep, textSeriesname, endButton)

}
