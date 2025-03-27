package com.example.unitconverterapp;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText inputValue;
    private Spinner sourceUnit, destinationUnit;
    private Button convertButton;
    private TextView resultText;

    private String[] lengthUnits = {"Inches", "Feet", "Yards", "Miles", "Centimeters", "Kilometers"};
    private String[] weightUnits = {"Pounds", "Ounces", "Tons", "Kilograms", "Grams"};
    private String[] temperatureUnits = {"Celsius", "Fahrenheit", "Kelvin"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        inputValue = findViewById(R.id.inputValue);
        sourceUnit = findViewById(R.id.sourceUnit);
        destinationUnit = findViewById(R.id.destinationUnit);
        convertButton = findViewById(R.id.convertButton);
        resultText = findViewById(R.id.resultText);
        Button resetButton = findViewById(R.id.resetButton);

        // Start with empty destination spinner
        ArrayAdapter<String> emptyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new String[]{});
        destinationUnit.setAdapter(emptyAdapter);

        // All units available for source spinner
        String[] allUnits = {
                "Inches", "Feet", "Yards", "Miles", "Centimeters", "Kilometers",
                "Pounds", "Ounces", "Tons", "Kilograms", "Grams",
                "Celsius", "Fahrenheit", "Kelvin"
        };

        ArrayAdapter<String> sourceAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, allUnits);
        sourceUnit.setAdapter(sourceAdapter);

        sourceUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = sourceUnit.getSelectedItem().toString();
                updateSpinnerOptions(selected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        convertButton.setOnClickListener(v -> {
            String source = (String) sourceUnit.getSelectedItem();
            String destination = (String) destinationUnit.getSelectedItem();
            String input = inputValue.getText().toString();

            if (source == null || destination == null || input.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please enter a value and select both units", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double value = Double.parseDouble(input);
                double result = convertUnits(source, destination, value);
                resultText.setText("Converted Value: " + result);
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Conversion error", Toast.LENGTH_SHORT).show();
            }
        });

        resetButton.setOnClickListener(v -> {
            inputValue.setText("");
            resultText.setText("Converted Value: ");
            sourceUnit.setSelection(0);
            destinationUnit.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new String[]{}));
        });
    }

    private boolean isInArray(String item, String[] array) {
        for (String element : array) {
            if (element.equals(item)) return true;
        }
        return false;
    }

    private void updateSpinnerOptions(String selectedUnit) {
        String[] newOptions;

        if (isInArray(selectedUnit, lengthUnits)) {
            newOptions = lengthUnits;
        } else if (isInArray(selectedUnit, weightUnits)) {
            newOptions = weightUnits;
        } else if (isInArray(selectedUnit, temperatureUnits)) {
            newOptions = temperatureUnits;
        } else {
            newOptions = new String[]{};
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, newOptions);
        destinationUnit.setAdapter(adapter);
    }


    private double convertUnits(String source, String destination, double value) {
        // LENGTH
        if (source.equals("Inches") && destination.equals("Centimeters")) return value * 2.54;
        if (source.equals("Feet") && destination.equals("Centimeters")) return value * 30.48;
        if (source.equals("Yards") && destination.equals("Centimeters")) return value * 91.44;
        if (source.equals("Miles") && destination.equals("Kilometers")) return value * 1.60934;

        // WEIGHT
        if (source.equals("Pounds") && destination.equals("Kilograms")) return value * 0.453592;
        if (source.equals("Ounces") && destination.equals("Grams")) return value * 28.3495;
        if (source.equals("Tons") && destination.equals("Kilograms")) return value * 907.185;

        // TEMPERATURE
        if (source.equals("Celsius") && destination.equals("Fahrenheit")) return (value * 1.8) + 32;
        if (source.equals("Celsius") && destination.equals("Kelvin")) return value + 273.15;
        if (source.equals("Fahrenheit") && destination.equals("Celsius")) return (value - 32) / 1.8;
        if (source.equals("Kelvin") && destination.equals("Celsius")) return value - 273.15;

        // Same unit
        if (source.equals(destination)) return value;

        Toast.makeText(this, "Conversion not supported!", Toast.LENGTH_SHORT).show();
        return 0;
    }
}
