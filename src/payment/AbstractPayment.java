package payment;

public abstract class AbstractPayment implements PaymentStrategy {

    private final String name;
    private String resultMessage = "No payment has been made yet.";

    protected AbstractPayment(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getResultMessage() {
        return resultMessage;
    }

    protected boolean finish(boolean success, String message) {
        this.resultMessage = message;
        return success;
    }
}
