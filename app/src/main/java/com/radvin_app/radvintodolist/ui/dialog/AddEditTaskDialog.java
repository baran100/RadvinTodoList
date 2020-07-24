package com.radvin_app.radvintodolist.ui.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.radvin_app.R;
import com.radvin_app.radvintodolist.SelectCategoryActivity;
import com.radvin_app.radvintodolist.storage.Category;
import com.radvin_app.radvintodolist.storage.Task;
import com.radvin_app.radvintodolist.storage.TodoDatabase;
import com.sardari.daterangepicker.customviews.DateRangeCalendarView;
import com.sardari.daterangepicker.dialog.DatePickerDialog;
import com.sardari.daterangepicker.utils.PersianCalendar;


import java.util.List;

import static android.content.ContentValues.TAG;

public class AddEditTaskDialog extends DialogFragment {

    private AddNewTaskCallback callBack;
    private Task task;
    private TextInputEditText etName, etNote;
    private MaterialTextView tvCategory, tvDate;
    private TodoDatabase database;
    private TextInputLayout inputName;
    private TextView tvHeader,imgDelete;
    private PersianCalendar persianCalendar;
    long categoryId;


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "fragment result called");
        super.onActivityResult(requestCode, resultCode, data);
        if(data !=null && resultCode == Activity.RESULT_OK ){
            Toast.makeText(getContext(), data.getStringExtra("nameAccount"), Toast.LENGTH_SHORT).show();
            tvCategory.setText(data.getStringExtra("nameAccount"));
            categoryId = data.getLongExtra("categoryId",-1);
        }
    }

    @Override
    public void onAttachFragment(@NonNull Fragment childFragment) {
        super.onAttachFragment(childFragment);
        callBack = (AddNewTaskCallback) childFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            callBack = (AddNewTaskCallback) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling Fragment must implement OnAddFriendListener");
        }
    }

    public interface AddNewTaskCallback {
        void onNewItemTask(Task task);
        void onDeleteItemTask(Task task);
        void onEditItemTask(Task task);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_edit_task, null, false);
        database = new TodoDatabase(getContext());

        etName = view.findViewById(R.id.et_dialog_name);
        etNote = view.findViewById(R.id.et_dialog_note);
        tvDate = view.findViewById(R.id.et_dialog_date);
        tvCategory = view.findViewById(R.id.et_dialog_category);
        tvHeader = view.findViewById(R.id.dialog_header_title);
        imgDelete = view.findViewById(R.id.dialog_image_delete);
        inputName = view.findViewById(R.id.til_dialog_name);

        tvCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCategory();
            }
        });

        persianCalendar = new PersianCalendar();
        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePickerDialog();
            }
        });

        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.onDeleteItemTask(task);
                Toast.makeText(getContext(), "ایتم حذف شد", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        if (getArguments() != null && getArguments().containsKey("NEW")) {
            tvCategory.setText(getFirstAccount().getName());
            categoryId = getFirstAccount().getId();
            tvHeader.setText(R.string.dialog_title_add_task);
            imgDelete.setVisibility(View.INVISIBLE);
            String date = persianCalendar.getPersianYear()+"-"+persianCalendar.getPersianMonth()+"-"+persianCalendar.getPersianDay();
            String date_now = persianCalendar.getPersianShortDate();
            tvDate.setText(date_now);
            View saveBtn = view.findViewById(R.id.btn_dialog_save);
            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (etName.length() > 0 ) {
                        Task task = new Task();
                        task.setTitle(etName.getText().toString());
                        task.setCompleted(false);
                        task.setPersianDate(tvDate.getText().toString());
                        task.setIdCategory(categoryId);
                        task.setNote(etNote.getText().toString());
                        callBack.onNewItemTask(task);
                        dismiss();
                    } else {
                        inputName.setError("عنوان نباید خالی باشد");
                    }
                }
            });
        }

        if (getArguments() != null && getArguments().containsKey("EDIT")) {
            task = getArguments().getParcelable("TASK");
            etName.setText(task.getTitle());
            etNote.setText(task.getNote());
            tvHeader.setText(R.string.dialog_title_edit_task);
            tvDate.setText(task.getPersianDate());
            tvCategory.setText(String.valueOf(task.getIdCategory()));
            View saveBtn = view.findViewById(R.id.btn_dialog_save);
            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (etName.length() > 0) {
                        task.setTitle(etName.getText().toString());
                        task.setPersianDate(tvDate.getText().toString());
                        task.setIdCategory(categoryId);
                        callBack.onEditItemTask(task);
                        dismiss();
                    } else {
                        inputName.setError("عنوا ن نباید خالی باشد");
                    }
                }
            });
        }

        builder.setView(view);
        return builder.create();
    }

    public void selectCategory(){
        Intent intent = new Intent(getContext(), SelectCategoryActivity.class);
        startActivityForResult(intent,1);
    }

    public void openDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext());
        datePickerDialog.setSelectionMode(DateRangeCalendarView.SelectionMode.Single);
        //datePickerDialog.setEnableTimePicker(true);
        datePickerDialog.setShowGregorianDate(true);
        datePickerDialog.setTextSizeTitle(10.0f);
        datePickerDialog.setTextSizeWeek(12.0f);
        datePickerDialog.setTextSizeDate(14.0f);
        datePickerDialog.setCanceledOnTouchOutside(true);
        datePickerDialog.setOnSingleDateSelectedListener(new DatePickerDialog.OnSingleDateSelectedListener() {
            @Override
            public void onSingleDateSelected(PersianCalendar date) {
                tvDate.setText(date.getPersianShortDate());
            }
        });

        datePickerDialog.showDialog();
    }



    private Category getFirstAccount() {
        List<Category> accountList = database.getCategories();
        if (accountList.size() == 0) {
            return null;
        } else {
            return accountList.get(0);
        }
    }
}
