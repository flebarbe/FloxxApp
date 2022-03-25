package org.floxx.env.repository

import doobie.implicits._
import doobie.util.fragment
import org.floxx.env.repository.DbTransactor.TxResource
import org.floxx.{FloxxError, model}
import org.floxx.model.{Hit, SlotId}
import zio._
import zio.interop.catz._

import java.util.UUID

object hitRepository {

  trait HitRepo {
    def loadHitBy(slotIds: Seq[SlotId]): Task[Seq[model.Hit]]
    def save(hit: Hit): Task[Int]

  }

  case class HitRepoCfg(r: TxResource) extends HitRepo  {

    def save(hit: Hit): Task[Int] =
      sql"insert into hit (hitid,hitslotid,percentage,datetime) values (${UUID
        .randomUUID()
        .toString}, ${hit.hitSlotId}, ${hit.percentage},${hit.dateTime} )".update.run.transact(r.xa)

    def loadHitBy(slotIds: Seq[SlotId]): Task[Seq[Hit]] = {

      val root: fragment.Fragment = sql"select hitid,hitslotid,percentage,datetime from hit where"
      val criteria                = fr"${slotIds.map(id => s"hitslotid=$id").mkString(",")}"

      (root ++ criteria).query[Hit].to[Seq].transact(r.xa)
    }

  }

  val layer: RLayer[Has[TxResource], Has[HitRepo]] = (HitRepoCfg(_)).toLayer

  def save(hit: Hit): RIO[Has[HitRepo], Int] = ZIO.serviceWith[HitRepo](_.save(hit))
  def loadHitBy(slotIds: Seq[SlotId]): RIO[Has[HitRepo], Seq[Hit]] =
    ZIO.serviceWith[HitRepo](_.loadHitBy(slotIds))

}
