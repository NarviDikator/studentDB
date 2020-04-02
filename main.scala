import java.io.{FileInputStream, FileOutputStream}
import java.io.{ObjectOutputStream, ObjectInputStream}
import scala.io.Source
import scala.collection.mutable.ArrayBuffer
import scala.io.StdIn.{readInt, readLine}

object StudentsDB {
  var students = ArrayBuffer.empty[Student]

  def printMenu(): Unit = {
    println("""Menu:
      |1) Show all students
      |2) Add new student
      |3) Edit student
      |4) Delete student
      |5) Save students
      |6) Load students
      |7) Exit""".stripMargin)
  }

  def printDB(): Unit = {
    print("Select sorting mode (1=id, 2=name, 3=age, 4=raiting, default=no sorting): ")

    val sortedStudents = try {
      readInt() match {
        case 1 => students.sortBy(_.id)
        case 2 => students.sortBy(_.name)
        case 3 => students.sortBy(_.age)
        case 4 => students.sortBy(_.raiting)
        case _ => students
      }
    } catch {
      case _: NumberFormatException => students
    }

    println("ID\t\tName\t\tAge\t\tRaiting")
    sortedStudents.foreach(s => {
      printf(s"${s.id}\t\t${s.name}\t\t${s.age}\t\t${s.raiting}\n")
    })
  }

  def readStudentData(): Student = {
    print("ID: ")
    val id = readLine()

    print("Name: ")
    val name = readLine()

    print("Age: ")
    val age = readInt()

    print("Raiting: ")
    val raiting = readInt()

    Student(id, name, age, raiting)
  }

  def addToDb(): Unit = {
    students.append(readStudentData())
  }

  def updateRecord(): Unit = {
    val newStudent = readStudentData()

    students.indexWhere(_.id == newStudent.id) match {
      case -1 => println(s"Student with ID ${newStudent.id} was not found!")
      case i  => students(i) = newStudent
    }
  }

  def deleteFromDb(): Unit = {
    print("ID: ")
    val id = readInt()

    students.indexWhere(_.id == id) match {
      case -1 => println(s"Wrong ID ${id}")
      case i  => students.remove(i)
    }
  }

  def save(): Unit = {
      print("File name: ")
      val file = readLine()
      val os = new ObjectOutputStream(new FileOutputStream(file))
      os.writeObject(students)
      os.close()
  }

  def load(): Unit = {
      print("File name: ")
      val file = readLine()
      val is = new ObjectInputStream(new FileInputStream(file))
      val obj = is.readObject.asInstanceOf[ArrayBuffer[Student]]
      students = obj
      is.close()
  }

  def main(args: Array[String]): Unit = {
    while (true) {
      printMenu()

      print("Select item: ")
      readInt() match {
        case 1 => printDB()
        case 2 => addToDb()
        case 3 => updateRecord()
        case 4 => deleteFromDb()
        case 5 => save()
        case 6 => load()
        case 7 =>
          System.exit(0)
        case _ =>
      }

    }
  }
}
