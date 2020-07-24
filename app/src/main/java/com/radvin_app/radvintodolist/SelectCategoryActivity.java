package com.radvin_app.radvintodolist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.radvin_app.R;
import com.radvin_app.radvintodolist.storage.Category;
import com.radvin_app.radvintodolist.storage.TodoDatabase;

import java.util.ArrayList;
import java.util.List;

public class SelectCategoryActivity extends AppCompatActivity {

    private TodoDatabase database;
    private ListView listViewCategory;
    private ArrayAdapter<String> adapter;
    List<Category> categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_category);
        listViewCategory = findViewById(R.id.listView);
        database = new TodoDatabase(this);

        listViewCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String categoryName = categoryList.get(position).getName();
                long categoryId = categoryList.get(position).getId();
                Intent intent = new Intent();
                intent.putExtra("nameAccount",categoryName);
                intent.putExtra("categoryId",categoryId);
                setResult(Activity.RESULT_OK,intent);

                finish();
            }
        });

        bindCategory();
    }

    public void bindCategory()
    {
        categoryList = database.getCategories();
        ArrayList<String> DataForList = new ArrayList<>();
        for (int i = 0; i< categoryList.size(); i++)
        {
            Category h = categoryList.get(i);
            DataForList.add(h.getName());
        }
        adapter = new ArrayAdapter<>(this, R.layout.item_category_select, DataForList);
        listViewCategory.setAdapter(adapter);
    }
}