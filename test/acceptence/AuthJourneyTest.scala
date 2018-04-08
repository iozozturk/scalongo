package acceptence

import org.scalatest.BeforeAndAfterAll
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.libs.json.Json
import play.api.mvc.Cookie
import play.api.test.FakeRequest
import play.api.test.Helpers._
import repos.{Mongo, PassResetRepo, SessionRepo, UserRepo}

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

class AuthJourneyTest extends PlaySpec with GuiceOneAppPerSuite with BeforeAndAfterAll {
  private val mongo = app.injector.instanceOf(classOf[Mongo])
  private val userRepo = app.injector.instanceOf(classOf[UserRepo])
  private val sessionRepo = app.injector.instanceOf(classOf[SessionRepo])
  private val passResetRepo = app.injector.instanceOf(classOf[PassResetRepo])
  private val patience = 5.seconds

  override protected def beforeAll(): Unit = {
    Await.result(mongo.db.drop().toFuture(), patience)
  }

  "AuthControllerTest" should {

    val sampleUser =
      """{
        |    "email": "iozozturk+test@gmail.com",
        |    "password": "123456",
        |    "name": "ismet"
        |}""".stripMargin

    "signup" in {
      val Some(response) = route(app, FakeRequest(POST, "/api/signup").withBody(Json.parse(sampleUser))
        .withHeaders(("contentType", "application/json")))

      status(response) mustBe OK
      contentAsBytes(response).length === 0
    }

    "activate account" in {
      val user = Await.result(userRepo.findByEmail("iozozturk+test@gmail.com"), patience).get

      val Some(response) = route(app, FakeRequest(POST, s"/api/activation/${user.activationId}")
        .withHeaders(("contentType", "application/json")))

      status(response) mustBe OK

      contentAsBytes(response).length === 0
    }

    "login" in {
      val loginBody =
        """
          |{
          |    "email": "iozozturk+test@gmail.com",
          |    "password": "123456"
          |}
        """.stripMargin


      val Some(response) = route(app, FakeRequest(POST, "/api/login").withBody(Json.parse(loginBody))
        .withHeaders(("contentType", "application/json"), ("User-Agent", "test-agent")))

      status(response) mustBe OK
      cookies(response).get("sessionId") mustBe defined
      cookies(response).get("sessionId").get.value must not be empty

      contentAsBytes(response).length === 0
    }

    "logout" in {
      val user = Await.result(userRepo.findByEmail("iozozturk+test@gmail.com"), patience).get
      val session = Await.result(sessionRepo.findByUserId(user._id), patience)
      val Some(response) = route(app, FakeRequest(POST, "/api/logout").withCookies(Cookie("sessionId", session.head.token)))

      status(response) mustBe OK
      cookies(response).get("sessionId").get.value mustBe empty

      contentAsBytes(response).length === 0
    }

    "reset pass request" in {
      val Some(response) = route(app, FakeRequest(POST, "/api/reset/request/iozozturk+test@gmail.com"))

      status(response) mustBe OK

      contentAsBytes(response).length === 0
    }

    "reset pass" in {
      val passBody =
        """
          |{
          |    "password": "123456"
          |}
        """.stripMargin

      val passReset = Await.result(passResetRepo.findByEmail("iozozturk+test@gmail.com"), patience).get
      val Some(response) = route(app, FakeRequest(POST, s"/api/reset/pass/${passReset._id.toString}").withBody(Json.parse(passBody)))

      status(response) mustBe OK

      contentAsBytes(response).length === 0
    }

  }


}
