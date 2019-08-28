package restdocs.tool.export.insomnia.exporter.creators;

import org.junit.jupiter.api.Test;
import restdocs.tool.export.insomnia.exporter.Export;

import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static restdocs.tool.export.insomnia.exporter.InsomniaAssertionUtils.assertTimeEpoch;
import static restdocs.tool.export.insomnia.exporter.InsomniaConstants.*;

public class ExportCreatorTest {

  @Test
  void create() {
    Export export = new ExportCreator().create(null);

    assertNotNull(export);
    assertEquals(EXPORT_TYPE, export.getType());
    assertEquals(EXPORT_FORMAT, export.getExportFormat().intValue());
    assertEquals(EXPORT_SOURCE, export.getExportSource());
    assertTimeEpoch(export.getExportDate().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
  }
}
