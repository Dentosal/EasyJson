import EasyJson.{EasyJson, ParseError}
import org.scalatest._

class DumpTest extends FlatSpec {
    "EasyJson" should "be able to load and dump an empty list" in {
        assert(EasyJson.dumps(EasyJson.loads("[]")) == "[]")
    }
    it should "be able to load and dump an empty object" in {
        assert(EasyJson.dumps(EasyJson.loads("{}")) == "{}")
    }
    it should "be able to load and dump an empty string" in {
        assert(EasyJson.dumps(EasyJson.loads("\"\"", true)) == "\"\"")
    }
    it should "be able to load and dump zero (integer)" in {
        assert(EasyJson.dumps(EasyJson.loads("0", true)) == "0")
    }
    it should "be able to load and dump zero (float)" in {
        assert(EasyJson.dumps(EasyJson.loads("0.0", true)) == "0.0")
    }
    it should "be able to load and dump a list with one integer" in {
        assert(EasyJson.dumps(EasyJson.loads("[1234]")) == "[1234]")
    }
    it should "be able to load and dump a list with a single key" in {
        assert(EasyJson.dumps(EasyJson.loads("{\"a\":1234}")) == "{\"a\":1234}")
    }
}
