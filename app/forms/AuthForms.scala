package forms

import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.Messages

/**
  * Created by ismet on 06/12/15.
  */
object AuthForms {

  case class SignupData(email: String, username: String, password: String, name: String)

  // Signup
  def SignupForm(implicit messages: Messages) :Form[SignupData]  = Form(
    mapping(
      "email" -> email,
      "username" -> nonEmptyText,
      "password" -> nonEmptyText.verifying(Messages("error.password.minLength"), password => password.length >= 6),
      "name" -> nonEmptyText
    )
    (SignupData.apply)(SignupData.unapply))

  // Sign in
  case class SignInData(email:String, password:String, rememberMe:Boolean)
  val signInForm = Form(mapping(
    "email" -> email,
    "password" -> nonEmptyText,
    "rememberMe" -> boolean
  )(SignInData.apply)(SignInData.unapply))

  // Start password recovery
  val emailForm = Form(single("email" -> email))

  // Password recovery
  def resetPasswordForm(implicit messages:Messages) = Form(tuple(
    "password1" -> nonEmptyText.verifying(Messages("error.password.minLength"),password => password.length < 6),
    "password2" -> nonEmptyText
  ).verifying(Messages("error.passwordsDontMatch"), password => password._1 == password._2))

}
