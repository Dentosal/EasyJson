import EasyJson.{EasyJson, ParseError}
import org.scalatest._

class RootObjectTest extends FlatSpec {
    "EasyJson" should "be able to load an empty list" in {
        EasyJson.loads("[]")
    }
    it should "be able to load an empty object" in {
        EasyJson.loads("{}")
    }
    it should "be able to load list with one empty string" in {
        EasyJson.loads("[\"\"]")
    }
    it should "be able to load object with one empty key" in {
        EasyJson.loads("{\"\":\"\"}")
    }
    it should "be able to load a list of integers" in {
        EasyJson.loads("[0,1,1,2,3,5,8]")
    }
    it should "be able to load a list of floats" in {
        EasyJson.loads("[0.1,1.2,2.3,3.14]")
    }
    it should "be able to load a list of strings" in {
        EasyJson.loads("[\"\", \"\", \"abc\"]")
    }
    it should "be able to load a list of lists" in {
        EasyJson.loads("[[], [1, 2, 3]]")
    }
    it should "be able to load a list of objects" in {
        EasyJson.loads("[{}, {\"\":\"\",\"a\":\"b\"}]")
    }
    it should "be able to load a mixed list" in {
        EasyJson.loads("[\"\", \"\", \"abc\", 1, 3.14, 8, [], {}]")
    }
    it should "be able to load an object with lists as values" in {
        EasyJson.loads("{\"a\": [], \"b\": [1, 2, 3]}")
    }
    it should "be able to load an object with only empty lists as values" in {
        EasyJson.loads("{\"a\": [], \"b\": []}")
    }
    it should "be able to load dataset 0" in {
        EasyJson.loads("{\"meta\": {}, \"options\": {}, \"data\": []}")
    }
    it should "be able to load dataset 1" in {
        EasyJson.loads("{\"meta\": {}, \"options\": {}, \"data\": [0, 0, 0, 1, 2, 100, 0]}")
    }
    it should "be able to load dataset 2" in {
        EasyJson.loads("{\"meta\": {}, \"options\": {\"title\": \"Workload\", \"axisLabels\": [\"Day\", \"Work\"], \"axisUnits\": [\"\", \"h\"], \"gridOn\": false, \"gridSize\": 0}, \"data\": [0, 0, 0, 1, 2, 100, 0]}")
    }
    it should "be able to load dataset 3" in {
        EasyJson.loads("{\"meta\": {\"version\": \"0.1.0\", \"libVersion\": \"0.1.0\", \"author\": \"John Smith\", \"created\": \"2017-04-23\", \"modified\": \"2017-04-23\"}, \"options\": {\"title\": \"Workload\", \"axisLabels\": [\"Day\", \"Work\"], \"axisUnits\": [\"\", \"h\"], \"gridOn\": false, \"gridSize\": 0}, \"data\": [0, 0, 0, 1, 2, 100, 0]}")
    }
    it should "throw an error on invalid root type" in {
        assertThrows[ParseError] {
            EasyJson.loads("0")
        }
    }
}
