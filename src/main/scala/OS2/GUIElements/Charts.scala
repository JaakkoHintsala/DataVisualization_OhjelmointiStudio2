package OS2.GUIElements

import OS2.DataChoosers.{ChartNewSeries, ChartValueUpdater}
import OS2.File.ChartFile
import javafx.scene.control.Tooltip
import org.graalvm.compiler.phases.common.NodeCounterPhase.Stage
import scalafx.Includes._
import scalafx.beans.property.{DoubleProperty, ObjectProperty}
import scalafx.collections.ObservableBuffer
import scalafx.event.ActionEvent
import scalafx.scene.Scene
import scalafx.scene.chart._
import scalafx.scene.control._
import scalafx.scene.layout.FlowPane
import scalafx.stage.Stage

trait NumberChart extends Chart {
  val objects: ObservableBuffer[NumberChartObject]
  val chart: XYChart[Number, Number]
  val titled: TitledPane
  val xAxis: NumberAxis
  val yAxis: NumberAxis

  def numContextMenu(flowPane: FlowPane, stage: scalafx.stage.Stage, scene: Scene) = {
     val seriesAdderMenu = new MenuItem("Add new series")
      seriesAdderMenu.onAction = (ae: ActionEvent) => {
        ChartNewSeries.popUpScene(scene, this)
      }

      val SeriesUpdateMenu = new Menu("Update data")
      for (obj <- objects) {
        val menuitem = new MenuItem()
        menuitem.text <== obj.dataSeries.name
        menuitem.onAction = (ae: ActionEvent) => {
          ChartValueUpdater.popUpSceneNumber(scene, obj)
        }
        SeriesUpdateMenu.items.addAll(menuitem)
      }
      objects.onChange({
        SeriesUpdateMenu.items.clear()
        for (obj <- objects) {
          val menuitem = new MenuItem()
          menuitem.text <== obj.dataSeries.name
          menuitem.onAction = (ae: ActionEvent) => {
            ChartValueUpdater.popUpSceneNumber(scene, obj)
          }
          SeriesUpdateMenu.items.addAll(menuitem)
        }
      })

    for(data <- objects)
   {
          data.XAxisName.onChange({
        println("Xchooser")
        chart.getXAxis.label = data.XAxisName.value

        val a = objects.foreach(x => {
          if (x.XAxisName.value != data.XAxisName.value) {
            x.XAxisName.value = data.XAxisName.value
          }
        })
      })

      data.YAxisName.onChange({
        println("Ychooser")
        chart.getYAxis.label = data.YAxisName.value
        val a = objects.foreach(x => {
          if (x.YAxisName.value != data.YAxisName.value) {
            x.YAxisName.value = data.YAxisName.value
          }
        })
      })

   }
    con.items.addAll(SeriesUpdateMenu, seriesAdderMenu)
  }

}

trait Chart {
  val chart: scalafx.scene.chart.Chart
  val w: DoubleProperty
  val h: DoubleProperty
  val xAxisName: ObjectProperty[String]
  val yAxisName: ObjectProperty[String]
  lazy val titled = new TitledPane()
  lazy val con = new ContextMenu()
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

 def contextMenu(flowPane: FlowPane, stage: scalafx.stage.Stage) = {



      val deletesYeetus = new MenuItem("Delete")
      deletesYeetus.onAction = ((ae: ActionEvent) => {
        val a = flowPane.children.removeAll(titled)
      })


      val save = new MenuItem("Save")
      save.onAction = (ae: ActionEvent) => {
        ChartFile.toFile(this, stage)
      }

      con.items.addAll(deletesYeetus, save)
  }

}


case class Scatter(numberChartObjects: NumberChartObject*) extends NumberChart with Chart {
  val a = numberChartObjects
  val objects = ObservableBuffer(numberChartObjects)
  val xAxis = new NumberAxis()
  val yAxis = new NumberAxis()
  val chart: XYChart[Number, Number] = new ScatterChart(xAxis, yAxis)
  val xAxisName: ObjectProperty[String] = xAxis.label
  val yAxisName: ObjectProperty[String] = yAxis.label
  val w = chart.prefWidth
  val h = chart.prefHeight
  chart.animated = false

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

case class Line(numberChartObjects: NumberChartObject*) extends NumberChart with Chart {

  val objects = ObservableBuffer(numberChartObjects)

  val xAxis = new NumberAxis()
  val yAxis = new NumberAxis()
  val chart: XYChart[Number, Number] = new LineChart(xAxis, yAxis)
  val xAxisName: ObjectProperty[String] = xAxis.label
  val yAxisName: ObjectProperty[String] = yAxis.label
  val w = chart.prefWidth
  val h = chart.prefHeight
  chart.animated = false



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

  val xAxis = new CategoryAxis()
  val yAxis = new NumberAxis()
  val chart: XYChart[String, Number] = new BarChart[String, Number](xAxis, yAxis)
  val xAxisName: ObjectProperty[String] = xAxis.label
  val yAxisName: ObjectProperty[String] = yAxis.label
  val w = chart.prefWidth
  val h = chart.prefHeight
  chart.animated = false



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

  def stringNumContextMenu(flowPane: FlowPane, stage: scalafx.stage.Stage, scene: Scene) = {
     val seriesAdderMenu = new MenuItem("Add new series")
      seriesAdderMenu.onAction = (ae: ActionEvent) => {
        ChartNewSeries.popUpSceneBar(scene, this)
      }

      val SeriesUpdateMenu = new Menu("Update data")
      for (obj <- objects) {
        val menuitem = new MenuItem()
        menuitem.text <== obj.dataSeries.name
        menuitem.onAction = (ae: ActionEvent) => {
          ChartValueUpdater.popUpSceneStringNumber(scene, obj)
        }
        SeriesUpdateMenu.items.addAll(menuitem)
      }
      objects.onChange({
        SeriesUpdateMenu.items.clear()
        for (obj <- objects) {
          val menuitem = new MenuItem()
          menuitem.text <== obj.dataSeries.name
          menuitem.onAction = (ae: ActionEvent) => {
            ChartValueUpdater.popUpSceneStringNumber(scene, obj)
          }
          SeriesUpdateMenu.items.addAll(menuitem)
        }
      })

    for(data <- objects)
   {
          data.XAxisName.onChange({
        println("Xchooser")
        chart.getXAxis.label = data.XAxisName.value

        val a = objects.foreach(x => {
          if (x.XAxisName.value != data.XAxisName.value) {
            x.XAxisName.value = data.XAxisName.value
          }
        })
      })

      data.YAxisName.onChange({
        println("Ychooser")
        chart.getYAxis.label = data.YAxisName.value
        val a = objects.foreach(x => {
          if (x.YAxisName.value != data.YAxisName.value) {
            x.YAxisName.value = data.YAxisName.value
          }
        })
      })

   }
    con.items.addAll(SeriesUpdateMenu, seriesAdderMenu)
  }
}

case class Pie(stringNumberChartObject: StringNumberChartObject) {

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


