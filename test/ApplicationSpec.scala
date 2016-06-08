import org.scalatestplus.play._
import play.api.libs.json.Json
import play.api.test._
import play.api.test.Helpers._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
class ApplicationSpec extends PlaySpec with OneAppPerTest {

  "Routes" should {

    "send 404 on a bad request" in  {
      route(app, FakeRequest(GET, "/boum")).map(status(_)) mustBe Some(NOT_FOUND)
    }

  }

  "HomeController" should {

    "render the index page" in {
      val home = route(app, FakeRequest(GET, "/")).get

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("Your new application is ready.")
    }

  }

  "TodoController" should {

    "render the posttest page is multi" in {
      val todo = route(app, FakeRequest(
        POST,
        "/posttest",
        FakeHeaders(Seq("Content-Type" -> "application/json")),
        Json.obj(
          "key1" -> "value1",
          "key2" -> "value2"
        )
      )).get

      status(todo) mustBe OK
      contentType(todo) mustBe Some("application/json")
      val node = Json.parse(contentAsString(todo))
      (node \ "key1").as[String] mustBe "value1"
      (node \ "key2").as[String] mustBe "value2"
    }

    "render the posttest page is single" in {
      val todo = route(app, FakeRequest(
        POST,
        "/posttest",
        FakeHeaders(Seq("Content-Type" -> "application/json")),
        Json.obj("key" -> "value")
      )).get

      status(todo) mustBe OK
      contentType(todo) mustBe Some("application/json")
      val node = Json.parse(contentAsString(todo))
      (node \ "key").as[String] mustBe "value"
    }

    "render the posttest page is empty" in {
      val todo = route(app, FakeRequest(
        POST,
        "/posttest"
      )).get

      status(todo) mustBe OK
      contentType(todo) mustBe Some("application/json")
      contentAsString(todo) isEmpty
    }

  }

}
