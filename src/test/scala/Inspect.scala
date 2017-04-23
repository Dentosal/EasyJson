import EasyJson.{EasyJson, ParseError}
import org.scalatest._


class InspectionTest extends FlatSpec {
    "JsonData" should "return correct length for a list" in {
        assert(EasyJson.loads("[]").length == 0)
        assert(EasyJson.loads("[1, 2, 3]").length == 3)
        assert(EasyJson.loads("[[]]").length == 1)
        assert(EasyJson.loads("[{}]").length == 1)
        assert(EasyJson.loads("[{}, []]").length == 2)
    }
    it should "return correct length for a string" in {
        assert(EasyJson.loads("\"\"", true).length == 0)
        assert(EasyJson.loads("\"\\\"\"", true).length == 1)
        assert(EasyJson.loads("\"abc\"", true).length == 3)
    }
    it should "return correct length for an object" in {
        assert(EasyJson.loads("{}").length == 0)
        assert(EasyJson.loads("{\"abc\": 1}").length == 1)
    }
    it should "return correct keys for an object" in {
        assert(EasyJson.loads("{}").keys == Set())
        assert(EasyJson.loads("{\"abc\": 1}").keys == Set("abc"))
        assert(EasyJson.loads("{\"abc\": 1, \"\": null}").keys == Set("abc", ""))
    }
    it should "return correct results for testing if an object contains a key" in {
        assert(!EasyJson.loads("{}").has_key(""))
        assert(EasyJson.loads("{\"\": 1}").has_key(""))
        assert(!EasyJson.loads("{}").has_key("test"))
        assert(EasyJson.loads("{\"test\": 1}").keys == Set("test"))
    }
}
