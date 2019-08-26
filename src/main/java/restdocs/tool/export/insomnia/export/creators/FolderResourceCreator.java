package restdocs.tool.export.insomnia.export.creators;

import restdocs.tool.export.common.creator.Creator;
import restdocs.tool.export.insomnia.export.Resource;

import static restdocs.tool.export.insomnia.export.InsomniaConstants.FOLDER_ID;
import static restdocs.tool.export.insomnia.export.InsomniaConstants.REQUEST_GROUP_TYPE;
import static restdocs.tool.export.insomnia.export.utils.InsomniaExportUtils.formatName;
import static restdocs.tool.export.insomnia.export.utils.InsomniaExportUtils.generateId;
import static restdocs.tool.export.insomnia.export.utils.InsomniaExportUtils.getEpochMillis;

public class FolderResourceCreator implements Creator<Resource, String> {

  @Override
  public Resource create(String appName) {
    if(appName != null) {
      Resource folderResource = new Resource();
      folderResource.setId(generateId(FOLDER_ID));
      folderResource.setType(REQUEST_GROUP_TYPE);
      folderResource.setName(formatName(appName));
      folderResource.setCreated(getEpochMillis());
      folderResource.setModified(getEpochMillis());

      return folderResource;
    }

    return null;
  }
}
