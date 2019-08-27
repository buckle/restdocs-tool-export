package restdocs.tool.export.insomnia.exporter;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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

  private Set<Resource> resources = null;

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
      this.resources = new HashSet<>();
    }
    this.resources.add(resource);
  }

  public Set<Resource> getResources() {
    return resources;
  }
}
