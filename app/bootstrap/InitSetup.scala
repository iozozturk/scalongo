package bootstrap

import com.google.inject.Inject
import com.mongodb.client.model.IndexOptions
import com.mongodb.client.model.Sorts._
import play.Logger
import repos.Mongo

class InitSetup @Inject()(mongo: Mongo) {

  def preMessages(): Unit = {
    Logger.info("Bootstrapping application..")
  }

  def defineIndexes(): Unit = {
    val options: IndexOptions = new IndexOptions().unique(true)
    mongo.db.getCollection("users").createIndex(ascending("email"), options).subscribe((s: String) => {
      Logger.info("email index created on User collection")
    })
  }

  preMessages()
  defineIndexes()

}
