package controllers

import javax.inject.Inject

import play.api.data.Forms._
import play.api.data._
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.mvc._
import services.{Todo, TodoService}
import views._
import entities.ItemCategory._
import entities._

class TodoController @Inject() (todoService: TodoService, val messagesApi: MessagesApi) extends Controller with I18nSupport {

  def index() = Action { implicit request =>
    Ok("Your new application is ready.").as("text/html")
  }

  def helloworld() = Action { implicit request =>
    Ok("Hello World")
  }

  def list() = Action { implicit request =>
    val items: Seq[Todo] = todoService.list()
    Ok(html.list(items))
  }

  val todoForm: Form[String] = Form("name" -> nonEmptyText)

  def create() = Action {
    Ok(html.createForm(todoForm))
  }

  def save() = Action { implicit request =>
    val name: String = todoForm.bindFromRequest().get
    todoService.insert(Todo(name))
    Redirect(routes.TodoController.list())
  }

  def postTest() = Action { implicit request =>
    val json: JsValue = request.body.asJson.getOrElse(JsNull)

    Ok(json)
  }

  def sample() = Action { implicit request =>

    def numberToJson(n: Option[Number]): JsValue = n.map(n => JsNumber(n.id)).getOrElse(JsNull)

    implicit val pageConditionWrites = (
      (__ \ 'limit).write[Int] ~
      (__ \ 'offset).write[Int]
    )(unlift(PageCondition.unapply))

    val category: ItemCategory = Weapon

    implicit val itemCategoryWrites =
      (__ \ 'itemCategory).write[String]
    unlift(ItemCategory.unapply)

    implicit object ItemCategoryWrites extends Writes[ItemCategory] {
      override def writes(category: ItemCategory): JsValue = {
        JsString(unlift(ItemCategory.unapply)(category))
      }
    }

    Ok(
      Json.obj(
        "test" -> 1,
        "some" -> Option("1").isDefined,
        "none" -> Option(null).isDefined,
        "num_some" -> numberToJson(Option(Number(1))),
        "num_none" -> numberToJson(None),
        "sub1" -> PageCondition(10, 0),
        "sub2" -> category
      )
    )
  }

  case class Number(id: Int)
  case class PageCondition(limit: Int, offset: Int) {
    require(limit >= 0 && offset >= 0)
  }

}
