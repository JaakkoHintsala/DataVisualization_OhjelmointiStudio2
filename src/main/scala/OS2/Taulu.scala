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


case class IntCell(a1: Int, a2: Int) {
  val value1 = new ObjectProperty(this, "1", a1)

  val value2 = new ObjectProperty(this, "1", a2)
}

case class Int2x(h1: String, h2: String, sarakkeet: Vector[IntCell]) {

  val data = ObservableBuffer(sarakkeet: _*)

  def printti() = data.foreach(x => {
    println("value1: " + x.value1); println("value2: " + x.value2)
  })

  val d = XYChart.Series[Number, Number](

    data.map(x => XYChart.Data[Number, Number](x.value1.value, x.value2.value))
  )

  def table = {

    val v = new TableView(data)
    v.editable = true

    val c1 = new TableColumn[IntCell, Int](h1)
    val c2 = new TableColumn[IntCell, Int](h2)
    c1.id = h1
    c2.id = h2
// turns out that values get updated by default
/*    c1.onEditCommit = { e =>
     // e.getRowValue.value1.value = e.getNewValue
      println("edit success")
    }
    c2.onEditCommit = { e =>
    //  e.getRowValue.value2.value = e.getNewValue
      println("edit success")
    }*/

    def fs(string: String): Int = string.toInt

    def ts(int: Int): String = int.toString

    val str = StringConverter[Int](fs, ts)

    c1.cellFactory = {
      val cell = TextFieldTableCell.forTableColumn[IntCell, Int](str)

      cell
    }
    c2.cellFactory = {
      val cell = TextFieldTableCell.forTableColumn[IntCell, Int](str)

      cell
    }
    c1.cellValueFactory = _.value.value1
    c2.cellValueFactory = _.value.value2
    v.columns ++= List(c1, c2)
    v
  }

  def chatter = {
    val x = NumberAxis()
    val y = NumberAxis()
    x.label = h1
    y.label = h2


    val plotti = new ScatterChart(x, y, ObservableBuffer(d))
    plotti.title = "chatteri"
    plotti
  }


  def line = {

    val x = NumberAxis()
    val y = NumberAxis()
    x.label = h1
    y.label = h2
    val d = XYChart.Series[Number, Number](

      data.map(x => XYChart.Data[Number, Number](x.value1.value, x.value2.value)))

    val l = new LineChart[Number, Number](x, y, ObservableBuffer(d))
    l.title = "viiva"
    l
  }

}