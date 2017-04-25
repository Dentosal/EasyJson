import EasyJson._
import org.scalatest._

class InspectionTest extends FlatSpec {
    "JsonData" should "return correct length for a list" in {
        assert(EasyJson.loads("[]").as[Vector[_]].length == 0)
        assert(EasyJson.loads("[1, 2, 3]").as[Vector[JsonInteger]].length == 3)
        assert(EasyJson.loads("[[]]").as[Vector[JsonArray]].length == 1)
        assert(EasyJson.loads("[{}]").as[Vector[JsonObject]].length == 1)
        assert(EasyJson.loads("[{}, []]").as[Vector[_]].length == 2)
    }
    it should "return correct length for a string" in {
        assert(EasyJson.loads("\"\"", true).as[String].length == 0)
        assert(EasyJson.loads("\"\\\"\"", true).as[String].length == 1)
        assert(EasyJson.loads("\"abc\"", true).as[String].length == 3)
    }
    it should "return correct length for an object" in {
        assert(EasyJson.loads("{}").as[Map[String, _]].keys.toSeq.length == 0)
        assert(EasyJson.loads("{\"abc\": 1}").as[Map[String, JsonInteger]].keys.toSeq.length == 1)
    }
    it should "return correct keys for an object" in {
        assert(EasyJson.loads("{}").as[Map[String,_]].keys == Set())
        assert(EasyJson.loads("{\"abc\": 1}").as[Map[String,_]].keys == Set("abc"))
        assert(EasyJson.loads("{\"abc\": 1, \"\": null}").as[Map[String,_]].keys == Set("abc", ""))
    }
    it should "return correct results for testing if an object contains a key" in {
        assert(!(EasyJson.loads("{}").as[Map[String,_]] contains ""))
        assert(EasyJson.loads("{\"\": 1}").as[Map[String,_]] contains "")
        assert(!(EasyJson.loads("{}").as[Map[String,_]] contains "test"))
        assert(EasyJson.loads("{\"test\": 1}").as[Map[String,_]] contains "test")
    }
}
