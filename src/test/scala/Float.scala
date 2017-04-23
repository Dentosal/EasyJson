import EasyJson.EasyJson
import org.scalatest._

class FlatTest extends FlatSpec {
    "EasyJson" should "be able to load 0" in {
        EasyJson.loads("0.0", true)
    }
    it should "be able to load negative zero" in {
        EasyJson.loads("-0.0", true)
    }
    it should "be able to load an approximation of pi" in {
        EasyJson.loads("3.14", true)
    }
    it should "be able to load a negative approximation of pi" in {
        EasyJson.loads("-3.14", true)
    }
}
