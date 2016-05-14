package models.common

import common.JsonExtensions._
import play.api.libs.json.{JsObject, Json}


trait Entity[This <: Entity[This]] extends DocumentModel {
  val This: JsObject => This

  def _id = json.getAs[String]("_id")

  def idSelector = Json.obj("_id" -> _id)

  def timestamp = json.getAs[Long]("timestamp")

  def timeUpdate = json.getAs[Long]("timeUpdate")

}

object Entity {
  type Id = String
}