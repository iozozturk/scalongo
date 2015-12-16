package controllers

import java.util.UUID

import com.google.inject.Inject
import forms.AuthForms
import models.User
import play.api.Play.current
import play.api.i18n.Messages.Implicits._
import play.api.i18n.MessagesApi
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc.{Action, BodyParsers, Controller}
import services.UserService

import com.github.t3hnar.bcrypt._

/**
  * Created by ismet on 13/12/15.
  */
class AuthController @Inject()(userService: UserService,
                               val messagesApi: MessagesApi) extends Controller {

  def signup = Action.async(BodyParsers.parse.json) { implicit request =>
    AuthForms.SignupForm.bindFromRequest().fold(
      errorForm => {
        scala.concurrent.Future {
          BadRequest(errorForm.errorsAsJson)
        }
      },
      signupForm => {
        val user = User(UUID.randomUUID(),
          signupForm.name,
          signupForm.email,
          null,
          signupForm.username,
          signupForm.password.bcrypt,
          System.currentTimeMillis())

        userService.save(user).toFuture().map((_) => {
          Ok
        })
      }
    )
  }

}
