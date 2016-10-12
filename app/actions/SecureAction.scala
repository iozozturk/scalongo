package actions

import com.google.inject.{Inject, Singleton}
import models.User
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc._
import services.{SessionService, UserService}

import scala.concurrent.Future
import scala.util.control.NonFatal

/**
  * Created by ismet on 21/12/15.
  */
class UserRequest[A](val user: User, val _session: models.Session, request: Request[A]) extends WrappedRequest[A](request) {
  def sessionId = request.cookies.get("sessionId").get.value
}

@Singleton
class SecureAction @Inject()(sessionService: SessionService,
                             userService: UserService) extends ActionBuilder[UserRequest] with ActionRefiner[Request, UserRequest] {

  override protected def refine[A](request: Request[A]): Future[Either[Result, UserRequest[A]]] = {

    request.cookies.get("sessionId").map { c: Cookie =>
      sessionService
        .findUserAndSession(c.value)
        .map(tuple => Right(new UserRequest[A](tuple._2, tuple._1, request)))
        .recover {
          case NonFatal(_) => Left(Results.Unauthorized)
        }
    }.getOrElse(Future {
      Left(Results.Unauthorized)
    })

  }
}
