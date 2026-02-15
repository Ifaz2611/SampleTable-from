package cse213.tableformatsec1;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PackageData {
    private final StringProperty packageName;
    private final StringProperty dataAmount;
    private final StringProperty validity;
    private final StringProperty price;
    private final StringProperty availability;
    private final StringProperty offerEnds;

    public PackageData(String packageName, String dataAmount, String validity,
                       String price, String availability, LocalDate offerEnds) {
        this.packageName = new SimpleStringProperty(packageName);
        this.dataAmount = new SimpleStringProperty(dataAmount);
        this.validity = new SimpleStringProperty(validity);
        this.price = new SimpleStringProperty(price);
        this.availability = new SimpleStringProperty(availability);

        // Handle null date safely
        if (offerEnds != null) {
            this.offerEnds = new SimpleStringProperty(offerEnds.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        } else {
            this.offerEnds = new SimpleStringProperty("");
        }
    }

    // Property getters for TableView binding (CRITICAL - must return StringProperty)
    public StringProperty packageNameProperty() {
        return packageName;
    }

    public StringProperty dataAmountProperty() {
        return dataAmount;
    }

    public StringProperty validityProperty() {
        return validity;
    }

    public StringProperty priceProperty() {
        return price;
    }

    public StringProperty availabilityProperty() {
        return availability;
    }

    public StringProperty offerEndsProperty() {
        return offerEnds;
    }

    // Standard getters (optional but good practice)
    public String getPackageName() {
        return packageName.get();
    }

    public String getDataAmount() {
        return dataAmount.get();
    }

    public String getValidity() {
        return validity.get();
    }

    public String getPrice() {
        return price.get();
    }

    public String getAvailability() {
        return availability.get();
    }

    public String getOfferEnds() {
        return offerEnds.get();
    }
}