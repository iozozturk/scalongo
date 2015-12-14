package controllers

import java.util.UUID

import com.google.inject.Inject
import forms.AuthForms
import models.User
import org.mongodb.scala.Completed
import play.Logger
import play.api.Play.current
import play.api.i18n.Messages.Implicits._
import play.api.i18n.MessagesApi
import play.api.mvc.{BodyParsers, Action, Controller}
import services.UserService

/**
  * Created by ismet on 13/12/15.
  */
class AuthController @Inject()(userService: UserService,
                               val messagesApi: MessagesApi) extends Controller {

  def signup = Action (BodyParsers.parse.json) { implicit request =>
    AuthForms.SignupForm.bindFromRequest().fold(
      errorForm => {
        BadRequest(errorForm.errorsAsJson)
      },
      signupForm => {
        val user = User(UUID.randomUUID(),
          signupForm.name,
          signupForm.email,
          null,
          signupForm.username,
          signupForm.password,
          System.currentTimeMillis())

        userService.save(user).subscribe((test: Completed) => {
          Logger.info("Signup success!")
        })
        Ok
      }
    )
  }

}
