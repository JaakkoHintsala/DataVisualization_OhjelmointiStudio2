package OS2

import scalafx.scene.layout.StackPane
import scalafx.scene.input.MouseEvent._
import scalafx.scene.Node
import scalafx.beans.property.{IntegerProperty, ObjectProperty, StringProperty}
import scalafx.collections.ObservableBuffer
import scalafx.scene.chart.XYChart.Series
import scalafx.scene.chart._
import scalafx.scene.control._
import scalafx.Includes._
import scalafx.event.ActionEvent
import scalafx.scene.AccessibleRole
import scalafx.scene.control.ContentDisplay._
import scalafx.scene.control.cell._
import scalafx.scene.control.cell.TextFieldTableCell.forTableColumn
import scalafx.scene.input.MouseEvent
import scalafx.util.converter.DefaultStringConverter

import java.beans.EventHandler

case class StrProp(string: String) {
  val strValue = StringProperty(string)


  def newStr(s: String) = {
    strValue.value = s
  }
}

case class GenericRow(vector: Vector[String]) {

  val propVector = vector.map(x => StrProp(x))

  val rowValue = ObjectProperty(this, "row", propVector)

  def valueAt(int: Int) = rowValue.value(int).strValue

  def addStr(string: String) = {
    rowValue.value = rowValue.value :+ StrProp(string)
  }

}

case class GenericTaulu(vector: Vector[GenericRow]) {

  val defaultSize = IntegerProperty(5)

  val data = ObservableBuffer(vector: _*)


  val table = new TableView(data)


  def addRow() = {
    if (vector.isEmpty)
      data += GenericRow(Vector.fill(defaultSize.value)(""))
    else
      data += GenericRow(Vector.fill(vector(0).rowValue.value.size)(""))

    refresh()
  }

  def addCol() = {
    for (row <- data) {
      row.addStr("")
    }

    refresh()
  }

  def refresh() = {

    table.columns.remove(0, table.columns.size)

    if (data.nonEmpty) {
      for (colIndex <- data(0).rowValue.value.indices) {


        val col = new TableColumn[GenericRow, String]()

        col.cellFactory = {
          val conv = DefaultStringConverter
          val cell = TextFieldTableCell.forTableColumn[GenericRow]()

          cell
        }
        col.cellValueFactory = _.value.valueAt(colIndex)
        col.prefWidth = 85

        col.id = s"C$colIndex"

        val label = new Label(s"col $colIndex")
        label.contentDisplay = TextOnly
        val stack = new StackPane()
        stack.id = "stack"

        stack.children.add(label)
        if (stack.lookup("#stack > .label") != null) {
          println(stack.lookup("#stack > .label").toString() + "löyty")
        }
        table.setTableMenuButtonVisible(true)


        stack.filterEvent(MouseEvent.MousePressed) {
          me: MouseEvent => {
            println("stack click")
            if (me.altDown) {
              println("alt")
              table.selectionModel.value.clearSelection()
              table.selectionModel.value.selectRange(0, col, data.size, col)
              me.consume()
            }
            if (me.clickCount > 1) {
              val text = new TextField()
              text.text = label.text.value
              text.onAction = (a: ActionEvent) => {
                label.text = text.text.value
              }
              text.focusedProperty().addListener((pro, oldV, newV) => {
                println("vals" + pro.toString + oldV + newV)
                if (!newV ) {
                  stack.children.remove(text)

                label.toFront
              }
              })
              stack.children.add(text)
              text.requestFocus()
              me.consume()

              //if (stack.lookup("#stack > .label") != null) {
                //println(stack.lookup("#stack > .label").toString() + "löyty")
              //}


            }


          }
        }
        col.graphic = stack


        table.columns.add(col)
        /*        if (table.lookup(s"#C$colIndex") != null) {
                  println(  "löyty")
                }*/
      }
    }

  }

  // table.filterEvent(MouseEvent.MousePressed

  table.selectionModel().selectionMode = SelectionMode.Multiple
  table.selectionModel().cellSelectionEnabled = true
  table.setEditable(true)
  val cmenu = new ContextMenu()
  val rowmenu = new MenuItem("Add row")
  rowmenu.onAction = {
    e: ActionEvent => {
      addRow()
    }
  }
  val colmenu = new MenuItem("Add column")
  colmenu.onAction = {
    e: ActionEvent => {
      addCol()
    }
  }
  cmenu.items ++= List(rowmenu, colmenu)
  table.contextMenu = cmenu
  // table.filterEvent()
  refresh()


}
