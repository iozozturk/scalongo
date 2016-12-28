package repos

import com.google.inject.{Inject, Singleton}
import com.typesafe.config.{Config, ConfigFactory}
import org.mongodb.scala.{MongoClient, MongoDatabase}
import play.Logger
import play.api.Configuration
import play.api.inject.ApplicationLifecycle

import scala.concurrent.Future

/**
  * Created by ismet on 07/12/15.
  */
@Singleton
class Mongo @Inject()(applicationLifecycle: ApplicationLifecycle, configuration: Configuration) {

  private val config: Config = ConfigFactory.load(getClass.getClassLoader, "application.conf")

  private val dbName: String = config.getString("mongo.name")
  private val dbAddress: String = config.getString("mongo.host")
  private val dbPort: String = config.getString("mongo.port")

  val client: MongoClient = MongoClient(s"mongodb://$dbAddress:$dbPort")

  val db: MongoDatabase = client.getDatabase(dbName)

  applicationLifecycle.addStopHook(() => {
    Logger.warn("Closing Mongo connection")
    Future.successful(client.close())
  })
}
