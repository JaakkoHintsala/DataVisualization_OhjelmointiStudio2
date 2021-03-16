package OS2

import scalafx.beans.property.ObjectProperty
import scalafx.collections.ObservableBuffer
import scalafx.scene.control._

case class IntCell(arvo : Int)

case class IntSarake(otsake: String, vektori: ObservableBuffer[IntCell]) {

  def elem = {
    val d = vektori
    val v = new TableView(d)

     println("elem: " + v.toString() )
    val c = new TableColumn[IntCell, Int](otsake)
    c.editable = true
    c.cellValueFactory = cdf => ObjectProperty(cdf.value.arvo)
    c
  }

}


case class IntTaulukko[T](otsikko: String, sarakkeet: Vector[IntSarake]) {

  def GUIElem = {
    val data = ObservableBuffer(sarakkeet.map(_.vektori)).flatten
    val v = new TableView(data)
    v.editable = true

     println("GUI: " + v.toString() )

    v.columns ++= sarakkeet.map(_.elem)

    v.columns.foreach(x => println(x.getTableView.toString))

    v

  }
}