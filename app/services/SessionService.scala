package services

import com.google.inject.Inject
import daos.SessionDao
import models.Session

import scala.concurrent.Future

/**
  * Created by ismet on 20/12/15.
  */
class SessionService @Inject()(sessionDao: SessionDao) {
  def find(sessionId: String): Future[Session] = sessionDao.find(sessionId)

  def findByUserId(userId: String): Future[Session] = sessionDao.findByUserId(userId)

  def save(session: Session) = sessionDao.save(session)

}
