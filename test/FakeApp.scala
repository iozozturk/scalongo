
import org.specs2.execute.AsResult
import org.specs2.mutable._
import play.Logger
import play.api.mvc._
import play.api.test.Helpers._
import play.api.test._

/**
  * Created by ismet on 29/12/15.
  */
trait FakeApp extends Around {

//  val appCfg = Map("mongo.db.name" -> "scalongo_test")
//
//  object FakeApp extends FakeApplication(
//    additionalConfiguration = appCfg
//  )
//
//  abstract override def around[T : AsResult](test: => T): Result = running(FakeApp) {
//    Logger.debug("Running test ==================================")
//    test  // run tests inside a fake application
//  }
}
