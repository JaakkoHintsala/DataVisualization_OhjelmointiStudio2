package OS2

import scalafx.scene.control.SortEvent._
import scalafx.scene.layout.StackPane
import scalafx.scene.input.MouseEvent._
import scalafx.scene.{AccessibleRole, Node}
import scalafx.beans.property.{IntegerProperty, ObjectProperty, StringProperty}
import scalafx.collections.ObservableBuffer
import scalafx.scene.chart.XYChart.Series
import scalafx.scene.chart._
import scalafx.scene.control._
import scalafx.Includes._
import scalafx.event.ActionEvent
import scalafx.scene.control.ContentDisplay._
import scalafx.scene.control.cell._
import scalafx.scene.control.cell.TextFieldTableCell.forTableColumn
import scalafx.scene.input.{InputMethodEvent, MouseEvent}
import scalafx.util.converter.DefaultStringConverter

import java.beans.EventHandler
import java.util.Comparator
import scala.collection.mutable

case class StrProp(string: String) {
  val strValue = StringProperty(string)


  def newStr(s: String) = {
    strValue.value = s
  }
}

case class GenericRow(vector: Vector[String]) {

  val propVector = vector.map(x => StrProp(x))

  val rowValue = ObjectProperty(this, "row", propVector)

  def valueAt(int: Int) = rowValue.value(int).strValue

  def addStr(string: String) = {
    rowValue.value = rowValue.value :+ StrProp(string)
  }

}

case class GenericTaulu(vector: Vector[GenericRow], initHeaders: Vector[String]) {

  val defaultSize = IntegerProperty(5)
  val data : ObservableBuffer[GenericRow] = ObservableBuffer(vector: _*)
  val table = new TableView(data)

  val headerfill = Vector.tabulate(vector.size)(x => s"col ${x+1}").drop(initHeaders.size)
  val headerStrs = ObservableBuffer((initHeaders ++ headerfill): _*)



  def addRow(): Unit = {
    if (vector.isEmpty)
      data += GenericRow(Vector.fill(defaultSize.value)(""))
    else
      data += GenericRow(Vector.fill(vector(0).rowValue.value.size)(""))

    refresh()
  }

  def addCol(): Unit = {
    for (row <- data) {
      row.addStr("")
    }
    val uuscol = new TableColumn[GenericRow,String]()
    headerStrs.append(s"col ${headerStrs.size + 1}")

    refresh()
  }

  def refresh() = {

    table.columns.remove(0, table.columns.size)

    if (data.nonEmpty) {
      for (colIndex <- data(0).rowValue.value.indices) {


        val col = new TableColumn[GenericRow, String]()

        col.cellFactory = {
          val conv = DefaultStringConverter
          val cell = TextFieldTableCell.forTableColumn[GenericRow]()

          cell
        }
        col.cellValueFactory = _.value.valueAt(colIndex)
        col.prefWidth = 85


        col.id = headerStrs(colIndex)

        val label = new Label(headerStrs(colIndex))
        label.text.onChange((a, oldV, newV) => {
          headerStrs.update(colIndex,newV)
        } )
        label.contentDisplay = TextOnly
        val stack = new StackPane()


        stack.children.add(label)
        if (stack.lookup("#stack > .label") != null) {
          println(stack.lookup("#stack > .label").toString() + "löyty")
        }
        table.setTableMenuButtonVisible(true)


        stack.filterEvent(MouseEvent.MousePressed) {
          me: MouseEvent => {
            println("stack click")
            if (me.controlDown) {
              table.selectionModel.value.selectRange(0, col, table.items().size(), col)
              table.refresh()
              me.consume()
            }
            if (me.clickCount > 1 && stack.children.forall(x => !(x.getClass.getSimpleName == "TextField"))) {
              val text = new TextField()
              // println("pseudoclass: " + text.pseudoClassStates + " styleclass: " + text.styleClass + " class: " + text.getClass.toString )
              text.text = label.text.value
              // text.maxWidth <== col.maxWidth
              text.onAction = (a: ActionEvent) => {
                label.text = text.text.value
              }
              text.focusedProperty().addListener((pro, oldV, newV) => {
                // println("vals" + pro.toString + oldV + newV)
                if (!newV) {
                 // println("focus " + stack.children.map(_.getClass.getSimpleName))
                  stack.children.remove(text)
                  label.toFront
                }
              })

              text.text.onChange((a, oldV, newV) => {
                label.text = newV
              })
              stack.children.add(text)
              text.requestFocus()
              me.consume()
            }
          }
        }
        col.graphic = stack
        val colcontext = new ContextMenu()
        val colrowmenu = new MenuItem("Add row")
        colrowmenu.onAction = {
          e: ActionEvent => {
            addRow()
          }
        }
        val colcolmenu = new MenuItem("Add column")
        colcolmenu.onAction = {
          e: ActionEvent => {
            addCol()
          }
        }
        val selectCol = new MenuItem("Select column")
        selectCol.onAction = {
          e: ActionEvent => {


            table.selectionModel.value.selectRange(0, col, table.items().size(), col)
            val tama = table.selectionModel.value
            table.selectionModel.update(tama)
            table.refresh()
            println(table.selectionModel.value.selectedCells)

          }
        }
        colcontext.items ++= List(selectCol, colcolmenu, colrowmenu)
        col.contextMenu = colcontext

        TableColumn.SortType.Ascending

        table.columns.add(col)

      }
    }

  }

  // table.filterEvent(MouseEvent.MousePressed

  table.selectionModel().selectionMode = SelectionMode.Multiple
  table.selectionModel().cellSelectionEnabled = true
  table.setEditable(true)
  val cmenu = new ContextMenu()
  cmenu

  val rowmenu = new MenuItem("Add row")
  rowmenu.onAction = {
    e: ActionEvent => {
      addRow()
    }
  }
  val colmenu = new MenuItem("Add column")
  colmenu.onAction = {
    e: ActionEvent => {
      addCol()
    }
  }
  cmenu.items ++= List(rowmenu, colmenu)
  // table.contextMenu = cmenu
  // table.filterEvent()
  refresh()


}
