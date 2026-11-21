package decorator;

import java.util.ArrayList;
import java.util.List;
import model.Customization;
import model.Waffle;
import java.util.LinkedHashMap;
import java.util.Map;

/** adds one customization (topping or sauce) to a waffle. */
public class ToppingDecorator extends WaffleDecorator {

    private final Customization customization;

    ToppingDecorator(Waffle wrapped, Customization customization) {
        super(wrapped);
        if (customization == null) {
            throw new IllegalArgumentException("Customization must not be null.");
        }
        this.customization = customization;
    }

    public Customization getCustomization() {
        return customization;
    }

    /**
     * Shows the plain waffle name, then each customization once.
     * Repeats are grouped, e.g. "Belgian waffle, Fresh strawberries x2, Maple syrup".
     */
    @Override
    public String getDescription() {
        // go down the layers to find the plain waffle at the bottom
        Waffle base = this;
        while (base instanceof WaffleDecorator) {
            base = ((WaffleDecorator) base).getWrapped();
        }

        // count each customization, keeping the order they were first added
        Map<Customization, Integer> counts = new LinkedHashMap<>();
        for (Customization c : getCustomizations()) {
            counts.merge(c, 1, Integer::sum);
        }

        StringBuilder description = new StringBuilder(base.getDescription());
        for (Map.Entry<Customization, Integer> entry : counts.entrySet()) {
            description.append(", ").append(entry.getKey().getLabel());
            if (entry.getValue() > 1) {
                description.append(" x").append(entry.getValue());
            }
        }
        return description.toString();
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
