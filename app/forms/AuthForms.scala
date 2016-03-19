package forms

import play.api.libs.json.Json

/**
  * Created by ismet on 06/12/15.
  */
object AuthForms {

  case class SignupData(email: String, username: String, password: String, name: String)

  implicit val signupFormat = Json.format[SignupData]

  case class LoginData(username: String, password: String)

  implicit val loginFormat = Json.format[LoginData]

}
