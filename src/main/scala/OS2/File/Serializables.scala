package OS2.File

import OS2.Elements.{Bar, Card, Chart, DataObject, GenericRow, GenericTaulu, Line, NumberChart, NumberChartObject, Pie, Scatter, StringNumberChartObject, TablePosVector}
import OS2.Elements._
import scalafx.beans.property.StringProperty
import scalafx.scene.control.{TableColumn, TablePosition, TableView, TitledPane}
import scalafx.Includes._

import java.io._

trait Saveable {
  val titled: TitledPane
}

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

case class CardSerializable(tyyppi: String, pos: Vector[TablePosSerializable], nimi: String, width: Double, height: Double)

case class DashBoardSerializable(sers: Vector[Serializable])


object ToSerializableConverters {

  def DashboardConverter(vector: Vector[Saveable]): DashBoardSerializable = {
    val ret = vector.map {
      case card: Card => {
        CardConverter(card)
      }
      case chart: Chart => {
        chartConverter(chart)
      }
      case pie: Pie => {
        pieChartConverter(pie)
      }
      case table: GenericTaulu => {
        val serializible = GenericTauluSerializable(table.data.toVector.map(_.rowValue.value.map(_.strValue.value)), table.headerStrs.toVector, table.table.id.name, table.table.width.value, table.table.height.value)
        serializible
      }
    }
    DashBoardSerializable(ret.map(_.asInstanceOf[Product with Serializable]))
  }

  def TablePosConverter(pos: TablePosition[GenericRow, String]): TablePosSerializable = {
    val ser = TablePosSerializable(pos.column, pos.row, pos.tableView.id.value, pos.tableView.items.getValue.get(pos.row).rowValue.value(pos.column).strValue.value)
    ser
  }

  def SeriesConverter[T <: DataObject](dataObject: T): SeriesSerializable[T] = {
    val ret = SeriesSerializable(
      dataObject.getClass.asInstanceOf[Class[T]],
      dataObject.Xpositions.toVector.map(x => TablePosConverter(new TablePosition(x))),
      dataObject.Ypositions.toVector.map(y => TablePosConverter(new TablePosition(y))),
      dataObject.dataName.value
    )
    ret
  }

