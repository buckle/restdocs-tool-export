package restdocs.tool.export.insomnia.export.creators;

import restdocs.tool.export.common.creator.Creator;
import restdocs.tool.export.insomnia.export.Export;

import java.time.LocalDateTime;

import static restdocs.tool.export.insomnia.export.InsomniaConstants.EXPORT_FORMAT;
import static restdocs.tool.export.insomnia.export.InsomniaConstants.EXPORT_SOURCE;
import static restdocs.tool.export.insomnia.export.InsomniaConstants.EXPORT_TYPE;

public class ExportCreator implements Creator<Export, String> {

  @Override
  public Export create(String appName) {

    Export export = new Export();
    export.setType(EXPORT_TYPE);
    export.setExportFormat(EXPORT_FORMAT);
    export.setExportDate(LocalDateTime.now());
    export.setExportSource(EXPORT_SOURCE);

    return export;
  }
}
