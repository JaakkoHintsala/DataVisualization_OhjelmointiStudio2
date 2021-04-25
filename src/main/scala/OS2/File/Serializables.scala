package OS2.File

import OS2.GUIElements.{Bar, Card, Chart, DataObject, GenericRow, GenericTaulu, NumberChart, Pie, StringNumberChartObject}
import OS2.{GUIElements, _}
import scalafx.beans.property.StringProperty
import scalafx.scene.control.{TableColumn, TablePosition, TableView}
import scalafx.Includes._

import java.io._

case class GenericTauluSerializable(
                                     vector: Vector[Vector[String]],
                                     initHeaders: Vector[String],
                                     id: String,
                                     width: Double,
                                     height: Double
                                   )

case class TablePosSerializable(colIndex: Int, rowIndex: Int, tableID: String, strValue: String)

case class SeriesSerializable[T <: DataObject](DOClass: Class[T], xVals: Vector[TablePosSerializable], yVals: Vector[TablePosSerializable], name: String)

case class ChartSerializable[T <: Chart](chartClass: Class[T], Serieses: Vector[SeriesSerializable[DataObject]], XaxisName: String, YaxisName: String, width: Double, height: Double)

case class PieChartSerializable(seriesSerializable: SeriesSerializable[StringNumberChartObject], width: Double, height: Double)

case class CardSerializable[T <: Card](pos: Vector[TablePosSerializable], nimi: String)






object ToSerializableConverters {

  def TablePosConverter(pos: TablePosition[GenericRow, String]): TablePosSerializable = {
    val ser = TablePosSerializable(pos.column, pos.row, pos.tableView.id.value, pos.tableView.items.getValue.get(pos.row).rowValue.value(pos.column).strValue.value)
    ser
  }

  def SeriesConverter[T <: DataObject]( dataObject: T): SeriesSerializable[T] = {
    val ret = SeriesSerializable(
      dataObject.getClass.asInstanceOf[Class[T]],
      dataObject.Xpositions.toVector.map(x => TablePosConverter(new TablePosition(x))),
      dataObject.Ypositions.toVector.map(x => TablePosConverter(new TablePosition(x))),
      dataObject.dataName.value
    )
    ret
  }

  def chartConverter[T <: Chart](chart: T): ChartSerializable[T] = {
    val ret: ChartSerializable[T] = chart match {
      case x: NumberChart => {
        ChartSerializable(
          chart.getClass.asInstanceOf[Class[T]],
          x.objects.map(y => SeriesConverter[DataObject]( y)).toVector,
          x.xAxis.label.value,
          x.yAxis.label.value,
          x.chart.width.value,
          x.chart.height.value
        )
      }
      case b: Bar => {
        ChartSerializable(
          chart.getClass.asInstanceOf[Class[T]],
          b.objects.map(y => SeriesConverter[DataObject](y)).toVector,
          b.xAxis.label.value,
          b.yAxis.label.value,
          b.chart.width.value,
          b.chart.height.value
        )
      }
    }
ret
  }

  def pieChartConverter(pie: Pie): PieChartSerializable = {
    val ret: PieChartSerializable = {
      PieChartSerializable(
        SeriesConverter(pie.stringNumberChartObject),
        pie.chart.width.value,
        pie.chart.height.value
      )
    }
    ret
  }
}


object FromSerializableConverters {
  def TablePosConverter(pos: TablePosSerializable, taulut: Vector[TableView[GenericRow]]): Option[TablePosition[GenericRow, String]] = {
    val table = taulut.find(x => x.id.value == pos.tableID)
    if (table.isEmpty) {
      None
    }
    else {
      val ret = new TablePosition[GenericRow, String](table.get, pos.rowIndex, table.get.columns.apply(pos.colIndex).asInstanceOf[TableColumn[GenericRow, String]])
      Option(ret)
    }
  }

  def SeriesConverter[T <: DataObject]( serSer: SeriesSerializable[T], taulut: Vector[TableView[GenericRow]]): T = {
    val Xs: Vector[TablePosition[GenericRow,String]] = serSer.xVals.flatMap(x => TablePosConverter(x, taulut))
    val Ys: Vector[TablePosition[GenericRow,String]]  = serSer.yVals.flatMap(x => TablePosConverter(x, taulut))
    val ret = serSer.DOClass.getConstructor(classOf[ Vector[TablePosition[GenericRow,String]] ], classOf[ Vector[TablePosition[GenericRow,String]] ]).newInstance(Xs, Ys)
    if (ret.Xpositions.isEmpty) {
      val a = ret.XStringProperties.setAll(serSer.xVals.map(_.strValue).map(x => StringProperty(x)): _*)
    }
    if (ret.Ypositions.isEmpty) {
      val a = ret.YStringProperties.setAll(serSer.yVals.map(_.strValue).map(x => StringProperty(x)): _*)
    }
    ret.dataName.value = serSer.name

    ret

  }

  def ChartConverter[T <: Chart: scala.reflect.runtime.universe.TypeTag](chartSer: ChartSerializable[T], taulut: Vector[TableView[GenericRow]]): T = {
    val serieses = chartSer.Serieses.map(x => SeriesConverter(x, taulut))
    val ret = chartSer.chartClass.getConstructor(classOf[DataObject]).newInstance(serieses)
    ret.w.value = chartSer.width
    ret.h.value = chartSer.height
    ret.xAxisName.value = chartSer.XaxisName
    ret.yAxisName.value = chartSer.YaxisName
    ret
  }

  def PieChartConverter(pieChartSer: PieChartSerializable, taulut: Vector[TableView[GenericRow]]): Pie = {
    val ret = GUIElements.Pie(SeriesConverter(pieChartSer.seriesSerializable, taulut))
    ret.chart.prefWidth = pieChartSer.width
    ret.chart.prefHeight = pieChartSer.height
    ret
  }
}
