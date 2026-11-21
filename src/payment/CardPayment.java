package payment;

/**
 * Card payment imitation. Only the last 4 digits are stored, never a full
 * card number. The card has a fake balance, so a large order gets declined.
 */
public class CardPayment extends AbstractPayment {

    private static final long BALANCE = 100_000;

    private final String lastFourDigits;
    private final String pin;

    public CardPayment(String lastFourDigits, String pin) {
        super("Card");
        this.lastFourDigits = lastFourDigits == null ? "" : lastFourDigits.trim();
        this.pin = pin == null ? "" : pin.trim();
    }

    @Override
    protected String validate(long amount) {
        if (!isFourDigits(lastFourDigits)) {
            return "Sorry, please enter exactly 4 digits for the card number.";
        }
        if (!isFourDigits(pin)) {
            return "Sorry, the PIN must be 4 digits.";
        }
        if (amount > BALANCE) {
            return "Sorry, your card was declined: not enough funds.";
        }
        return null;
    }

    @Override
    protected String completePayment(long amount) {
        return "Paid " + format(amount) + " by card ending in " + lastFourDigits + ".";
    }

    private boolean isFourDigits(String s) {
        return s.matches("\\d{4}");
    }
}
