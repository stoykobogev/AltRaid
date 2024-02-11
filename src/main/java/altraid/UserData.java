package altraid;

import java.util.HashSet;
import java.util.Set;

public class UserData {

  public String characterName;
  public String guildName;
  public long memberId;
  public String role;
  public String characterClass;
  public long mainMessageId;
  public long channelId;
  public Set<Long> messageIds = new HashSet<>();
  public boolean absent;
}
