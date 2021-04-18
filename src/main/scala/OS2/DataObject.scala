package OS2

import javafx.scene.control.Tooltip
import javafx.scene.layout.StackPane
import scalafx.beans.property.ObjectProperty
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

class NumberChartObject(xCol: Vector[javafx.scene.control.TablePosition[GenericRow, String]], yCol: Vector[javafx.scene.control.TablePosition[GenericRow, String]]) {


  val XStringProperties = xCol.map((x: javafx.scene.control.TablePosition[GenericRow, String]) => {
    val Prop = (x: javafx.scene.control.TablePosition[GenericRow, String]).tableView.items.value(x.row).rowValue.value.apply(x.column).strValue
    Prop
  })

  val YStringProperties = yCol.map(x => {
    val Prop = x.tableView.items.value(x.row).rowValue.value.apply(x.column).strValue
    Prop
  })
  println("x: " + XStringProperties)
  println("y: " + YStringProperties)
  val XYProps = XStringProperties.zip(YStringProperties)


  def update: XYChart.Series[Number, Number] = {
    var nums = Vector[(Number, Number)]()
    for (pair <- XYProps) {
      if (pair._1.value.trim.toDoubleOption.nonEmpty && pair._2.value.trim.toDoubleOption.nonEmpty) {
        nums = nums :+ (pair._1.value.toDouble, pair._2.value.toDouble)
      }
    }


    val dataPoints = nums.map(x => {
      val initData = XYChart.Data[Number, Number](x._1, x._2)
      val nodeStack = new StackPane()
      nodeStack.setMinSize(10.0, 10.0)
      initData.setNode(nodeStack)
      //println(initData.getNode)


      initData.getNode.filterEvent(MouseEvent.MousePressed) {
        e: MouseEvent => {
          // setTooltip( new Tooltip(s"x: ${initData.getXValue} y: ${initData.getYValue}"))
          println(s"x: ${initData.getXValue} y: ${initData.getYValue}")

          e.consume()
        }
      }


      val toolT: Tooltip = new Tooltip(s"x: ${initData.getXValue} y: ${initData.getYValue}")
      javafx.scene.control.Tooltip.install(initData.getNode, toolT)

      println(s"x: ${initData.getXValue} y: ${initData.getYValue}")

      initData

    })
    val a = XYChart.Series[Number, Number](ObservableBuffer(dataPoints))

    a
  }

  val dataSeries: XYChart.Series[Number, Number] = update
  val dataName = dataSeries.name

  XStringProperties.foreach(x => {
    x.onChange({
      dataSeries.setData(update.data.value)
    })
  })

  YStringProperties.foreach(x => {
    x.onChange({
      dataSeries.setData(update.data.value)
    })
  })


}


case class Scatter(numberChartObjects: NumberChartObject*) {
  val objects = ObservableBuffer(numberChartObjects)
  val xAxis = new NumberAxis()
  val yAxis = new NumberAxis()


  val scatterChart = new ScatterChart(xAxis, yAxis)
  val titled = new TitledPane()
  titled.content = scatterChart
  var oldwidth = 0.0
 titled.expanded.onChange((obs, oldV, newV) => {
    if(!newV)
      {
        oldwidth = titled.width.value
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
    scatterChart.prefWidth = scatterChart.width.value + 100d
    scatterChart.prefHeight = scatterChart.height.value + 100d

  }
  item2.onAction = (ae: ActionEvent) => {
    scatterChart.prefWidth = scatterChart.width.value - 100d
    scatterChart.prefHeight = scatterChart.height.value - 100d
  }
  titled.contextMenu = con
  val initData: ObservableBuffer[javafx.scene.chart.XYChart.Series[Number, Number]] = (objects.map(_.dataSeries: XYChart.Series[Number, Number]))
  scatterChart.data = initData
  objects.onChange( {
    val newdata : ObservableBuffer[javafx.scene.chart.XYChart.Series[Number, Number]] = (objects.map(_.dataSeries: XYChart.Series[Number, Number]))
    scatterChart.data = newdata
  } )

  scatterChart.userData = this

}