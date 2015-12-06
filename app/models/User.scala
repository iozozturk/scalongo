package models

/**
  * Created by ismet on 06/12/15.
  */
case class User(
                 _id: String,
                 name: String,
                 email: String,
                 phoneNumber: String,
                 username: String,
                 password: String,
                 timestamp: Long
               )
