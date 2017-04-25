import EasyJson._
import org.scalatest._

class SelectTest extends FlatSpec {
    "JsonData.select" should "be able to select an element from an object by key" in {
        assert(EasyJson.loads("{\"a\": 2}").select("a").as[Int]*3 == 6)
    }
    it should "be able to select an element from a list by index" in {
        assert(EasyJson.loads("[1, 2, 3]").select(1).as[Int]*3 == 6)
    }
    it should "be able to select multiple levels at once (list, list)" in {
        assert(EasyJson.loads("[[1, 2], [3, 4]]").select(0, 1).as[Int]*3 == 6)
    }
    it should "be able to select multiple levels at once (object, list)" in {
        assert(EasyJson.loads("{\"a\": [1, 2]}").select("a", 1).as[Int]*3 == 6)
    }
    it should "be able to select multiple levels at once (list, object, list)" in {
        assert(EasyJson.loads("[1, {\"a\": [1, 2]}]").select(1, "a", 1).as[Int]*3 == 6)
    }
    "JsonData.index" should "be able to select an element from a list" in {
        assert(EasyJson.loads("[1, 2, 3]").index(1).as[Int]*3 == 6)
    }
    "JsonData.key" should "be able to select an element from an object" in {
        assert(EasyJson.loads("{\"a\": 2}").key("a").as[Int]*3 == 6)
    }
}
