import EasyJson._

object Example extends App {
    def main() = {
        val d1 = EasyJson.load("example.json");
        println(d1.select("Arrays", 1, 1).to[Int].get*2);
        println(EasyJson.dumps(d1));

        val d2 = EasyJson.loads("[1234, {\"test\": 2}]");
        println(EasyJson.dumps(d2));
    }

    main()
}
