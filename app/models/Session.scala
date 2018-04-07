package models

import java.util.Date

import common.JsonDecoders
import org.mongodb.scala.bson.ObjectId
import play.api.libs.functional.syntax.toFunctionalBuilderOps
import play.api.libs.json.{JsPath, Json, Reads, Writes}

case class Session(_id: ObjectId, token: String, userId: ObjectId, userAgent: String, ip: String, timestamp: Date, timeUpdate: Date)

object Session extends JsonDecoders {
  def apply(token: String, userId: ObjectId, userAgent: String, ip: String): Session = new Session(new ObjectId, token, userId, userAgent, ip, new Date, new Date)

  implicit val sessionWrites = new Writes[Session] {
    def writes(session: Session) = Json.obj(
      "_id" -> session._id.toString,
      "token" -> session.token,
      "userId" -> session.userId.toString,
      "userAgent" -> session.userAgent,
      "ip" -> session.ip,
      "timestamp" -> session.timestamp.toString,
      "timeUpdate" -> session.timeUpdate.toString
    )
  }

  implicit val sessionReads: Reads[Session] = (
    (JsPath \ "_id").read[ObjectId] and
      (JsPath \ "token").read[String] and
      (JsPath \ "userId").read[ObjectId] and
      (JsPath \ "userAgent").read[String] and
      (JsPath \ "ip").read[String] and
      (JsPath \ "timestamp").read[Date] and
      (JsPath \ "timeUpdate").read[Date]
    ) (Session.apply(_, _, _, _, _, _, _))
}