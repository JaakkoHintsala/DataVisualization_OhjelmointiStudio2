package OS2

import javafx.scene.control.Tooltip

import javafx.scene.layout.StackPane
import scalafx.beans.property.ObjectProperty
import scalafx.collections.ObservableBuffer
import scalafx.scene.chart.XYChart.Series
import scalafx.scene.chart._
import scalafx.scene.control._
import scalafx.Includes._
import scalafx.scene.control.cell._
import scalafx.scene.control.cell.TextFieldTableCell.forTableColumn
import scalafx.scene.input.MouseEvent
import scalafx.util.StringConverter

class NumberChartObject(taulu: GenericTaulu, colIndexRowIndexForX: Vector[(Int, Int)], colIndexRowIndexForY: Vector[(Int, Int)]) {
  val XStringProperties = colIndexRowIndexForX.map(x => {
    val Prop = taulu.data(x._2).rowValue.value(x._1).strValue
    Prop
  })

  val YStringProperties = colIndexRowIndexForY.map(x => {
    val Prop = taulu.data(x._2).rowValue.value(x._1).strValue
    Prop
  })
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
     val a =  XYChart.Series[Number, Number](ObservableBuffer(dataPoints))

    a
  }

  val dataSeries: XYChart.Series[Number, Number] = update

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


case class Scatter(tauluS: GenericTaulu, colIndexRowIndexForXS: Vector[(Int, Int)], colIndexRowIndexForYS: Vector[(Int, Int)])
  extends NumberChartObject(tauluS, colIndexRowIndexForXS, colIndexRowIndexForYS) {
  val xAxis = new NumberAxis()
  val yAxis = new NumberAxis()
  val scatterChart = new ScatterChart(xAxis, yAxis)
  scatterChart.data = dataSeries
  scatterChart.userData = this

}