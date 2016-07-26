package controllers

import javax.inject.Inject

import scala.concurrent.Future
import com.mohiva.play.silhouette.api.Silhouette
import play.api.libs.json._
import play.api.mvc._
import play.api.i18n.{I18nSupport, Messages, MessagesApi}
import models.User
import User._
import com.mohiva.play.silhouette.api.actions.SecuredErrorHandler
import utils.auth.CookieEnv

class RestApi @Inject() (
                          val messagesApi: MessagesApi,
                          val silhouette: Silhouette[CookieEnv]) extends Controller with I18nSupport {

  import silhouette._

  def profile = SecuredAction.async { implicit request =>
    val json = Json.toJson(request.identity.profileFor(request.authenticator.loginInfo).get)
    val prunedJson = json.transform(
      (__ \ 'loginInfo).json.prune andThen
        (__ \ 'passordInfo).json.prune andThen
        (__ \ 'oauth1Info).json.prune)
    prunedJson.fold(
      _ => Future.successful(InternalServerError(Json.obj("error" -> Messages("error.profileError")))),
      js => Future.successful(Ok(js))
    )
  }

  val errorHandler = new SecuredErrorHandler {
    override def onNotAuthenticated(implicit request: RequestHeader) = {
      Future.successful(Unauthorized(Json.obj("error" -> Messages("error.profileUnauth"))))
    }
    override def onNotAuthorized(implicit request: RequestHeader) = {
      Future.successful(Forbidden("local.not.authorized"))
    }
  }
}
