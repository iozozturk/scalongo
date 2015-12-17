package services

import com.google.inject.{Inject, Singleton}
import org.mongodb.scala.{MongoClient, MongoDatabase}
import play.Logger
import play.api.inject.ApplicationLifecycle


import scala.concurrent.Future

/**
  * Created by ismet on 07/12/15.
  */
@Singleton
class Mongo @Inject()(applicationLifecycle: ApplicationLifecycle) {

  val client: MongoClient = MongoClient()

  val db: MongoDatabase = client.getDatabase("durt")

  applicationLifecycle.addStopHook(() => {
    Logger.warn("Closing Mongo connection")
    Future.successful(client.close())
  })
}
