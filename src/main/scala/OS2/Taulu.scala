package OS2

import scalafx.beans.property.ObjectProperty
import scalafx.collections.ObservableBuffer
import scalafx.scene.chart.XYChart.Series
import scalafx.scene.chart._
import scalafx.scene.control._
import scalafx.Includes._
import scalafx.scene.control.cell._
import scalafx.scene.control.cell.TextFieldTableCell.forTableColumn
import scalafx.util.StringConverter


case class IntCell( a1 : Int, a2: Int)

case class Int2x(h1: String,  h2: String, sarakkeet: Vector[IntCell]) {

  def table = {
    val data = ObservableBuffer(sarakkeet)
    val v = new TableView(data)
    v.editable = true
    val c1 = new TableColumn[IntCell, Int](h1)
    val c2 = new TableColumn[IntCell, Int](h2)

    def fs(string: String): Int = string.toInt
    def ts(int: Int): String = int.toString

    val str = StringConverter[Int](fs,ts)

   // c1.setCellFactory(TextFieldTableCell.forTableColumn[IntCell,Int](str))


    c1.cellValueFactory = x => ObjectProperty(x.value.a1)
    c2.cellValueFactory = x => ObjectProperty(x.value.a2)
    v.columns ++= List(c1, c2)
    v
  }

  def chatter = {
    val x = NumberAxis()
    val y = NumberAxis()
   // x.setAutoRanging(true)
    val d = XYChart.Series[Number, Number]("chatter",
      ObservableBuffer(
         sarakkeet.map(x => XYChart.Data[Number, Number](x.a1, x.a2)))
    )
    val plotti = new ScatterChart(x, y, ObservableBuffer(d))
    plotti
  }


def line = {
  val x = NumberAxis()
  val y = NumberAxis()
 val d = XYChart.Series[Number, Number]("line",
      ObservableBuffer(
         sarakkeet.map(x => XYChart.Data[Number, Number](x.a1, x.a2)))
    )
  val l = new LineChart[Number, Number](x,y, ObservableBuffer(d))
  l
}

}