package services

import com.google.inject.Inject
import models.{Session, User}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import repos.{SessionRepo, UserRepo}

import scala.async.Async._
import scala.concurrent.Future

/**
  * Created by ismet on 20/12/15.
  */
class SessionService @Inject()(sessionRepo: SessionRepo, userRepo: UserRepo) {
  def find(sessionId: String): Future[Session] = sessionRepo.findById(sessionId)

  def findUserAndSession(sessionId: String): Future[(Session, User)] = async {
    val session = await(find(sessionId))
    val user = await(userRepo.findById(session.userId))
    sessionRepo.updateLastActivity(sessionId)
    (session, user)
  }

  def findByUserId(userId: String): Future[Seq[Session]] = sessionRepo.findByUserId(userId)

  def save(session: Session) = sessionRepo.save(session)

  def delete(sessionId: String) = sessionRepo.delete(sessionId)

  def updateLastActivity(sessionId: String) = sessionRepo.updateLastActivity(sessionId)

  def setToken(sessionId: String, token: String) = sessionRepo.setToken(sessionId, token)
}
