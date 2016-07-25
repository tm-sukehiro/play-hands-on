package daos

import java.util.UUID
import javax.inject.Inject

import scala.concurrent.{Await, Future}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import play.modules.reactivemongo.ReactiveMongoApi
import play.modules.reactivemongo.json._
import reactivemongo.play.json.collection.JSONCollection
import models.UserToken

import scala.concurrent.duration.Duration

trait UserTokenDao {
  def find(id:UUID):Future[Option[UserToken]]
  def save(token:UserToken):Future[UserToken]
  def remove(id:UUID):Future[Unit]
}

class MongoUserTokenDao @Inject() (
                                    val reactiveMongoApi: ReactiveMongoApi
                                  ) extends UserTokenDao {
  val tokens = Await.result(reactiveMongoApi.database.map(_.collection[JSONCollection]("tokens")), Duration.Inf)

  def find(id:UUID):Future[Option[UserToken]] =
    tokens.find(Json.obj("id" -> id)).one[UserToken]

  def save(token:UserToken):Future[UserToken] = for {
    _ <- tokens.insert(token)
  } yield token

  def remove(id:UUID):Future[Unit] = for {
    _ <- tokens.remove(Json.obj("id" -> id))
  } yield ()
}
