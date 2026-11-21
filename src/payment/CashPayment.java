package payment;

public class CashPayment extends AbstractPayment {

    private final long cashGiven;

    public CashPayment(long cashGiven) {
        super("Cash");
        this.cashGiven = cashGiven;
    }

    public long getChange(long amount) {
        return cashGiven - amount;
    }

    @Override
    protected String validate(long amount) {
        if (cashGiven < amount) {
            return "Sorry, that is not enough cash. The total is " + format(amount)
                    + ", but you gave " + format(cashGiven) + ".";
        }
        return null;
    }

    @Override
    protected String completePayment(long amount) {
        return "Paid in cash. Your change is " + format(getChange(amount)) + ".";
    }
}
