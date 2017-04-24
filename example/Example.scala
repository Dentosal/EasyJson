import EasyJson._

import shapeless._
import shapeless.syntax.typeable._
import scala.reflect._

object Example extends App {
    def main() = {
        val d1 = EasyJson.load("example.json");
        val map = d1.as[Map[String,JsonData]];
        val vec = map("Ints").as[Vector[JsonInteger]];
        println(vec(3).value*2) // prints 8

        val d2 = EasyJson.loads("[1234, {\"test\": 2}]");
        println(EasyJson.dumps(d2));
    }

    main()
}
