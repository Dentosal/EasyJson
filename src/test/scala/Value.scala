import EasyJson.{EasyJson, ParseError}
import org.scalatest._

class SimpleValueTest extends FlatSpec {
    "EasyJson" should "be able to load true" in {
        EasyJson.loads("true", true)
    }
    it should "be able to load false" in {
        EasyJson.loads("false", true)
    }
    it should "be able to load null" in {
        EasyJson.loads("null", true)
    }
}
