package com.radvin_app.radvintodolist.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textview.MaterialTextView;
import com.radvin_app.R;
import com.sardari.daterangepicker.customviews.DateRangeCalendarView;
import com.sardari.daterangepicker.dialog.DatePickerDialog;
import com.sardari.daterangepicker.utils.PersianCalendar;

public class DialogFilter extends DialogFragment {

    private MaterialTextView tvFromDate, tvToDate;
    private OnAddFriendListener callback;

    public interface OnAddFriendListener {
         void onAddFriendSubmit(String fromDate , String toDate );
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            callback = (OnAddFriendListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling Fragment must implement OnAddFriendListener");
        }
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_filter,null,false);

        tvFromDate = view.findViewById(R.id.from_date);
        tvToDate = view.findViewById(R.id.to_date);

        tvFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext());
                datePickerDialog.setSelectionMode(DateRangeCalendarView.SelectionMode.Range);
                datePickerDialog.setShowGregorianDate(true);
                datePickerDialog.setTextSizeTitle(10.0f);
                datePickerDialog.setTextSizeWeek(12.0f);
                datePickerDialog.setTextSizeDate(14.0f);
                datePickerDialog.setCanceledOnTouchOutside(true);
                datePickerDialog.setOnRangeDateSelectedListener(new DatePickerDialog.OnRangeDateSelectedListener() {
                    @Override
                    public void onRangeDateSelected(PersianCalendar startDate, PersianCalendar endDate) {
                        tvFromDate.setText(startDate.getPersianShortDate());
                        tvToDate.setText(endDate.getPersianShortDate());
                    }
                });

                datePickerDialog.showDialog();
            }
        });
        builder.setTitle(R.string.filter)
                .setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String fromDate = tvFromDate.getText().toString();
                        final String toDate = tvToDate.getText().toString();
                        callback.onAddFriendSubmit(fromDate,toDate);
                    }
                })
                .setNegativeButton(R.string.canel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });

        builder.setView(view);
        return builder.create();
    }
}
