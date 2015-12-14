package services

import org.mongodb.scala.{MongoClient, MongoDatabase}

/**
  * Created by ismet on 07/12/15.
  */
object Mongo {

  val client: MongoClient = MongoClient()

  val db: MongoDatabase = client.getDatabase("durt")

}
