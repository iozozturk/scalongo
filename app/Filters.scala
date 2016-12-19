import javax.inject.Inject

import filters.{ExceptionFilter, LoggingFilter}
import play.api.http.HttpFilters

/**
  * Created by ismet on 16/12/15.
  */
class Filters @Inject()(
                         log: LoggingFilter,
                         exceptionFilter: ExceptionFilter
                       ) extends HttpFilters {

  val filters = Seq(log, exceptionFilter)
}
