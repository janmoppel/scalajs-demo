import intro._
import org.scalajs.dom._

object Main {

  def main(args: Array[String]): Unit = {
    Korrutaja.renderInto(document.getElementById("korrutaja"))
  }

}
