package altraid;

import discord4j.core.object.entity.GuildEmoji;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Constants {

  public static final String SIGN_IN_ID = "sign-in";
  public static final String SIGN_OUT_1 = "sign-out-1";
  public static final String SIGN_OUT_2 = "sign-out-2";
  public static final String ALL_CHARACTERS = "All characters";
  public static final String CLOSE_ID = "close";
  public static final String DELETE_ID = "delete";
  public static final String SIGN_IN_SUBMIT_1 = "sign-in-submit-1";
  public static final String SIGN_IN_SUBMIT_2 = "sign-in-submit-2";
  public static final String SIGN_IN_SUBMIT_3 = "sign-in-submit-3";
  public static final String REMOVE_ROLE_SUBMIT_1 = "remove-role-submit-1";
  public static final String REMOVE_ROLE_SUBMIT_2 = "remove-role-submit-2";
  public static final String ADD_ROLE_SUBMIT_1 = "add-role-submit-1";
  public static final String ADD_ROLE_SUBMIT_2 = "add-role-submit-2";
  public static final String ABSENT_ID = "absent";

  public static final String DRUID = "Druid";
  public static final String HUNTER = "Hunter";
  public static final String MAGE = "Mage";
  public static final String PRIEST = "Priest";
  public static final String ROGUE = "Rogue";
  public static final String SHAMAN = "Shaman";
  public static final String WARLOCK = "Warlock";
  public static final String WARRIOR = "Warrior";

  public static final String TANK_ROLE = "tank";
  public static final String HEALER_ROLE = "healer";
  public static final String DAMAGE_DEALER_ROLE = "damage-dealer";

  public static final Map<String, List<String>> ROLE_CLASS_MAP = Map.of(
      TANK_ROLE, List.of(SHAMAN, WARLOCK, WARRIOR, ROGUE, DRUID),
      HEALER_ROLE, List.of(PRIEST, DRUID, SHAMAN, MAGE),
      DAMAGE_DEALER_ROLE, List.of(DRUID, HUNTER, MAGE, PRIEST, ROGUE, SHAMAN, WARLOCK, WARRIOR)
  );

  public static final int TITLE_SPACES = 32;
  public static final long RIHANA_ID = 1070745849166041129L;
  public static final long DIST_ID = 693485391734374402L;

  public static final List<GuildEmoji> EMOJIS = new ArrayList<>();
}
