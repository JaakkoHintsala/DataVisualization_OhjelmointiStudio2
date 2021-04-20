package OS2

import scalafx.beans.property.{ObjectProperty, StringProperty}
import scalafx.event._
import scalafx.Includes._
import scalafx.geometry.Pos
import scalafx.scene.control.{ContextMenu, Label, MenuItem, TitledPane}
import scalafx.scene.text.Font

trait Card {
  val result: ObjectProperty[Number] = ObjectProperty(0: Number)
  val stringResult = StringProperty("")
  if (result != null && result.value != null) {
    if (result.value.doubleValue().isValidInt) {
      stringResult.value = result.value.intValue().toString
    }

    else {
      stringResult.value = result.value.toString
    }
  }
  result.onChange((obs, o, n) => {
    if (n != null) {
      if (n.doubleValue().isValidInt) {
        stringResult.value = n.intValue().toString
      }

      else {
        stringResult.value = n.toString
      }
    }
  })
  val con = new ContextMenu()
  val label = new Label()
  label.font = new Font(40d)
  val titled = new TitledPane()
  titled.prefWidth = 200d
  titled.prefHeight = 200d
  titled.alignment = Pos.Center
  titled.content = label
  titled.contextMenu = con
  label.text <== stringResult

  val item1 = new MenuItem()
  item1.text = "zoom +"
  val item2 = new MenuItem()
  item2.text = "zoom -"
  con.items.addAll(item1, item2)
  item1.onAction = (ae: ActionEvent) => {
    titled.prefWidth = titled.width.value + 100d
    titled.prefHeight = titled.height.value + 100d

  }
  item2.onAction = (ae: ActionEvent) => {
    titled.prefWidth = titled.width.value - 100d
    titled.prefHeight = titled.width.value - 100d

  }

}

class SumCard(cardDataObject: CardDataObject) extends Card {
  result.value = (value)

  def value: Number = {
    val a = cardDataObject.numbers.foldLeft(0: Number)(_.doubleValue() + _.doubleValue())
    a
  }

  cardDataObject.numbers.onChange({
    result.value = value
  })
}

class MaxCard(cardDataObject: CardDataObject) extends Card {
  result.value = (value)


  def value: Number = {
    val a = cardDataObject.numbers.map(_.doubleValue()).max
    a
  }

  cardDataObject.numbers.onChange({
    result.value = value
  })
}

class MinCard(cardDataObject: CardDataObject) extends Card {
  result.value = (value)

  def value: Number = {
    val a = cardDataObject.numbers.map(_.doubleValue()).min
    a
  }

  cardDataObject.numbers.onChange({
    result.value = value
  })
}

class AverageCard(cardDataObject: CardDataObject) extends Card {
  result.value = (value)

  def value: Number = {
    val a = (cardDataObject.numbers.foldLeft(0: Double)(_ + _.doubleValue())) / cardDataObject.numbers.size().toDouble
    a
  }

  cardDataObject.numbers.onChange({
    result.value = value
  })
}

class StandardDeviationCard(cardDataObject: CardDataObject) extends Card {
  result.value = (value)

  def value: Number = {
    val average: Number = (cardDataObject.numbers.foldLeft(0: Double)(_ + _.doubleValue())) / cardDataObject.numbers.size().toDouble
    val mapWithAverage = cardDataObject.numbers.map(_.doubleValue() - average.doubleValue())
    val squared = mapWithAverage.map(x => x * x)
    val meanOfSquared = (squared.foldLeft(0: Double)(_ + _)) / cardDataObject.numbers.size().toDouble
    val squareRoot = math.sqrt(meanOfSquared)
    squareRoot

  }

  cardDataObject.numbers.onChange({
    result.value = value
  })
}