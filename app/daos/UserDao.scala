package daos


import java.util.UUID

import com.google.inject.{Inject, Singleton, ImplementedBy}
import models.User
import org.mongodb.scala._
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.result.UpdateResult
import play.api.libs.json.Json
import services.Mongo

/**
  * Created by ismet on 06/12/15.
  */
@ImplementedBy(classOf[MongoUserDao])
trait UserDao {
  def find(userId: UUID): Observable[Document]

  def find(username: String): Observable[Document]

  def update(user: User): Observable[UpdateResult]

  def save(user: User): Observable[Completed]
}

@Singleton
class MongoUserDao @Inject()(mongo: Mongo) extends UserDao {
  private val users: MongoCollection[Document] = mongo.db.getCollection("user")

  override def find(userId: UUID): Observable[Document] = {
    users.find(equal("_id", userId)).first()
  }

  override def find(username: String): Observable[Document] = {
    users.find(equal("username", username)).first()
  }

  override def update(user: User): Observable[UpdateResult] = {
    users.updateOne(equal("_id", user._id), Document(Json.toJson(user).toString))
  }

  override def save(user: User): Observable[Completed] = {
    val userJson: String = Json.toJson(user).toString
    val doc: Document = Document(userJson)
    users.insertOne(doc)
  }

}
