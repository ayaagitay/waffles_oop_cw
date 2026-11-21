package model;

import java.util.List;

public abstract class Waffle {
    public abstract String getDescription();
    public abstract long getPrice();
    public abstract List<Customization> getCustomizations();

    public String getFormattedPrice() {
        return String.format("%,d UZS", getPrice());
    }

    public String toString() {
        return getDescription() + " - " + getFormattedPrice();
    }
}