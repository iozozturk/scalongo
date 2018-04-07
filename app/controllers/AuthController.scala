package controllers

import java.util.UUID

import actions.SecureAction
import com.github.t3hnar.bcrypt._
import com.google.inject.Inject
import common.AppLogger
import common.JsonExtensions.ForJsValue
import mail.MailService
import play.api.libs.json.{JsObject, JsValue, Json}
import play.api.mvc._
import services.{PassResetService, SessionService, UserService}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

class AuthController @Inject()(userService: UserService,
                               sessionService: SessionService,
                               secureAction: SecureAction,
                               mailService: MailService,
                               passResetService: PassResetService)
                              (implicit val executionContext: ExecutionContext) extends InjectedController with AppLogger {

  def signup = Action.async { implicit request =>
    val body: JsValue = request.body.asJson.get

    val name = (body \ "name").as[String]
    val email = (body \ "email").as[String]
    val password = (body \ "password").as[String].bcrypt

    userService.save(name, email, password).map((user) => {
      passResetService.sendActivateAccountMail(user)
      Ok
    })
  }

  def login = Action.async { implicit request =>
    val body: JsValue = request.body.asJson.get.as[JsObject]
    val email = body.getAs[String]("email")
    val password = body.getAs[String]("password")

    userService.findByEmail(email).map { user =>
      if (user.isEmpty) {
        logger.info(s"login blocked, reason=user_not_found, user=$email")
        Unauthorized
      } else {
        if (!user.get.active) {
          logger.info(s"login blocked, reason=inactive_account, user=$email")
          Unauthorized
        } else if (password.isBcrypted(user.get.password)) {
          val sessionId: String = UUID.randomUUID().toString
          sessionService.save(user.get._id, request.remoteAddress, request.headers.get("User-Agent").get, sessionId)
          val responseObj = Json.obj("sessionId" -> sessionId, "user" -> user.get)
          Ok(responseObj).withCookies(Cookie("sessionId", sessionId))
        } else {
          logger.info(s"login blocked, reason=wrong_pass, user=$email")
          Unauthorized
        }
      }
    }.recover {
      case e =>
        logger.info(s"login blocked, reason=${e.getMessage}")
        e.printStackTrace()
        Unauthorized
    }
  }

  def securedSampleAction = secureAction { implicit request =>
    Ok
  }

  def logout = secureAction { implicit request =>
    sessionService.delete(request.sessionId)
    Ok.discardingCookies(DiscardingCookie("sessionId"))
  }

  def requestResetPassword(email: String) = Action { implicit request =>
    userService.findByEmail(email) andThen {
      case Success(user) =>
        if (user.isDefined)
          passResetService.save(email).map { resetObj =>
            passResetService.sendResetRequestMail(user.get, resetObj)
          }
        else {
          logger.error(s"user not found with email=$email")
        }
      case Failure(e) =>
        logger.error(s"user not found with email:$email, message:${e.getMessage}")
        e.printStackTrace()
    }
    Accepted
  }

  def resetPassword(id: String) = Action.async { implicit request =>
    val body: JsValue = request.body.asJson.get.as[JsObject]
    val password = body.getAs[String]("password")


    passResetService.findIfNotUsed(id).flatMap { resetObj =>
      if (resetObj.isDefined) {
        passResetService.useResetRequest(id)
        userService.findByEmail(resetObj.get.email).flatMap { user =>
          passResetService.sendPassChangeMail(user.get, resetObj.get)
          userService.changePasword(user.get._id, password).map(_ => Ok)
        }
      } else {
        logger.error(s"reset password for resetId=$id not found")
        Future {
          Forbidden
        }
      }
    }
  }

  def activateAccount(activationId: String) = Action.async { implicit request =>
    userService.findByActivationId(activationId).map { user =>
      if (user.isDefined) {
        passResetService.sendUserActivatedMail(user.get)
        userService.activateUser(user.get._id)
        Ok
      } else {
        logger.info(s"user not found, activationId=$activationId")
        NotFound
      }
    }
  }

}
