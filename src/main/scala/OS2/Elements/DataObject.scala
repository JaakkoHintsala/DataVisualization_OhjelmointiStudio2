package OS2.Elements

import javafx.scene.control.Tooltip
import javafx.scene.layout.StackPane
import scalafx.Includes._
import scalafx.beans.property.{ObjectProperty, StringProperty}
import scalafx.collections.ObservableBuffer
import scalafx.scene.chart._
import scalafx.scene.input.MouseEvent

trait DataObject {

  val Xpositions :ObservableBuffer[javafx.scene.control.TablePosition[GenericRow, String]]
  val Ypositions : ObservableBuffer[javafx.scene.control.TablePosition[GenericRow, String]]
  val dataName: StringProperty
  val XStringProperties: ObservableBuffer[StringProperty]
  val YStringProperties: ObservableBuffer[StringProperty]

}
case class TablePosVector(col: Vector[javafx.scene.control.TablePosition[GenericRow, String]])

class NumberChartObject(xCol: TablePosVector, yCol: TablePosVector) extends DataObject {

  var XAxisName = ObjectProperty("")
  var YAxisName = ObjectProperty("")
  val Xpositions = ObservableBuffer(xCol.col)
  val Ypositions = ObservableBuffer(yCol.col)

  val XStringProperties = ObservableBuffer(xCol.col.map((x: javafx.scene.control.TablePosition[GenericRow, String]) => {
    val Prop = (x: javafx.scene.control.TablePosition[GenericRow, String]).tableView.items.value(x.row).rowValue.value.apply(x.column).strValue
    Prop
  }))


  val YStringProperties = ObservableBuffer(yCol.col.map(x => {
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
val extraData = (x._2.doubleValue() / nums.map(_._2.doubleValue()).max) * 100

      val toolT: Tooltip = new Tooltip(s"x: ${initData.getXValue} y: ${initData.getYValue} ${extraData.toInt}% from the max value")


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

class StringNumberChartObject(xCol: TablePosVector, yCol: TablePosVector) extends DataObject{

  var XAxisName = ObjectProperty("")
  var YAxisName = ObjectProperty("")
  val Xpositions = ObservableBuffer(xCol.col)
  val Ypositions = ObservableBuffer(yCol.col)

  val XStringProperties = ObservableBuffer(xCol.col.map((x: javafx.scene.control.TablePosition[GenericRow, String]) => {
    val Prop = (x: javafx.scene.control.TablePosition[GenericRow, String]).tableView.items.value(x.row).rowValue.value.apply(x.column).strValue
    Prop
  }))


  val YStringProperties = ObservableBuffer(yCol.col.map(x => {
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



      initData.getNode.filterEvent(MouseEvent.MousePressed) {
        e: MouseEvent => {



          e.consume()
        }
      }
      val extraData = (x._2.doubleValue() / nums.map(_._2.doubleValue().abs).sum) * 100

      val toolT: Tooltip = new Tooltip(s"x: ${initData.getXValue} y: ${initData.getYValue} ${extraData.toInt}% from the max value")
      javafx.scene.control.Tooltip.install(initData.getNode, toolT)




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


class CardDataObject(x: Vector[javafx.scene.control.TablePosition[GenericRow, String]])  {
  val dataName = StringProperty("")
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
  stringProperties.foreach(x => {
    val a = update()
    x.onChange({
      val a = update()
    })
  })
  stringProperties.onChange({
    val a = update()
    stringProperties.foreach(x => {
      val a = update()
      x.onChange({
        val a = update()
      })
    })
  })

}