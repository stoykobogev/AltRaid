package altraid.cache;

import altraid.UserData;
import altraid.Util;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static altraid.Singletons.OBJECT_MAPPER;

public class UserCacheManager extends CacheManager<List<UserData>> {

  private final File file = new File(Util.getPath() + "/alt-raid-cache.json");

  public UserCacheManager() {
    super();

    try {
      if (getFile().createNewFile()) {
        cache = new ArrayList<>();
        saveCache();
      } else {
        cache = OBJECT_MAPPER.readValue(getFile(), new TypeReference<>() {});
        if (cache == null) {
          cache = new ArrayList<>();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      cache = new ArrayList<>();
    }
  }

  @Override
  protected File getFile() {
    return file;
  }
}
