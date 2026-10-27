package com.example.studytaskmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements TaskAdapter.Listener {

    private DBHelper db;
    private TaskAdapter adapter;
    private TextView tvEmpty, tvSummary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DBHelper(this);
        tvEmpty = findViewById(R.id.tvEmpty);
        tvSummary = findViewById(R.id.tvSummary);
        Button btnAdd = findViewById(R.id.btnAdd);
        RecyclerView rvTasks = findViewById(R.id.rvTasks);

        adapter = new TaskAdapter(this);
        rvTasks.setLayoutManager(new LinearLayoutManager(this));
        rvTasks.setAdapter(adapter);

        // Open the second screen with an explicit Intent
        btnAdd.setOnClickListener(v -> startActivity(new Intent(this, AddTaskActivity.class)));
    }

    // Refresh the list every time we come back from AddTaskActivity
    @Override
    protected void onResume() {
        super.onResume();
        loadTasks();
    }

    private void loadTasks() {
        List<Task> list = db.getAllTasks();
        adapter.setTasks(list);
        tvEmpty.setVisibility(list.isEmpty() ? View.VISIBLE : View.GONE);
        tvSummary.setText("Pending: " + db.getPendingCount() + "   |   Total: " + list.size());
    }

    @Override
    public void onToggle(Task task, boolean done) {
        db.setDone(task.getId(), done);
        loadTasks();
    }

    @Override
    public void onDelete(Task task) {
        new AlertDialog.Builder(this)
                .setTitle("Delete task")
                .setMessage("Delete \"" + task.getTitle() + "\"?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    db.deleteTask(task.getId());
                    loadTasks();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
