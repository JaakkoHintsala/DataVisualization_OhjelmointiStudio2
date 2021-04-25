package OS2.GUIElements

import javafx.scene.control.Tooltip
import scalafx.Includes._
import scalafx.beans.property.{DoubleProperty, ObjectProperty}
import scalafx.collections.ObservableBuffer
import scalafx.event.ActionEvent
import scalafx.scene.chart._
import scalafx.scene.control._

trait NumberChart extends Chart{
  val objects: ObservableBuffer[NumberChartObject]
  val chart: XYChart[Number, Number]
  val titled: TitledPane
  val xAxis: NumberAxis
  val yAxis: NumberAxis

}
trait Chart{
  val w: DoubleProperty
  val h: DoubleProperty
  val xAxisName: ObjectProperty[String]
  val yAxisName: ObjectProperty[String]

}


class Scatter(numberChartObjects: NumberChartObject*) extends NumberChart with Chart {
  val objects = ObservableBuffer(numberChartObjects)
  val titled = new TitledPane()
  val xAxis = new NumberAxis()
  val yAxis = new NumberAxis()
  val chart: XYChart[Number, Number] = new ScatterChart(xAxis, yAxis)
  val xAxisName: ObjectProperty[String] = xAxis.label
  val yAxisName: ObjectProperty[String] = yAxis.label
  val w = chart.prefWidth
  val h = chart.prefHeight
  chart.animated = false


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

case class Line(numberChartObjects: NumberChartObject*) extends NumberChart with Chart  {

  val objects = ObservableBuffer(numberChartObjects)
  val titled = new TitledPane()
  val xAxis = new NumberAxis()
  val yAxis = new NumberAxis()
  val chart: XYChart[Number, Number] = new LineChart(xAxis, yAxis)
  val xAxisName: ObjectProperty[String] = xAxis.label
  val yAxisName: ObjectProperty[String] = yAxis.label
  val w = chart.prefWidth
  val h = chart.prefHeight
  chart.animated = false


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

case class Bar(stringNumberChartObjects: StringNumberChartObject*) extends Chart {
  val objects = ObservableBuffer(stringNumberChartObjects)
  val titled = new TitledPane()
  val xAxis = new CategoryAxis()
  val yAxis = new NumberAxis()
  val chart: XYChart[String, Number] = new BarChart[String, Number](xAxis, yAxis)
  val xAxisName: ObjectProperty[String] = xAxis.label
  val yAxisName: ObjectProperty[String] = yAxis.label
  val w = chart.prefWidth
  val h = chart.prefHeight
  chart.animated = false


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
  val initData: ObservableBuffer[javafx.scene.chart.XYChart.Series[String, Number]] = (objects.map(_.dataSeries: XYChart.Series[String, Number]))
  chart.data = initData
  objects.onChange({
    val newdata: ObservableBuffer[javafx.scene.chart.XYChart.Series[String, Number]] = (objects.map(_.dataSeries: XYChart.Series[String, Number]))
    chart.data = newdata
    objects.foreach(x => {
      x.XAxisName = xAxis.label
      x.YAxisName = yAxis.label
    })
  })
}

case class Pie(stringNumberChartObject: StringNumberChartObject)  {

  val titled = new TitledPane()
  val chart = new PieChart()

  chart.animated = false


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
  // val initData: javafx.scene.chart.XYChart.Series[String, Number] = stringNumberChartObject.dataSeries: XYChart.Series[String, Number]
  // val pieData = pieChartObject.PiedataSeries


  def pieUpdate() = {

    var nums = Vector[(String, Number)]()
    for (pair <- stringNumberChartObject.XYProps) {
      if (pair._1 != null && pair._2.value.trim.toDoubleOption.nonEmpty) {
        nums = nums :+ (pair._1.value, pair._2.value.toDouble)
      }
    }

    val dataPoints = nums.map(x => {
      val initData = PieChart.Data(x._1, x._2.doubleValue())
      initData

    })
    chart.data = ObservableBuffer(dataPoints)


    for (d <- chart.data.value) {
      println(d.getNode)
      val toolT: Tooltip = new Tooltip(s"name: ${d.getName} value: ${d.getPieValue}")
      javafx.scene.control.Tooltip.install(d.getNode, toolT)
    }

  }

  pieUpdate()

  stringNumberChartObject.XStringProperties.foreach(x => {
    x.onChange({
      pieUpdate()
    })
  })

  stringNumberChartObject.YStringProperties.foreach(x => {
    x.onChange({
      pieUpdate()
    })
  })

  stringNumberChartObject.XStringProperties.onChange({
    pieUpdate()
    stringNumberChartObject.XStringProperties.foreach(x => {
      x.onChange({
        pieUpdate()
      })
    })
  })
  stringNumberChartObject.YStringProperties.onChange({
    pieUpdate()
    stringNumberChartObject.YStringProperties.foreach(x => {
      x.onChange({
        pieUpdate()
      })
    })
  })
}


