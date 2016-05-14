package models

import _root_.common.JsonExtensions._
import models.common.{DocumentModelFactory, Entity}
import play.api.libs.json.JsObject

/**
  * Created by ismet on 06/12/15.
  */
case class Session(json: JsObject) extends Entity[Session] {
  override val This = Session
  def token = json.getAs[String]("token")

  def userId = json.getAs[String]("userId")
}

object Session extends DocumentModelFactory[Session]