package daos

import javax.inject.Inject

import scala.concurrent.{Await, Future}
import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.impl.providers.OAuth1Info
import com.mohiva.play.silhouette.persistence.daos.DelegableAuthInfoDAO
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import play.modules.reactivemongo.ReactiveMongoApi
import play.modules.reactivemongo.json._
import reactivemongo.play.json.collection.JSONCollection
import models.User
import User._

import scala.concurrent.duration.Duration

class OAuth1InfoDao @Inject() (
                                val reactiveMongoApi: ReactiveMongoApi
                              ) extends DelegableAuthInfoDAO[OAuth1Info] {
  val users = Await.result(reactiveMongoApi.database.map(_.collection[JSONCollection]("users")), Duration.Inf)

  def find(loginInfo:LoginInfo):Future[Option[OAuth1Info]] = for {
    user <- users.find(Json.obj(
      "profiles.loginInfo" -> loginInfo
    )).one[User]
  } yield user.flatMap(_.profiles.find(_.loginInfo == loginInfo)).flatMap(_.oauth1Info)

  def add(loginInfo:LoginInfo, authInfo:OAuth1Info):Future[OAuth1Info] =
    users.update(Json.obj(
      "profiles.loginInfo" -> loginInfo
    ), Json.obj(
      "$set" -> Json.obj("profiles.$.oauth1Info" -> authInfo)
    )).map(_ => authInfo)

  def update(loginInfo:LoginInfo, authInfo:OAuth1Info):Future[OAuth1Info] =
    add(loginInfo, authInfo)

  def save(loginInfo:LoginInfo, authInfo:OAuth1Info):Future[OAuth1Info] =
    add(loginInfo, authInfo)

  def remove(loginInfo:LoginInfo):Future[Unit] =
    users.update(Json.obj(
      "profiles.loginInfo" -> loginInfo
    ), Json.obj(
      "$pull" -> Json.obj(
        "profiles" -> Json.obj("loginInfo" -> loginInfo)
      )
    )).map(_ => ())
}
