package OS2

import scalafx.beans.property.{IntegerProperty, ObjectProperty, StringProperty}
import scalafx.collections.ObservableBuffer
import scalafx.scene.chart.XYChart.Series
import scalafx.scene.chart._
import scalafx.scene.control._
import scalafx.Includes._
import scalafx.event.ActionEvent
import scalafx.scene.control.cell._
import scalafx.scene.control.cell.TextFieldTableCell.forTableColumn
import scalafx.util.converter.DefaultStringConverter

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
        col.graphic.value
        table.columns.add(col)
      }
    }

  }

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
  refresh()


}
