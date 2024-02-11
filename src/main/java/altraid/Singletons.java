package altraid;

import altraid.cache.RoleCacheManager;
import altraid.cache.UserCacheManager;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import discord4j.core.DiscordClient;

public class Singletons {

  public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
      .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
  public static final RoleCacheManager ROLE_CACHE_MANAGER = new RoleCacheManager();
  public static final UserCacheManager USER_CACHE_MANAGER = new UserCacheManager();

  public static DiscordClient CLIENT;
}
