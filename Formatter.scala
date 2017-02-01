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
        '['+(v map {v: Any => format(new JsonData(v))}).mkString(",")+']'
    }
    def fmtMap(m: Map[String, _]) = {
        '{'+(m map {case (k: String, v: Any) => format(new JsonData(k))+":"+format(new JsonData(v))} mkString ",")+'}'
    }

    def format(data: JsonData): String = {
        data.typename match {
            case "Null"     => "null"
            case "Boolean"  => data.as[Boolean].toString
            case "Int"      => data.as[Int].toString
            case "Double"   => data.as[Double].toString
            case "String"   => fmtString(data.as[String])
            case "Vector"   => fmtVector(data.as[Vector[_]])
            case "Map"      => fmtMap(data.as[Map[String, _]])
        }
    }
}
