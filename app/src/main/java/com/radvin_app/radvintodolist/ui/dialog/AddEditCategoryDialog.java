package com.radvin_app.radvintodolist.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.radvin_app.R;
import com.radvin_app.radvintodolist.storage.Category;
import com.radvin_app.radvintodolist.storage.TodoDatabase;

public class AddEditCategoryDialog extends DialogFragment {
    private AddEditNewCategoryCallBack callBack;
    private Category category;
    private TextInputEditText etName;
    private TextInputLayout inputName;
    private TextView tvHeader,imgDelete;

    @Override
    public void onAttachFragment(@NonNull Fragment childFragment) {
        super.onAttachFragment(childFragment);
        callBack = (AddEditNewCategoryCallBack) childFragment;

        if (getArguments() !=null){
            category = getArguments().getParcelable("CATEGORY");
            if (category == null){
                dismiss();
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            callBack = (AddEditNewCategoryCallBack) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling Fragment must implement OnAddFriendListener");
        }

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_edit_category, null, false);

        etName = view.findViewById(R.id.et_dialog_name);
        tvHeader = view.findViewById(R.id.dialog_header_title);
        imgDelete = view.findViewById(R.id.dialog_image_delete);
        inputName = view.findViewById(R.id.til_dialog_name);

        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.onDeleteButtonClick(category);
                Toast.makeText(getContext(), "ایتم حذف شد", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        assert getArguments() != null;
        if (getArguments().containsKey("NEW")){
            imgDelete.setVisibility(View.INVISIBLE);
            tvHeader.setText(R.string.dialog_title_add_category);
            View saveBtn = view.findViewById(R.id.btn_dialog_save);
            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (etName.length() > 0) {
                        Category category = new Category();
                        category.setName(etName.getText().toString());
                        callBack.onNewCategory(category);
                        dismiss();
                    } else {
                        inputName.setError("عنوان نباید خالی باشد");
                    }
                }
            });
        }else
        if (getArguments() !=null && getArguments().containsKey("CATEGORY")){
            category = getArguments().getParcelable("CATEGORY");
            etName.setText(category.getName());
            if (getArguments().containsKey("CATEGORY")){
                tvHeader.setText(R.string.dialog_title_edit_category);
                View saveBtn = view.findViewById(R.id.btn_dialog_save);
                saveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (etName.length() > 0) {
                            category.setName(etName.getText().toString());
                            callBack.onEditCategory(category);
                            dismiss();
                        } else {
                            inputName.setError("عنوا ن نباید خالی باشد");
                        }
                    }
                });
            }
        }else {

        }

        builder.setView(view);
        return builder.create();
    }


    public interface AddEditNewCategoryCallBack {
        void onNewCategory(Category category);
        void onEditCategory(Category category);
        void onDeleteButtonClick(Category category);

    }
}
