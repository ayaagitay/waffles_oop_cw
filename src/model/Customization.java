package model;

public enum Customization {
    NUTELLA("Nutella", 8000, null),
    STRAWBERRIES("Fresh strawberries", 7000, null),
    BANANA("Banana slices", 5000, null),
    WHIPPED_CREAM("Whipped cream", 4000, null),
    ICE_CREAM("Vanilla ice cream", 9000, null),
    MAPLE_SYRUP("Maple syrup", 6000, "Sauce"),
    CHOCOLATE_SAUCE("Chocolate sauce", 5000, "Sauce"),
    CARAMEL_SAUCE("Caramel sauce", 5000, "Sauce"),
    HONEY("Honey", 4000, "Sauce");

    private final String label;
    private final long price;
    private final String exclusiveGroup;

    Customization(String label, long price, String exclusiveGroup) {
        this.label = label;
        this.price = price;
        this.exclusiveGroup = exclusiveGroup;
    }

    public String getLabel() {
        return label;
    }

    public long getPrice() {
        return price;
    }

    public String getExclusiveGroup() {
        return exclusiveGroup;
    }

    public boolean isExclusive() {
        return exclusiveGroup != null;
    }
}
