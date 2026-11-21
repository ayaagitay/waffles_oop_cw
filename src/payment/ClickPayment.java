package payment;

/** Click payment imitation. Needs an Uzbek phone number: +998 and 9 digits. */
public class ClickPayment extends AbstractPayment {

    private final String phone;

    public ClickPayment(String phone) {
        super("Click");
        // remove spaces, brackets and dashes so "+998 90 123-45-67" is accepted
        this.phone = phone == null ? "" : phone.replaceAll("[\\s()-]", "");
    }

    @Override
    protected String validate(long amount) {
        if (!phone.matches("\\+998\\d{9}")) {
            return "Sorry, please enter the phone number as +998 followed by 9 digits.";
        }
        return null;
    }

    @Override
    protected String completePayment(long amount) {
        return "Paid " + format(amount) + " via Click from " + maskPhone() + ".";
    }

    /** Hides the middle digits: +998901234567 becomes "+998 ** *** 45 67". */
    private String maskPhone() {
        return "+998 ** *** " + phone.substring(9, 11) + " " + phone.substring(11);
    }
}
