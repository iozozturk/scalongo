package actions

import com.google.inject.{Inject, Singleton}
import models.User
import play.api.mvc._
import services.{SessionService, UserService}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.control.NonFatal

class UserRequest[A](val user: User, val _session: models.Session, request: Request[A]) extends WrappedRequest[A](request) {
  def sessionId: String = request.cookies.get("sessionId").get.value
}

@Singleton
class SecureAction @Inject()(sessionService: SessionService,
                             userService: UserService, val parser: BodyParsers.Default)
                            (implicit val executionContext: ExecutionContext)
  extends ActionBuilder[UserRequest, AnyContent] with ActionRefiner[Request, UserRequest] {

  override protected def refine[A](request: Request[A]): Future[Either[Result, UserRequest[A]]] = {

    request.cookies.get("sessionId").map { c: Cookie =>
      sessionService
        .findUserAndSession(c.value)
        .map { tuple =>
          if (tuple.isDefined)
            Right(new UserRequest[A](tuple.get._2, tuple.get._1, request))
          else Left(Results.Unauthorized)
        }
        .recover {
          case NonFatal(_) => Left(Results.Unauthorized)
        }
    }.getOrElse(Future {
      Left(Results.Unauthorized)
    })

  }
}
