package EasyJson

import scala.collection.mutable;

object Parser {
    def scan(string: String, allowAnyRoot: Boolean): Result[Any] = {
        val tokens = mutable.Buffer[String]();
        val value = scanForwardValue(string.trim());

        value match {
            case Ok(v) => {
                if (allowAnyRoot) {
                    Ok(v._1)
                }
                else {
                    if (!v._1.isInstanceOf[Vector[_]] && !v._1.isInstanceOf[Map[_,_]]) {
                        Err("Invalid root type.")
                    }
                    else {
                        Ok(v._1)
                    }
                }
            }
            case Err(e) => Err(e)
        }
    }

    def scanForwardWhitespace(string: String): Int = {
        string.takeWhile(Character.isWhitespace(_)).length
    }

    def scanForwardValue(string: String): Result[(Any, Int)] = {
        if (string.startsWith("null")) {
            Ok((null, 4))
        }
        else if (string.startsWith("true")) {
            Ok((true, 4))
        }
        else if (string.startsWith("false")) {
            Ok((false, 5))
        }
        else if (string.startsWith("\"")) {
            scanForwardString(string)
        }
        else if (string.startsWith("[")) {
            scanForwardArray(string)
        }
        else if (string.startsWith("{")) {
            scanForwardObject(string)
        }
        else if ("-0123456789" contains string(0)) {
            scanForwardNumber(string)
        }
        else {
            Err("Unexpected character '"+string(0)+"'. (continues with "+string.take(10)+")")
        }
    }

    def scanForwardObject(string: String): Result[(Map[String, Any], Int)] = {
        if (string.length < 2) {
            Err("Unexpected end of data.")
        }
        else if (string(0) != '{') {
            Err("Object cannot start with '"+string(0)+"'.")
        }
        else if (string(1) == '}') {
            Ok((Map(), 2))
        }
        else {
            val result = mutable.Map[String, Any]();
            var index = 1;
            while (index < string.length) {
                index += scanForwardWhitespace(string.drop(index));
                val key = scanForwardString(string.drop(index)) match {
                    case Ok((k, len)) => {
                        index += len;
                        k
                    }
                    case Err(e) => {return Err(e)}
                }
                index += scanForwardWhitespace(string.drop(index));
                if (string(index) != ':') {
                    return Err("Object key must be followed by ':'.");
                }
                index += 1;
                index += scanForwardWhitespace(string.drop(index));
                val value = scanForwardValue(string.drop(index)) match {
                    case Ok((v, len)) => {
                        index += len;
                        v
                    }
                    case Err(e) => {return Err(e)}
                }
                result += ((key, value));
                index += scanForwardWhitespace(string.drop(index));
                if (string(index) == '}') {
                    return Ok((result.toMap, index+1));
                }
                else if (string(index) != ',') {
                    return Err("A key-value pair in object must be followed by ','.");
                }
                index += 1;
            }

            Err("Unexpected end of data.")
        }
    }

    def scanForwardArray(string: String): Result[(Vector[Any], Int)] = {
        if (string.length < 2) {
            Err("Unexpected end of data.")
        }
        else if (string(0) != '[') {
            Err("List must start with '['.")
        }
        else if (string(1) == ']') {
            Ok((Vector(), 2))
        }
        else {
            val result = mutable.Buffer[Any]();
            var index = 1;
            while (index < string.length) {
                index += scanForwardWhitespace(string.drop(index));
                val value = scanForwardValue(string.drop(index)) match {
                    case Ok((v, len)) => {
                        index += len;
                        v
                    }
                    case Err(e) => {return Err(e)}
                };
                result += value;
                index += scanForwardWhitespace(string.drop(index));
                if (string(index) == ']') {
                    return Ok((result.toVector, index+1));
                }
                else if (string(index) != ',') {
                    return Err("Array items must be separated with ','.");
                }
                index += 1;
            }
            Err("Unexpected end of data.")
        }
    }

