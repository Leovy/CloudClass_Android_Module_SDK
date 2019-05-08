//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.allin.basefeature.common.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.app.DialogFragment;
import android.view.Window;
import android.view.WindowManager.LayoutParams;

public abstract class BaseDialog extends DialogFragment {
    private static int INVALID_WINDOW_ANIMATION = -1;
    protected Context mContext;

    public BaseDialog() {
    }

    @CallSuper
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @NonNull
    protected Activity requiredActivity() {
        if (this.mContext instanceof Activity) {
            return (Activity)this.mContext;
        } else {
            throw new IllegalStateException("not found Activity");
        }
    }

    @CallSuper
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = this.getArguments();
        if (args != null) {
            this.onRetrieveArgumentsFromBundle(args);
        }

    }

    protected void onRetrieveArgumentsFromBundle(@NonNull Bundle bundle) {
    }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(this.mContext, this.getDialogStyle());
        dialog.requestWindowFeature(1);
        dialog.setContentView(this.getContentView());
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(this.isCanceledOnTouchOutside());
        dialog.setOnKeyListener((dialog1, keyCode, event) -> {
            if (keyCode == 4) {
                if (this.isCanceledOnKeyBack()) {
                    dialog1.dismiss();
                }

                return true;
            } else {
                return false;
            }
        });
        Window window = dialog.getWindow();
        if (window != null) {
            if (this.getWindowAnimations() != INVALID_WINDOW_ANIMATION) {
                window.setWindowAnimations(this.getWindowAnimations());
            }

            this.onResetWindowAttributes(window);
        }

        this.onInitContentView(dialog);
        return dialog;
    }

    @StyleRes
    protected abstract int getDialogStyle();

    @LayoutRes
    protected abstract int getContentView();

    @StyleRes
    protected int getWindowAnimations() {
        return INVALID_WINDOW_ANIMATION;
    }

    protected boolean isCanceledOnTouchOutside() {
        return true;
    }

    protected boolean isCanceledOnKeyBack() {
        return true;
    }

    protected void onResetWindowAttributes(@NonNull Window window) {
        LayoutParams params = window.getAttributes();
        params.gravity = 80;
        params.width = -1;
        params.height = -2;
        window.setAttributes(params);
    }

    protected abstract void onInitContentView(@NonNull Dialog var1);
}
