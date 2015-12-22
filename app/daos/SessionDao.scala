package daos

import com.google.inject.{ImplementedBy, Inject, Singleton}
import com.mongodb.client.result.UpdateResult
import models.Session
import org.mongodb.scala._
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.Updates._
import org.mongodb.scala.result.DeleteResult
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import services.Mongo

import scala.concurrent.Future

/**
  * Created by ismet on 20/12/15.
  */
@ImplementedBy(classOf[MongoSessionDao])
trait SessionDao {
  def find(sessionId: String): Future[Session]

  def findByUserId(userId: String): Future[Session]

  def save(session: Session): Future[Completed]

  def delete(sessionId: String): Future[DeleteResult]

  def updateLastActivity(sessionId: String): Future[UpdateResult]
}

@Singleton
class MongoSessionDao @Inject()(mongo: Mongo) extends SessionDao {
  private val sessions: MongoCollection[Document] = mongo.db.getCollection("session")

  override def find(sessionId: String): Future[Session] = {
    sessions.find(equal("_id", sessionId)).head().map[Session]((doc: Document) => {
      documentToSession(doc)
    })
  }

  override def findByUserId(userId: String): Future[Session] = {
    sessions.find(equal("userId", userId)).head().map[Session]((doc: Document) => {
      documentToSession(doc)
    })
  }

  override def save(session: Session): Future[Completed] = {
    val sessionJson: String = Json.toJson(session).toString
    val doc: Document = Document(sessionJson)
    sessions.insertOne(doc).head()
  }

  override def delete(sessionId: String): Future[DeleteResult] = {
    sessions.deleteOne(equal("_id", sessionId)).head()
  }

  override def updateLastActivity(sessionId: String): Future[UpdateResult] = {
    sessions.updateOne(equal("_id", sessionId), set("lastActivity", System.currentTimeMillis())).head()
  }

  private def documentToSession(doc: Document): Session = {
    Session(
      doc.get("_id").get.asString().getValue,
      doc.get("userId").get.asString().getValue,
      doc.get("ip").get.asString().getValue,
      doc.get("userAgent").get.asString().getValue,
      doc.get("timestamp").get.asInt64().getValue,
      doc.get("lastActivity").get.asInt64().getValue
    )
  }

}
