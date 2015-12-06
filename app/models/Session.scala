package models

/**
  * Created by ismet on 06/12/15.
  */
case class Session(
                    id: String,
                    userId: String,
                    ip: String,
                    userAgent: String,
                    consumer: String,
                    timestamp: Long,
                    lastActivity: Long
                  )
