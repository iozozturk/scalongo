package repos

import com.google.inject.{Inject, Singleton}
import com.mongodb.client.result.UpdateResult
import org.mongodb.scala.bson.collection.mutable.Document
import org.mongodb.scala.model.UpdateOptions
import org.mongodb.scala.result.DeleteResult
import org.mongodb.scala.{Completed, MongoDatabase}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._

import scala.async.Async._
import scala.concurrent.Future

@Singleton
class MongoCollectionFactory @Inject()(mongo: Mongo) {

  def makeCollection(collName: String, db: MongoDatabase = mongo.db) = new MongoCollection(collName, db)

  class MongoCollection(val collName: String, db: MongoDatabase) {

    def coll = db.getCollection(collName)

    def find(query: JsObject = Json.obj()): Future[Seq[JsObject]] = async {

      val items = await(coll.find(Document(query.toString())).toFuture())
      val jsObjects = items.map { d =>
        Json.parse(d.toJson()).as[JsObject]
      }
      jsObjects
    }

    def findOne(query: JsObject = Json.obj()): Future[JsObject] = find(query).map(_.head)

    def findOneWithId(id: String): Future[JsObject] = {
      val query = Json.obj("_id" -> id)
      findOne(query)
    }

    def insert(data: JsObject): Future[Completed] = {
      coll.insertOne(org.mongodb.scala.Document(data.toString())).head()
    }

    def upsert(data: JsObject): Future[UpdateResult] = async {
      val currentData = await(find(Json.obj("_id" -> "1")))
      await(_updateOne(Json.obj("_id" -> "1"), data, upsert = true))
    }

    def bulkUpsert(data: Seq[JsObject]) = data.map(upsert)

    def partialUpdate(selector: JsObject, data: JsObject) = _updateOne(selector, data, upsert = false)

    def removeOne(query: JsObject): Future[DeleteResult] = coll.deleteOne(Document(query.toString())).head()

    def removeMany(query: JsObject): Future[DeleteResult] = coll.deleteMany(Document(query.toString())).head()

    def count(query: JsObject): Future[Long] = {
      coll.count(Document(query.toString())).head()
    }

    private def _updateOne(selector: JsObject, data: JsObject, upsert: Boolean): Future[UpdateResult] = {
      coll.updateOne(Document(selector.toString), Document(data.toString()), UpdateOptions().upsert(upsert)).head().map { result =>
        result
      }
    }
  }

}
