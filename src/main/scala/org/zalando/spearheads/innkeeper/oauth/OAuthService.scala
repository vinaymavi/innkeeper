package org.zalando.spearheads.innkeeper.oauth

import com.google.inject.{Inject, Singleton}
import org.slf4j.LoggerFactory
import org.zalando.spearheads.innkeeper.oauth.OAuthJsonProtocol.authorizedUserFormat
import org.zalando.spearheads.innkeeper.utils.{OAuthServiceClient, EnvConfig, HttpClient}

import scala.util.Try

/**
 * @author dpersa
 */
trait AuthService {
  def authenticate(token: String): Try[AuthenticatedUser]
}

@Singleton
class OAuthService @Inject() (
    config: EnvConfig,
    @OAuthServiceClient() httpClient: HttpClient) extends AuthService {

  val logger = LoggerFactory.getLogger(this.getClass)

  override def authenticate(token: String): Try[AuthenticatedUser] = {
    httpClient.callJson(url(token), None).map(_.convertTo[AuthenticatedUser])
  }

  private lazy val OAUTH_URL = config.getString("oauth.url")

  private def url(token: String) = OAUTH_URL + token
}
