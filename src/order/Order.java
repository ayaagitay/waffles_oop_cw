package order;

import model.Waffle;
import notification.OrderEvent;
import notification.OrderObserver;
import payment.PaymentResult;
import payment.PaymentStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Order {
  private final int id;
  private final List<OrderItem> items = new ArrayList<>();
  private final List<OrderObserver> observers = new ArrayList<>();
// NEW = no waffles yet; CUSTOMIZING = has at least one waffle; PAID = closed.
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
    notifyObservers(OrderEvent.ITEM_ADDED, "Added " + item.getWaffle().getDescription() + ".");
  }

  public void replaceWaffle(OrderItem item, Waffle newWaffle, OrderEvent event, String change) {
    ensureNotPaid();
    if (!items.contains(item)) {
      throw new IllegalArgumentException("This item does not belong to order #" + id + ".");
    }
    item.setWaffle(newWaffle);
    notifyObservers(event, change + ". Your waffle: " + newWaffle.getDescription() + ".");
  }

  public boolean checkout(PaymentStrategy strategy) {
    ensureNotPaid();
    Objects.requireNonNull(strategy, "strategy must not be null");
    if (items.isEmpty()) {
      return false;
    }

    PaymentResult result = strategy.pay(getTotal());
    Objects.requireNonNull(result, "payment result must not be null");

    if (result.isSuccess()) {
      status = OrderStatus.PAID;
      paymentMethod = strategy.getName();
      notifyObservers(OrderEvent.PAID, result.getMessage());
    } else {
      notifyObservers(OrderEvent.PAYMENT_FAILED, result.getMessage());
    }
    return result.isSuccess();
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
}