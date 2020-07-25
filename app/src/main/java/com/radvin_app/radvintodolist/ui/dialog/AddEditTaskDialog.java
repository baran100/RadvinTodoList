package com.radvin_app.radvintodolist.ui.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.radvin_app.R;
import com.radvin_app.radvintodolist.ui.category.SelectCategoryActivity;
import com.radvin_app.radvintodolist.storage.Category;
import com.radvin_app.radvintodolist.storage.Task;
import com.radvin_app.radvintodolist.storage.TodoDatabase;
import com.sardari.daterangepicker.customviews.DateRangeCalendarView;
import com.sardari.daterangepicker.dialog.DatePickerDialog;
import com.sardari.daterangepicker.utils.PersianCalendar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;

public class AddEditTaskDialog extends DialogFragment implements View.OnClickListener {

    private AddNewTaskCallback callBack;
    private Task task;
    private TextInputEditText etTitle, etNote;
    private MaterialTextView tvCategory, tvDate, tvTime;
    private TodoDatabase database;
    private TextInputLayout inputName;
    private TextView tvHeader,imgDelete;
    private PersianCalendar persianCalendar;
    private Button btnSave;
    private RadioGroup rgPriority;

    private long categoryId;
    private int priority;


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "fragment result called");
        super.onActivityResult(requestCode, resultCode, data);
        if(data !=null && resultCode == Activity.RESULT_OK ){
            Toast.makeText(getContext(), data.getStringExtra("categoryName"), Toast.LENGTH_SHORT).show();
            tvCategory.setText(data.getStringExtra("categoryName"));
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
        persianCalendar = new PersianCalendar();
        task = getArguments().getParcelable("TASK");

        etTitle = view.findViewById(R.id.et_dialog_name);
        etNote = view.findViewById(R.id.et_dialog_note);
        tvDate = view.findViewById(R.id.et_dialog_date);
        tvTime = view.findViewById(R.id.et_dialog_time);
        tvCategory = view.findViewById(R.id.et_dialog_category);
        tvHeader = view.findViewById(R.id.dialog_header_title);
        imgDelete = view.findViewById(R.id.dialog_image_delete);
        inputName = view.findViewById(R.id.til_dialog_name);
        btnSave = view.findViewById(R.id.btn_dialog_save);
        rgPriority = view.findViewById(R.id.radioGroup_priority);

        tvCategory.setOnClickListener(AddEditTaskDialog.this);
        tvDate.setOnClickListener(AddEditTaskDialog.this);
        tvTime.setOnClickListener(AddEditTaskDialog.this);
        imgDelete.setOnClickListener(AddEditTaskDialog.this);
        btnSave.setOnClickListener(AddEditTaskDialog.this);



        rgPriority.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId==R.id.priority_low){
                    priority=0;
                }else if (checkedId==R.id.priority_normal){
                    priority=1;
                }else if (checkedId==R.id.priority_high){
                    priority=2;
                }
                //Toast.makeText(getContext(), String.valueOf(priority), Toast.LENGTH_SHORT).show();
            }
        });

        if (getArguments() != null && getArguments().containsKey("NEW")) {
            tvCategory.setText(getFirstCategory().getName());
            categoryId = getFirstCategory().getId();
            tvHeader.setText(R.string.dialog_title_add_task);
            imgDelete.setVisibility(View.INVISIBLE);
            String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
            tvTime.setText(currentTime);
            String date = persianCalendar.getPersianYear()+"-"+persianCalendar.getPersianMonth()+"-"+persianCalendar.getPersianDay();
            String date_now = persianCalendar.getPersianShortDate();
            tvDate.setText(date_now);
        }


        if (getArguments() != null && getArguments().containsKey("EDIT")) {
            etTitle.setText(task.getTitle());
            etNote.setText(task.getNote());
            tvHeader.setText(R.string.dialog_title_edit_task);
            tvDate.setText(task.getPersianDate());
            tvCategory.setText(database.getCategory(task.getIdCategory()));
            if (task.getPriority() == 0) {
                rgPriority.check(R.id.priority_low);
            }else if (task.getPriority() == 1){
                rgPriority.check(R.id.priority_normal);
            }else {
                rgPriority.check(R.id.priority_high);
            }
        }

        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.et_dialog_category:selectCategory();
                break;
            case R.id.et_dialog_date:openDatePickerDialog();
                break;
            case R.id.et_dialog_time:openTimePickerDialog();
                break;
            case R.id.dialog_image_delete:
                callBack.onDeleteItemTask(task);
                Toast.makeText(getContext(), "ایتم حذف شد", Toast.LENGTH_SHORT).show();
                dismiss();
                break;
            case R.id.btn_dialog_save:submitForm();
                break;
        }

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

    public void openTimePickerDialog(){
        // TODO Auto-generated method stub
        Calendar mCurrentTime = Calendar.getInstance();
        int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mCurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                tvTime.setText( selectedHour + ":" + selectedMinute);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle(getString(R.string.select_time));
        mTimePicker.show();
    }

    private void submitForm(){

        if (!validateTitle()){
            return;
        }
        if (!validateCategory()){
            return;
        }
        if (!validatePriority()){
            return;
        }
        if (getArguments() != null && getArguments().containsKey("NEW")) {
            Task task = new Task();
            task.setTitle(etTitle.getText().toString());
            task.setCompleted(false);
            task.setPersianDate(tvDate.getText().toString());
            task.setIdCategory(categoryId);
            task.setNote(etNote.getText().toString());
            task.setPriority(priority);
            callBack.onNewItemTask(task);
            dismiss();
        }

        if (getArguments() != null && getArguments().containsKey("EDIT")) {
            //priority = task.getPriority();
            //categoryId = task.getIdCategory();
            task.setTitle(etTitle.getText().toString());
            task.setNote(etNote.getText().toString());
            task.setPersianDate(tvDate.getText().toString());
            task.setIdCategory(categoryId);
            task.setPriority(priority);
            callBack.onEditItemTask(task);
            dismiss();
        }

        Toast.makeText(getContext(), "ذخیره شد", Toast.LENGTH_SHORT).show();

    }

    private boolean validateTitle(){
        if (etTitle.getText().toString().trim().isEmpty()){
            inputName.setError(getString(R.string.cant_be_empty));
            etTitle.requestFocus();
            return false;
        }else {
            inputName.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateCategory(){
        if (tvCategory.getText().toString().equals(R.string.select_category)){
            inputName.setError(getString(R.string.cant_be_empty));
            tvCategory.setBackground(getResources().getDrawable(R.drawable.background_category_select_error));
            return false;
        }else {
            tvCategory.setBackground(getResources().getDrawable(R.drawable.background_category_select));
        }
        return true;
    }

    private boolean validatePriority(){
        if (rgPriority.getCheckedRadioButtonId() == -1){
            Toast.makeText(getContext(), "اولویت را انتخاب کنید", Toast.LENGTH_SHORT).show();
            rgPriority.setBackground(getResources().getDrawable(R.drawable.background_category_select_error));
            return false;
        }else {
            rgPriority.setBackground(getResources().getDrawable(R.drawable.background_category_select));

        }
        return true;
    }

    private Category getFirstCategory() {
        List<Category> categoryList = database.getCategories();
        if (categoryList.size() == 0) {
            return null;
        } else {
            return categoryList.get(0);
        }
    }
}
