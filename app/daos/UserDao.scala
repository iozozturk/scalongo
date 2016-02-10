package daos


import com.google.inject.{ImplementedBy, Inject, Singleton}
import models.User
import org.mongodb.scala._
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.result.UpdateResult
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import services.Mongo

import scala.concurrent.Future

/**
  * Created by ismet on 06/12/15.
  */
@ImplementedBy(classOf[MongoUserDao])
trait UserDao {
  def findByEmail(email: String): Future[User]

  def find(userId: String): Future[User]

  def findByUsername(username: String): Future[User]

  def update(user: User): Future[UpdateResult]

  def save(user: User): Future[Completed]
}

@Singleton
class MongoUserDao @Inject()(mongo: Mongo) extends UserDao {
  private val users: MongoCollection[Document] = mongo.db.getCollection("user")

  override def find(userId: String): Future[User] = {
    users.find(equal("_id", userId)).head().map[User]((doc: Document) => {
      documentToUser(doc)
    })
  }

  override def findByEmail(email: String): Future[User] = {
    users.find(equal("email", email)).head().map[User]((doc: Document) => {
      documentToUser(doc)
    })
  }

  override def findByUsername(username: String): Future[User] = {
    users.find(equal("username", username)).head().map[User]((doc: Document) => {
      documentToUser(doc)
    })
  }

  override def update(user: User): Future[UpdateResult] = {
    users.updateOne(equal("_id", user._id), Document(Json.toJson(user).toString)).head()
  }

  override def save(user: User): Future[Completed] = {
    val userJson: String = Json.toJson(user).toString
    val doc: Document = Document(userJson)
    users.insertOne(doc).head()
  }

  private def documentToUser(doc: Document): User = {
    User(
      doc.get("_id").get.asString().getValue,
      doc.get("name").get.asString().getValue,
      doc.get("email").get.asString().getValue,
      doc.get("username").get.asString().getValue,
      doc.get("password").get.asString().getValue,
      doc.get("timestamp").get.asInt64().getValue
    )
  }
}
