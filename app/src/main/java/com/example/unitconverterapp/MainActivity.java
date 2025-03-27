package com.example.unitconverterapp;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText inputField;
    private Spinner sourceUnit, destinationUnit;
    private Button convertBtn, resetBtn;
    private TextView outputText;

    // Unit categories
    private final String[] length = {"Inches", "Feet", "Yards", "Miles", "Centimeters", "Kilometers"};
    private final String[] weight = {"Pounds", "Ounces", "Tons", "Kilograms", "Grams"};
    private final String[] temperature = {"Celsius", "Fahrenheit", "Kelvin"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        inputField = findViewById(R.id.inputValue);
        sourceUnit = findViewById(R.id.sourceUnit);
        destinationUnit = findViewById(R.id.destinationUnit);
        convertBtn = findViewById(R.id.convertButton);
        resetBtn = findViewById(R.id.resetButton);
        outputText = findViewById(R.id.resultText);

        // Keep destination spinner empty until a unit is chosen
        destinationUnit.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new String[]{}));

        // Populate source spinner with all available units
        String[] allUnits = {
                "Inches", "Feet", "Yards", "Miles", "Centimeters", "Kilometers",
                "Pounds", "Ounces", "Tons", "Kilograms", "Grams",
                "Celsius", "Fahrenheit", "Kelvin"
        };
        sourceUnit.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, allUnits));

        sourceUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = sourceUnit.getSelectedItem().toString();
                loadMatchingUnits(selected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        convertBtn.setOnClickListener(v -> {
            String from = (String) sourceUnit.getSelectedItem();
            String to = (String) destinationUnit.getSelectedItem();
            String input = inputField.getText().toString();

            if (from == null || to == null || input.isEmpty()) {
                Toast.makeText(this, "Please select both units and enter a value", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double inputNumber = Double.parseDouble(input);
                double result = convertValue(from, to, inputNumber);
                outputText.setText("Result: " + result);
            } catch (Exception e) {
                Toast.makeText(this, "Oops! Something went wrong.", Toast.LENGTH_SHORT).show();
            }
        });

        resetBtn.setOnClickListener(v -> {
            inputField.setText("");
            outputText.setText("Result:");
            sourceUnit.setSelection(0);
            destinationUnit.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new String[]{}));
        });
    }

    private void loadMatchingUnits(String unit) {
        String[] list;
        if (isinArray(unit, length)) list = length;
        else if (isinArray(unit, weight)) list = weight;
        else if (isinArray(unit, temperature)) list = temperature;
        else list = new String[]{};

        destinationUnit.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, list));
    }

    private boolean isinArray(String item, String[] group) {
        for (String s : group) {
            if (s.equals(item)) return true;
        }
        return false;
    }

    private double convertValue(String from, String to, double number) {
        // Length
        if (from.equals("Inches") && to.equals("Centimeters")) return number * 2.54;
        if (from.equals("Feet") && to.equals("Centimeters")) return number * 30.48;
        if (from.equals("Yards") && to.equals("Centimeters")) return number * 91.44;
        if (from.equals("Miles") && to.equals("Kilometers")) return number * 1.60934;

        // Weight
        if (from.equals("Pounds") && to.equals("Kilograms")) return number * 0.453592;
        if (from.equals("Ounces") && to.equals("Grams")) return number * 28.3495;
        if (from.equals("Tons") && to.equals("Kilograms")) return number * 907.185;

        // Temperature
        if (from.equals("Celsius") && to.equals("Fahrenheit")) return (number * 1.8) + 32;
        if (from.equals("Celsius") && to.equals("Kelvin")) return number + 273.15;
        if (from.equals("Fahrenheit") && to.equals("Celsius")) return (number - 32) / 1.8;
        if (from.equals("Kelvin") && to.equals("Celsius")) return number - 273.15;

        // No conversion or same unit
        if (from.equals(to)) return number;

        Toast.makeText(this, "Unsupported conversion", Toast.LENGTH_SHORT).show();
        return 0;
    }
}
