package repos

import java.util.Date

import com.google.inject.{Inject, Singleton}
import com.mongodb.client.model.Updates.set
import com.mongodb.client.result.{DeleteResult, UpdateResult}
import models.Session
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.{Completed, MongoCollection}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class SessionRepo @Inject()(val mongo: Mongo)(implicit val executionContext: ExecutionContext) {

  lazy val collection: MongoCollection[Session] = mongo.db.getCollection("sessions")

  def findById(id: String): Future[Option[Session]] = {
    collection.find(equal("_id", id)).first().toFutureOption()
  }

  def findByToken(token: String): Future[Option[Session]] = {
    collection.find(equal("token", token)).first().toFutureOption()
  }

  def save(item: Session): Future[Completed] = {
    collection.insertOne(item).toFuture()
  }

  def delete(id: String): Future[DeleteResult] = {
    collection.deleteOne(equal("_id", id)).toFuture()
  }

  def findByUserId(userId: String): Future[Seq[Session]] = {
    collection.find(equal("userId", userId)).toFuture()
  }

  def updateLastActivity(sessionId: String): Future[UpdateResult] = {
    collection.updateOne(equal("_id", sessionId), set("timeUpdate", new Date())).toFuture()
  }

  def setToken(sessionId: String, token: String): Future[UpdateResult] = {
    collection.updateOne(equal("_id", sessionId), set("token", token)).toFuture()
  }

}
