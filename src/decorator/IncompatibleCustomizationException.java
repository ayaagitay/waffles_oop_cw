package decorator;

/** thrown when a customization conflicts with one already on the waffle. */

public class IncompatibleCustomizationException extends Exception {

    private static final long serialVersionUID = 1L;

    public IncompatibleCustomizationException(String message) {
        super(message);
    }
}
