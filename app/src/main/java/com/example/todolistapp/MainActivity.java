package com.example.todolistapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
+
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    Button add; // Button to trigger adding a new task
    LinearLayout layout; // Layout where task cards will be added
    int selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute; // Variables to store selected date and time

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Set the layout for the activity

        add = findViewById(R.id.add); // Initialize the add button by finding it by its ID
        layout = findViewById(R.id.container); // Initialize the container layout by finding it by its ID

        // Set click listener for the add button to open the task dialog for adding a new task
        add.setOnClickListener(v -> openTaskDialog(null));
    }

    // Method to open the task dialog (used for both creating and editing tasks)
    private void openTaskDialog(View existingView) {
        // Create a new AlertDialog builder to build the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Inflate the layout for the dialog
        View view = getLayoutInflater().inflate(R.layout.dialog, null);

        // Initialize the input fields in the dialog layout
        EditText name = view.findViewById(R.id.nameEdit); // Task name input
        EditText description = view.findViewById(R.id.descriptionEdit); // Task description input
        TextView dateTime = view.findViewById(R.id.dateTime); // TextView to show the selected date and time

        // If there is no existing view (i.e., creating a new task), clear the fields
        if (existingView == null) {
            name.setText(""); // Clear the name field
            description.setText(""); // Clear the description field
            dateTime.setText("Select Date and Time"); // Set default text for date/time
        } else {
            // If editing an existing task, populate the fields with the current values
            TextView nameView = existingView.findViewById(R.id.name);
            TextView descriptionView = existingView.findViewById(R.id.description);
            TextView dateTimeView = existingView.findViewById(R.id.dateTime);

            name.setText(nameView.getText().toString()); // Set the name field with existing task name
            description.setText(descriptionView.getText().toString()); // Set the description field
            dateTime.setText(dateTimeView.getText().toString()); // Set the date/time field
        }

        // Set the click listener for the date/time TextView to show the date/time picker dialog
        dateTime.setOnClickListener(v -> showDateTimePicker(dateTime));

        // Build the AlertDialog with title and buttons
        builder.setView(view)
                .setTitle(existingView == null ? "Enter your Task" : "Edit your Task") // Set title based on if it's a new or existing task
                .setPositiveButton("SAVE", (dialog, which) -> {
                    // When the save button is clicked, retrieve the input values
                    String taskName = name.getText().toString();
                    String taskDescription = description.getText().toString();
                    String taskDateTime = dateTime.getText().toString();

                    // If it's a new task, add a new card, else update the existing task card
                    if (existingView == null) {
                        addCard(taskName, taskDescription, taskDateTime, null); // Add new task card
                    } else {
                        updateCard(existingView, taskName, taskDescription, taskDateTime); // Update existing task card
                    }
                })
                .setNegativeButton("Cancel", null); // Cancel button with no action

        builder.create().show(); // Show the dialog
    }

    // Method to show the date and time picker dialog
    private void showDateTimePicker(TextView dateTimeView) {
        Calendar calendar = Calendar.getInstance(); // Get the current date and time
        selectedYear = calendar.get(Calendar.YEAR); // Get the current year
        selectedMonth = calendar.get(Calendar.MONTH); // Get the current month
        selectedDay = calendar.get(Calendar.DAY_OF_MONTH); // Get the current day
        selectedHour = calendar.get(Calendar.HOUR_OF_DAY); // Get the current hour
        selectedMinute = calendar.get(Calendar.MINUTE); // Get the current minute

        // Show the date picker dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            selectedYear = year; // Set selected year
            selectedMonth = month; // Set selected month
            selectedDay = dayOfMonth; // Set selected day

            // Show the time picker dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view1, hourOfDay, minute) -> {
                selectedHour = hourOfDay; // Set selected hour
                selectedMinute = minute; // Set selected minute
                // Format the date and time and set it to the TextView
                String dateTimeString = String.format("%d/%d/%d %02d:%02d", selectedDay, selectedMonth + 1, selectedYear, selectedHour, selectedMinute);
                dateTimeView.setText(dateTimeString); // Set the selected date and time to the TextView
            }, selectedHour, selectedMinute, true);
            timePickerDialog.show(); // Show the time picker dialog
        }, selectedYear, selectedMonth, selectedDay);
        datePickerDialog.show(); // Show the date picker dialog
    }

    // Method to add a new task card to the layout
    private void addCard(String name, String description, String dateTime, View existingView) {
        // Inflate the layout for the card (reuse existing card if editing)
        View view = existingView != null ? existingView : getLayoutInflater().inflate(R.layout.card, null);

        // Initialize the elements of the task card
        TextView nameView = view.findViewById(R.id.name); // Task name TextView
        TextView descriptionView = view.findViewById(R.id.description); // Task description TextView
        TextView dateTimeView = view.findViewById(R.id.dateTime); // Task date/time TextView
        Button delete = view.findViewById(R.id.delete); // Delete button

        // Set the text values for the card
        nameView.setText(name); // Set the name
        descriptionView.setText(description); // Set the description
        dateTimeView.setText(dateTime); // Set the date/time

        // Set a click listener for the delete button to remove the card from the layout
        delete.setOnClickListener(v -> layout.removeView(view));

        // Set a click listener for the card to open the task dialog for editing
        view.setOnClickListener(v -> openTaskDialog(view));

        layout.addView(view); // Add the task card to the layout
    }

    // Method to update an existing task card
    private void updateCard(View view, String name, String description, String dateTime) {
        // Initialize the elements of the existing task card
        TextView nameView = view.findViewById(R.id.name);
        TextView descriptionView = view.findViewById(R.id.description);
        TextView dateTimeView = view.findViewById(R.id.dateTime);

        // Update the text values of the existing task card
        nameView.setText(name);
        descriptionView.setText(description);
        dateTimeView.setText(dateTime);
    }
}