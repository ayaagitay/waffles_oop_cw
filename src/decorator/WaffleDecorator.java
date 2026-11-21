package decorator;

import java.util.List;
import model.Customization;
import model.Waffle;

// Decorator. A decorator is a Waffle that wraps another Waffle.

public abstract class WaffleDecorator extends Waffle {

    private final Waffle wrapped;

    protected WaffleDecorator(Waffle wrapped) {
        if (wrapped == null) {
            throw new IllegalArgumentException("A decorator must wrap a waffle.");
        }
        this.wrapped = wrapped;
    }

    public Waffle getWrapped() {
        return wrapped;
    }

    @Override
    public String getDescription() {
        return wrapped.getDescription();
    }

    @Override
    public long getPrice() {
        return wrapped.getPrice();
    }

    @Override
    public List<Customization> getCustomizations() {
        return wrapped.getCustomizations();
    }
}
