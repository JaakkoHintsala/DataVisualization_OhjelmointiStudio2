package OS2

import scalafx.beans.property.ObjectProperty
case class GenericRow(vector: Vector[String]){

  val rowValue = ObjectProperty(this, "row", vector)


}

case class GenericTaulu(vector: Vector[GenericRow]) {


}
