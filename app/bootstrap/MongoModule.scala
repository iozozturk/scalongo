package bootstrap

import com.google.inject.AbstractModule

/**
  * Created by ismet on 16/12/15.
  */
class MongoModule extends AbstractModule {
  protected def configure(): Unit = {
    bind(classOf[InitSetup]).asEagerSingleton()
  }
}