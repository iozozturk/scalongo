package services

import com.google.inject.Inject
import models.User
import org.mongodb.scala.result.UpdateResult
import repos.UserRepo

import scala.concurrent.Future

/**
  * Created by ismet on 06/12/15.
  */
class UserService @Inject()(userRepo: UserRepo) {
  def findById(userId: String): Future[User] = userRepo.findById(userId)

  def findByUsername(username: String): Future[User] = userRepo.findByUsername(username)

  def findByEmail(email: String): Future[User] = userRepo.findByEmail(email)

  def update(user: User): Future[UpdateResult] = userRepo.update(user)

  def save(user: User) = userRepo.save(user)

}