  def chartConverter[T <: Chart](chart: T): ChartSerializable[T] = {
    val ret: ChartSerializable[T] = chart match {
      case x: NumberChart => {
        ChartSerializable(
          chart.getClass.asInstanceOf[Class[T]],
          x.objects.map(y => {
            println("x: " + y.Xpositions)
            println("y: " + y.Ypositions)
            println(y.dataSeries)
            val a = SeriesConverter[DataObject](y)
            println("x: " + a.xVals)
            println("y: " + a.yVals)
            a
          }).toVector,
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

  def CardConverter(card: Card): CardSerializable = {


    val tyypp = {
      if (card.getClass.toString.endsWith("SumCard")) {
        "SumCard"
      }
      else if (card.getClass.toString.endsWith("MinCard")) {
        "MinCard"
      }
      else if (card.getClass.toString.endsWith("MaxCard")) {
        "MaxCard"
      }
      else if (card.getClass.toString.endsWith("AverageCard")) {
        "AverageCard"
      }
      else {
        "StandardDeviationCard"
      }
    }

    val positions = card.cardDataObject.positions.map(x => {
      TablePosConverter(x)
    }).toVector

    CardSerializable(tyypp, positions, card.titled.text.value, card.titled.width.value, card.titled.height.value)
  }
}


object FromSerializableConverters {

  def DashboardConverter(dashBoardSerializable: DashBoardSerializable): (Vector[Saveable], Vector[GenericTaulu]) = {
    var t = Vector[GenericTaulu]()
    var ret = Vector[Saveable]()

    for (ser <- dashBoardSerializable.sers) {
      ser match {
        case taulu: GenericTaulu =>
          t = t :+ taulu
        case _ =>
      }
    }
    val tables = t.map(_.table)


    for (ser <- dashBoardSerializable.sers) {
          ser match {
      case card: CardSerializable => {
        ret = ret :+ CardConverter(card, tables)
      }
      case chart: ChartSerializable[_] => {
        ret = ret :+ ChartConverter(chart, tables)
      }
      case pie: PieChartSerializable => {
        ret = ret :+ PieChartConverter(pie, tables)
      }
      case table: GenericTauluSerializable => {
        val a = GenericTaulu(table.vector.map(GenericRow(_)), table.initHeaders)
        a.table.prefHeight = table.height
        a.table.prefWidth = table.width
        a.table.id = table.id

        ret = ret :+ a
      }
      case _ =>

    }}
    (ret, t)
  }

  def TablePosConverter(pos: TablePosSerializable, taulut: Vector[TableView[GenericRow]]): Option[TablePosition[GenericRow, String]] = {
    val table = taulut.find(x => x.id.value == pos.tableID)
    if (table.isEmpty) {
      None
    }
    else {
      try {
        val ret = new TablePosition[GenericRow, String](table.get, pos.rowIndex, table.get.columns.apply(pos.colIndex).asInstanceOf[javafx.scene.control.TableColumn[GenericRow, String]])
        Option(ret)
      } catch {
        case e: Exception => None
      }
    }
  }

  def SeriesConverter[T <: DataObject](serSer: SeriesSerializable[T], taulut: Vector[TableView[GenericRow]]): T = {
    val Xs = serSer.xVals.flatMap(x => TablePosConverter(x, taulut)).map(_.delegate)
    val Ys = serSer.yVals.flatMap(x => TablePosConverter(x, taulut)).map(_.delegate)
    val ret = serSer.DOClass.getConstructor(TablePosVector(Vector()).getClass, TablePosVector(Vector()).getClass).newInstance(TablePosVector(Xs), TablePosVector(Ys))
    if (ret.Xpositions.isEmpty) {
      val a = ret.XStringProperties.setAll(serSer.xVals.map(_.strValue).map(x => StringProperty(x)): _*)
    }
    if (ret.Ypositions.isEmpty) {
      val a = ret.YStringProperties.setAll(serSer.yVals.map(_.strValue).map(x => StringProperty(x)): _*)
    }
    ret.dataName.value = serSer.name

    ret

  }

  def ChartConverter[T <: Chart](chartSer: ChartSerializable[T], taulut: Vector[TableView[GenericRow]]): T = {
    val serieses = chartSer.Serieses.map(x => SeriesConverter(x, taulut))
    val ret: Chart = chartSer.chartClass match {
      case a if a.getName.endsWith("Bar") => new Bar(serieses.map(_.asInstanceOf[StringNumberChartObject]): _*)
      case b if b.getName.endsWith("Scatter") => new Scatter(serieses.map(_.asInstanceOf[NumberChartObject]): _*)
      case c if c.getName.endsWith("Line") => new Line(serieses.map(_.asInstanceOf[NumberChartObject]): _*)
    }

    //chartSer.chartClass.getConstructor(classOf[DataObject].asSubclass(classOf[DataObject])).newInstance(serieses)
    ret.w.value = chartSer.width
    ret.h.value = chartSer.height
    ret.xAxisName.value = chartSer.XaxisName
    ret.yAxisName.value = chartSer.YaxisName
    ret.asInstanceOf[T]
  }

  def PieChartConverter(pieChartSer: PieChartSerializable, taulut: Vector[TableView[GenericRow]]): Pie = {
    val ret = Pie(SeriesConverter(pieChartSer.seriesSerializable, taulut))
    ret.chart.prefWidth = pieChartSer.width
    ret.chart.prefHeight = pieChartSer.height
    ret
  }

  def CardConverter(cardSerializable: CardSerializable, taulut: Vector[TableView[GenericRow]]): Card = {
    val data = new CardDataObject(cardSerializable.pos.flatMap(x => TablePosConverter(x, taulut)).map(_.delegate))
    val c: Card = cardSerializable.tyyppi match {
      case "SumCard" => {
        new SumCard(data)
      }
      case "MinCard" => {
        new MinCard(data)
      }
      case "MaxCard" => {
        new MaxCard(data)
      }
      case "AverageCard" => {
        new AverageCard(data)
      }
      case "StandardDeviationCard" => {
        new StandardDeviationCard(data)
      }
    }

    c.titled.text = cardSerializable.nimi
    c.titled.prefWidth = cardSerializable.width
    c.titled.prefHeight = cardSerializable.height
    c
  }
}
