package mail

import com.google.inject.Inject
import common.AppLogger
import play.api.Configuration
import play.api.libs.ws.{WSAuthScheme, WSClient}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

class MailService @Inject()(WS:WSClient, config:Configuration) extends AppLogger{
  private val url: String = config.get[String]("mailgun.url") + "/messages"
  private val apiKey: String = config.get[String]("mailgun.apiKey")

  def send(name:String, to:String, content:String, subject:String): Unit =  {
    val htmlContent = "<html><b>Hi</b> this is a sample <h1>mail</h1></html>"
    val mailData = Map(
      "from" -> Seq("Play Team <no-reply@ismetozozturk.com>"),
      "to" -> Seq(s"$name <$to>"),
      "subject" -> Seq(subject),
      "text" -> Seq(content))
      "html" -> Seq(htmlContent)
    logger.info(s"Sending mail to:$to with subject: $subject")

    //todo improve logging on error response from mailgun
    val response = WS.url(url).withAuth("api", apiKey, WSAuthScheme.BASIC).post(mailData).onComplete {
      case Success(value) =>  logger.info(s"Mailgun response: ${value.json}")
      case Failure(f) => logger.error(s"Sending mail failed with: ${f.getMessage}")
    }

  }

}
