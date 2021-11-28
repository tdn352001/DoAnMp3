package com.example.doanmp3.Dialog;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.doanmp3.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class BottomDialog extends BottomSheetDialog {
    public BottomDialog(@NonNull Context context) {
        super(context, R.style.BottomSheetDialogStyle);
    }
}
