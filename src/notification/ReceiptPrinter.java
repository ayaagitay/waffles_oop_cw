package notification;

import model.Waffle;
import order.Order;
import order.OrderItem;
import util.MoneyFormat;

public class ReceiptPrinter implements OrderObserver {
  @Override
  public void update(OrderEvent event, Order order, String message) {
    if (event != OrderEvent.PAID) {
      return;
    }
    System.out.println("------------- RECEIPT -------------");
    System.out.println("Order #" + order.getId());
    int number = 1;
    for (OrderItem item : order.getItems()) {
      Waffle waffle = item.getWaffle();
      System.out.println(number + ". " + waffle.getDescription() + " - " + MoneyFormat.format(waffle.getPrice()));
      number++;
    }
    System.out.println("Total: " + MoneyFormat.format(order.getTotal()));
    System.out.println("Paid by: " + order.getPaymentMethod());
    System.out.println("Thank you for your order!");
    System.out.println("-----------------------------------");
  }
}