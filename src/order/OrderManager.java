package order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrderManager {
  private final List<Order> orders = new ArrayList<>();
  private int nextId = 1;

  public Order createOrder() {
    Order order = new Order(nextId++);
    orders.add(order);
    return order;
  }

  public List<Order> getActiveOrders() {
    List<Order> active = new ArrayList<>();
    for (Order order : orders) {
      if (!order.isPaid()) {
        active.add(order);
      }
    }
    return Collections.unmodifiableList(active);
  }

  public Order findActive(int id) {
    for (Order order : orders) {
      if (order.getId() == id && !order.isPaid()) {
        return order;
      }
    }
    return null;
  }

  public boolean cancelOrder(int id) {
    Order order = findActive(id);
    if (order == null) {
      return false;
    }
    orders.remove(order);
    return true;
  }
}