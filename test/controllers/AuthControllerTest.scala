package controllers

import com.mongodb.client.result.DeleteResult
import models.{Session, User}
import org.mockito.Matchers._
import org.mockito.Mockito._
import org.mongodb.scala.Completed
import org.scalatest.BeforeAndAfterEach
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import play.api.Configuration
import play.api.inject.DefaultApplicationLifecycle
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.{JsObject, Json}
import play.api.test.FakeRequest
import play.api.test.Helpers._
import repos.Mongo
import services.{SessionService, UserService}

import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}

/**
  * Created by trozozti on 13/10/2016.
  */
class AuthControllerTest extends PlaySpec with OneAppPerSuite with MockitoSugar with BeforeAndAfterEach {
  val mongo = new Mongo(new DefaultApplicationLifecycle, Configuration("mongo.db.name" -> "scalongo"))
  override protected def beforeEach(): Unit = {
    Await.result(mongo.db.drop().toFuture(), 5.seconds)
  }

  "AuthControllerTest" should {

    val mockUserService = mock[UserService]
    val mockSessionService = mock[SessionService]
    val sampleUser =
      """{
        |    "email": "iozozturk@gmail.com",
        |    "username": "iozozturk",
        |    "password": "123456",
        |    "name": "ismet"
        |}""".stripMargin

    "signup" in {
      when(mockUserService.save(User(Json.parse(sampleUser).as[JsObject]))) thenReturn Future {
        new Completed
      }

      val Some(response) = route(app, FakeRequest(POST, "/signup").withBody(Json.parse(sampleUser))
        .withHeaders(("contentType", "application/json")))

      status(response) mustBe OK
      contentAsBytes(response).length === 0
    }

    "login" in {
      val sampleUser = Json.parse(
        """{
          |    "email": "iozozturk@gmail.com",
          |    "username": "iozozturk",
          |    "password": "123456",
          |    "name": "ismet"
          |}""".stripMargin).as[JsObject]

      val loginBody =
        """
          |{
          |    "username": "iozozturk",
          |    "password": "123456"
          |}
        """.stripMargin

      when(mockUserService.findByUsername("iozozturk")) thenReturn Future(User(sampleUser))
      when(mockSessionService.save(any[Session])) thenReturn Future(new Completed)

      val Some(response) = route(app, FakeRequest(POST, "/login").withBody(Json.parse(loginBody))
        .withHeaders(
          ("contentType", "application/json"),
          ("User-Agent", "test-agent")))

      status(response) mustBe OK
      println(headers(response))
      cookies(response).get("sessionId") mustBe defined

      contentAsBytes(response).length === 0
    }

    "logout" in {
      when(mockSessionService.delete(any[String])) thenReturn Future(new DeleteResult {override def getDeletedCount = ???
        override def wasAcknowledged() = true
      })

      val Some(response) = route(app, FakeRequest(POST, "/logout"))

      status(response) mustBe OK
      cookies(response).get("sessionId") mustBe None

      contentAsBytes(response).length === 0
    }

  }


}
