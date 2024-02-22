package restdocs.tool.export.common.assertion;

import org.apache.commons.text.WordUtils;
import org.junit.platform.commons.util.StringUtils;

import static org.junit.jupiter.api.Assertions.*;

public class AssertionUtils {

  public static void assertNameReadably(String name, String key) {
    assertFalse(StringUtils.isBlank(name));
    assertNotNull(name);
    assertTrue(name.matches("[a-zA-Z0-9 ]+"));
    assertTrue(name.contains(WordUtils.capitalize(key)));
  }

  public static void assertNameVariable(String name, String key) {
    assertFalse(StringUtils.isBlank(name));
    assertNotNull(name);
    assertTrue(name.matches("[a-zA-Z0-9_]+"));
    assertTrue(name.contains(WordUtils.uncapitalize(key)));
  }
}
