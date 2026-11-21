package payment;

public interface PaymentStrategy {

    String getName();

    boolean pay(long amount);

    String getResultMessage();
}
