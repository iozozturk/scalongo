import javax.inject.Inject

import filters.LoggingFilter
import play.api.http.HttpFilters

/**
  * Created by ismet on 16/12/15.
  */
class Filters @Inject()(
                         log: LoggingFilter
                       ) extends HttpFilters {

  val filters = Seq(log)
}
