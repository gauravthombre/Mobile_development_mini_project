package com.example.studytaskmanager;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Locale;

public class AddTaskActivity extends AppCompatActivity {

    private EditText etTitle, etSubject;
    private Button btnDate;
    private RadioGroup rgPriority;
    private String selectedDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        etTitle = findViewById(R.id.etTitle);
        etSubject = findViewById(R.id.etSubject);
        btnDate = findViewById(R.id.btnDate);
        rgPriority = findViewById(R.id.rgPriority);
        Button btnSave = findViewById(R.id.btnSave);

        btnDate.setOnClickListener(v -> showDatePicker());
        btnSave.setOnClickListener(v -> saveTask());
    }

    private void showDatePicker() {
        Calendar cal = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, day) -> {
            selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", day, month + 1, year);
            btnDate.setText(selectedDate);
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void saveTask() {
        String title = etTitle.getText().toString().trim();
        String subject = etSubject.getText().toString().trim();

        if (title.isEmpty()) {
            etTitle.setError("Title is required");
            return;
        }
        if (selectedDate.isEmpty()) {
            Toast.makeText(this, "Please choose a due date", Toast.LENGTH_SHORT).show();
            return;
        }
        if (subject.isEmpty()) {
            subject = "General";
        }

        String priority = "Medium";
        int checkedId = rgPriority.getCheckedRadioButtonId();
        if (checkedId == R.id.rbLow) priority = "Low";
        else if (checkedId == R.id.rbHigh) priority = "High";

        new DBHelper(this).addTask(title, subject, selectedDate, priority);
        Toast.makeText(this, "Task saved", Toast.LENGTH_SHORT).show();
        finish();   // go back to the list
    }
}
