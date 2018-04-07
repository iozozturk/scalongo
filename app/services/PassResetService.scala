package services

import com.google.inject.Inject
import com.mongodb.client.result.{DeleteResult, UpdateResult}
import common.AppLogger
import mail.MailService
import models.{PassReset, User}
import org.bson.types.ObjectId
import play.api.Configuration
import repos.PassResetRepo

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class PassResetService @Inject()(passResetRepo: PassResetRepo,
                                 mailService: MailService,
                                 config: Configuration) extends AppLogger {

  def findIfNotUsed(id: String): Future[Option[PassReset]] = passResetRepo.findNotUsed(new ObjectId(id))

  def find(resetId: String): Future[Option[PassReset]] = passResetRepo.findById(resetId)

  def findByMail(mail: String): Future[Option[PassReset]] = passResetRepo.findByEmail(mail)

  def save(email: String): Future[PassReset] = {
    val resetObj = PassReset(email, used = false)
    passResetRepo.save(resetObj).map(_ => resetObj)
  }

  def delete(resetId: String): Future[DeleteResult] = passResetRepo.delete(resetId)

  def sendResetRequestMail(user: User, resetObj: PassReset): Unit = {
    logger.info(s"Sending reset password mail to:${user.name} ${user.email}")
    mailService.send(user.name, user.email, buildResetRequestMailContent(resetObj), "Reset Password")
  }

  def sendPassChangeMail(user: User, resetObj: PassReset): Unit = {
    logger.info(s"Sending password change mail to:${user.name} ${user.email}")
    mailService.send(user.name, user.email, buildPassResetMailContent(), "Password Change")
  }

  def sendActivateAccountMail(user: User): Unit = {
    logger.info(s"Sending account activation mail to:${user.name} ${user.email}")
    mailService.send(user.name, user.email, buildActivateAccountMailContent(user), "Activate Account")
  }

  def sendUserActivatedMail(user: User): Unit = {
    logger.info(s"Sending user activated mail to:${user.name} ${user.email}")
    mailService.send(user.name, user.email, buildAccountActivatedMailContent(), "Account Activated")
  }

  private def buildResetRequestMailContent(resetObj: PassReset) = {
    val domainUrl: String = config.get[String]("domainUrl")
    val resetLink = s"$domainUrl/auth/reset-password/${resetObj._id}"
    s"You can reset your password with this link: $resetLink \n Thank you."
  }

  private def buildActivateAccountMailContent(user: User) = {
    val domainUrl: String = config.get[String]("domainUrl")
    val activateLink = s"$domainUrl/auth/activation/${user.activationId}"
    s"Please activate your account with this link: $activateLink \n Thank you."
  }

  private def buildAccountActivatedMailContent() = {
    s"You activated your account. \n Thank you."
  }

  private def buildPassResetMailContent() = {
    s"You have changed your password. \n Thank you."
  }

  def useResetRequest(id: String): Future[UpdateResult] = {
    passResetRepo.setUsed(id, isUsed = true)
  }

}
