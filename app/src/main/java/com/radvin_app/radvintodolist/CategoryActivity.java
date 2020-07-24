package com.radvin_app.radvintodolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.radvin_app.R;
import com.radvin_app.radvintodolist.adapter.TasksAdapter;
import com.radvin_app.radvintodolist.storage.Task;
import com.radvin_app.radvintodolist.storage.TodoDatabase;
import com.radvin_app.radvintodolist.ui.category.CategoriesFragment;
import com.radvin_app.radvintodolist.ui.dialog.AddEditCategoryDialog;
import com.radvin_app.radvintodolist.ui.dialog.AddEditTaskDialog;

import java.util.List;

public class CategoryActivity extends AppCompatActivity implements TasksAdapter.TaskItemEventListener{

    private RecyclerView recyclerView;
    public static TasksAdapter adapter;
    private TodoDatabase database;
    long idCategory = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        Intent intent =getIntent();
        idCategory = intent.getLongExtra("idCategory",-1);
        database = new TodoDatabase(this);

        Toolbar toolbar = findViewById(R.id.toolbar);

        if (getSupportActionBar() == null) {
            toolbar.setTitle(database.getCategory(idCategory));
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }
        recyclerView = findViewById(R.id.rv_main_tasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));
        adapter = new TasksAdapter(this);
        recyclerView.setAdapter(adapter);
        database = new TodoDatabase(this);
        List<Task> categoryList = database.getTasksById(idCategory);
        adapter.addItems(categoryList);

    }

    @Override
    public void onDeleteButtonClick(Task task) {

    }

    @Override
    public void onItemClickPress(Task task) {
        AddEditTaskDialog dialog = new AddEditTaskDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelable("TASK",task);
        bundle.putInt("EDIT",1);
        dialog.setArguments(bundle);
        //dialog.setTargetFragment(TasksFragment.this,1);
        dialog.show(getSupportFragmentManager(),null);
    }

    @Override
    public void onItemCheckedChange(Task task) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_edit_action){
            AddEditCategoryDialog dialog = new AddEditCategoryDialog();
            Bundle bundle = new Bundle();
            //bundle.putParcelable("CATEGORY",idCategory);
            bundle.putLong("idCategory",idCategory);
            dialog.setArguments(bundle);
            //dialog.setTargetFragment(CategoriesFragment.this, 1);
            dialog.show(getSupportFragmentManager(),null);
        }
        return super.onOptionsItemSelected(item);
    }
}