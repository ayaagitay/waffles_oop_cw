package decorator;

import java.util.ArrayList;
import java.util.List;
import model.Customization;
import model.Waffle;

/** adds one customization (topping or sauce) to a waffle. */
public class ToppingDecorator extends WaffleDecorator {

    private final Customization customization;

    public ToppingDecorator(Waffle wrapped, Customization customization) {
        super(wrapped);
        if (customization == null) {
            throw new IllegalArgumentException("Customization must not be null.");
        }
        this.customization = customization;
    }

    public Customization getCustomization() {
        return customization;
    }

    @Override
    public String getDescription() {
        return super.getDescription() + ", " + customization.getLabel();
    }

    @Override
    public long getPrice() {
        return super.getPrice() + customization.getPrice();
    }

    @Override
    public List<Customization> getCustomizations() {
        List<Customization> list = new ArrayList<>(super.getCustomizations());
        list.add(customization);
        return List.copyOf(list);
    }
}
