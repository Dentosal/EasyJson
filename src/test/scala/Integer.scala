import EasyJson.EasyJson
import org.scalatest._

class IntegerTest extends FlatSpec {
    "EasyJson" should "be able to load 0" in {
        EasyJson.loads("0", true)
    }
    it should "be able to load 1" in {
        EasyJson.loads("1", true)
    }
    it should "be able to load -1" in {
        EasyJson.loads("-1", true)
    }
    it should "be able to load 42" in {
        EasyJson.loads("42", true)
    }
    it should "be able to load -42" in {
        EasyJson.loads("-42", true)
    }
}
