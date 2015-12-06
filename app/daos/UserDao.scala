package daos

import java.util.UUID

import com.google.inject.ImplementedBy
import models.User

import scala.concurrent.Future

/**
  * Created by ismet on 06/12/15.
  */
@ImplementedBy(classOf[MongoUserDao])
trait UserDao {
  def find(userId: UUID): Future[Option[User]]

  def save(user: User): Future[User]

  def update(user: User): Future[User]
}

class MongoUserDao extends UserDao {
  override def find(userId: UUID): Future[Option[User]] = ???

  override def update(user: User): Future[User] = ???

  override def save(user: User): Future[User] = ???
}
