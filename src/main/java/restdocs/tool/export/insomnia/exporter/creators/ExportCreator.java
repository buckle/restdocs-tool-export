package restdocs.tool.export.insomnia.exporter.creators;

import restdocs.tool.export.common.creator.Creator;
import restdocs.tool.export.insomnia.exporter.Export;

import java.time.LocalDateTime;

import static restdocs.tool.export.insomnia.exporter.InsomniaConstants.EXPORT_FORMAT;
import static restdocs.tool.export.insomnia.exporter.InsomniaConstants.EXPORT_SOURCE;
import static restdocs.tool.export.insomnia.exporter.InsomniaConstants.EXPORT_TYPE;

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
