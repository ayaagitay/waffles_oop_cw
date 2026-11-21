package payment;
import util.MoneyFormat;


/**
 * Shared parent for all payment methods.
 * Template Method pattern: pay() fixes the steps, and each child
 * fills in validate() and completePayment() with its own details.
 */
public abstract class AbstractPayment implements PaymentStrategy {

    private final String name;

    protected AbstractPayment(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }


    @Override
    public final PaymentResult pay(long amount) {
        if (amount <= 0) {
            return PaymentResult.failure("Sorry, the amount to pay is not valid.");
        }
        String problem = validate(amount);
        if (problem != null) {
            return PaymentResult.failure(problem);
        }
        return PaymentResult.success(completePayment(amount));
    }

    /** Returns null if the payment can go ahead, otherwise the reason it can't. */
    protected abstract String validate(long amount);

    /** Called only when validate() passed. Returns the success message. */
    protected abstract String completePayment(long amount);

    /** Formats money the same way everywhere, e.g. "51,000 UZS". */
    protected String format(long amount) {
        return MoneyFormat.format(amount);
    }
}
