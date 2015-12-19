package controllers

import java.util.UUID

import com.github.t3hnar.bcrypt._
import com.google.inject.Inject
import com.mongodb.MongoWriteException
import forms.AuthForms
import models.User
import org.mongodb.scala.Observable
import org.mongodb.scala.bson.collection.immutable.Document
import play.api.Play.current
import play.api.data.Form
import play.api.i18n.Messages.Implicits._
import play.api.i18n.MessagesApi
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.{JsResult, Json}
import play.api.mvc._
import services.UserService

import scala.concurrent.Future

/**
  * Created by ismet on 13/12/15.
  */
class AuthController @Inject()(userService: UserService,
                               val messagesApi: MessagesApi) extends Controller {

  def signup = Action.async(BodyParser { implicit request => parse.form(AuthForms.signupForm, onErrors = { errorForm: Form[AuthForms.SignupData] => BadRequest(errorForm.errorsAsJson) })(request) }) { implicit request =>

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

//  def login = Action.async(BodyParser { implicit request => parse.form(AuthForms.loginForm, onErrors = { errorForm: Form[AuthForms.LoginData] => BadRequest(errorForm.errorsAsJson) })(request) }) { implicit request =>
//
//    val loginData = request.body
//    userService.find(loginData.username).head().map((userDoc:Document) =>{
//      val userJs: JsResult[User] = Json.fromJson[User](Json.parse(userDoc.toJson()))
//      val user :User = userJs.get
//      Ok
//
//    })
//       val result: JsResult[User] = Json.fromJson[User](Json.parse(userDoc.toJson()))



//  }

  def logout = TODO


}
