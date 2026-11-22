package ui;

import command.AddCustomizationCommand;
import command.CommandHistory;
import decorator.IncompatibleCustomizationException;
import factory.WaffleFactory;
import model.Customization;
import model.Waffle;
import model.WaffleType;
import notification.ConsoleNotifier;
import notification.OrderObserver;
import notification.ReceiptPrinter;
import order.Order;
import order.OrderItem;
import order.OrderManager;
import payment.CardPayment;
import payment.CashPayment;
import payment.ClickPayment;
import payment.PaymentStrategy;
import util.MoneyFormat;

import java.util.List;

public class ConsoleMenu {
  private final OrderManager orderManager = new OrderManager();
  private final InputReader input = new InputReader();
  private final List<OrderObserver> observers = List.of(new ConsoleNotifier(), new ReceiptPrinter());
  private Order currentOrder;

  public void start() {
    System.out.println("===================================");
    System.out.println("  Welcome to the Waffle Universe!");
    System.out.println("===================================");

    boolean running = true;
    while (running) {
      printMenu();
      int choice = input.readInt("Your choice: ", 0, 8);
      switch (choice) {
        case 1:
          newOrder();
          break;
        case 2:
          switchOrder();
          break;
        case 3:
          addWaffle();
          break;
        case 4:
          customize();
          break;
        case 5:
          undo();
          break;
        case 6:
          viewOrder();
          break;
        case 7:
          pay();
          break;
        case 8:
          cancel();
          break;
        default:
          running = !confirmExit();
          break;
      }
    }
    System.out.println("Thank you for visiting the Waffle Universe. Goodbye!");
  }

  // ------------------------------------------------------------ menu

  private void printMenu() {
    System.out.println("=== MAIN MENU ===");
    if (currentOrder == null) {
      System.out.println("No order selected");
    } else {
      System.out.println("Current: Order #" + currentOrder.getId() + " | " + currentOrder.getItems().size() + " waffle(s)" + " | Total: " + MoneyFormat.format(currentOrder.getTotal()));
    }
    System.out.println("1. Start a new order");
    System.out.println("2. Switch to another order");
    System.out.println("3. Add a waffle");
    System.out.println("4. Customize a waffle");
    System.out.println("5. Undo the last customization");
    System.out.println("6. View the current order");
    System.out.println("7. Pay for the current order");
    System.out.println("8. Cancel the current order");
    System.out.println("0. Exit");
  }

  // ------------------------------------------------------------ orders

  private void newOrder() {
    currentOrder = orderManager.createOrder();
    for (OrderObserver observer : observers) {
      currentOrder.addObserver(observer);
    }
    System.out.println("Order #" + currentOrder.getId() + " has been created.");
  }

  private void switchOrder() {
    List<Order> active = orderManager.getActiveOrders();
    if (active.isEmpty()) {
      System.out.println("You have no unpaid orders. Please start a new order.");
      return;
    }
    System.out.println("Your unpaid orders:");
    for (Order order : active) {
      System.out.println("- Order #" + order.getId() + " | " + order.getItems().size() + " waffle(s) | " + MoneyFormat.format(order.getTotal()));
    }
    int id = input.readInt("Enter the order number: ", 1, Integer.MAX_VALUE);
    Order found = orderManager.findActive(id);
    if (found == null) {
      System.out.println("There is no unpaid order with that number.");
      return;
    }
    currentOrder = found;
    System.out.println("Switched to order #" + id + ".");
  }

  // ------------------------------------------------------------ waffles

  private void addWaffle() {
    if (!hasCurrentOrder()) {
      return;
    }
    WaffleType[] types = WaffleType.values();
    System.out.println("Please choose a waffle:");
    for (int i = 0; i < types.length; i++) {
      Waffle sample = WaffleFactory.create(types[i]);
      System.out.println((i + 1) + ". " + sample.getDescription() + " - " + MoneyFormat.format(sample.getPrice()));
    }
    System.out.println("0. Go back");
    int choice = input.readInt("Your choice: ", 0, types.length);
    if (choice == 0) {
      return;
    }
    currentOrder.addItem(new OrderItem(WaffleFactory.create(types[choice - 1])));
  }

