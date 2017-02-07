package EasyJson

/** Like Option, but None has an error message. */
sealed trait Result[+T] {
    def isOk: Boolean;
    def get(): T;
}

case class Ok[+T](value: T) extends Result[T] {
    def isOk = true;
    def get = value;
}
case class Err(error: String) extends Result[Nothing] {
    def isOk = false;
    def get = throw new NoSuchElementException("Cannot get Err.");
}
