package org.floxx.env.configuration

import org.floxx.env.configuration.config.GlobalConfig
import org.scalatest.funsuite.AnyFunSuite
import pureconfig.ConfigSource
import pureconfig.generic.auto._

class ConfigTest extends AnyFunSuite{

  test("Load config test")  {

    ConfigSource.default.loadOrThrow[GlobalConfig]

  }

}
