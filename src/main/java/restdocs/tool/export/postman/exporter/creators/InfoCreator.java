package restdocs.tool.export.postman.exporter.creators;

import restdocs.tool.export.common.creator.Creator;
import restdocs.tool.export.common.utils.ExportUtils;
import restdocs.tool.export.postman.exporter.Info;
import restdocs.tool.export.postman.exporter.PostmanConstants;

import java.util.UUID;

public class InfoCreator implements Creator<Info, String> {

  @Override
  public Info create(String appName) {
    if(appName != null) {
      Info info = new Info();

      info.setPostmanId(UUID.randomUUID().toString());
      info.setName(ExportUtils.formatName(appName));
      info.setSchema(PostmanConstants.SCHEMA_V2_1_0);

      return info;
    }

    return null;
  }
}
