package OS2.File

import OS2.GUIElements._
import scalafx.stage._
import scalafx.scene.control.TableView
import scalafx.stage.FileChooser
import scalafx.Includes._
import java.io._

class DashbFilter extends FileChooser.ExtensionFilter("Dashboard", "*.dashb")
class LineCFilter extends FileChooser.ExtensionFilter("LineChart", "*.lineC")
class ScatterCFilter extends FileChooser.ExtensionFilter("ScatterChart", "*.scatterC")
class BarCFilter extends FileChooser.ExtensionFilter("BarChart", "*.barC")
class PieFilter extends FileChooser.ExtensionFilter("PieChart", "*.pieC")
class CardFilter extends FileChooser.ExtensionFilter("Card", "*.card")
class TableFilter extends FileChooser.ExtensionFilter("Table", "*.table")

object ChartFile {
  def toFile[T<: Chart](chart: T, taulut: Vector[TableView[GenericRow]], stage: Stage) = {
    val curFilter = chart match {
      case a: Scatter => new ScatterCFilter
      case b: Line => new LineCFilter
      case c: Bar => new BarCFilter
    }
    val chooser = new FileChooser()
    chooser.extensionFilters.add(curFilter)
    val file = chooser.showSaveDialog(stage)
  }
}
object GenericTableFile {

  def toFile(fileName: String, table: GenericTaulu) = {
    val ser = GenericTauluSerializable(table.data.toVector.map(_.rowValue.value.map(_.strValue.value)), table.headerStrs.toVector, table.table.id.name, table.table.width.value, table.table.height.value)
    val oos = new ObjectOutputStream(new FileOutputStream(fileName))
    oos.writeObject(ser)
    oos.close()

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