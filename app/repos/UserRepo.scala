package repos

import com.google.inject.{Inject, Singleton}
import models.User
import play.api.libs.json.Json
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.async.Async._
import scala.concurrent.Future

/**
  * Created by ismet on 06/12/15.
  */
@Singleton
class UserRepo @Inject()(val mongoCollectionFactory: MongoCollectionFactory) extends Repository[User] {
  override val collectionName = "users"

  def findByEmail(email: String): Future[User] = async {
    User(await(collection.findOne(Json.obj("email" -> email))))
  }

  def findByUsername(username: String): Future[User] = async {
    User(await(collection.findOne(Json.obj("username" -> username))))
  }

}
