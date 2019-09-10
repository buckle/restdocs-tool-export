package restdocs.tool.export.postman.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.operation.Operation;
import restdocs.tool.export.common.document.Documentor;
import restdocs.tool.export.postman.exporter.PostmanExporter;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PostmanToolHandlerTest {

  private PostmanExporter postmanExporter;
  private Documentor documentor;

  @BeforeEach
  void setUp() {
    postmanExporter = mock(PostmanExporter.class);
    documentor = mock(Documentor.class);
  }

  @Test
  void initialize() throws Exception {
    File workingDirectory = mock(File.class);
    String appName = "appName";

    PostmanToolHandler postmanToolHandler = new PostmanToolHandler(postmanExporter, documentor);
    postmanToolHandler.initialize(workingDirectory, appName);

    verify(postmanExporter, times(1)).initialize(workingDirectory, appName, postmanToolHandler.getToolName());
    verify(documentor, times(1)).initialize(workingDirectory, appName, postmanToolHandler.getToolName());
  }

  @Test
  void getToolName() {
    assertEquals("POSTMAN", new PostmanToolHandler().getToolName());
  }

  @Test
  void handleOperation() throws Exception {
    Operation operation = mock(Operation.class);
    File exportFile = mock(File.class);
    when(postmanExporter.getExportFile()).thenReturn(exportFile);
    PostmanToolHandler postmanToolHandler = new PostmanToolHandler(postmanExporter, documentor);

    postmanToolHandler.handleOperation(operation);

    verify(postmanExporter, times(1)).processOperation(operation);
    verify(documentor, times(1)).document(exportFile, "json");
  }
}
