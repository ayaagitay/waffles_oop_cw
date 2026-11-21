package order;

import model.Waffle;
import notification.OrderEvent;
import notification.OrderObserver;
import util.MoneyFormat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Order {
  private int id;
  private List<OrderItem> items = new ArrayList<>();
  private List<OrderObserver> observers = new ArrayList<>();
  private OrderStatus status = OrderStatus.NEW;
  private String paymentMethod;

  Order(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }

  public OrderStatus getStatus() {
    return status;
  }

  public boolean isPaid() {
    return status == OrderStatus.PAID;
  }

  public String getPaymentMethod() {
    return paymentMethod;
  }

  public List<OrderItem> getItems() {
    return Collections.unmodifiableList(items);
  }

  public long getTotal() {
    long total = 0;
    for (OrderItem item : items) {
      total += item.getWaffle().getPrice();
    }
    return total;
  }

  public void addItem(OrderItem item) {
    Objects.requireNonNull(item, "item must not be null");
    ensureNotPaid();
    items.add(item);
    status = OrderStatus.CUSTOMIZING;
    notifyObservers(OrderEvent.ITEM_ADDED,
      "Added " + describe(item) + ". Order total: " + MoneyFormat.format(getTotal()) + ".");
  }

  public void replaceWaffle(OrderItem item, Waffle newWaffle, OrderEvent event, String change) {
    ensureNotPaid();
    if (!items.contains(item)) {
      throw new IllegalArgumentException("This item does not belong to order #" + id + ".");
    }
    item.setWaffle(newWaffle);
    notifyObservers(event,
      change + " Your waffle: " + describe(item) + ". Order total: " + MoneyFormat.format(getTotal()) + ".");
  }

  public void addObserver(OrderObserver observer) {
    if (observer != null && !observers.contains(observer)) {
      observers.add(observer);
    }
  }

  public void removeObserver(OrderObserver observer) {
    observers.remove(observer);
  }

  private void notifyObservers(OrderEvent event, String message) {
    for (OrderObserver observer : observers) {
      observer.update(event, this, message);
    }
  }

  private void ensureNotPaid() {
    if (isPaid()) {
      throw new IllegalStateException("Order #" + id + " has already been paid.");
    }
  }

  private static String describe(OrderItem item) {
    Waffle waffle = item.getWaffle();
    return waffle.getDescription() + " (" + MoneyFormat.format(waffle.getPrice()) + ")";
  }
}