import EasyJson.{EasyJson, JsonData}
import collection.JavaConverters._

object Example extends App {
    // val d = EasyJson.load("simple.json");

    val d = EasyJson.load("example.json");
    println(d.select("Arrays", 1, 1).to[Int].get*2);

    println(EasyJson.dumps(d));
}