    def scanForwardString(string: String): Result[(String, Int)] = {
        if (string.length < 2) {
            Err("Unexpected end of data.")
        }
        else if (string(0) != '"') {
            Err("String must start with '\"'.")
        }
        else if (string(0)=='"' && string(1)=='"') {
            Ok(("", 2))
        }
        else {
            var indices = (1 until string.length) takeWhile {i: Int =>
                string(i) != '"' || (string(i-1) == '\\' && string(i) == '"')
            }

            var escapeNext = false;
            var escapeHex = false;
            var result = "";
            var hexbuffer = "";

            indices foreach {i: Int =>
                if (escapeHex) {
                    if (!("0123456789abcdefABCDEFG" contains string(i))) {
                        return Err("Unicode escapes (\\uXXXX) must contain only hexadecimal characters.");
                    }
                    hexbuffer += string(i);
                    if (hexbuffer.length == 4) {
                        result += Character.toString(Integer.parseInt(hexbuffer.toLowerCase(), 16).asInstanceOf[Char]);
                        hexbuffer = "";
                        escapeHex = false;
                    }
                }
                else if (escapeNext) {
                    escapeNext = false;
                    string(i) match {
                        case '\\' => {result += '\\'}
                        case '"' => {result += '"'}
                        case '/' => {result += '/'}
                        case 't' => {result += '\t'}
                        case 'r' => {result += '\r'}
                        case 'n' => {result += '\n'}
                        case 'f' => {result += '\f'}
                        case 'b' => {result += '\b'}
                        case 'u' => {escapeHex = true}
                        case _ => {return Err("Invalid escape character '"+string(i)+"'.")}
                    }
                }
                else {
                    if (string(i) == '\\') {
                        escapeNext = true;
                    }
                    else {
                        result += string(i);
                    }
                }
            }
            Ok((result, indices.last+2))
        }
    }
    def scanForwardNumber(string: String): Result[(Any, Int)] = {
        if (string.length == 0) {
            Err("Unexpected end of data.")
        }
        else {
            var negative = string(0)=='-';
            val numval = string.drop(if (negative) 1 else 0);
            if (numval.length == 0) {
                Err("Unexpected end of data.")
            }
            else {
                var interger_part = "";
                var fractional_part = "";
                var exponent = "";
                var integer = true;
                var nfloat = false;
                var efloat = false;
                var index = 0;
                var all_over = false;

                while (integer && !all_over) {
                    if (index >= numval.length) {
                        all_over = true;
                    }
                    else {
                        if ("0123456789" contains numval(index)) {
                            interger_part += numval(index);
                        }
                        else if (numval(index) == '.') {
                            nfloat = true;
                            integer = false;
                        }
                        else if ("eE" contains numval(index)) {
                            efloat = true;
                            integer = false;
                        }
                        else {
                            index -= 1;
                            all_over = true;
                        }
                        index += 1;
                    }
                }
                if (nfloat) {
                    var over = false;
                    while (!over) {
                        if (index >= numval.length) {
                            over = true;
                        }
                        else {
                            if ("0123456789" contains numval(index)) {
                                fractional_part += numval(index);
                            }
                            else if ("eE" contains numval(index)) {
                                over = true;
                                efloat = true;
                            }
                            else {
                                over = true;
                                all_over = true;
                                index -= 1;
                            }
                            index += 1;
                        }
                    }
                }

                if (efloat) {
                    if (numval(index) == '+') {
                        index += 1;
                    }
                    else if (numval(index) == '-') {
                        exponent += '-';
                        index += 1;
                    }

                    while (!all_over) {
                        if ("0123456789" contains numval(index)) {
                            exponent += numval(index);
                        }
                        else {
                            index -= 1;
                            all_over = true;
                        }
                        index += 1;
                    }
                }

                if (interger_part.isEmpty) {
                    return Err("Unexpected end of data.");
                }

                if (nfloat || efloat) {
                    Ok(((if (negative) -1 else 1) * (if (nfloat) {
                        (interger_part+"."+fractional_part).toDouble
                    }
                    else {
                        interger_part.toDouble
                    }) * Math.pow(10, if (efloat) exponent.toDouble else 0), index + (if (negative) 1 else 0)))
                }
                else {
                    Ok(((if (negative) -1 else 1) * interger_part.toInt, index + (if (negative) 1 else 0)))
                }
            }
        }
    }
}
