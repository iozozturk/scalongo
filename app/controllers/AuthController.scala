package controllers

import java.util.UUID

import actions.SecureAction
import com.github.t3hnar.bcrypt._
import com.google.inject.Inject
import com.mongodb.MongoWriteException
import forms.AuthForms
import models.{Session, User}
import play.api.Play.current
import play.api.data.Form
import play.api.i18n.Messages.Implicits._
import play.api.i18n.MessagesApi
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import play.api.mvc._
import services.{SessionService, UserService}

import scala.concurrent.Future

/**
  * Created by ismet on 13/12/15.
  */
class AuthController @Inject()(userService: UserService,
                               sessionService: SessionService,
                               secureAction: SecureAction,
                               val messagesApi: MessagesApi) extends Controller {

  def signup = Action.async(BodyParser { implicit request => parse.form(AuthForms.signupForm, onErrors = { errorForm: Form[AuthForms.SignupData] => BadRequest(errorForm.errorsAsJson) })(request) }) { implicit request =>

    val signupData = request.body

    val user = User(
      UUID.randomUUID().toString,
      signupData.name,
      signupData.email,
      signupData.username,
      signupData.password.bcrypt,
      System.currentTimeMillis())

    userService.save(user).map((_) => {
      Ok
    }).recoverWith {
      case e: MongoWriteException => Future {
        Forbidden
      }
      case _ => Future {
        Forbidden
      }
    }

  }

  def login = Action.async(BodyParser { implicit request => parse.form(AuthForms.loginForm, onErrors = { errorForm: Form[AuthForms.LoginData] => BadRequest(errorForm.errorsAsJson) })(request) }) { implicit request =>
    val loginData = request.body
    userService.findByEmail(loginData.username).map((user: User) => {
      if (loginData.password.isBcrypted(user.password)) {
        val sessionId: String = UUID.randomUUID().toString
        val currentTimeMillis: Long = System.currentTimeMillis()
        val session: Session = models.Session(
          sessionId,
          user._id,
          request.remoteAddress,
          request.headers.get("User-Agent").get,
          currentTimeMillis,
          currentTimeMillis
        )
        sessionService.save(session)
        val response = Map("sessionId" -> sessionId)
        Ok(Json.toJson(response)).withCookies(Cookie("sessionId", sessionId))
      } else {
        Unauthorized
      }
    }).recoverWith {
      case e: IllegalStateException => Future {
        Forbidden
      }
    }
  }

  def securedSampleAction = secureAction { implicit request =>
    Ok
  }

  def logout = secureAction { implicit request =>
    sessionService.delete(request.sessionId)
    Ok.discardingCookies(DiscardingCookie("sessionId"))
  }


}
