package common

import play.api.Logger

trait AppLogger {
  protected lazy val logger = Logger(getClass.getName)
}
