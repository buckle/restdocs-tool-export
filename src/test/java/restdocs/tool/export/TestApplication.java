package restdocs.tool.export;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class TestApplication {

  public static void main(String[] args) {
    SpringApplication.run(TestApplication.class, args);
  }

  @GetMapping(value = "/{sEcho}")
  public Echo testRequest(@PathVariable String sEcho) {
    Echo echo = new Echo();
    echo.setEcho(sEcho);
    return echo;
  }
}
