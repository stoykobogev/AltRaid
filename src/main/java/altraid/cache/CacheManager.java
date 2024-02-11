package altraid.cache;

import java.io.File;
import java.util.function.Consumer;
import java.util.function.Function;

import static altraid.Singletons.OBJECT_MAPPER;

public abstract class CacheManager<T> {

  protected T cache;

  public void useCache(Consumer<T> consumer) {
    useCache(cache ->  {
      consumer.accept(cache);
      return null;
    });
  }

  public synchronized <E> E useCache(Function<T, E> function) {
    return function.apply(cache);
  }

  public synchronized void saveCache() {
    try {
      OBJECT_MAPPER.writeValue(getFile(), cache);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  
  protected abstract File getFile();
}
