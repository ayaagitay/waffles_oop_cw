package payment;

public interface PaymentStrategy {

    String getName();

    PaymentResult pay(long amount);
}
