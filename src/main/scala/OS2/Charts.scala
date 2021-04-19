package OS2

import javafx.scene.control.Tooltip
import javafx.scene.layout.StackPane
import scalafx.beans.property.{ObjectProperty, StringProperty}
import scalafx.collections.ObservableBuffer
import scalafx.scene.chart.XYChart.Series
import scalafx.scene.chart._
import scalafx.scene.control._
import scalafx.Includes._
import scalafx.event.ActionEvent
import scalafx.scene.control.cell._
import scalafx.scene.control.cell.TextFieldTableCell.forTableColumn
import scalafx.scene.input.MouseEvent
import scalafx.util.StringConverter

case class Scatter(numberChartObjects: NumberChartObject*) extends NumberChart {
  val objects = ObservableBuffer(numberChartObjects)
  val titled = new TitledPane()
  val xAxis = new NumberAxis()
  val yAxis = new NumberAxis()
  val chart = new ScatterChart(xAxis, yAxis)


  var oldwidth = 0.0
  titled.expanded.onChange((obs, oldV, newV) => {
    if (!newV) {
      oldwidth = chart.width.value
      titled.prefWidth = 5d
    }
    else {
      titled.prefWidth = oldwidth
    }
  })


  val con = new ContextMenu()
  val item1 = new MenuItem()
  item1.text = "zoom +"
  val item2 = new MenuItem()
  item2.text = "zoom -"
  con.items.addAll(item1, item2)
  item1.onAction = (ae: ActionEvent) => {
    chart.prefWidth = chart.width.value + 100d
    chart.prefHeight = chart.height.value + 100d
    titled.prefWidth = chart.width.value + 100d
    titled.prefHeight = chart.height.value + 100d

  }
  item2.onAction = (ae: ActionEvent) => {
    chart.prefWidth = chart.width.value - 100d
    chart.prefHeight = chart.width.value - 100d
    titled.prefWidth = chart.width.value - 100d
    titled.prefHeight = chart.height.value - 100d
  }
  chart.userData = this

  titled.content = chart
    titled.contextMenu = con
  val initData: ObservableBuffer[javafx.scene.chart.XYChart.Series[Number, Number]] = (objects.map(_.dataSeries: XYChart.Series[Number, Number]))
  chart.data = initData
  objects.onChange({
    val newdata: ObservableBuffer[javafx.scene.chart.XYChart.Series[Number, Number]] = (objects.map(_.dataSeries: XYChart.Series[Number, Number]))
    chart.data = newdata
    objects.foreach(x => {
      x.XAxisName = xAxis.label
      x.YAxisName = yAxis.label
    })
  })

}

case class Line(numberChartObjects: NumberChartObject*) extends NumberChart {
  val objects = ObservableBuffer(numberChartObjects)
  val titled = new TitledPane()
  val xAxis = new NumberAxis()
  val yAxis = new NumberAxis()
  val chart = new LineChart(xAxis, yAxis)


  var oldwidth = 0.0
  titled.expanded.onChange((obs, oldV, newV) => {
    if (!newV) {
      oldwidth = chart.width.value
      titled.prefWidth = 5d
    }
    else {
      titled.prefWidth = oldwidth
    }
  })


  val con = new ContextMenu()
  val item1 = new MenuItem()
  item1.text = "zoom +"
  val item2 = new MenuItem()
  item2.text = "zoom -"
  con.items.addAll(item1, item2)
  item1.onAction = (ae: ActionEvent) => {
    chart.prefWidth = chart.width.value + 100d
    chart.prefHeight = chart.height.value + 100d
    titled.prefWidth = chart.width.value + 100d
    titled.prefHeight = chart.height.value + 100d

  }
  item2.onAction = (ae: ActionEvent) => {
    chart.prefWidth = chart.width.value - 100d
    chart.prefHeight = chart.width.value - 100d
    titled.prefWidth = chart.width.value - 100d
    titled.prefHeight = chart.height.value - 100d
  }
  chart.userData = this

  titled.content = chart
  titled.contextMenu = con
  val initData: ObservableBuffer[javafx.scene.chart.XYChart.Series[Number, Number]] = (objects.map(_.dataSeries: XYChart.Series[Number, Number]))
  chart.data = initData
  objects.onChange({
    val newdata: ObservableBuffer[javafx.scene.chart.XYChart.Series[Number, Number]] = (objects.map(_.dataSeries: XYChart.Series[Number, Number]))
    chart.data = newdata
    objects.foreach(x => {
      x.XAxisName = xAxis.label
      x.YAxisName = yAxis.label
    })
  })
}
