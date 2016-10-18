package models.common

import common.ConvertibleToJson
import common.JsonExtensions.ForJsValue
import play.api.libs.json._

/**
  * Created by trozozti on 01/03/16.
  */
abstract class DocumentModel extends ConvertibleToJson {
  def json: JsObject
  final def toJson = {
    val docTimestamp = json.getAs[JsObject]("timestamp").getAs[String]("$numberLong")
    val parsedTimestamp = JsNumber(BigDecimal(docTimestamp))
    json - "password" - "timestamp" +("timestamp", parsedTimestamp)
  }
}

abstract class DocumentModelFactory[T] extends (JsObject => T) {
  implicit val reads = JsSuccess(apply(_))
  implicit val maker = this
}

object DocumentModelFactory {
  def apply[A](implicit f: DocumentModelFactory[A]) = f
}