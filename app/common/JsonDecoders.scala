package common

import org.mongodb.scala.bson.ObjectId
import play.api.libs.json._

trait JsonDecoders {
  implicit val objectIdWrites = new Writes[ObjectId] {
    def writes(objId: ObjectId) = JsString(objId.toString)
  }

  implicit val objectIdReads = new Reads[ObjectId] {
    override def reads(json: JsValue): JsResult[ObjectId] = JsSuccess(new ObjectId(json.as[String]))
  }
}
