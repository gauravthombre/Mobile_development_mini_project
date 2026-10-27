package com.example.studytaskmanager;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    public interface Listener {
        void onToggle(Task task, boolean done);
        void onDelete(Task task);
    }

    private final List<Task> tasks = new ArrayList<>();
    private final Listener listener;

    public TaskAdapter(Listener listener) {
        this.listener = listener;
    }

    public void setTasks(List<Task> newTasks) {
        tasks.clear();
        tasks.addAll(newTasks);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder h, int position) {
        Task task = tasks.get(position);

        h.tvTitle.setText(task.getTitle());
        h.tvMeta.setText(task.getSubject() + "  •  Due " + task.getDueDate()
                + "  •  " + task.getPriority());

        // Remove old listener before setting state (RecyclerView reuses views)
        h.cbDone.setOnCheckedChangeListener(null);
        h.cbDone.setChecked(task.isDone());
        if (task.isDone()) {
            h.tvTitle.setPaintFlags(h.tvTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            h.tvTitle.setPaintFlags(h.tvTitle.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        }

        h.cbDone.setOnCheckedChangeListener((button, checked) -> listener.onToggle(task, checked));
        h.btnDelete.setOnClickListener(v -> listener.onDelete(task));
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        CheckBox cbDone;
        TextView tvTitle, tvMeta;
        Button btnDelete;

        TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            cbDone = itemView.findViewById(R.id.cbDone);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvMeta = itemView.findViewById(R.id.tvMeta);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
