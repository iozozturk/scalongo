package common

import play.api.libs.json._

abstract class ConvertibleToJson {
  def toJson: JsValue
}

object ConvertibleToJson {
  implicit val writes = Writes((_: ConvertibleToJson).toJson)
}
