package services

import java.util.UUID

import com.google.inject.Inject
import com.mongodb.async.client.Observable
import com.mongodb.client.result.UpdateResult
import daos.UserDao
import models.User
import org.mongodb.scala.Document

/**
  * Created by ismet on 06/12/15.
  */
class UserService @Inject()(userDao: UserDao) {
  def find(userId: UUID): Observable[Document] = userDao.find(userId)

  def update(user: User): Observable[UpdateResult] = userDao.update(user)

  def save(user: User) = userDao.save(user)

}
