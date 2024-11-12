//package com.crop.phototocartooneffect.popeffect.support;
//
//import android.app.Activity;
//import android.app.AlarmManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//
//import com.ineffect.color_splash.VioEffect;
//import com.ineffect.color_splash.activities.NeoEffectHomeActivity;
//
//
//public class MyExceptionHandlerPix implements Thread.UncaughtExceptionHandler {
//    private Activity activity;
//
//    public MyExceptionHandlerPix(Activity a) {
//        activity = a;
//    }
//
//    @Override
//    public void uncaughtException(Thread thread, Throwable ex) {
//        Intent intent = null;
//        if (activity != null) {
//            intent = new Intent(activity, NeoEffectHomeActivity.class);
//        } else if (VioEffect.getContext() != null) {
//            intent = new Intent(VioEffect.getContext(), NeoEffectHomeActivity.class);
//        }
//
//
//        if (intent != null) {
//            intent.putExtra("crash", true);
////        add error into intent
////        intent.putExtra("report", ex.getMessage());
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//                    | Intent.FLAG_ACTIVITY_CLEAR_TASK
//                    | Intent.FLAG_ACTIVITY_NEW_TASK);
//            PendingIntent pendingIntent = PendingIntent.getActivity(VioEffect.getContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
//            AlarmManager mgr = (AlarmManager) VioEffect.getContext().getSystemService(Context.ALARM_SERVICE);
//            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 10, pendingIntent);
//            //activity.finish();
//            System.exit(2);
//        }
//    }
//}