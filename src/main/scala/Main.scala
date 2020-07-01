import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder
import zio.{Task, ZIO, ZManaged}
import zio._
import zio.clock._
import zio.internal.Platform
import zio.interop.catz._
import zio.interop.catz.implicits._

import scala.concurrent.ExecutionContext

object Main extends App with Http4sDsl[Task] {

  override val platform =
    Platform.default.withReportFailure(cause =>
      if (!cause.interrupted) println(cause.prettyPrint))

  val serverManaged: ZManaged[Any, Throwable, Unit] = server(
    HttpRoutes.of[Task] {
      case GET -> Root / "status" =>
        Ok("Alive")
    })

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] =
    serverManaged.useForever.exitCode

  private def server(routes: HttpRoutes[Task]): ZManaged[Any, Throwable, Unit] =
    withConcurrentEffect { implicit runtime =>
      BlazeServerBuilder[Task](ExecutionContext.global)
        .bindHttp(8080, "0.0.0.0")
        .withHttpApp(routes.orNotFound)
        .withoutBanner
        .resource
        .toManaged
        .unit
    } provideLayer Clock.live

  private def withConcurrentEffect[R <: Clock, E, A](
      f: Runtime[Clock] => ZManaged[R, E, A]): ZManaged[R, E, A] =
    ZIO.runtime[Clock].toManaged_.flatMap(f)

}
