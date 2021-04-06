package OS2

// import javafx.util.StringConverter

import scalafx.beans.property.ObjectProperty
import scalafx.collections.ObservableBuffer
import scalafx.scene.chart.XYChart.Series

import scalafx.scene.chart._
import scalafx.scene.control._
import scalafx.Includes._
import scalafx.scene.control.cell._
import scalafx.scene.control.cell.TextFieldTableCell.forTableColumn
import scalafx.util.StringConverter


case class IntCell(a1: Number, a2: Number) {
  val value1 = new ObjectProperty(this, "val1", a1)

  val value2 = new ObjectProperty(this, "val2", a2)
}

case class Int2x(h1: String, h2: String, sarakkeet: Vector[IntCell]) {

  val data = ObservableBuffer(sarakkeet: _*)

  def printti() = data.foreach(x => {
    println("value1: " + x.value1)
    println("value2: " + x.value2)
  })

  val d = XYChart.Series[Number, Number](

    data.map(x => XYChart.Data[Number, Number](x.value1.value, x.value2.value))
  )

  def updateChart() = {
    d.dataProperty.setValue(data.map(x => XYChart.Data[Number, Number](x.value1.value, x.value2.value)))
    line.setData(ObservableBuffer(d))
    chatter.setData(ObservableBuffer(d))
    // chatter = chatterr // doesnt work
  }

  def table = {

    val v = new TableView(data)
    v.editable = true

    val c1 = new TableColumn[IntCell, Number](h1)
    val c2 = new TableColumn[IntCell, Number](h2)
    c1.id = h1
    c2.id = h2

    v.tableMenuButtonVisible = true
    // turns out that values get updated by default
    c1.onEditCommit = { e =>
      e.getRowValue.value1.value = e.getNewValue
      updateChart()
      println("edit success")
    }
    c2.onEditCommit = { e =>
      e.getRowValue.value2.value = e.getNewValue
      updateChart()
      println("edit success")
    }

    def fs(string: String): Number = string.toInt

    def ts(int: Number): String = int.toString

    val str = StringConverter[Number](fs, ts)

    c1.cellFactory = {
      val cell = TextFieldTableCell.forTableColumn[IntCell, Number](str)

      cell
    }
    c2.cellFactory = {
      val cell = TextFieldTableCell.forTableColumn[IntCell, Number](str)

      cell
    }
    c1.cellValueFactory = _.value.value1
    c2.cellValueFactory = _.value.value2

    v.columns ++= List(c1, c2)
    v.selectionModel().selectionMode = SelectionMode.Multiple
    v.selectionModel().cellSelectionEnabled = true


    v.focusModel.value.focusedCellProperty().onChange((obs, oldVal, newVal) => {
      if(newVal.getTableColumn != null){

      val alku : TableColumnBase[IntCell,_] = v.columns(newVal.getColumn)
      v.selectionModel.value.selectRange(0, alku, v.items().size(), alku)
       println("Selected TableColumn: "+ newVal.getTableColumn.text)
       println("Selected column index: "+ newVal.getColumn)
      }
    })

     v.selectionModel.apply.selectedItem.onChange {
        println("Selected" + v.selectionModel.apply.getSelectedItems + " index: " + v.selectionModel.value.getFocusedIndex)
      }


    v
  }

  def chatterr = {
    val x = new NumberAxis
    val y = new NumberAxis
    x.label = h1
    y.label = h2

    val aaa = ObservableBuffer(d)

//aaa.addListener(
    val plotti = new ScatterChart(x, y, ObservableBuffer(d))
    plotti.title = "chatteri"
    plotti
  }


  def linee = {

    val x = new NumberAxis
    val y = new NumberAxis
    x.label = h1
    y.label = h2


    val l = new LineChart[Number, Number](x, y, ObservableBuffer(d))

    l.title = "viiva"
    l
  }

  val line = linee
  val chatter = chatterr

}