package repos

import com.google.inject.{Inject, Singleton}
import com.mongodb.client.result.{DeleteResult, UpdateResult}
import models.PassReset
import org.mongodb.scala.bson.ObjectId
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.Updates.set
import org.mongodb.scala.{Completed, MongoCollection}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PassResetRepo @Inject()(val mongo: Mongo)(implicit val executionContext: ExecutionContext) {

  val collectionName = "passresets"
  lazy val collection: MongoCollection[PassReset] = mongo.db.getCollection("passresets")

  def findById(id: String): Future[Option[PassReset]] = {
    collection.find(equal("_id", id)).first().toFutureOption()
  }

  def save(item: PassReset): Future[Completed] = {
    collection.insertOne(item).toFuture()
  }

  def delete(id: String): Future[DeleteResult] = {
    collection.deleteOne(equal("_id", id)).toFuture()
  }

  def findNotUsed(resetId: ObjectId): Future[Option[PassReset]] = {
    collection.find(and(equal("_id", resetId), equal("used", false))).first().toFutureOption()
  }

  def findByEmail(email: String): Future[Option[PassReset]] = {
    collection.find(equal("email", email)).first().toFutureOption()
  }

  def setUsed(resetId: String, isUsed: Boolean): Future[UpdateResult] = {
    collection.updateOne(equal("_id", resetId), set("used", isUsed)).toFuture()
  }


}
