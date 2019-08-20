package restdocs.tool.export.insomnia.export;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static restdocs.tool.export.insomnia.export.InsomniaConstants.PAIR_ID;

public class PairTest {

  @Test
  public void equalsWhenObjectsSame() {
    Pair pair = new Pair();

    assertTrue(pair.equals(pair));
    assertEquals(pair.hashCode(), pair.hashCode());
  }

  @Test
  public void equalsWhenSameData() {
    PairBuilder builder = PairBuilder.builder();
    Pair pair1 = builder.build();
    Pair pair2 = builder.build();

    assertTrue(pair1.equals(pair2));
    assertEquals(pair1.hashCode(), pair2.hashCode());
  }

  @Test
  public void equalsWhenDifferentId() {
    PairBuilder builder = PairBuilder.builder();
    Pair pair1 = builder.build();
    Pair pair2 = builder.build();
    pair2.setId(InsomniaExportUtils.generateId(PAIR_ID));

    assertFalse(pair1.equals(pair2));
    assertNotEquals(pair1.hashCode(), pair2.hashCode());
  }

  @Test
  public void equalsWhenDifferentName() {
    PairBuilder builder = PairBuilder.builder();
    Pair pair1 = builder.build();
    Pair pair2 = builder.build();
    pair2.setName("Name" + UUID.randomUUID().toString());

    assertFalse(pair1.equals(pair2));
    assertNotEquals(pair1.hashCode(), pair2.hashCode());
  }

  @Test
  public void equalsWhenDifferentValue() {
    PairBuilder builder = PairBuilder.builder();
    Pair pair1 = builder.build();
    Pair pair2 = builder.build();
    pair2.setValue("Value" + UUID.randomUUID().toString());

    assertFalse(pair1.equals(pair2));
    assertNotEquals(pair1.hashCode(), pair2.hashCode());
  }

  @Test
  public void equalsWhenNull() {
    Pair pair = new Pair();

    assertFalse(pair.equals(null));
  }

  @Test
  public void equalsWhenDifferentClass() {
    Pair pair = new Pair();

    assertFalse(pair.equals(String.class));
  }

}
