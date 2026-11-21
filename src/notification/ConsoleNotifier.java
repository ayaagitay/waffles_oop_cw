package notification;

import order.Order;
import util.MoneyFormat;

public class ConsoleNotifier implements OrderObserver {
  @Override
  public void update(OrderEvent event, Order order, String message) {
    System.out.println("Order #" + order.getId() + ": " + message + " | Total: " + MoneyFormat.format(order.getTotal()));
  }
}