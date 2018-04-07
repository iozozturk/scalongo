package services

import com.github.t3hnar.bcrypt.Password
import com.google.inject.Inject
import common.AppLogger
import models.User
import org.mongodb.scala.bson.ObjectId
import org.mongodb.scala.result.UpdateResult
import repos.UserRepo


import scala.concurrent.{ExecutionContext, Future}

class UserService @Inject()(userRepo: UserRepo)(implicit executionContext: ExecutionContext) extends AppLogger {

  def changePasword(userId: ObjectId, password: String): Future[UpdateResult] = {
    logger.info(s"Updating password for user: $userId")
    userRepo.updatePassword(userId, password.bcrypt)
  }

  def activateUser(userId: ObjectId): Future[UpdateResult] = {
    userRepo.updateActivation(userId, active = true)
  }

  def findByActivationId(id: String): Future[Option[User]] = userRepo.findByActivationId(id)

  def findById(userId: ObjectId): Future[Option[User]] = userRepo.findById(userId)

  def findByEmail(email: String): Future[Option[User]] = userRepo.findByEmail(email)

  def update(user: User): Future[UpdateResult] = userRepo.update(user)

  def save(name: String, email: String, password: String): Future[User] = {
    val user = User(email, name, password)
    userRepo.save(user).map(_ => user)
  }

}
