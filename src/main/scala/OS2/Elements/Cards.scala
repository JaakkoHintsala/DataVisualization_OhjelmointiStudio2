package OS2.Elements

import OS2.DataChoosers.ChartValueUpdater
import OS2.File.{CardFile, Saveable}
import scalafx.Includes._
import scalafx.beans.property.{ObjectProperty, StringProperty}
import scalafx.event._
import scalafx.geometry.Pos
import scalafx.scene.Scene
import scalafx.scene.control.{ContextMenu, Label, Menu, MenuItem, TitledPane}
import scalafx.scene.layout.FlowPane
import scalafx.scene.text.Font

trait Card extends Saveable{
  val cardDataObject: CardDataObject
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
  titled.userData = this
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

  def contextMenu(flowPane: FlowPane, stage: scalafx.stage.Stage, scene: Scene) = {
    val SeriesUpdateMenu = new Menu("Update data")

    val menuitem = new MenuItem()
    menuitem.text <== cardDataObject.dataName
    menuitem.onAction = (ae: ActionEvent) => {
      ChartValueUpdater.popUpSceneCardNums(scene, cardDataObject)
    }
    SeriesUpdateMenu.items.addAll(menuitem)
    val deletesYeetus = new MenuItem("Delete")
    deletesYeetus.onAction = ((ae: ActionEvent) => {
      val a = flowPane.children.removeAll(titled)
    })

    val save = new MenuItem("Save")
    save.onAction = (ae: ActionEvent) => {
    CardFile.toFile( stage, this)
    }
    con.items.addAll(deletesYeetus, SeriesUpdateMenu, save)
  }

}

class SumCard(val cardDataObject: CardDataObject) extends Card {
  result.value = (value)

  def value: Number = {
    val a = cardDataObject.numbers.foldLeft(0: Number)(_.doubleValue() + _.doubleValue())
    a
  }

  cardDataObject.numbers.onChange({
    result.value = value
  })
}

class MaxCard(val cardDataObject: CardDataObject) extends Card {
  result.value = (value)


  def value: Number = {
    val a = cardDataObject.numbers.map(_.doubleValue()).max
    a
  }

  cardDataObject.numbers.onChange({
    result.value = value
  })
}

class MinCard(val cardDataObject: CardDataObject) extends Card {
  result.value = (value)

  def value: Number = {
    val a = cardDataObject.numbers.map(_.doubleValue()).min
    a
  }

  cardDataObject.numbers.onChange({
    result.value = value
  })
}

class AverageCard(val cardDataObject: CardDataObject) extends Card {
  result.value = (value)

  def value: Number = {
    val a = (cardDataObject.numbers.foldLeft(0: Double)(_ + _.doubleValue())) / cardDataObject.numbers.size().toDouble
    a
  }

  cardDataObject.numbers.onChange({
    result.value = value
  })
}

class StandardDeviationCard(val cardDataObject: CardDataObject) extends Card {
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