package models

import java.util.Date

import common.JsonDecoders
import org.mongodb.scala.bson.ObjectId
import play.api.libs.functional.syntax.toFunctionalBuilderOps
import play.api.libs.json.{JsPath, Json, Reads, Writes}


case class PassReset(_id: ObjectId, email: String, used: Boolean, timestamp: Date)

object PassReset extends JsonDecoders {
  def apply(email: String, used: Boolean): PassReset = new PassReset(new ObjectId, email, used, new Date())

  implicit val sessionWrites = new Writes[PassReset] {
    def writes(passReset: PassReset) = Json.obj(
      "_id" -> passReset._id.toString,
      "email" -> passReset.email,
      "used" -> passReset.used,
      "timestamp" -> passReset.timestamp.toString
    )
  }

  implicit val sessionReads: Reads[PassReset] = (
    (JsPath \ "_id").read[ObjectId] and
      (JsPath \ "email").read[String] and
      (JsPath \ "used").read[Boolean] and
      (JsPath \ "timestamp").read[Date]
    ) (PassReset.apply(_, _, _, _))
}