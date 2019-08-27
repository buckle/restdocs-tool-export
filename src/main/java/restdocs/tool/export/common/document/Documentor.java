package restdocs.tool.export.common.document;

import java.io.File;
import java.io.IOException;

public interface Documentor {

  void initialize(File workingDirectory, String applicationName, String toolName) throws IOException;

  File getDocFile();

  void document(File content, String type) throws IOException;

}
