package common

import play.api.libs.json._

import scala.language.existentials

object JsonExtensions {
  implicit class ForJsValue(val underlying: JsValue) extends AnyVal {
    def getAs[A : Reads : Manifest](key: String): A = {
      getAs[A](JsPath \ key)
    }

    def getAs[A : Reads : Manifest](path: JsPath): A = {
      path.read[A].reads(underlying).get
    }

    def getAsOpt[A : Reads : Manifest](key: String): Option[A] = {
      getAsOpt[A](JsPath \ key)
    }

    def getAsOpt[A : Reads : Manifest](path: JsPath): Option[A] = {
      path.readNullable[A].reads(underlying).getOrElse(None)
    }

    def getAsSeq[A : Reads : Manifest](key: String): Seq[A] = {
      getAsSeq[A](JsPath \ key)
    }

    def getAsSeq[A : Reads : Manifest](path: JsPath): Seq[A] = {
      path.readNullable[Seq[A]].reads(underlying).getOrElse(None).getOrElse(Nil)
    }

  }
}

