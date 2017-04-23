import EasyJson.{EasyJson, ParseError}
import org.scalatest._

class FloatTest extends FlatSpec {
    "EasyJson" should "be able to load zero" in {
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
    it should "throw an error when loading NaN" in {
        assertThrows[ParseError] {
            EasyJson.loads("NaN", true)
        }
    }
    it should "throw an error when loading Infinity" in {
        assertThrows[ParseError] {
            EasyJson.loads("Infinity", true)
        }
    }
    it should "throw an error when loading Inf" in {
        assertThrows[ParseError] {
            EasyJson.loads("Inf", true)
        }
    }
    it should "throw an error when loading negative Infinity" in {
        assertThrows[ParseError] {
            EasyJson.loads("-Infinity", true)
        }
    }
    it should "throw an error when loading negative Inf" in {
        assertThrows[ParseError] {
            EasyJson.loads("-Inf", true)
        }
    }
}
