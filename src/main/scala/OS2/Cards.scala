package OS2

import scalafx.beans.property.{ObjectProperty, StringProperty}
import scalafx.geometry.Pos
import scalafx.scene.control.{Label, TitledPane}

trait Card {
  val result: ObjectProperty[Number]
  val stringResult = StringProperty("")
  if (result.value != null) {
    stringResult.value = result.value.toString
  }
  result.onChange((obs, o, n) => {
    if (n != null) {
      stringResult.value = n.toString
    }
  })

  val label = new Label()
  val titled = new TitledPane()
  titled.alignment = Pos.Center
  titled.content = label
  label.text <== stringResult

}

case class SumCard(cardDataObject: CardDataObject) extends Card {
  val result: ObjectProperty[Number] = ObjectProperty(value)

  def value: Number = {
    val a = cardDataObject.numbers.foldLeft(0: Number)(_.doubleValue() + _.doubleValue())
    a
  }

  cardDataObject.numbers.onChange({
    result.value = value
  })
}

case class MaxCard(cardDataObject: CardDataObject) extends Card {
  val result: ObjectProperty[Number] = ObjectProperty(value)

  def value: Number = {
    val a = cardDataObject.numbers.map(_.doubleValue()).max
    a
  }

  cardDataObject.numbers.onChange({
    result.value = value
  })
}

case class MinCard(cardDataObject: CardDataObject) extends Card {
  val result: ObjectProperty[Number] = ObjectProperty(value)

  def value: Number = {
    val a = cardDataObject.numbers.map(_.doubleValue()).min
    a
  }

  cardDataObject.numbers.onChange({
    result.value = value
  })
}

case class AverageCard(cardDataObject: CardDataObject) extends Card {
  val result: ObjectProperty[Number] = ObjectProperty(value)

  def value: Number = {
    val a = (cardDataObject.numbers.foldLeft(0: Double)(_.doubleValue() + _.doubleValue())) / cardDataObject.numbers.size().toDouble
    a
  }

  cardDataObject.numbers.onChange({
    result.value = value
  })
}

case class StandardDeviationCard(cardDataObject: CardDataObject) extends Card {
  val result: ObjectProperty[Number] = ObjectProperty(value)

  def value: Number = {
    val average = (cardDataObject.numbers.foldLeft(0: Double)(_.doubleValue() + _.doubleValue())) / cardDataObject.numbers.size().toDouble
    val mapWithAverage = cardDataObject.numbers.map( _.doubleValue() - average.doubleValue())
    val squared = mapWithAverage.map(x => x * x)
    val meanOfSquared = (squared.foldLeft(0: Double)(_.doubleValue() + _.doubleValue())) / cardDataObject.numbers.size().toDouble
    val squareRoot = math.sqrt(meanOfSquared)
    squareRoot

  }

  cardDataObject.numbers.onChange({
    result.value = value
  })
}