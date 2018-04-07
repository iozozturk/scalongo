package filters

import akka.stream.Materializer
import com.google.inject.Inject
import org.mongodb.scala._
import play.api.Logger
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

class ExceptionFilter @Inject()(implicit val mat: Materializer, ec: ExecutionContext) extends Filter {

  def apply(nextFilter: RequestHeader => Future[Result])
           (requestHeader: RequestHeader): Future[Result] = {

    nextFilter(requestHeader).map { result =>
      result
    } transform (byPassResult, logError) recover {
      case _: MongoWriteException => Results.Forbidden
      case e: Exception => Results.InternalServerError(e.getMessage)
      case _ => Results.InternalServerError
    }
  }

  def logError(throwable: Throwable) = {
    Logger.error(throwable.getMessage)
    throwable.printStackTrace
    throwable
  }

  def byPassResult(result: Result) = result

}
