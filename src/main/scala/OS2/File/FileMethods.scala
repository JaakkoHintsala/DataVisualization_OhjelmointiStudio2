package OS2.File

import OS2.Elements._
import scalafx.stage._
import scalafx.scene.control.{MenuItem, TableView}
import scalafx.stage._
import scalafx.scene._
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.event.ActionEvent
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

class AllFilter extends FileChooser.ExtensionFilter("DashBoard Object", Seq("*.table", "*.card", "*.pieC", "*.barC", "*.scatterC", "*.lineC", "*.dashb"))

object UniversalFileOpener {

  def openSesame(stage: Stage, flowPane: FlowPane, tbls: Vector[TableView[GenericRow]], scene: Scene) = {

    try {
      val chooser = new FileChooser()
      chooser.extensionFilters.addAll(new AllFilter)
      val currentPath = Paths.get(".").toAbsolutePath.normalize().toString
      chooser.setInitialDirectory(new File(currentPath))

      val selected = chooser.showOpenDialog(stage)
      if (selected != null) {
        if (selected.getAbsolutePath.endsWith(".table")) {
          val taulu = GenericTableFile.fromFile(selected.getAbsolutePath)
          val savemenu = new MenuItem("Save Table")
          savemenu.onAction = (ae: ActionEvent) => {
            OS2.File.GenericTableFile.toFile(stage, taulu)

          }
          taulu.cmenu.items.add(savemenu)
          val newTable = taulu.table

          val deletesYeetus = new MenuItem("Delete")
          deletesYeetus.onAction = ((ae: ActionEvent) => {
            val a = flowPane.children.removeAll(taulu.titled)
          })

          taulu.cmenu.items.add(deletesYeetus)
          flowPane.children.add(taulu.titled)
        }
        if (selected.getAbsolutePath.endsWith(".lineC") ||
          selected.getAbsolutePath.endsWith(".scatterC") ||
          selected.getAbsolutePath.endsWith(".barC")) {
          val ch = ChartFile.fromFile(selected.getAbsolutePath, tbls)
          ch.contextMenu(flowPane, stage, scene)

          flowPane.children.add(ch.titled)
        }
        if (selected.getAbsolutePath.endsWith(".pieC")) {
          val pie = PieChartFile.fromFile(selected.getAbsolutePath, tbls)
          pie.contextMenu(flowPane, stage, scene)
          flowPane.children.add(pie.titled)
        }
        if (selected.getAbsolutePath.endsWith(".card")) {
          val c = CardFile.fromFile(selected.getAbsolutePath, tbls)
          c.contextMenu(flowPane, stage, scene)
          flowPane.children.add(c.titled)
        }
        if (selected.getAbsolutePath.endsWith(".dashb")) {
          DashBoardFile.fromFile(selected.getAbsolutePath, stage, flowPane, tbls, scene)
        }
      }
    }
    catch {
      case fe: IOException => println("io exeption during opening a file")
      case e: Exception => println("some other exeption during opening a file")
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

      val serializible = GenericTauluSerializable(table.data.toVector.map(_.rowValue.value.map(_.strValue.value)), table.headerStrs.toVector, table.table.id.value, table.table.width.value, table.table.height.value)
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

object PieChartFile {

  def fromFile(polku: String, taulut: Vector[TableView[GenericRow]]): Pie = {
    val ois = new ObjectInputStream(new FileInputStream(polku))
    val ret = ois.readObject.asInstanceOf[PieChartSerializable]
    ois.close()
    FromSerializableConverters.PieChartConverter(ret, taulut)
  }


  def toFile(stage: Stage, pie: Pie) = {
    val chooser = new FileChooser()
    chooser.extensionFilters.add(new PieFilter)
    val currentPath = Paths.get(".").toAbsolutePath.normalize().toString
    chooser.setInitialDirectory(new File(currentPath))
    val selected = chooser.showSaveDialog(stage)
    if (selected != null) {
      val serializible = ToSerializableConverters.pieChartConverter(pie)
      val oos = new ObjectOutputStream(new FileOutputStream(selected.getAbsolutePath))
      oos.writeObject(serializible)
      oos.close()
    }
  }
}

object CardFile {

  def fromFile(polku: String, taulut: Vector[TableView[GenericRow]]) = {
    val ois = new ObjectInputStream(new FileInputStream(polku))
    val ret = ois.readObject.asInstanceOf[CardSerializable]
    ois.close()
    FromSerializableConverters.CardConverter(ret, taulut)
  }

  def toFile(stage: Stage, card: Card) = {
    val chooser = new FileChooser()
    chooser.extensionFilters.add(new CardFilter)
    val currentPath = Paths.get(".").toAbsolutePath.normalize().toString
    chooser.setInitialDirectory(new File(currentPath))
    val selected = chooser.showSaveDialog(stage)
    if (selected != null) {
      val serializible = ToSerializableConverters.CardConverter(card)
      val oos = new ObjectOutputStream(new FileOutputStream(selected.getAbsolutePath))
      oos.writeObject(serializible)
      oos.close()
    }
  }
}

object DashBoardFile {
  def toFile(stage: Stage, vector: Vector[Saveable]) {
    val chooser = new FileChooser()
    chooser.extensionFilters.add(new DashbFilter)
    val currentPath = Paths.get(".").toAbsolutePath.normalize().toString
    chooser.setInitialDirectory(new File(currentPath))
    val selected = chooser.showSaveDialog(stage)
    if (selected != null) {

      val ser = ToSerializableConverters.DashboardConverter(vector)
      val oos = new ObjectOutputStream(new FileOutputStream(selected.getAbsolutePath))
      oos.writeObject(ser)
      oos.close()
    }
  }

  def fromFile(polku: String, stage: Stage, flowPane: FlowPane, tbls: Vector[TableView[GenericRow]], scene: Scene) = {
    val ois = new ObjectInputStream(new FileInputStream(polku))
    val ret = ois.readObject.asInstanceOf[DashBoardSerializable]
    ois.close()
    val tupla = FromSerializableConverters.DashboardConverter(ret, stage, flowPane, tbls, scene)
    val gui = new GUI {

      tables = tupla._2.map(_.table)
      flowPane.children = (tupla._1.map(_.titled).toList)
    }

    gui.stage.show()
  }
}