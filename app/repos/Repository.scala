package repos

import models.common.{DocumentModelFactory, Entity}
import org.mongodb.scala.Completed
import org.mongodb.scala.result._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.{JsObject, Json}

import scala.async.Async._
import scala.concurrent.Future

abstract class Repository[E <: Entity[E] : DocumentModelFactory] {
  val collectionName: String

  def mongoCollectionFactory: MongoCollectionFactory

  lazy val collection: MongoCollectionFactory#MongoCollection = mongoCollectionFactory.makeCollection(collectionName)

  final def findOne(query: JsObject): Future[E] = {
    collection.findOne(query).map(DocumentModelFactory[E])
  }

  def find(query: JsObject): Future[Seq[E]] = async {
    val jsObjects = await(collection.find(query = query))
    jsObjects.map(DocumentModelFactory[E])
  }

  def findById(id: String): Future[E] = {
    collection.findOne(Json.obj("_id" -> id)).map(DocumentModelFactory[E])
  }

  def update(item: E): Future[UpdateResult] = {
    collection.partialUpdate(Json.obj("_id" -> item._id), item.json)
  }

  def save(item: E): Future[Completed] = {
    collection.insert(item.json)
  }

  def delete(id: String): Future[DeleteResult] = async {
    await(collection.removeOne(Json.obj("_id" -> id)))
  }

}

