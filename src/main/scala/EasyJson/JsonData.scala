package EasyJson

class JsonData(private val data: Any) {
    def has_key(k: String): Boolean = this.as[Map[String, Any]] contains k

    def keys: Set[String] = this.as[Map[String, Any]].keys.toSet

    def length: Int = this.data match {
        case c: String      => c.length
        case c: Vector[_]   => c.length
        case c: Map[_,_]    => c.toSeq.length
        case _ => throw new QueryError("Does not have length.")
    }

    def key(k: String): JsonData = {
        this.to[Map[String, Any]] match {
            case Some(v) => {
                if (v contains k) {
                    new JsonData(v(k))
                }
                else {
                    throw new NoSuchElementException("Key does not exist.")
                }
            }
            case None => throw new NoSuchElementException("Not an Object.")
        }
    }

    def index(i: Int): JsonData = {
        if (i < 0) {
            throw new IndexOutOfBoundsException("Negative index.")
        }
        this.to[Vector[Any]] match {
            case Some(v) => {
                if (i < v.length) {
                    new JsonData(v(i))
                }
                else {
                    throw new IndexOutOfBoundsException("Index too large.")
                }
            }
            case None => throw new NoSuchElementException("Not an Array.")
        }
    }

    def select(a: Any): JsonData = {
        a match {
            case i: Int => this.index(i)
            case k: String => this.key(k)
            case p: Product => {
                this.select(p.productIterator.toVector)
            }
            case Seq(elem) => {
                this.select(elem)
            }
            case Seq(elem, elems@_*) => {
                this.select(elem).select(elems)
            }
            case _ => throw new QueryError("Invalid type to query with.")
        }
    }

    def jstypename: String = {
        this.data match {
            case null => "null"
            case false => "boolean"
            case true => "boolean"
            case _: Int => "number"
            case _: Double => "number"
            case _: String => "string"
            case _: Vector[_] => "array"
            case _: Map[_,_] => "object"
        }
    }

    def typename: String = {
        this.data match {
            case null => "Null"
            case false => "Boolean"
            case true => "Boolean"
            case _: Int => "Int"
            case _: Double => "Double"
            case _: String => "String"
            case _: Vector[_] => "Vector"
            case _: Map[_,_] => "Map"
        }
    }

    /** Test if the selected value is an isntance of the give type. */
    def is[T](implicit ct: reflect.ClassTag[T]): Boolean = this.to[T].isDefined

    /** Dynamically convert the selected value to the given type, wrapped in option. */
    def to[T](implicit ct: reflect.ClassTag[T]): Option[T] = ct.unapply(this.data)

    /** Dynamically convert the selected value to the given type. */
    def as[T](implicit ct: reflect.ClassTag[T]): T = {
        try this.to[T].get catch {
            case _: NoSuchElementException => throw new QueryError("Incorrect type.")
        }
    }
}
