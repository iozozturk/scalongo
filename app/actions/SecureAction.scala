package actions

import com.google.inject.{Singleton, Inject}
import models.User
import play.api.mvc._
import services.{SessionService, UserService}

import scala.concurrent.Future

/**
  * Created by ismet on 21/12/15.
  */
class UserRequest[A](val user: Future[User], request: Request[A]) extends WrappedRequest[A](request) {
  def sessionId = request.cookies.get("sessionId").get.value
}

@Singleton
class SecureAction @Inject()(sessionService: SessionService,
                             userService: UserService) extends ActionBuilder[UserRequest] with ActionRefiner[Request, UserRequest] {

  override protected def refine[A](request: Request[A]): Future[Either[Result, UserRequest[A]]] = {

    val result = request.cookies.get("sessionId")
      .map { sessionId =>
        sessionService.updateLastActivity(sessionId.value)
        sessionService.findUserBySessionId(sessionId.value)
      }
      .map(user => new UserRequest[A](user, request))
      .toRight(left = Results.Forbidden)

    Future.successful(result)
  }

}