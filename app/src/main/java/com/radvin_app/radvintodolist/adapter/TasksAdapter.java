package com.radvin_app.radvintodolist.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.radvin_app.R;
import com.radvin_app.radvintodolist.storage.Task;
import com.radvin_app.radvintodolist.storage.TodoDatabase;

import java.util.ArrayList;
import java.util.List;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TaskViewHolder> {
    private List<Task> taskList = new ArrayList<>();
    private TaskItemEventListener eventListener;

    public TasksAdapter(TaskItemEventListener eventListener) {

        this.eventListener = eventListener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TaskViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        holder.bindTask(taskList.get(position));
    }

    public void addItem(Task task) {
        taskList.add(0, task);
        notifyItemInserted(0);
    }

    public void updateItem(Task task) {
        for (int i = 0; i < taskList.size(); i++) {
            if (task.getId() == taskList.get(i).getId()) {
                taskList.set(i, task);
                notifyItemChanged(i);
            }
        }
    }

    public void addItems(List<Task> tasks) {
        this.taskList.addAll(tasks);
        notifyDataSetChanged();
    }

    public void deleteItem(Task task) {
        for (int i = 0; i < taskList.size(); i++) {
            if (taskList.get(i).getId() == task.getId()) {
                taskList.remove(i);
                notifyItemRemoved(i);
                break;
            }
        }
    }

    public void clearItems() {
        this.taskList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public void setTasks(List<Task> tasks) {
        this.taskList = tasks;
        notifyDataSetChanged();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        private CheckBox checkBox;
        private View deleteBtn;
        private MaterialTextView tvCategoryNameItemTask, tvCategoryDateItemTask;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox_task);
            //deleteBtn = itemView.findViewById(R.id.btn_task_delete);
            tvCategoryNameItemTask = itemView.findViewById(R.id.tvCategoryNameItemTask);
            tvCategoryDateItemTask = itemView.findViewById(R.id.tvCategoryDateItemTask);

        }

        public void bindTask(final Task task) {
            checkBox.setOnCheckedChangeListener(null);
            checkBox.setChecked(task.isCompleted());
            checkBox.setText(task.getTitle());
            TodoDatabase database = new TodoDatabase(itemView.getContext());
            String nam = database.getCategory(task.getIdCategory());

            tvCategoryNameItemTask.setText(nam);
            tvCategoryDateItemTask.setText(task.getPersianDate());
            //Toast.makeText(itemView.getContext(), String.valueOf(nam), Toast.LENGTH_SHORT).show();
            //deleteBtn.setOnClickListener(new View.OnClickListener() {
                //@Override
                //public void onClick(View v) {
                    //eventListener.onDeleteButtonClick(task);
                //}
            //});


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    eventListener.onItemClickPress(task);
                }
            });

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    task.setCompleted(isChecked);
                    eventListener.onItemCheckedChange(task);
                }
            });
        }
    }

    public interface TaskItemEventListener {
        void onDeleteButtonClick(Task task);

        void onItemClickPress(Task task);

        void onItemCheckedChange(Task task);
    }


}
