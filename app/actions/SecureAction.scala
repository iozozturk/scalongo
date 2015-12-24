package actions

import com.google.inject.{Inject, Singleton}
import models.User
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc._
import services.{SessionService, UserService}

import scala.concurrent.Future

/**
  * Created by ismet on 21/12/15.
  */
class UserRequest[A](val user: User, request: Request[A]) extends WrappedRequest[A](request) {
  def sessionId = request.cookies.get("sessionId").get.value
}

@Singleton
class SecureAction @Inject()(sessionService: SessionService,
                             userService: UserService) extends ActionBuilder[UserRequest] with ActionRefiner[Request, UserRequest] {

  override protected def refine[A](request: Request[A]): Future[Either[Result, UserRequest[A]]] = {
    val sessionId: String = request.cookies.get("sessionId").getOrElse[Cookie](Cookie("sessionId", "n/a")).value

    sessionService
      .findUserBySessionId(sessionId)
      .map(u => Right(new UserRequest[A](u, request)))
      .recover {
        case _ => Left[Result, UserRequest[A]](Results.Forbidden)
      }
  }
}
