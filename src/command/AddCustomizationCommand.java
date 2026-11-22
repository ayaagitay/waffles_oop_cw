package command;

import decorator.Customizer;
import decorator.IncompatibleCustomizationException;
import model.Customization;
import model.Waffle;
import notification.OrderEvent;
import order.Order;
import order.OrderItem;

import java.util.Objects;

public class AddCustomizationCommand implements Command {
  private final Order order;
  private final OrderItem item;
  private final Customization customization;
  private Waffle previous;

  public AddCustomizationCommand(Order order, OrderItem item, Customization customization) {
    this.order = Objects.requireNonNull(order, "order must not be null");
    this.item = Objects.requireNonNull(item, "item must not be null");
    this.customization = Objects.requireNonNull(customization, "customization must not be null");
  }

  @Override
  public void execute() throws IncompatibleCustomizationException {
    Waffle before = item.getWaffle();
    Waffle after = Customizer.apply(before, customization);
    previous = before;
    order.replaceWaffle(item, after, OrderEvent.CUSTOMIZED, customization.getLabel() + " added");
  }

  @Override
  public void undo() {
    if (previous == null) {
      return;
    }
    order.replaceWaffle(item, previous, OrderEvent.UNDONE, customization.getLabel() + " removed");
    previous = null;
  }
}