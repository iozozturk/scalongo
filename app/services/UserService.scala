package services

import com.google.inject.Inject
import daos.UserDao
import models.User
import org.mongodb.scala.result.UpdateResult

import scala.concurrent.Future

/**
  * Created by ismet on 06/12/15.
  */
class UserService @Inject()(userDao: UserDao) {
  def find(userId: String): Future[User] = userDao.find(userId)

  def findByUsername(username: String): Future[User] = userDao.findByUsername(username)

  def findByEmail(email: String): Future[User] = userDao.findByEmail(email)

  def update(user: User): Future[UpdateResult] = userDao.update(user)

  def save(user: User) = userDao.save(user)

}
