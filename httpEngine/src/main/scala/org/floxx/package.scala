package org

import akka.http.scaladsl.model.{StatusCode, StatusCodes}

package object floxx {


  type SlotId = String

  sealed trait BusinessError {
    val code: String
    val message: String

    override def toString: String = s"$code - $message"

  }

  type BusinessVal[T] = Either[BusinessError, T]

  case class InvalidError(message: String) extends BusinessError {
    val code: String = "000"
  }



  def handleError(businessError: BusinessError): (StatusCode, String) =
    businessError match {
      case e: InvalidError => conflictHandle(e)
    }

  private def conflictHandle(message: BusinessError): (StatusCode, String) = StatusCodes.Conflict -> message.toString


  object Messages {

    def ofCampaign(campaignId: String): String = s"Reference to non-existent campaign: $campaignId"

  }


}
