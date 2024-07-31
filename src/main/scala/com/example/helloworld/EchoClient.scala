package com.example.helloworld

//#import
import com.typesafe.scalalogging.StrictLogging
import echo.{EchoServiceClient, StreamEchoRequest, StreamEchoResponse}
import org.apache.pekko
import org.apache.pekko.{Done, NotUsed}
import org.apache.pekko.actor.typed.ActorSystem
import org.apache.pekko.actor.typed.scaladsl.Behaviors
import org.apache.pekko.grpc.GrpcClientSettings
import org.apache.pekko.stream.scaladsl.{Sink, Source}

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

//#import

//#client-request-reply
object EchoClient extends App with StrictLogging {

  implicit val system: ActorSystem[Nothing] = ActorSystem[Nothing](Behaviors.empty[Nothing], "EchoService")
  implicit val ec: ExecutionContext = system.executionContext

  val clientSettings = GrpcClientSettings.connectToServiceAt("127.0.0.1", 5000).withTls(false)

  val client = EchoServiceClient(clientSettings)

  def runClient(): Future[Unit] = {
    val request = StreamEchoRequest(message = "Hello, world!", repeat = 5)

    val responses = client.streamEcho(request)

    responses
      .runWith(Sink.foreach { response: StreamEchoResponse =>
        logger.info(s"Received: ${response.message}")
      })
      .map { _ =>
        logger.info("Stream completed")
      }
  }

  val clientRunFuture = runClient()

  clientRunFuture.onComplete {
    case Success(_) =>
      logger.info("Client completed successfully")
      system.terminate()
    case Failure(e) =>
      logger.error(s"Client failed: ${e.getMessage}")
      system.terminate()
  }

  // Handle shutdown
  sys.addShutdownHook {
    logger.info("Shutdown hook triggered. Stopping client...")
    clientRunFuture.foreach(_ => system.terminate())
  }

  // Keep the main thread alive
  Thread.sleep(Long.MaxValue)

}

//#client-request-reply
