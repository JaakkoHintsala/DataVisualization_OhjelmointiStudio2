package OS2

import java.io.{BufferedReader, BufferedWriter, FileNotFoundException, FileReader, FileWriter, IOException, Reader}

object fileMuunnin {

  val header = "#2COLUMNDATA"
  val columnHeader1 = "#VALUE1"
  val columnHeader2 = "#VALUE2"
  val EOF = "#EOF"

  def toFile(fileName: String, cols: Int2x) = {
    val data = cols.sarakkeet

    try {
      val f = new FileWriter(fileName)
      val b = new BufferedWriter(f)

      try {
        b.write(header)
        b.newLine()
        b.write(columnHeader1 + " " + cols.h1)
        b.newLine()

        for (p <- data) {
          b.write(p.value1.value.toString)
          b.newLine()
        }

        b.write(columnHeader2 + " " + cols.h2)
        b.newLine()

        for (p <- data) {
          b.write(p.value2.value.toString)
          b.newLine()
        }

        b.write(EOF)

      } finally {
        b.flush()
        b.close()

        f.close()

      }

    } catch {
      case f: FileNotFoundException => println("file not found")
      case e: IOException => println("ioExeption")
    }
  }

  def fromFile(polku: String): Int2x = {


    try {
      val f = new FileReader(polku)
      val b = new BufferedReader(f)
      try {

        var valsC1 = Vector[Number]()
        var valsC2 = Vector[Number]()
        var hdC1 = ""
        var hdC2 = ""
        var curHeader = ""

        var lineTry: String = null
        var headerExists = false

        while ( {

          lineTry = b.readLine()
          lineTry != null

        }) {


          if (lineTry.startsWith("#")) {
            val cur = lineTry.split(" ").head
            curHeader = cur
            if (curHeader == columnHeader1)
              hdC1 = lineTry.split(" ").tail.headOption.getOrElse("")

            if (curHeader == columnHeader2)
              hdC2 = lineTry.split(" ").tail.headOption.getOrElse("")
          }
          curHeader match {
            case "" => {}
            case fileMuunnin.header => {
              headerExists = true
            }
            case EOF => {
              if (!headerExists) //to check if the main header existed
                throw new Exception("File not formatted correctly")
            }
            case fileMuunnin.columnHeader1 => {
              if (!headerExists)
                throw new Exception("File not formatted correctly")
              else {
                val cur = lineTry.trim.toDoubleOption
                if (cur.nonEmpty) {
                  if (cur.get.isValidInt) {
                    val num: Number = cur.get.toInt
                    valsC1 = valsC1 :+ num
                  }
                  else {
                    val num: Number = cur.get
                    valsC1 = valsC1 :+ num
                  }
                }
              }
            }
            case fileMuunnin.columnHeader2 => {
              if (!headerExists)
                throw new Exception("File not formatted correctly")
              else {
                val cur = lineTry.trim.toDoubleOption
                if (cur.nonEmpty) {
                  if (cur.get.isValidInt) {
                    val num: Number = cur.get.toInt
                    valsC2 = valsC2 :+ num
                  }
                  else {
                    val num: Number = cur.get
                    valsC2 = valsC2 :+ num
                  }
                }
              }
            }
              case _ => {}
          }
        }

        val ret = new Int2x(
          hdC1,
          hdC2,
          valsC1.zip(valsC2).map(x => IntCell(x._1, x._2))
        )
        ret

      } finally {
        b.close()
        f.close()

      }

    }

    catch {
      case f: FileNotFoundException => throw f
      case e: IOException => throw e
    }


  }

}
