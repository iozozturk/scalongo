package repos

import com.google.inject.{Inject, Singleton}
import com.mongodb.client.model.Updates.set
import com.mongodb.client.result.UpdateResult
import models.User
import org.mongodb.scala.bson.ObjectId
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.{Completed, MongoCollection}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserRepo @Inject()(val mongo: Mongo)(implicit val executionContext: ExecutionContext) {

  lazy val collection: MongoCollection[User] = mongo.db.getCollection("users")

  def findById(id: ObjectId): Future[Option[User]] = {
    collection.find(equal("_id", id)).first().toFutureOption()
  }

  def update(user: User): Future[UpdateResult] = {
    collection.replaceOne(equal("_id", user._id), user).toFuture()
  }

  def save(user: User): Future[Completed] = {
    collection.insertOne(user).toFuture()
  }

  def findByActivationId(id: String): Future[Option[User]] = {
    collection.find(equal("activationId", id)).first().toFutureOption()
  }

  def findByEmail(email: String): Future[Option[User]] = {
    collection.find(equal("email", email)).first().toFutureOption()
  }

  def findByUsername(username: String): Future[Option[User]] = {
    collection.find(equal("username", username)).first().toFutureOption()
  }

  def updatePassword(userId: ObjectId, password: String): Future[UpdateResult] = {
    collection.updateOne(equal("_id", userId), set("password", password)).toFuture()
  }

  def updateActivation(userId: ObjectId, active: Boolean): Future[UpdateResult] = {
    collection.updateOne(equal("_id", userId), set("active", active)).toFuture()
  }

}
