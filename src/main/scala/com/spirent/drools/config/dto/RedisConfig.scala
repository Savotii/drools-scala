package com.spirent.drools.config.dto

case class RedisConfig(host: String,
                       port: Int,
                       timeout: Int,
                       password: String,
                       redisEntityPrefixes: RedisEntityPrefixes)