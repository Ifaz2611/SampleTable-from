package cse213.tableformatsec1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

public class TableFor1 {
    @FXML private TextField PriceTextField;
    @FXML private TextField PakageNameTextField;
    @FXML private TextField DataAmountTextField;
    @FXML private ComboBox<String> AvailabilityComboBox;
    @FXML private ComboBox<String> ValidatyComboBox;
    @FXML private DatePicker OfferEndsDP;
    @FXML private TableView<PackageData> TableViewC;
    @FXML private Label ShowMassageLebal;
    @FXML private AnchorPane DataAmountLabel;

    // Table columns (need to be defined in FXML or here)
    @FXML private TableColumn<PackageData, String> packageNameColumn;
    @FXML private TableColumn<PackageData, String> dataAmountColumn;
    @FXML private TableColumn<PackageData, String> validityColumn;
    @FXML private TableColumn<PackageData, String> priceColumn;
    @FXML private TableColumn<PackageData, String> availabilityColumn;
    @FXML private TableColumn<PackageData, String> offerEndsColumn;

    private ObservableList<PackageData> packageList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Initialize ComboBox options
        ValidatyComboBox.setItems(FXCollections.observableArrayList(
                "3 Days", "7 Days", "15 Days", "30 Days", "Unlimited"
        ));

        AvailabilityComboBox.setItems(FXCollections.observableArrayList(
                "App only", "Recharge only", "App and recharge"
        ));

        // Configure DatePicker format (optional but user-friendly)
        OfferEndsDP.setConverter(new StringConverter<LocalDate>() {
            private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            @Override
            public String toString(LocalDate date) {
                return date != null ? dateTimeFormatter.format(date) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                try {
                    return string != null && !string.isEmpty() ? LocalDate.parse(string, dateTimeFormatter) : null;
                } catch (DateTimeParseException e) {
                    return null;
                }
            }
        });

        // Setup TableView columns - IMPORTANT: Add these fx:id to your FXML columns
        packageNameColumn.setCellValueFactory(cellData -> cellData.getValue().packageNameProperty());
        dataAmountColumn.setCellValueFactory(cellData -> cellData.getValue().dataAmountProperty());
        validityColumn.setCellValueFactory(cellData -> cellData.getValue().validityProperty());
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty());
        availabilityColumn.setCellValueFactory(cellData -> cellData.getValue().availabilityProperty());
        offerEndsColumn.setCellValueFactory(cellData -> cellData.getValue().offerEndsProperty());

        // Bind the observable list to the TableView
        TableViewC.setItems(packageList);

        // Set message label style for better visibility
        ShowMassageLebal.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
    }

    @FXML
    public void ResetButtonOnAction(ActionEvent actionEvent) {
        PakageNameTextField.clear();
        DataAmountTextField.clear();
        ValidatyComboBox.getSelectionModel().clearSelection();
        PriceTextField.clear();
        AvailabilityComboBox.getSelectionModel().clearSelection();
        OfferEndsDP.setValue(null);
        ShowMassageLebal.setText("");

        // Set focus back to first field
        PakageNameTextField.requestFocus();
    }

    @FXML
    public void CreatePakageButtonOnAction(ActionEvent actionEvent) {
        // Clear previous messages
        ShowMassageLebal.setText("Add Succesfully ");

        // Validation flags
        boolean isValid = true;
        StringBuilder errorMessage = new StringBuilder("Please fix the following errors:\n");

        // 1. Package Name validation
        String packageName = PakageNameTextField.getText().trim();
        if (packageName.isEmpty()) {
            errorMessage.append("\n• Package name cannot be empty");
            isValid = false;
        } else if (packageName.length() < 3) {
            errorMessage.append("\n• Package name must be at least 3 characters");
            isValid = false;
        }

        // 2. Data Amount validation (must be numeric and positive)
        String dataAmount = DataAmountTextField.getText().trim();
        if (dataAmount.isEmpty()) {
            errorMessage.append("\n• Data amount cannot be empty");
            isValid = false;
        } else if (!Pattern.matches("^\\d+(\\.\\d+)?$", dataAmount)) {
            errorMessage.append("\n• Data amount must be a valid number");
            isValid = false;
        } else if (Double.parseDouble(dataAmount) <= 0) {
            errorMessage.append("\n• Data amount must be positive");
            isValid = false;
        } else {
            // Format data amount nicely (e.g., "1.5 GB")
            dataAmount = dataAmount + " GB";
        }

        // 3. Validity validation
        String validity = ValidatyComboBox.getValue();
        if (validity == null || validity.isEmpty()) {
            errorMessage.append("\n• Please select a validity period");
            isValid = false;
        }

        // 4. Price validation
        String price = PriceTextField.getText().trim();
        if (price.isEmpty()) {
            errorMessage.append("\n• Price cannot be empty");
            isValid = false;
        } else if (!Pattern.matches("^\\d+(\\.\\d{1,2})?$", price)) {
            errorMessage.append("\n• Price must be a valid number (max 2 decimal places)");
            isValid = false;
        } else if (Double.parseDouble(price) <= 0) {
            errorMessage.append("\n• Price must be positive");
            isValid = false;
        } else {
            // Format price nicely
            price = "$" + price;
        }

        // 5. Availability validation
        String availability = AvailabilityComboBox.getValue();
        if (availability == null || availability.isEmpty()) {
            errorMessage.append("\n• Please select availability status");
            isValid = false;
        }

        // 6. Offer Ends validation (must be future date)
        LocalDate offerEnds = OfferEndsDP.getValue();
        if (offerEnds == null) {
            errorMessage.append("\n• Please select an offer end date");
            isValid = false;
        } else if (offerEnds.isBefore(LocalDate.now())) {
            errorMessage.append("\n• Offer end date cannot be in the past");
            isValid = false;
        }

        // If validation failed, show errors
        if (!isValid) {
            ShowMassageLebal.setText(errorMessage.toString());
            ShowMassageLebal.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            return;
        }

        // Create new package and add to table
        PackageData newPackage = new PackageData(
                packageName,
                dataAmount,
                validity,
                price,
                availability,
                offerEnds
        );

        packageList.add(newPackage);

        // Show success message
        ShowMassageLebal.setText("✓ Package created successfully!");
        ShowMassageLebal.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");

        // Reset form for next entry
        ResetButtonOnAction(null);
    }
}