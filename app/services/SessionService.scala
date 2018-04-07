package services

import com.google.inject.Inject
import com.mongodb.client.result.{DeleteResult, UpdateResult}
import common.AppLogger
import models.{Session, User}
import org.mongodb.scala.Completed
import org.mongodb.scala.bson.ObjectId
import repos.{SessionRepo, UserRepo}

import scala.async.Async._
import scala.concurrent.{ExecutionContext, Future}

class SessionService @Inject()(sessionRepo: SessionRepo, userRepo: UserRepo)
                              (implicit val executionContext: ExecutionContext) extends AppLogger {

  def find(token: String): Future[Option[Session]] = sessionRepo.findByToken(token)

  def findUserAndSession(token: String): Future[Option[(Session, User)]] = async {
    val session: Option[Session] = await(find(token))
    if (session.isDefined) {
      val user = await(userRepo.findById(session.get.userId)).get
      sessionRepo.updateLastActivity(token)
      Some((session.get, user))
    } else {
      logger.info(s"session not found, session=$token")
      None
    }
  }

  def findByUserId(userId: String): Future[Seq[Session]] = sessionRepo.findByUserId(userId)

  def save(userId:ObjectId, ip:String, userAgent:String,token:String): Future[Completed] ={
    sessionRepo.save(Session(token,userId,userAgent,ip))
  }

  def delete(sessionId: String): Future[DeleteResult] = sessionRepo.delete(sessionId)

  def updateLastActivity(sessionId: String): Future[UpdateResult] = sessionRepo.updateLastActivity(sessionId)

  def setToken(sessionId: String, token: String): Future[UpdateResult] = sessionRepo.setToken(sessionId, token)
}
