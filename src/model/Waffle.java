package model;

import java.util.List;
import util.MoneyFormat;

public abstract class Waffle {
    public abstract String getDescription();
    public abstract long getPrice();
    public abstract List<Customization> getCustomizations();

    public String getFormattedPrice() {
        return MoneyFormat.format(getPrice());
    }

    public String toString() {
        return getDescription() + " - " + getFormattedPrice();
    }
}