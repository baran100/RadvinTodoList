package com.radvin_app.radvintodolist.ui.category;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.radvin_app.R;
import com.radvin_app.radvintodolist.adapter.CategoriesAdapter;
import com.radvin_app.radvintodolist.storage.Category;
import com.radvin_app.radvintodolist.storage.TodoDatabase;
import com.radvin_app.radvintodolist.ui.dialog.AddEditCategoryDialog;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CategoriesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoriesFragment extends Fragment implements AddEditCategoryDialog.AddEditNewCategoryCallBack, CategoriesAdapter.CategoryItemEventListener {
  private RecyclerView recyclerView;
  public static CategoriesAdapter adapter;
  private TodoDatabase database;


  // TODO: Rename parameter arguments, choose names that match
  // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";

  // TODO: Rename and change types of parameters
  private String mParam1;
  private String mParam2;

  public CategoriesFragment() {
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @param param1 Parameter 1.
   * @param param2 Parameter 2.
   * @return A new instance of fragment SmsFragment.
   */
  // TODO: Rename and change types and number of parameters
  public static CategoriesFragment newInstance(String param1, String param2) {
    CategoriesFragment fragment = new CategoriesFragment();
    Bundle args = new Bundle();
    args.putString(ARG_PARAM1, param1);
    args.putString(ARG_PARAM2, param2);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mParam1 = getArguments().getString(ARG_PARAM1);
      mParam2 = getArguments().getString(ARG_PARAM2);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_tasks, container, false);



    recyclerView = view.findViewById(R.id.rv_main_tasks);
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false));
    adapter = new CategoriesAdapter(CategoriesFragment.this);
    recyclerView.setAdapter(adapter);
    database = new TodoDatabase(getContext());
    List<Category> categoryList = database.getCategory();
    adapter.addCategories(categoryList);

    TextView tvTitle = view.findViewById(R.id.app_bar_title);
    tvTitle.setText(R.string.category_task_title);
    View addNewTaskFab = view.findViewById(R.id.fab_main_addNewTask);
    addNewTaskFab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        AddEditCategoryDialog dialog= new AddEditCategoryDialog();
        Bundle bundle = new Bundle();
        bundle.putInt("NEW",-1);
        dialog.setArguments(bundle);
        dialog.setTargetFragment(CategoriesFragment.this, 0);
        dialog.show(getFragmentManager(),null);

      }
    });

    return view;
  }

  @Override
  public void onNewCategory(Category category) {
      long categoryId = database.addCategory(category);
      if (categoryId != -1){
        category.setId(categoryId);
        adapter.addCategory(category);
      }else {
        Log.e("CategoryFragment", "onAddCategory: Item not inserted" );
      }
  }

  @Override
  public void onEditCategory(Category category) {
    int result = database.updateCategory(category);
    if (result>0){
      adapter.updateItemCategory(category);
    }
  }

  @Override
  public void onDeleteButtonClick(Category category) {
    int result = database.deleteCategory(category);
    if (result>0){
      adapter.deleteCategory(category);
      Toast.makeText(getContext(), "ایتم حذف شد", Toast.LENGTH_SHORT).show();
    }else {

    }
  }


  @Override
  public void onItemClickPress(Category category) {
      AddEditCategoryDialog dialog = new AddEditCategoryDialog();
      Bundle bundle = new Bundle();
      bundle.putParcelable("CATEGORY",category);
      dialog.setArguments(bundle);
      dialog.setTargetFragment(CategoriesFragment.this, 1);
      dialog.show(getFragmentManager(),null);
  }
}
