# EasyJson

An Easy-to-use, not-so-idiomatic and mostly functional JSON library written in Scala. It provides an api similar to the one from [the json library of Python](https://docs.python.org/3.5/library/json.html).

# Usage



```scala
import EasyJson._ // Import the library:

object Example extends App {
    // Load the contents from a file
    val data1 = EasyJson.load("example.json");

    // Or from a string
    val data2 = EasyJson.loads("{\"test\": 123}");

    // Examine the data
    assert(data2.select("test").as[Int] * 2 == 246);

    // Save the data to a file
    EasyJson.dump("result.json", data2);

    // Or to a string
    val text_data_1 = EasyJson.dumps(data1);

    // Minify a json object
    assert(EasyJson.dumps(EasyJson.loads("[1, 2, 3]")) == "[1,2,3]");
}
```

# Building

Install [sbt](http://www.scala-sbt.org/download.html).

Run `sbt assembly`. Then run the example with `cd example && scala -classpath /path/to/EasyJson.jar Example.scala`. You can get the path from the output of `sbt assembly`.

Or just:

```bash
JAR="$(sbt -no-colors assembly 2>&1 | sed -En 's/\[info\][^/]+(\/.+\/target\/.+\.jar).*/\1/p' | tr -d '\n')" && cd example && scala -classpath $JAR Example.scala && cd ..
```


# TODO

* Documentation would possibly be a cool thing to add.
* Unit test would probably find some edge cases.
* Deserialization to case classes would most likely be helpful.

# License

Copyright (c) 2017 Hannes Karppila

Published under the MIT License, see [LICENSE](/LICENSE) for more information.
