package model;

import java.util.List;

public abstract class PlainWaffle extends Waffle {

    private final String name;
    private final long basePrice;

    protected PlainWaffle(String name, long basePrice) {
        if (basePrice < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }
        this.name = name;
        this.basePrice = basePrice;
    }

    public String getName() {
        return name;
    }

    public long getBasePrice() {
        return basePrice;
    }

    @Override
    public String getDescription() {
        return name;
    }

    @Override
    public long getPrice() {
        return basePrice;
    }

    @Override
    public List<Customization> getCustomizations() {
        return List.of(); // a plain waffle has no customizations
    }
}
