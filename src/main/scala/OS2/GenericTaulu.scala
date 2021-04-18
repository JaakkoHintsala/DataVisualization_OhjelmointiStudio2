package OS2

import scalafx.scene.control.SortEvent._
import scalafx.scene.layout.StackPane
import scalafx.scene.input.MouseEvent._
import scalafx.scene.{AccessibleRole, Node}
import scalafx.beans.property.{DoubleProperty, IntegerProperty, ObjectProperty, StringProperty}
import scalafx.collections.ObservableBuffer
import scalafx.scene.chart.XYChart.Series
import scalafx.scene.chart._
import scalafx.scene.control._
import scalafx.Includes._
import scalafx.application.Platform
import scalafx.event.ActionEvent
import scalafx.scene.control.ContentDisplay._
import scalafx.scene.control.TreeTableColumn.CellEditEvent
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

  val rowHeight = DoubleProperty(30d)
  val columWidth = DoubleProperty(85d)
  val defaultcols = IntegerProperty(3)
  val defaultrows = IntegerProperty(5)
  val data: ObservableBuffer[GenericRow] = ObservableBuffer(vector: _*)
  if (data.isEmpty) {
    data ++= Vector.fill(defaultrows.value)(GenericRow(Vector.fill(defaultcols.value)("")))
  }
  val table = new TableView(data)
  table.userData = this


  val headerfill = Vector.tabulate(data.head.rowValue.value.size)(x => s"col ${x + 1}").drop(initHeaders.size)
  val headerStrs = ObservableBuffer((initHeaders ++ headerfill): _*)


  def addRow(): Unit = {

    data += GenericRow(Vector.fill(data(0).rowValue.value.size)(""))

    refresh()
  }

  def addCol(): Unit = {

    for (row <- data) {
      row.addStr("")
    }
    headerStrs.append(s"col ${headerStrs.size + 1}")

    refresh()
  }

  def add5Row() = {
    for (n <- 1 to 5) {
      data += GenericRow(Vector.fill(data(0).rowValue.value.size)(""))
    }
    refresh()
  }

  def add5Col() = {
    for (n <- 1 to 5) {
      for (row <- data) {
        row.addStr("")
      }
      headerStrs.append(s"col ${headerStrs.size + 1}")
    }
    refresh()
  }

  def refresh() = {

    table.columns.remove(0, table.columns.size)

    //println("bruhfdt" + data)

    if (data.isEmpty || data.headOption.exists(_.propVector.isEmpty)) {
      addCol()
      println("bruhhhh")
    }
    for (colIndex <- data(0).rowValue.value.indices) {

      val col = new TableColumn[GenericRow, String]()

      col.cellFactory = {
        val conv = DefaultStringConverter
        val cell = TextFieldTableCell.forTableColumn[GenericRow]()

        cell
      }
      col.cellValueFactory = _.value.valueAt(colIndex)
      col.prefWidth <== columWidth

      col.id = headerStrs(colIndex)
      col.onEditCommit = e => {
        table.requestFocus()
        e.getRowValue.rowValue.value.apply(colIndex).strValue.value = e.getNewValue
        table.refresh()
        table.selectionModel.value.select(table.items.value.indexWhere(_.eq(e.getRowValue)), col)
      }

      val label = new Label(headerStrs(colIndex))
      label.text.onChange((a, oldV, newV) => {
        headerStrs.update(colIndex, newV)
      })
      label.contentDisplay = TextOnly
      val stack = new StackPane()


      stack.children.add(label)
      /*      if (stack.lookup("#stack > .label") != null) {
              println(stack.lookup("#stack > .label").toString() + "löyty")
            }*/
      table.setTableMenuButtonVisible(false)


      stack.filterEvent(MouseEvent.MousePressed) {
        me: MouseEvent => {
          println("stack click")
          table.selectionModel.value.clearSelection()
          if (me.controlDown) {
            table.selectionModel.value.selectRange(0, col, table.items().size(), col)
            label.requestFocus() // refresh for chartValueChooser
            table.requestFocus()
            table.refresh()

            me.consume()
          }
          if (me.clickCount > 1 && stack.children.forall(x => !(x.getClass.getSimpleName == "TextField"))) {
            val text = new TextField()
            // println("pseudoclass: " + text.pseudoClassStates + " styleclass: " + text.styleClass + " class: " + text.getClass.toString )
            text.text = label.text.value

            text.onAction = (a: ActionEvent) => {
              label.text = text.text.value
              table.requestFocus()
            }
            text.focusedProperty().addListener((pro, oldV, newV) => {
              // println("vals" + pro.toString + oldV + newV)
              if (!newV) {
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
          label.requestFocus() // refresh for chartValueChooser
          table.requestFocus()

          table.refresh()
          // println(table.selectionModel.value.selectedCells)

        }
      }
      colcontext.items ++= List(selectCol, colcolmenu, colrowmenu)
      col.contextMenu = colcontext

      col.sortable = false

      table.columns.add(col)


    }


    table.prefHeight = (table.fixedCellSize.value + 0.5) * table.items.value.size() + 30d
    table.minHeight = (table.fixedCellSize.value + 0.5) * table.items.value.size() + 30d
    table.maxHeight = (table.fixedCellSize.value + 0.5) * table.items.value.size() + 30d
    data.onChange({
      println("data")
      table.prefHeight = (table.fixedCellSize.value + 0.5) * table.items.value.size() + 30d
      table.minHeight = (table.fixedCellSize.value + 0.5) * table.items.value.size() + 30d
      table.maxHeight = (table.fixedCellSize.value + 0.5) * table.items.value.size() + 30d
    })

    table.prefWidth = table.columns.size * (columWidth.value + 1d) + 15d
    table.maxWidth = table.columns.size * (columWidth.value + 1d) + 15d
    table.minWidth = table.columns.size * (columWidth.value + 1d) + 15d

    table.columns.onChange({
      println("cols")
      table.prefWidth = table.columns.size * (columWidth.value + 1d) + 15d
      table.maxWidth = table.columns.size * (columWidth.value + 1d) + 15d
      table.minWidth = table.columns.size * (columWidth.value + 1d) + 15d

    })

  }

  table.fixedCellSize <== rowHeight


  table.selectionModel().selectionMode = SelectionMode.Multiple
  table.selectionModel().cellSelectionEnabled = true
  table.setEditable(true)

  val cmenu = new ContextMenu()


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
    val row5menu = new MenuItem("Add 5 rows")
  row5menu.onAction = {
    e: ActionEvent => {
      add5Row()
    }
  }
  val col5menu = new MenuItem("Add 5 columns")
  col5menu.onAction = {
    e: ActionEvent => {
      add5Col()
    }
  }

  val item1 = new MenuItem()
  item1.text = "zoom +"
  val item2 = new MenuItem()
  item2.text = "zoom -"
  item1.onAction = (ae: ActionEvent) => {
    refresh()
    table.prefWidth = table.width.value * (1.0 / 0.7)
    table.maxWidth = table.width.value * (1.0 / 0.7)
    table.minWidth = table.width.value * (1.0 / 0.7)

    table.prefHeight = table.height.value * (1.0 / 0.7)
    table.maxHeight = table.height.value * (1.0 / 0.7)
    table.minHeight = table.height.value * (1.0 / 0.7)

  }
  item2.onAction = (ae: ActionEvent) => {
    refresh()
    table.prefWidth = table.width.value * 0.7
    table.maxWidth = table.width.value * 0.7
    table.minWidth = table.width.value * 0.7

    table.prefHeight = table.height.value * 0.7
    table.maxHeight = table.height.value * 0.7
    table.minHeight = table.height.value * 0.7
  }
  cmenu.items ++= List(rowmenu,row5menu, colmenu, col5menu, item1, item2)
  table.contextMenu = cmenu
  // table.contextMenu = cmenu
  // table.filterEvent()
  //  table.managed = false // things go wrong if this is set to false
  refresh()


}
