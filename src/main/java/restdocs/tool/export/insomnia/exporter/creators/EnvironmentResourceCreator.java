package restdocs.tool.export.insomnia.exporter.creators;

import restdocs.tool.export.common.creator.Creator;
import restdocs.tool.export.insomnia.exporter.Resource;

import static restdocs.tool.export.common.utils.ExportUtils.formatNameReadably;
import static restdocs.tool.export.insomnia.exporter.InsomniaConstants.ENVIRONMENT_TYPE;
import static restdocs.tool.export.insomnia.exporter.InsomniaConstants.ENV_ID;
import static restdocs.tool.export.insomnia.exporter.utils.InsomniaExportUtils.generateId;
import static restdocs.tool.export.insomnia.exporter.utils.InsomniaExportUtils.getEpochMillis;

public class EnvironmentResourceCreator implements Creator<Resource, String> {
  @Override
  public Resource create(String appName) {
    Resource environmentResource = new Resource();
    environmentResource.setId(generateId(ENV_ID));
    environmentResource.setType(ENVIRONMENT_TYPE);
    environmentResource.setName(formatNameReadably(appName) + " Environment");
    environmentResource.setCreated(getEpochMillis());
    environmentResource.setModified(getEpochMillis());
    return environmentResource;
  }
}
