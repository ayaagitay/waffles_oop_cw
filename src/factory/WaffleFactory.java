package factory;

import model.BelgianWaffle;
import model.BubbleWaffle;
import model.ClassicWaffle;
import model.Waffle;
import model.WaffleType;

//Factory. Callers ask for a waffle by type and get back a Waffle.

public final class WaffleFactory {

    private WaffleFactory() {
    }

    public static Waffle create(WaffleType type) {
        if (type == null) {
            throw new IllegalArgumentException("Waffle type must not be null.");
        }
        switch (type) {
            case CLASSIC:
                return new ClassicWaffle();
            case BELGIAN:
                return new BelgianWaffle();
            case BUBBLE:
                return new BubbleWaffle();
            default:
                throw new IllegalArgumentException("Unknown waffle type: " + type);
        }
    }
}
