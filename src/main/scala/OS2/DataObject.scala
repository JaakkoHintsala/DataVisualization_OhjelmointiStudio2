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

class NumberChartObject(xCol: Vector[javafx.scene.control.TablePosition[GenericRow, String]], yCol: Vector[javafx.scene.control.TablePosition[GenericRow, String]]) {

  var XAxisName = ObjectProperty("")
  var YAxisName = ObjectProperty("")
  val Xpositions = ObservableBuffer(xCol)
  val Ypositions = ObservableBuffer(yCol)

  val XStringProperties = ObservableBuffer(xCol.map((x: javafx.scene.control.TablePosition[GenericRow, String]) => {
    val Prop = (x: javafx.scene.control.TablePosition[GenericRow, String]).tableView.items.value(x.row).rowValue.value.apply(x.column).strValue
    Prop
  }))


  val YStringProperties = ObservableBuffer(yCol.map(x => {
    val Prop = x.tableView.items.value(x.row).rowValue.value.apply(x.column).strValue
    Prop
  }))

  Xpositions.onChange({
    val newStuff = Xpositions.toVector.map((x: javafx.scene.control.TablePosition[GenericRow, String]) => {
      val Prop = (x: javafx.scene.control.TablePosition[GenericRow, String]).tableView.items.value(x.row).rowValue.value.apply(x.column).strValue
      Prop
    })
    val a = XStringProperties.setAll(newStuff: _*)
  })
  Ypositions.onChange({
    val newStuff = Ypositions.toVector.map((x: javafx.scene.control.TablePosition[GenericRow, String]) => {
      val Prop = (x: javafx.scene.control.TablePosition[GenericRow, String]).tableView.items.value(x.row).rowValue.value.apply(x.column).strValue
      Prop
    })
    val a = YStringProperties.setAll(newStuff: _*)
  })

  def XYProps = XStringProperties.zip(YStringProperties)


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

  XStringProperties.onChange({
    dataSeries.setData(update.data.value)
    XStringProperties.foreach(x => {
      x.onChange({
        dataSeries.setData(update.data.value)
      })
    })
  })
  YStringProperties.onChange({
    dataSeries.setData(update.data.value)
    YStringProperties.foreach(x => {
      x.onChange({
        dataSeries.setData(update.data.value)
      })
    })
  })


}

class StringNumberChartObject(xCol: Vector[javafx.scene.control.TablePosition[GenericRow, String]], yCol: Vector[javafx.scene.control.TablePosition[GenericRow, String]]) {

  var XAxisName = ObjectProperty("")
  var YAxisName = ObjectProperty("")
  val Xpositions = ObservableBuffer(xCol)
  val Ypositions = ObservableBuffer(yCol)

  val XStringProperties = ObservableBuffer(xCol.map((x: javafx.scene.control.TablePosition[GenericRow, String]) => {
    val Prop = (x: javafx.scene.control.TablePosition[GenericRow, String]).tableView.items.value(x.row).rowValue.value.apply(x.column).strValue
    Prop
  }))


  val YStringProperties = ObservableBuffer(yCol.map(x => {
    val Prop = x.tableView.items.value(x.row).rowValue.value.apply(x.column).strValue
    Prop
  }))

  Xpositions.onChange({
    val newStuff = Xpositions.toVector.map((x: javafx.scene.control.TablePosition[GenericRow, String]) => {
      val Prop = (x: javafx.scene.control.TablePosition[GenericRow, String]).tableView.items.value(x.row).rowValue.value.apply(x.column).strValue
      Prop
    })
    val a = XStringProperties.setAll(newStuff: _*)
  })
  Ypositions.onChange({
    val newStuff = Ypositions.toVector.map((x: javafx.scene.control.TablePosition[GenericRow, String]) => {
      val Prop = (x: javafx.scene.control.TablePosition[GenericRow, String]).tableView.items.value(x.row).rowValue.value.apply(x.column).strValue
      Prop
    })
    val a = YStringProperties.setAll(newStuff: _*)
  })

  def XYProps = XStringProperties.zip(YStringProperties)


  def update: XYChart.Series[String, Number] = {
    var nums = Vector[(String, Number)]()
    for (pair <- XYProps) {
      if (pair._1 != null && pair._2.value.trim.toDoubleOption.nonEmpty) {
        nums = nums :+ (pair._1.value, pair._2.value.toDouble)
      }
    }


    val dataPoints = nums.map(x => {
      val initData = XYChart.Data[String, Number](x._1, x._2)
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
    val a = XYChart.Series[String, Number](ObservableBuffer(dataPoints))

    a
  }

  val dataSeries: XYChart.Series[String, Number] = update
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

  XStringProperties.onChange({
    dataSeries.setData(update.data.value)
    XStringProperties.foreach(x => {
      x.onChange({
        dataSeries.setData(update.data.value)
      })
    })
  })
  YStringProperties.onChange({
    dataSeries.setData(update.data.value)
    YStringProperties.foreach(x => {
      x.onChange({
        dataSeries.setData(update.data.value)
      })
    })
  })
}

class PieChartObject(x: Vector[javafx.scene.control.TablePosition[GenericRow, String]], y: Vector[javafx.scene.control.TablePosition[GenericRow, String]])
  extends StringNumberChartObject(x, y) {

}

class CardDataObject(x: Vector[javafx.scene.control.TablePosition[GenericRow, String]]) {
  val positions = ObservableBuffer(x)
  val stringProperties = ObservableBuffer(positions.map(x => {
    val Prop = x.tableView.items.value(x.row).rowValue.value.apply(x.column).strValue
    Prop
  }).toVector)
  positions.onChange({
    val a = stringProperties.setAll(positions.map(x => {
      val Prop = x.tableView.items.value(x.row).rowValue.value.apply(x.column).strValue
      Prop
    }).toVector: _*)
  })
  val numbers: ObservableBuffer[Number] = ObservableBuffer[Number]()

  def update() = {
    var nums = Vector[Number]()
    stringProperties.foreach(x => {
      if (x.value.toDoubleOption.nonEmpty) {
        nums = nums :+ x.value.toDouble
      }
    })

    numbers.setAll(nums: _*)
  }

  update()
  stringProperties.onChange({
    val a = update()
  })

}