package restdocs.tool.export.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@RestController
public class TestApplication {

  public static void main(String[] args) {
    SpringApplication.run(TestApplication.class, args);
  }

  @PostMapping("/post/test/{pathVariable}")
  public Map<String, Object> testPost(@RequestBody PostData postData, @PathVariable String pathVariable, @RequestParam String param1) {
    Map<String, Object> response = new HashMap<>();
    response.put("postData", postData);
    response.put("pathVariable", pathVariable);
    response.put("requestParam", param1);
    return response;
  }

  @PostMapping("/post/form/{pathVariable}")
  public ResponseEntity greetingSubmit(@ModelAttribute FormData formData, @PathVariable String pathVariable) {
    return ResponseEntity.ok().build();
  }
}
