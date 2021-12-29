package com.spirent.drools.config

import com.spirent.drools.config.dto.RedisConfig
import com.spirent.drools.dto.events.TestSessionLifecycleEvent
import redis.clients.jedis.Jedis

/**
 * @author ysavi2
 * @since 28.12.2021
 */
object RedisRepositorySink {

  def apply(redisConfig: RedisConfig): RedisRepositorySink = {
    val initRedisClient = () => {
      val jedis = new Jedis(redisConfig.host, redisConfig.port, redisConfig.timeout)
      if (redisConfig.password.nonEmpty) {
        jedis.auth(redisConfig.password)
      }
      new RedisRepository(jedis, redisConfig.redisEntityPrefixes)
    }

    new RedisRepositorySink(initRedisClient)
  }
}

final class RedisRepositorySink(initRedisRepository: () => RedisRepository) extends Serializable {

  private[this] lazy val redisRepository = initRedisRepository()

  def cacheSession(implicit testSessionLifecycleEvent: TestSessionLifecycleEvent): TestSessionLifecycleEvent = {
    redisRepository.cacheSession
  }

  def updateSessionStartTime(implicit testSessionLifecycleEvent: TestSessionLifecycleEvent): TestSessionLifecycleEvent = {
    redisRepository.updateSessionStartTime
  }

  def updateSessionEndTime(implicit testSessionLifecycleEvent: TestSessionLifecycleEvent): TestSessionLifecycleEvent = {
    redisRepository.updateSessionEndTime
  }
}
