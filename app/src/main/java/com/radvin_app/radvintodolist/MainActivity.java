package com.radvin_app.radvintodolist;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.idescout.sql.SqlScoutServer;
import com.radvin_app.R;
import com.radvin_app.radvintodolist.ui.task.TasksFragment;
import com.radvin_app.radvintodolist.ui.DonsFragment;
import com.radvin_app.radvintodolist.ui.category.CategoriesFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

  BottomNavigationView bottomNavigation;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    SqlScoutServer.create(this, getPackageName());


    bottomNavigation = findViewById(R.id.bottom_navigation);
    bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
    openFragment(TasksFragment.newInstance("", ""));
  }

  public void openFragment(Fragment fragment) {
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    transaction.replace(R.id.container, fragment);
    transaction.addToBackStack(null);
    transaction.commit();
  }

  BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
      new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
          switch (item.getItemId()) {
            //case R.id.navigation_dashboard:
              //openFragment(DashboardFragment.newInstance("", ""));
              //return true;
            case R.id.navigation_list_task:
              openFragment(TasksFragment.newInstance("", ""));
              return true;
            case R.id.navigation_sms:
              openFragment(CategoriesFragment.newInstance("", ""));
              return true;
            case R.id.navigation_notifications:
              openFragment(DonsFragment.newInstance("", ""));
              return true;
          }
          return false;
        }
      };
}
