import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import play.api.test.FakeRequest
import play.api.test.Helpers._

class ApplicationSpec extends PlaySpec with OneAppPerSuite {

  "Application" should {

    "send 404 on a bad request" in {
      route(app, FakeRequest(GET, "/boum")).map(status) mustBe Some(NOT_FOUND)
    }

    "check the service is alive" in {
      val Some(home) = route(app, FakeRequest(GET, "/"))

      status(home) mustBe OK
      contentAsBytes(home).length === 0
    }
  }
}
