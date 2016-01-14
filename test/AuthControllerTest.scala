import java.util.UUID
import java.util.concurrent.TimeUnit

import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import org.specs2.specification.{AfterEach, BeforeEach}
import play.api.test._
import services.{UserService, Mongo}

import scala.Some
import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
  * Created by ismet on 25/12/15.
  */
@RunWith(classOf[JUnitRunner])
class AuthControllerTest extends PlaySpecification with BeforeEach with AfterEach {

  val fakeApplication = FakeApplication(additionalConfiguration = Map("mongo.db.name" -> "scalongo_test"))
  val mongo = fakeApplication.injector.instanceOf(classOf[Mongo])
  val userService = fakeApplication.injector.instanceOf(classOf[UserService])

  val signupRequestBody =
    """{
        "username" : "ismet_user",
        "password" : "ismet_pass",
        "email" : "ismet@test.com",
        "name" : "ismet"
      }"""

  val loginRequestBody =
    """{
        "username" : "ismet_user",
        "password" : "ismet_pass"
      }"""

  override protected def before: Any = {

  }

  override protected def after: Any = {

  }

  "AuthControllerTest" should {

    "sign up successfully" in new WithApplication(fakeApplication) {
      Await.result(mongo.db.drop().head(), Duration(10, TimeUnit.SECONDS))
      val signupRequest: FakeRequest[String] = FakeRequest(POST, "/signup").withBody(signupRequestBody).withHeaders(("Content-Type", "application/json"))
      val response = route(signupRequest).get
      status(response) must be_==(OK)
      contentAsBytes(response).length === 0
    }

  }
}
