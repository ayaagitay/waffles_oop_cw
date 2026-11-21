package model;

public enum WaffleType {
    CLASSIC(ClassicWaffle.NAME, ClassicWaffle.PRICE),
    BELGIAN(BelgianWaffle.NAME, BelgianWaffle.PRICE),
    BUBBLE(BubbleWaffle.NAME, BubbleWaffle.PRICE);

    private final String label;
    private final long price;

    WaffleType(String label, long price) {
        this.label = label;
        this.price = price;
    }

    public String getLabel() {
        return label;
    }

    public long getPrice() {
        return price;
    }
}
