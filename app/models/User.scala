package models

import java.util.{Date, UUID}

import common.JsonDecoders
import org.mongodb.scala.bson.ObjectId
import play.api.libs.functional.syntax.toFunctionalBuilderOps
import play.api.libs.json.{JsPath, Json, Reads, Writes}

case class User(_id: ObjectId, email: String, name: String, password: String, activationId: String, active: Boolean, timestamp: Date)

object User extends JsonDecoders {

  def apply(email: String, name: String, password: String): User = new User(new ObjectId, email, name, password, UUID.randomUUID().toString, false, new Date())

  implicit val userWrites = new Writes[User] {
    def writes(user: User) = Json.obj(
      "_id" -> user._id.toString,
      "email" -> user.email,
      "name" -> user.name,
      "activationId" -> user.activationId,
      "active" -> user.active,
      "timestamp" -> user.timestamp.toString
    )
  }

  implicit val userReads: Reads[User] = (
    (JsPath \ "_id").read[ObjectId] and
      (JsPath \ "email").read[String] and
      (JsPath \ "name").read[String] and
      (JsPath \ "password").read[String] and
      (JsPath \ "activationId").read[String] and
      (JsPath \ "active").read[Boolean] and
      (JsPath \ "timestamp").read[Date]
    ) (User.apply(_, _, _, _, _, _, _))
}