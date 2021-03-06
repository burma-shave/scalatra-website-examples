package com.example.app

import _root_.akka.actor.ActorSystem
import _root_.akka.dispatch.{Future, ExecutionContext}
import _root_.akka.dispatch.{Promise => AkkaPromise}

import dispatch._
import org.scalatra._

object DispatchAkka {

  def retrievePage()(implicit ctx: ExecutionContext): Future[String] = {
    val prom = AkkaPromise[String]()
    dispatch.Http(url("http://slashdot.org/") OK as.String) onComplete {
      case r => prom.complete(r)
    }
    prom.future
  }
}

class PageRetriever(system: ActorSystem) extends ScalatraServlet with FutureSupport {

  protected implicit def executor: ExecutionContext = system.dispatcher

  get("/") {
    DispatchAkka.retrievePage()
  }

}

