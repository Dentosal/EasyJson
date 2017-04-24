package EasyJson

import scala.io.Source
import java.io.{File, PrintWriter}

case class ParseError(message: String) extends Exception(message, null)
case class QueryError(message: String) extends Exception(message, null)

object EasyJson {
    /** Load JSON data from given file */
    def load(filename: String, allowAnyRoot: Boolean = false) = loads(Source.fromFile(filename).mkString(""), allowAnyRoot)

    /** Load JSON data from given string */
    def loads(jsondata: String, allowAnyRoot: Boolean = false): JsonData = {
        Parser.scan(jsondata, allowAnyRoot) match {
            case Ok(v) => JsonData(v)
            case Err(e) => throw new ParseError(e)
        }
    }

    /** Dump given data as JSON string to given file */
    def dump(data: JsonData, filename: String) = {
        new PrintWriter(new File(filename)) {
            write(dumps(data));
            close;
        }
    }

    /** Dump given data to JSON string */
    def dumps(data: JsonData): String = {
        Formatter.format(data)
    }
}
