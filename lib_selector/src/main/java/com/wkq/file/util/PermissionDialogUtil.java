package com.wkq.file.util;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.wkq.file.R;


public class PermissionDialogUtil {

    public static Dialog showTwoButtonDialog(Context context, String leftString, String rightString, String content, int leftTextColor, int rightTextColor, final DialogTwoListener l) {
        final Dialog dialog = new Dialog(context, R.style.PermissionDialogStyleMediaPicker);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_alert_dialog_permission, null);
        ((TextView) (view.findViewById(R.id.content))).setText(content);
        ((TextView) (view.findViewById(R.id.btn_left))).setText(leftString);
        ((TextView) (view.findViewById(R.id.btn_left))).setTextColor(context.getResources().getColor(leftTextColor));
        ((TextView) (view.findViewById(R.id.btn_right))).setText(rightString);
        ((TextView) (view.findViewById(R.id.btn_right))).setTextColor(context.getResources().getColor(rightTextColor));
        ViewGroup.LayoutParams vl = new ViewGroup.LayoutParams(getScreenWidth(context) - dp2px(context, 146), ViewGroup.LayoutParams.WRAP_CONTENT);
        view.findViewById(R.id.btn_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (l == null) {
                    dialog.dismiss();
                } else {
                    l.onClickLeft(dialog);
                }
            }
        });
        view.findViewById(R.id.btn_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (l == null) {
                    dialog.dismiss();
                } else {
                    l.onClickRight(dialog);
                }
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.addContentView(view, vl);
        dialog.show();
        return dialog;
    }


    /**
     * 获得屏幕高度
     *
     * @param context
     * @return
     */
    public  int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     * 获得屏幕宽度
     *
     * @param context
     * @return
     */
    public  static int  getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * dp转px
     *
     * @param context
     * @param dpVal
     * @return
     */
    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }
    public interface DialogTwoListener {
        void onClickLeft(Dialog dialog);

        void onClickRight(Dialog dialog);
    }
}
