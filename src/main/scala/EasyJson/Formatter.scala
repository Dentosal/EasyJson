package EasyJson

object Formatter {
    def isPrintable(c: Char): Boolean = {
        val block = Character.UnicodeBlock.of(c);
        (!Character.isISOControl(c)) && block != null && block != Character.UnicodeBlock.SPECIALS;
    }
    def escape(c: Char): String = {
        if (isPrintable(c)) {
            (c match {
                case '\\' => "\\"+'\\'
                case '"'  => "\\"+'"'
                case '/'  => "\\"+'/'
                case '\t' => "\\"+'t'
                case '\r' => "\\"+'r'
                case '\n' => "\\"+'n'
                case '\f' => "\\"+'f'
                case '\b' => "\\"+'b'
                case _    => c.toString
            })
        }
        else {
            "\\u%04x".format(c.toInt)
        }
    }
    def fmtString(s: String) = {
        '"'+(s map escape mkString "")+'"'
    }
    def fmtVector(v: Vector[_]) = {
        '['+(v map {v: Any => v match {
            case d: JsonData => format(d)
            case x => x
        }}).mkString(",")+']'
    }
    def fmtMap(m: Map[String, _]) = {
        '{'+(m map {case (k: String, v: JsonData) => fmtString(k)+":"+format(v)} mkString ",")+'}'
    }

    def format(data: JsonData): String = {
        data match {
            case JsonNull()     => "null"
            case JsonBoolean(v) => v.toString
            case JsonInteger(v) => v.toString
            case JsonFloat(v)   => v.toString
            case JsonString(v)  => fmtString(v)
            case JsonArray(v)   => fmtVector(v)
            case JsonObject(v)  => fmtMap(v)
        }
    }
}
