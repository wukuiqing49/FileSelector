package com.wkq.file.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import androidx.core.app.ActivityCompat;


import com.wkq.file.R;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class PermissionUtil {
    private static boolean hasAlertWindow = false;

    public static boolean isHasAlertWindow(){
        return hasAlertWindow;
    }
    /**
     * 检查权限的方法
     *
     * @param activity              发起检查的Activity
     * @param permissions           权限组
     * @param requestCode           请求Code
     * @param dialogMsgForRationale 为权限用途作解释的Dialog内容
     * @return 是否有权限，没有权限时会发起请求权限
     */
    public static boolean checkPermissions(final Activity activity, String[] permissions
            , final int requestCode, final int dialogMsgForRationale) {
        return checkPermissions(activity,
                R.string.dialog_imagepicker_permission_nerver_ask_cancel,
                R.string.dialog_imagepicker_permission_confirm,
                permissions, requestCode, dialogMsgForRationale, false);
    }

    /**
     * @param activity              发起检查的Activity
     * @param permissions           权限组
     * @param requestCode           请求Code
     * @param dialogMsgForRationale 为权限用途作解释的Dialog内容
     * @param activityFinish        是否结束activity
     * @return 是否有权限，没有权限时会发起请求权限
     */
    public static boolean checkPermissions(final Activity activity, String[] permissions
            , final int requestCode, final int dialogMsgForRationale, boolean activityFinish) {
        return checkPermissions(activity,
                R.string.dialog_imagepicker_permission_nerver_ask_cancel,
                R.string.dialog_imagepicker_permission_confirm,
                permissions, requestCode, dialogMsgForRationale, activityFinish);
    }

    public static boolean checkPermissions(final Activity activity, int leftButton, int rightButton, String[] permissions
            , final int requestCode, final int dialogMsgForRationale, boolean activityFinish) {
        //Android6.0以下默认有权限
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true;

        final List<String> needList = new ArrayList<>();
        boolean needShowRationale = false;
        int length = permissions.length;

        for (int i = 0; i < length; i++) {
            String permisson = permissions[i];
            if (TextUtils.isEmpty(permisson)) continue;
            if (ActivityCompat.checkSelfPermission(activity, permisson)
                    != PackageManager.PERMISSION_GRANTED) {
                needList.add(permisson);
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permisson))
                    needShowRationale = true;
            }
        }

        if (needList.size() != 0) {
            if (needShowRationale) {
                hasAlertWindow = true;

                PermissionDialogUtil.showTwoButtonDialog(
                        activity,
                        activity.getResources().getString(leftButton),
                        activity.getResources().getString(rightButton),
                        activity.getResources().getString(dialogMsgForRationale),
                        R.color.color_dialog_btn, R.color.color_ffa300, new PermissionDialogUtil.DialogTwoListener() {
                            @Override
                            public void onClickLeft(Dialog dialog) {
                                hasAlertWindow = false;
                                dialog.dismiss();
                                if (activityFinish) {
                                    activity.finish();
                                }
                            }

                            @Override
                            public void onClickRight(Dialog dialog) {
                                hasAlertWindow = false;
                                dialog.dismiss();
                                ActivityCompat.requestPermissions(activity, needList.toArray(new String[needList.size()]), requestCode);
                            }
                        });
                return false;
            }

            ActivityCompat.requestPermissions(activity, needList.toArray(new String[needList.size()]), requestCode);
            return false;
        } else {
            return true;
        }
    }

    /**
     * 判断请求权限的结果
     *
     * @param activity                发起请求的Activity
     * @param permissions             权限组
     * @param grantResults            请求结果
     * @param finishAfterCancelDialog Dialog被取消后是否关闭Activity
     * @param dialogMsgForNerverAsk   “不再提醒”的Dialog提示内容
     * @return 检查结果、是否显示NerverAsk
     */

    public static boolean[] onRequestPermissionsResult(final Activity activity, int requestCode, String[] permissions, int[] grantResults,
                                                       final boolean finishAfterCancelDialog, int dialogMsgForNerverAsk) {
        return onRequestPermissionsResult(activity, requestCode, permissions, grantResults,finishAfterCancelDialog, finishAfterCancelDialog, dialogMsgForNerverAsk);
    }

    public static boolean[] onRequestPermissionsResult(final Activity activity, int requestCode, String[] permissions, int[] grantResults,
                                                       boolean cencelFinish, boolean confirmFinish, int dialogMsgForNerverAsk) {
        boolean result = true;
        boolean isNerverAsk = false;

        int length = grantResults.length;
        for (int i = 0; i < length; i++) {
            String permission = permissions[i];
            int grandResult = grantResults[i];
            if (grandResult == PackageManager.PERMISSION_DENIED) {
                result = false;
                if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permission))
                    isNerverAsk = true;
            }
        }

        if (!result) {
//            Toast.makeText(activity, R.string.toast_imagepicker_permission_denied, Toast.LENGTH_SHORT).show();
            if (isNerverAsk) {
                hasAlertWindow = true;

                PermissionDialogUtil.showTwoButtonDialog(
                        activity,
                        activity.getResources().getString(R.string.dialog_imagepicker_permission_nerver_ask_cancel),
                        activity.getResources().getString(R.string.dialog_imagepicker_permission_nerver_ask_confirm),
                        activity.getResources().getString(dialogMsgForNerverAsk),
                        R.color.color_dialog_btn, R.color.color_ffa300, new PermissionDialogUtil.DialogTwoListener() {
                            @Override
                            public void onClickLeft(Dialog dialog) {
                                hasAlertWindow = false;
                                dialog.dismiss();
                                if (cencelFinish) activity.finish();
                            }

                            @Override
                            public void onClickRight(Dialog dialog) {
                                hasAlertWindow = false;
                                dialog.dismiss();
                                settingPermissionActivity(activity, 1007);
                                if (confirmFinish) activity.finish();
                            }
                        });
            }
        }

        return new boolean[]{result, isNerverAsk};
    }

    public static void settingPermissionActivity(Activity activity, int CODE_REQUEST_CAMERA_PERMISSIONS) {
        //判断是否为小米系统
        if (TextUtils.equals(BrandDeviceUtils.getSystemInfo().getOs(), BrandDeviceUtils.SYS_MIUI)) {
            Intent miuiIntent = new Intent("miui.intent.action.APP_PERM_EDITOR");
            miuiIntent.putExtra("extra_pkgname", activity.getPackageName());
            //检测是否有能接受该Intent的Activity存在
            List<ResolveInfo> resolveInfos = activity.getPackageManager().queryIntentActivities(miuiIntent, PackageManager.MATCH_DEFAULT_ONLY);
            if (resolveInfos.size() > 0) {
                activity.startActivityForResult(miuiIntent, CODE_REQUEST_CAMERA_PERMISSIONS);
                return;
            }
        }
        //如果不是小米系统 则打开Android系统的应用设置页
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        activity.startActivityForResult(intent, CODE_REQUEST_CAMERA_PERMISSIONS);
    }
}
