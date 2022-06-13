package org.floxx.env.api

import org.floxx.UserInfo
import org.floxx.env.service.statService
import org.http4s.AuthedRoutes
import org.http4s.dsl.Http4sDsl
import io.circe.syntax._
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import zio.interop.catz._
import org.floxx.env.utils.json.CirceValueClassCustomAuto._

object statsApi {

  val dsl =  Http4sDsl[ApiTask]

  import dsl._




  def api = AuthedRoutes.of[UserInfo,ApiTask] {
    case GET -> Root / "stats" / "slots" as _ =>
        statService.slotsStatus >>= (statItems =>
          Ok(statItems.asJson)
          )
  }

}
