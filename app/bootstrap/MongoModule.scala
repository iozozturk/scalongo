package bootstrap

import com.google.inject.AbstractModule

class MongoModule extends AbstractModule {
  protected def configure(): Unit = {
    bind(classOf[InitSetup]).asEagerSingleton()
  }
}