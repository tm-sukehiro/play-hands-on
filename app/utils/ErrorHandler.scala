package utils

import javax.inject.{Inject, Provider}

import com.mohiva.play.silhouette.api.actions.SecuredErrorHandler
import play.api.http.DefaultHttpErrorHandler
import play.api.i18n.{I18nSupport, Messages, MessagesApi}
import play.api.mvc.Results._
import play.api.mvc.{RequestHeader, Result}
import play.api.routing.Router
import play.api.{Configuration, OptionalSourceMapper}

import scala.concurrent.Future
import controllers.{WebJarAssets, routes}

class ErrorHandler @Inject() (
                               val messagesApi: MessagesApi,
                               env: play.api.Environment,
                               config: Configuration,
                               sourceMapper: OptionalSourceMapper,
                               router: javax.inject.Provider[Router],
                               p: Provider[WebJarAssets])
  extends DefaultHttpErrorHandler(env, config, sourceMapper, router)
    with SecuredErrorHandler with I18nSupport {

  // https://www.playframework.com/documentation/2.5.x/Migration25#Handling-legacy-components
  implicit lazy val webJarAssets = p.get()

  override def onNotAuthenticated(implicit request: RequestHeader): Future[Result] =
    Future.successful(Redirect(routes.Auth.signIn()))

  override def onNotAuthorized(implicit request: RequestHeader): Future[Result] =
    Future.successful(Redirect(routes.Auth.signIn()).flashing("error" -> Messages("error.accessDenied")))

  override def onNotFound(request: RequestHeader, message: String): Future[Result] =
    Future.successful(Ok(views.html.errors.notFound(request)))

  override def onServerError(request:RequestHeader, exception:Throwable):Future[Result] =
    Future.successful(Ok(views.html.errors.serverError(request, exception)))
}
