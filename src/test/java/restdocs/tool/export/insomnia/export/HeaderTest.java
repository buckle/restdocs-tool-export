package restdocs.tool.export.insomnia.export;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static restdocs.tool.export.insomnia.export.InsomniaConstants.PAIR_ID;

public class HeaderTest {

  @Test
  public void equalsWhenObjectsSame() {
    Header header = new Header();

    assertTrue(header.equals(header));
    assertEquals(header.hashCode(), header.hashCode());
  }

  @Test
  public void equalsWhenSameData() {
    HeaderBuilder builder = HeaderBuilder.builder();
    Header header1 = builder.build();
    Header header2 = builder.build();

    assertTrue(header1.equals(header2));
    assertEquals(header1.hashCode(), header2.hashCode());
  }

  @Test
  public void equalsWhenDifferentId() {
    HeaderBuilder builder = HeaderBuilder.builder();
    Header header1 = builder.build();
    Header header2 = builder.build();
    header2.setId(InsomniaExportUtils.generateId(PAIR_ID));

    assertFalse(header1.equals(header2));
    assertNotEquals(header1.hashCode(), header2.hashCode());
  }

  @Test
  public void equalsWhenDifferentName() {
    HeaderBuilder builder = HeaderBuilder.builder();
    Header header1 = builder.build();
    Header header2 = builder.build();
    header2.setName("Name" + UUID.randomUUID().toString());

    assertFalse(header1.equals(header2));
    assertNotEquals(header1.hashCode(), header2.hashCode());
  }

  @Test
  public void equalsWhenDifferentValue() {
    HeaderBuilder builder = HeaderBuilder.builder();
    Header header1 = builder.build();
    Header header2 = builder.build();
    header2.setValue("Value" + UUID.randomUUID().toString());

    assertFalse(header1.equals(header2));
    assertNotEquals(header1.hashCode(), header2.hashCode());
  }

  @Test
  public void equalsWhenNull() {
    Header header = new Header();

    assertFalse(header.equals(null));
  }

  @Test
  public void equalsWhenDifferentClass() {
    Header header = new Header();

    assertFalse(header.equals(String.class));
  }

}
