package notification;

import order.Order;

public interface OrderObserver {
  void update(OrderEvent event, Order order, String message);
}