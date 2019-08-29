package restdocs.tool.export.utils;

import org.apache.commons.io.FileUtils;

import java.io.File;

public class ITTestUtils {

  public static void cleanSnippetsDir() {
    if(System.getProperty("restdocs.too.export.snippets.cleaned") == null) {
      System.setProperty("restdocs.too.export.snippets.cleaned", "true");

      try {
        FileUtils.deleteDirectory(new File("build/generated-snippets"));
      } catch(Exception e) {}
    }
  }
}
