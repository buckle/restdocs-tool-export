package restdocs.tool.export;

import org.apache.commons.lang3.RandomUtils;
import restdocs.tool.export.application.PostData;

import java.util.UUID;

public class CrossTestData {

  public static final String pathVariable = UUID.randomUUID().toString();
  public static final String parameter = UUID.randomUUID().toString();
  public static final String headerXKey = UUID.randomUUID().toString();
  public static final PostData postData = new PostData();

  static {
    postData.setsField1(UUID.randomUUID().toString());
    postData.setiField2(RandomUtils.nextInt());
  }
}
