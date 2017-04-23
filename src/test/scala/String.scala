import EasyJson.EasyJson
import org.scalatest._

class StringTest extends FlatSpec {
    "EasyJson" should "be able to load an empty string" in {
        EasyJson.loads("\"\"", true)
    }
    it should "be able to load a string with one space" in {
        EasyJson.loads("\" \"", true)
    }
    it should "be able to load a string with example name" in {
        EasyJson.loads("\"Example Name\"", true)
    }
    it should "be able to load a string with some data" in {
        EasyJson.loads("\"a b.32 asf!!\"", true)
    }
    it should "be able to load a string only an integer" in {
        EasyJson.loads("\"42\"", true)
    }
    it should "be able to load a string only a float" in {
        EasyJson.loads("\"3.14\"", true)
    }
    it should "be able to load a string with escaped chars" in {
        EasyJson.loads("\"\\\"abc\"", true)
    }
}
