package EasyJson

import shapeless._
import shapeless.syntax.typeable._

abstract class JsonData {
    /** Test if the selected value is an isntance of the give type. */
    def is[T: Typeable]: Boolean = this.to[T].isDefined

    /** Dynamically convert the selected value to the given type, wrapped in option. */
    def to[T: Typeable]: Option[T] = this match {
        case c: JsonObject => c.value.cast[T].map(_.asInstanceOf[T])
        case c: JsonArray => c.value.cast[T].map(_.asInstanceOf[T])
        case c: JsonString => c.value.cast[T].map(_.asInstanceOf[T])
        case c: JsonFloat => c.value.cast[T].map(_.asInstanceOf[T])
        case c: JsonInteger => c.value.cast[T].map(_.asInstanceOf[T])
        case c: JsonBoolean => c.value.cast[T].map(_.asInstanceOf[T])
        case c: JsonNull => throw new QueryError("Can't cast null.")
        case _ => throw new QueryError("Incorrect type?.")
    }

    /** Dynamically convert the selected value to the given type. */
    def as[T: Typeable]: T = {
        try this.to[T].get catch {
            case _: NoSuchElementException => throw new QueryError("Incorrect type.")
        }
    }
}

case class JsonObject(value: Map[String, JsonData]) extends JsonData;
case class JsonArray(value: Vector[JsonData]) extends JsonData;
case class JsonString(value: String) extends JsonData;
case class JsonFloat(value: Double) extends JsonData;
case class JsonInteger(value: Int) extends JsonData;
case class JsonBoolean(value: Boolean) extends JsonData;
case class JsonNull() extends JsonData;

object JsonData {
    def apply(data: Any): JsonData = {
        data match {
            case null => JsonNull()
            case v: Boolean => JsonBoolean(v)
            case v: Int => JsonInteger(v)
            case v: Double => JsonFloat(v)
            case v: String => JsonString(v)
            case v: Vector[_] => JsonArray(v.map(JsonData(_)))
            case v: Map[String,_] @unchecked => JsonObject(v.map(x => x._1 -> JsonData(x._2)).toMap)
        }
    }
}
