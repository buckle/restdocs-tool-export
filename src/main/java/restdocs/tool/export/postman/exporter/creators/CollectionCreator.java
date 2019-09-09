package restdocs.tool.export.postman.exporter.creators;

import restdocs.tool.export.common.creator.Creator;
import restdocs.tool.export.postman.exporter.Collection;

public class CollectionCreator implements Creator<Collection, String> {

  private InfoCreator infoCreator;

  public CollectionCreator() {
    this.infoCreator = new InfoCreator();
  }

  public CollectionCreator(InfoCreator infoCreator) {
    this.infoCreator = infoCreator;
  }

  @Override
  public Collection create(String appName) {
    if(appName != null) {
      Collection collection = new Collection();
      collection.setInfo(infoCreator.create(appName));
      return collection;
    }

    return null;
  }
}
