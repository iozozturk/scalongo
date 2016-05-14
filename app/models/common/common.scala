package models.common

import common.ConvertibleToJson
import play.api.libs.json._

/**
  * Created by trozozti on 01/03/16.
  */
abstract class DocumentModel extends ConvertibleToJson {
  def json: JsObject
  final def toJson = json
}

abstract class DocumentModelFactory[T] extends (JsObject => T) {
  implicit val reads = JsSuccess(apply(_))
  implicit val maker = this
}

object DocumentModelFactory {
  def apply[A](implicit f: DocumentModelFactory[A]) = f
}