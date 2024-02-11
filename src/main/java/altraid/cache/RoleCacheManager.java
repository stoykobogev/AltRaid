package altraid.cache;

import altraid.Util;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static altraid.Singletons.OBJECT_MAPPER;

public class RoleCacheManager extends CacheManager<Map<Long, Set<String>>> {

  private final File file = new File(Util.getPath() + "/alt-raid-roles.json");

  public RoleCacheManager() {
    super();

    try {
      if (getFile().createNewFile()) {
        cache = new HashMap<>();
        saveCache();
      } else {
        cache = OBJECT_MAPPER.readValue(getFile(), new TypeReference<>() {});
        if (cache == null) {
          cache = new HashMap<>();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      cache = new HashMap<>();
    }
  }

  @Override
  protected File getFile() {
    return file;
  }
}
