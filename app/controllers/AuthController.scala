package controllers

import java.util.UUID

import com.github.t3hnar.bcrypt._
import com.google.inject.Inject
import com.mongodb.MongoWriteException
import forms.AuthForms
import models.User
import play.api.Play.current
import play.api.data.Form
import play.api.i18n.Messages.Implicits._
import play.api.i18n.MessagesApi
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc.{Action, BodyParser, Controller}
import services.UserService

import scala.concurrent.Future

/**
  * Created by ismet on 13/12/15.
  */
class AuthController @Inject()(userService: UserService,
                               val messagesApi: MessagesApi) extends Controller {

  def signup = Action.async(BodyParser { implicit request => parse.form(AuthForms.SignupForm, onErrors = { errorForm: Form[AuthForms.SignupData] => BadRequest(errorForm.errorsAsJson) })(request) }) { implicit request =>

    val signupData = request.body

    val user = User(UUID.randomUUID(),
      signupData.name,
      signupData.email,
      null,
      signupData.username,
      signupData.password.bcrypt,
      System.currentTimeMillis())

    userService.save(user).toFuture().map((_) => {
      Ok
    }).recoverWith {
      case e: MongoWriteException => Future {
        Forbidden
      }
    }

  }

  def login = TODO

  def logout = TODO


}
