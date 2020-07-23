package com.radvin_app.radvintodolist.ui.task;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.radvin_app.R;
import com.radvin_app.radvintodolist.adapter.CategoriesAdapter;
import com.radvin_app.radvintodolist.adapter.TasksAdapter;
import com.radvin_app.radvintodolist.storage.Category;
import com.radvin_app.radvintodolist.storage.Task;
import com.radvin_app.radvintodolist.storage.TodoDatabase;
import com.radvin_app.radvintodolist.ui.category.CategoriesFragment;
import com.radvin_app.radvintodolist.ui.dialog.AddEditCategoryDialog;
import com.radvin_app.radvintodolist.ui.dialog.AddEditTaskDialog;
import com.radvin_app.radvintodolist.ui.dialog.DialogFilter;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TasksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TasksFragment extends Fragment implements TasksAdapter.TaskItemEventListener, AddEditTaskDialog.AddNewTaskCallback
    ,DialogFilter.OnAddFriendListener{

  private RecyclerView recyclerView;
  private TasksAdapter adapter;
  private TodoDatabase database;

  String fromDate,toDate = "";


  // TODO: Rename parameter arguments, choose names that match
  // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";

  // TODO: Rename and change types of parameters
  private String mParam1;
  private String mParam2;

  public TasksFragment() {
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @param param1 Parameter 1.
   * @param param2 Parameter 2.
   * @return A new instance of fragment HomeFragment.
   */
  // TODO: Rename and change types and number of parameters
  public static TasksFragment newInstance(String param1, String param2) {
    TasksFragment fragment = new TasksFragment();
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
  public void onAddFriendSubmit(String fromDate , String toDate) {
    List<Task> taskList = database.getTasksByDate(fromDate,toDate);
    adapter.setTasks(taskList);
    Toast.makeText(getContext(), toDate+" "+fromDate, Toast.LENGTH_SHORT).show();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_tasks, container, false);

      recyclerView = view.findViewById(R.id.rv_main_tasks);
      recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false));
      adapter = new TasksAdapter(TasksFragment.this);
      recyclerView.setAdapter(adapter);
      database = new TodoDatabase(getContext());
      List<Task> taskList = database.getTasks();
      adapter.addItems(taskList);


      View addNewTaskFab = view.findViewById(R.id.fab_main_addNewTask);
      addNewTaskFab.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          AddEditTaskDialog dialog= new AddEditTaskDialog();
          Bundle bundle= new Bundle();
          bundle.putInt("NEW",0);
          dialog.setArguments(bundle);
          dialog.setTargetFragment(TasksFragment.this, 0);
          dialog.show(getFragmentManager(),null);

          }
      });

    EditText searchEt = view.findViewById(R.id.et_main_search);
    searchEt.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 0) {
          List<Task> tasks = database.searchTask(s.toString());
          adapter.setTasks(tasks);
        } else {
          List<Task> tasks = database.getTasks();
          adapter.setTasks(tasks);
        }
      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    });

    View clearTskBtn = view.findViewById(R.id.iv_main_clearTask);
    clearTskBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        new AlertDialog.Builder(v.getContext())
                .setTitle(R.string.delete_all_title)
                .setMessage(R.string.delete_all_mesage)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int which) {
                    database.clearAllTask();
                    adapter.clearItems();
                  }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
      }
    });

    View filterTaskBtn = view.findViewById(R.id.iv_main_filterTask);
    filterTaskBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        DialogFilter dialogFilter = new DialogFilter();
        dialogFilter.setTargetFragment(TasksFragment.this,10);
        dialogFilter.show(getFragmentManager(),null);
      }
    });

    return view;
  }

  @Override
  public void onDeleteButtonClick(Task task) {
    int result = database.deleteTask(task);
    if (result>0){
      adapter.deleteItem(task);
      Toast.makeText(getContext(), "ایتم حذف شد", Toast.LENGTH_SHORT).show();
    }else {

    }
  }

  @Override
  public void onItemClickPress(Task task) {
    AddEditTaskDialog dialog = new AddEditTaskDialog();
    Bundle bundle = new Bundle();
    bundle.putParcelable("TASK",task);
    bundle.putInt("EDIT",1);
    dialog.setArguments(bundle);
    dialog.setTargetFragment(TasksFragment.this,1);
    dialog.show(getFragmentManager(),null);

  }

  @Override
  public void onItemCheckedChange(Task task) {

  }

  @Override
  public void onNewItemTask(Task task) {
    long taskId = database.addTask(task);
    if (taskId != -1) {
      task.setId(taskId);
      adapter.addItem(task);
    } else {
      Log.e("MainActivity", "onNewTask: item did not inserted");
    }
  }

  @Override
  public void onDeleteItemTask(Task task) {
    int result = database.deleteTask(task);
    if (result>0){
      adapter.deleteItem(task);
      Toast.makeText(getContext(), "ایتم حذف شد", Toast.LENGTH_SHORT).show();
    }else {
      Log.e("TasksFragment", "onDeleteItemTask: " );
    }
  }

  @Override
  public void onEditItemTask(Task task) {
    int result = database.updateTask(task);
    if (result>0){
      adapter.updateItem(task);
    }
  }


}
