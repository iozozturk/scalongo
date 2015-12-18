package bootstrap

import com.google.inject.Inject
import com.mongodb.client.model.IndexOptions
import com.mongodb.client.model.Sorts._
import play.Logger
import services.Mongo


/**
  * Created by ismet on 16/12/15.
  */
class InitSetup @Inject()(mongo: Mongo) {

  def defineIndexes(): Unit = {
    val options: IndexOptions = new IndexOptions().unique(true)
    mongo.db.getCollection("user").createIndex(ascending("username"), options).subscribe((s: String) => {
      Logger.info("User index created")
    })
  }

  defineIndexes()

}
