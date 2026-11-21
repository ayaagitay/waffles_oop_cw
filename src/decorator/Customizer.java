package decorator;

import model.Customization;
import model.Waffle;

/**
 * Adds customizations to a waffle.
 * Checks the rules first (only one sauce), then wraps the waffle.
 */
public final class Customizer {

    private Customizer() {
    }

    /**
     * Adds a customization and returns the new waffle.
     * The old waffle stays the same, so undo can just go back to it.
     * Throws IncompatibleCustomizationException if a sauce is already added.
     */
    public static Waffle apply(Waffle waffle, Customization c)
            throws IncompatibleCustomizationException {
        if (waffle == null || c == null) {
            throw new IllegalArgumentException("Waffle and customization must not be null.");
        }
        if (c.isExclusive()) {
            for (Customization existing : waffle.getCustomizations()) {
                if (c.getExclusiveGroup().equals(existing.getExclusiveGroup())) {
                    throw new IncompatibleCustomizationException(String.format(
                            "Sorry, you can only choose one %s. %s is already added.",
                            c.getExclusiveGroup().toLowerCase(), existing.getLabel()));
                }
            }
        }
        return new ToppingDecorator(waffle, c);
    }
}
