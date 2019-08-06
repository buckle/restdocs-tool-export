package restdocs.tool.export.insomnia;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Export {

  @JsonProperty("_type")
  private String type;

  @JsonProperty("__export_format")
  private Integer exportFormat;

  @JsonProperty("__export_date")
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
  private LocalDateTime exportDate;

  @JsonProperty("__export_source")
  private String exportSource;

  private List<Resource> resources;

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Integer getExportFormat() {
    return exportFormat;
  }

  public void setExportFormat(Integer exportFormat) {
    this.exportFormat = exportFormat;
  }

  public LocalDateTime getExportDate() {
    return exportDate;
  }

  public void setExportDate(LocalDateTime exportDate) {
    this.exportDate = exportDate;
  }

  public String getExportSource() {
    return exportSource;
  }

  public void setExportSource(String exportSource) {
    this.exportSource = exportSource;
  }

  public void addResource(Resource resource) {
    if(this.resources == null) {
      this.resources = new ArrayList<>();
    }
    this.resources.add(resource);
  }

  public List<Resource> getResources() {
    return resources;
  }

  public void setResources(List<Resource> resources) {
    this.resources = resources;
  }
}
