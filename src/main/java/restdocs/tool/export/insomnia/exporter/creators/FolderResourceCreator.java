package restdocs.tool.export.insomnia.exporter.creators;

import restdocs.tool.export.common.creator.Creator;
import restdocs.tool.export.insomnia.exporter.Resource;

import static restdocs.tool.export.common.utils.ExportUtils.formatNameReadably;
import static restdocs.tool.export.insomnia.exporter.InsomniaConstants.FOLDER_ID;
import static restdocs.tool.export.insomnia.exporter.InsomniaConstants.REQUEST_GROUP_TYPE;
import static restdocs.tool.export.insomnia.exporter.utils.InsomniaExportUtils.generateId;
import static restdocs.tool.export.insomnia.exporter.utils.InsomniaExportUtils.getEpochMillis;

public class FolderResourceCreator implements Creator<Resource, String> {

  @Override
  public Resource create(String appName) {
    if(appName != null) {
      Resource folderResource = new Resource();
      folderResource.setId(generateId(FOLDER_ID));
      folderResource.setType(REQUEST_GROUP_TYPE);
      folderResource.setName(formatNameReadably(appName));
      folderResource.setCreated(getEpochMillis());
      folderResource.setModified(getEpochMillis());

      return folderResource;
    }

    return null;
  }
}
