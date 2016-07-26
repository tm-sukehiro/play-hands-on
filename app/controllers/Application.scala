package controllers

import javax.inject.Inject

import scala.concurrent.Future
import com.mohiva.play.silhouette.api.Silhouette
import com.mohiva.play.silhouette.impl.providers.SocialProviderRegistry
import play.api.mvc._
import play.api.i18n.{I18nSupport, MessagesApi}
import utils.auth.CookieEnv

class Application @Inject() (
                              val messagesApi: MessagesApi,
                              silhouette: Silhouette[CookieEnv],
                              socialProviderRegistry: SocialProviderRegistry,
                              implicit val webJarAssets: WebJarAssets) extends Controller {

  import silhouette._

  def index = UserAwareAction.async { implicit request =>
    Future.successful(Ok(views.html.index(request.identity, request.authenticator.map(_.loginInfo))))
  }

  def profile = SecuredAction { implicit request =>
    Ok(views.html.profile(request.identity, request.authenticator.loginInfo, socialProviderRegistry))
  }
}
