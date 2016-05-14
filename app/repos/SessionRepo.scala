package repos

import com.google.inject.{Inject, Singleton}
import com.mongodb.client.result.UpdateResult
import models.Session
import play.api.libs.json.Json

import scala.concurrent.Future

/**
  * Created by ismet on 20/12/15.
  */
@Singleton
class SessionRepo @Inject()(val mongoCollectionFactory: MongoCollectionFactory) extends Repository[Session] {
  override val collectionName = "sessions"

  def findByUserId(userId: String): Future[Seq[Session]] = {
    find(Json.obj("userId" -> userId))
  }

  def updateLastActivity(sessionId: String): Future[UpdateResult] = {
    collection.partialUpdate(Json.obj("_id" -> sessionId), Json.obj("$set" -> Json.obj("timeUpdate" -> System.currentTimeMillis())))
  }

  def setToken(sessionId: String, token: String): Future[UpdateResult] = {
    collection.partialUpdate(Json.obj("_id" -> sessionId), Json.obj("$set" -> Json.obj("token" -> token)))
  }

}
