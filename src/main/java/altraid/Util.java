package altraid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Util {

  private static final Logger LOGGER = LoggerFactory.getLogger(Util.class);

  public static String getPath() {
    return System.getProperty("os.name").contains("Windows")
        ? "."
        : "/home/debian/discord";
  }

  public static void errorHandler(Throwable t) {
    LOGGER.info("error {}", t.getMessage());
  }
}
