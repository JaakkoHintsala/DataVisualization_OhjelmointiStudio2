package OS2.File

import OS2.GUIElements._
import scalafx.stage._
import scalafx.scene.control.TableView
import scalafx.stage._
import scalafx.scene._
import scalafx.Includes._
import scalafx.scene.layout.FlowPane

import java.nio.file.Paths
import java.io.File
import java.io._

class DashbFilter extends FileChooser.ExtensionFilter("Dashboard", "*.dashb")

class LineCFilter extends FileChooser.ExtensionFilter("LineChart", "*.lineC")

class ScatterCFilter extends FileChooser.ExtensionFilter("ScatterChart", "*.scatterC")

class BarCFilter extends FileChooser.ExtensionFilter("BarChart", "*.barC")

class PieFilter extends FileChooser.ExtensionFilter("PieChart", "*.pieC")

class CardFilter extends FileChooser.ExtensionFilter("Card", "*.card")

class TableFilter extends FileChooser.ExtensionFilter("Table", "*.table")

object UniversalFileOpener {
  def openSesame(stage: Stage, flowPane: FlowPane, tbls: Vector[TableView[GenericRow]]) = {


    val chooser = new FileChooser()
    chooser.extensionFilters.addAll(new TableFilter, new DashbFilter, new LineCFilter, new ScatterCFilter, new BarCFilter, new PieFilter, new CardFilter)
    val currentPath = Paths.get(".").toAbsolutePath.normalize().toString
    chooser.setInitialDirectory(new File(currentPath))
    val selected = chooser.showOpenDialog(stage)
    if (selected != null) {
      if (selected.getAbsolutePath.endsWith(".table")) {
        val tabl = GenericTableFile.fromFile(selected.getAbsolutePath)
        flowPane.children.add(tabl.titled)
      }
      if (selected.getAbsolutePath.endsWith(".lineC") ||
        selected.getAbsolutePath.endsWith(".scatterC") ||
        selected.getAbsolutePath.endsWith(".barC")) {
        val ch = ChartFile.fromFile(selected.getAbsolutePath, tbls)
        flowPane.children.add(ch.titled)
      }
    }
  }

}

object ChartFile {
  def toFile(chart: Chart, stage: Stage) = {
    val curFilter = chart match {
      case a: Scatter => new ScatterCFilter
      case b: Line => new LineCFilter
      case c: Bar => new BarCFilter
    }
    val chooser = new FileChooser()
    chooser.extensionFilters.add(curFilter)
    val currentPath = Paths.get(".").toAbsolutePath.normalize().toString
    chooser.setInitialDirectory(new File(currentPath))
    val selected = chooser.showSaveDialog(stage)
    if (selected != null) {
      val serializible = ToSerializableConverters.chartConverter(chart)
      val oos = new ObjectOutputStream(new FileOutputStream(selected.getAbsolutePath))
      oos.writeObject(serializible)
      oos.close()
    }
  }

  def fromFile(polku: String, taulut: Vector[TableView[GenericRow]]): Chart = {
    val ois = new ObjectInputStream(new FileInputStream(polku))
    val chartti = {
      if (polku.endsWith(".lineC")) {
        ois.readObject.asInstanceOf[ChartSerializable[Line]]
      }
      else if (polku.endsWith(".scatterC")) {
        ois.readObject.asInstanceOf[ChartSerializable[Scatter]]
      }
      else {
        ois.readObject.asInstanceOf[ChartSerializable[Bar]]
      }
    }
    ois.close()

    val chart = FromSerializableConverters.ChartConverter(chartti, taulut)
    chart
  }

}

object GenericTableFile {

  def toFile(stage: Stage, table: GenericTaulu) = {
    val chooser = new FileChooser()
    chooser.extensionFilters.add(new TableFilter)
    val currentPath = Paths.get(".").toAbsolutePath.normalize().toString
    chooser.setInitialDirectory(new File(currentPath))
    val selected = chooser.showSaveDialog(stage)

    if (selected != null) {
      val serializible = GenericTauluSerializable(table.data.toVector.map(_.rowValue.value.map(_.strValue.value)), table.headerStrs.toVector, table.table.id.name, table.table.width.value, table.table.height.value)
      val oos = new ObjectOutputStream(new FileOutputStream(selected.getAbsolutePath))
      oos.writeObject(serializible)
      oos.close()
    }
  }


  def fromFile(polku: String): GenericTaulu = {
    val ois = new ObjectInputStream(new FileInputStream(polku))
    val table = ois.readObject.asInstanceOf[GenericTauluSerializable]
    ois.close()
    val a = GenericTaulu(table.vector.map(GenericRow(_)), table.initHeaders)
    a.table.prefHeight = table.height
    a.table.prefWidth = table.width
    a.table.id = table.id
    a

  }
}