  private void customize() {
    if (!hasCurrentOrder()) {
      return;
    }
    OrderItem item = chooseItem();
    if (item == null) {
      return;
    }
    CommandHistory history = currentOrder.getHistory();
    Customization[] options = Customization.values();

    while (true) {
      System.out.println();
      System.out.println("Customizing: " + item.getWaffle().getDescription());
      for (int i = 0; i < options.length; i++) {
        String line = (i + 1) + ". " + options[i].getLabel() + " (+" + MoneyFormat.format(options[i].getPrice()) + ")";
        if (options[i].isExclusive()) {
          line += " - only one " + options[i].getExclusiveGroup().toLowerCase() + " per waffle";
        }
        System.out.println(line);
      }
      System.out.println("0. Done");
      int choice = input.readInt("Your choice: ", 0, options.length);
      if (choice == 0) {
        return;
      }
      try {
        history.run(new AddCustomizationCommand(currentOrder, item, options[choice - 1]));
      } catch (IncompatibleCustomizationException e) {
        System.out.println(e.getMessage());
      }
    }
  }

  private OrderItem chooseItem() {
    List<OrderItem> items = currentOrder.getItems();
    if (items.isEmpty()) {
      System.out.println("Your order is empty. Please add a waffle first.");
      return null;
    }
    if (items.size() == 1) {
      return items.get(0);
    }
    System.out.println("Which waffle would you like to customize?");
    for (int i = 0; i < items.size(); i++) {
      System.out.println((i + 1) + ". " + items.get(i).getWaffle().getDescription());
    }
    System.out.println("0. Go back");
    int choice = input.readInt("Your choice: ", 0, items.size());
    if (choice == 0) {
      return null;
    }
    return items.get(choice - 1);
  }

  private void undo() {
    if (!hasCurrentOrder()) {
      return;
    }
    if (!currentOrder.getHistory().undoLast()) {
      System.out.println("There is nothing to undo in this order.");
    }
  }

  private void viewOrder() {
    if (!hasCurrentOrder()) {
      return;
    }
    System.out.println("=== Order #" + currentOrder.getId() + " ===");
    List<OrderItem> items = currentOrder.getItems();
    if (items.isEmpty()) {
      System.out.println("Your order is empty.");
      return;
    }
    for (int i = 0; i < items.size(); i++) {
      Waffle waffle = items.get(i).getWaffle();
      System.out.println((i + 1) + ". " + waffle.getDescription() + " - " + MoneyFormat.format(waffle.getPrice()));
    }
    System.out.println("Total: " + MoneyFormat.format(currentOrder.getTotal()));
  }

  // ------------------------------------------------------------ payment and cancel

  private void pay() {
    if (!hasCurrentOrder()) {
      return;
    }
    if (currentOrder.getItems().isEmpty()) {
      System.out.println("Your order is empty. Please add at least one waffle before paying.");
      return;
    }
    System.out.println("Amount to pay: " + MoneyFormat.format(currentOrder.getTotal()));
    System.out.println("Please choose a payment method:");
    System.out.println("1. Card");
    System.out.println("2. Cash");
    System.out.println("3. Click");
    System.out.println("0. Go back");
    int choice = input.readInt("Your choice: ", 0, 3);
    if (choice == 0) {
      return;
    }

    PaymentStrategy strategy;
    if (choice == 1) {
      String digits = input.readText("Last 4 digits of your card: ");
      String pin = input.readText("PIN (4 digits): ");
      strategy = new CardPayment(digits, pin);
    } else if (choice == 2) {
      int cash = input.readInt("How much cash are you giving? ", 0, Integer.MAX_VALUE);
      strategy = new CashPayment(cash);
    } else {
      String phone = input.readText("Phone number (+998XXXXXXXXX): ");
      strategy = new ClickPayment(phone);
    }

    if (currentOrder.checkout(strategy)) {
      currentOrder = null;
    }
  }

  private void cancel() {
    if (!hasCurrentOrder()) {
      return;
    }
    if (!input.readYesNo("Are you sure you want to cancel order #" + currentOrder.getId() + "? (y/n): ")) {
      return;
    }
    int id = currentOrder.getId();
    orderManager.cancelOrder(id);
    currentOrder = null;
    System.out.println("Order #" + id + " has been cancelled.");
  }

  private boolean confirmExit() {
    if (orderManager.getActiveOrders().isEmpty()) {
      return true;
    }
    return input.readYesNo("You have unpaid orders that will be lost. Do you really want to exit? (y/n): ");
  }

  // ------------------------------------------------------------ helpers

  private boolean hasCurrentOrder() {
    if (currentOrder == null) {
      System.out.println("Please start a new order or switch to an existing one first.");
      return false;
    }
    return true;
  }
}