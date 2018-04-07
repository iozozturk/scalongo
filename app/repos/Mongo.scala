package repos

import com.google.inject.{Inject, Singleton}
import common.AppLogger
import models._
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.bson.codecs.Macros._
import org.mongodb.scala.{MongoClient, MongoDatabase}
import play.api.Configuration
import play.api.inject.ApplicationLifecycle

import scala.concurrent.Future

@Singleton
class Mongo @Inject()(applicationLifecycle: ApplicationLifecycle, config: Configuration) extends AppLogger {

  private val dbName: String = config.get[String]("mongo.name")
  private val dbAddress: String = config.get[String]("mongo.host")
  private val dbPort: String = config.get[String]("mongo.port")

  private val codecRegistry = fromRegistries(fromProviders(
    classOf[Session],
    classOf[PassReset],
    classOf[User]
  ), DEFAULT_CODEC_REGISTRY)

  val client: MongoClient = MongoClient(s"mongodb://$dbAddress:$dbPort")

  val db: MongoDatabase = client.getDatabase(dbName).withCodecRegistry(codecRegistry)

  applicationLifecycle.addStopHook(() => {
    logger.warn("Closing Mongo connection")
    Future.successful(client.close())
  })
}
