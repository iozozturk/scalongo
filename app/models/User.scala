package models

import java.util.UUID

/**
  * Created by ismet on 06/12/15.
  */
case class User(
                 _id: UUID,
                 name: String,
                 email: String,
                 phoneNumber: Option[String],
                 username: String,
                 password: String,
                 timestamp: Long
               )

object User {

  import play.api.libs.json.Json

  implicit val userFormat = Json.format[User]
}
