package OS2

import java.io._
case class GenericTauluSerializable(vector: Vector[Vector[String]], initHeaders: Vector[String])

object GenericTableConverter {

  val mainHeader = "#GENERICTAULUDATA"
  val headersHeader = "#HEADERS"
  val rowHeader = "#ROW"
  val EOF = "#EOF"

  def toFile(fileName: String, table: GenericTaulu) = {
    val ser = GenericTauluSerializable(table.data.toVector.map(_.rowValue.value.map(_.strValue.value)), table.headerStrs.toVector)
    val oos = new ObjectOutputStream(new FileOutputStream(fileName))
    oos.writeObject(ser)
    oos.close
    /*val data = table.data.toVector
    val headers = table.headerStrs.toVector

    try {
      val f = new FileWriter(fileName)
      val b = new BufferedWriter(f)

      try {
        b.write(mainHeader)
        b.newLine()
        b.write(headersHeader)
        b.newLine()
        for (hed <- headers) {
          b.write(hed)
          b.newLine()
        }
        for (line <- data) {
          b.write(rowHeader)
          b.newLine()
          for (word <- line.rowValue.value.map(_.strValue.value )) {
            b.write(word)
            b.newLine()
          }
        }
        b.write(EOF)

      } finally {
        b.flush()
        b.close()

        f.close()

      }

    }

    catch {
      case f: FileNotFoundException => println("file not found")
      case e: IOException => println("ioExeption")
    }*/
  }


  def fromFile(polku: String): GenericTaulu = {
    val ois = new ObjectInputStream(new FileInputStream(polku))
    val table = ois.readObject.asInstanceOf[GenericTauluSerializable]
    ois.close
    GenericTaulu(table.vector.map(GenericRow(_)),table.initHeaders)

   /* try {
      val f = new FileReader(polku)
      val b = new BufferedReader(f)
      try {

        var rows = Vector[GenericRow]()
        var currentrow = Vector[String]()
        var colheaders = Vector[String]()

        var curIndex = 0
        var curHeader = ""

        var lineTry: String = null
        var headerExists = false

        while ( {

          lineTry = b.readLine()
          lineTry != null

        }) {
          if (lineTry.startsWith("#")) {
            if (lineTry == mainHeader) {
              curHeader = lineTry
            }
            else if (lineTry == EOF) {
              rows = rows :+ GenericRow(currentrow)

              curHeader = lineTry
            }
            else if (lineTry == rowHeader) {

              if(currentrow.nonEmpty){
                rows = rows :+ GenericRow(currentrow)
                currentrow = Vector()
              }
              curHeader = lineTry
            }
            else {

              curIndex = 0
              curHeader = lineTry
            }
          }
          curHeader match {

            case GenericTableConverter.mainHeader => {
              headerExists = true
            }
            case EOF => {
              if (!headerExists) //to check if the main header existed
                throw new Exception("File not formatted correctly")
            }
            case GenericTableConverter.rowHeader => {
              if(lineTry != rowHeader){

                currentrow = currentrow :+ lineTry
                println("current row" + currentrow)
              }
            }
            case GenericTableConverter.headersHeader => {
              if(lineTry != headersHeader){
                colheaders = colheaders :+ lineTry
              }
            }

            case _ => {
            }
          }
        }

        val ret = new GenericTaulu(rows, colheaders)
        ret

      } finally {
        b.close()
        f.close()

      }

    }

    catch {
      case f: FileNotFoundException => throw f
      case e: IOException => throw e
    }*/


  }

}
