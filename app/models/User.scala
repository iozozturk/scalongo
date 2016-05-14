package models

import _root_.common.JsonExtensions._
import models.common.{DocumentModelFactory, Entity}
import play.api.libs.json.JsObject

/**
  * Created by ismet on 06/12/15.
  */
case class User(json: JsObject) extends Entity[User]{
  override val This = User
  val password = json.getAs[String]("password")
}

object User extends DocumentModelFactory[User]