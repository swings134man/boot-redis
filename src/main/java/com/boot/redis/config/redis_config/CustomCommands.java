package com.boot.redis.config.redis_config;

import io.lettuce.core.dynamic.Commands;
import io.lettuce.core.dynamic.annotation.Command;
import io.lettuce.core.dynamic.annotation.Param;

/**
 * Redis Lock 을 위한 Lettuce Custom Command Interface
 */
public interface CustomCommands extends Commands {

    // FIXME
    @Command("FCALL :funcName :keyCnt :jobsKey :inboxRef :jobsRef :jobIdentity :frwrdMsg ")
    Object fcall_responseJob(@Param("funcName") byte[] functionName, @Param("keyCnt") Integer keysCount,
                             @Param("jobsKey") byte[] jobsKey, @Param("inboxRef") byte[] inboxRef,
                             @Param("jobsRef") byte[] jobsRef, @Param("jobIdentity") byte[] jobIdentity,
                             @Param("frwrdMsg") byte[] frwrdMsg);
}
