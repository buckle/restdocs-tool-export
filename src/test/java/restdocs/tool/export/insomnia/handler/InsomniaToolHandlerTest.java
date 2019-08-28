package restdocs.tool.export.insomnia.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.operation.Operation;
import restdocs.tool.export.common.document.Documentor;
import restdocs.tool.export.insomnia.exporter.InsomniaExporter;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class InsomniaToolHandlerTest {

  private InsomniaToolHandler insomniaToolHandler;
  private InsomniaExporter insomniaExporter;
  private Documentor documentor;

  @BeforeEach
  void setUp() {
    insomniaExporter = mock(InsomniaExporter.class);
    documentor = mock(Documentor.class);
    insomniaToolHandler = new InsomniaToolHandler(insomniaExporter, documentor);
  }

  @Test
  void initialize() throws Exception {
    File workingDirectory = mock(File.class);
    String appName = "appName";

    insomniaToolHandler.initialize(workingDirectory, appName);

    verify(insomniaExporter, times(1)).initialize(workingDirectory, appName, insomniaToolHandler.getToolName());
    verify(documentor, times(1)).initialize(workingDirectory, appName, insomniaToolHandler.getToolName());
  }

  @Test
  void getToolName() {
    assertEquals("INSOMNIA", new InsomniaToolHandler().getToolName());
  }

  @Test
  void handleOperation() throws Exception {
    Operation operation = mock(Operation.class);
    File exportFile = mock(File.class);
    when(insomniaExporter.getExportFile()).thenReturn(exportFile);

    insomniaToolHandler.handleOperation(operation);

    verify(insomniaExporter, times(1)).processOperation(operation);
    verify(documentor, times(1)).document(exportFile, "json");
  }
}
