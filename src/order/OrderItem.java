package order;

import model.Waffle;
import java.util.Objects;

public class OrderItem {
  private Waffle waffle;

  public OrderItem(Waffle waffle) {
    setWaffle(waffle);
  }

  public Waffle getWaffle() {
    return waffle;
  }

  public final void setWaffle(Waffle waffle) {
    this.waffle = Objects.requireNonNull(waffle, "waffle must not be null");
  }
}
