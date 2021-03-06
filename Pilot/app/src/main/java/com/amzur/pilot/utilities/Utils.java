package com.amzur.pilot.utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.amzur.pilot.MainActivity;
import com.amzur.pilot.MyApplication;
import com.amzur.pilot.R;
import com.amzur.pilot.interfaces.ConformationListener;

/**
 * Created by MRamesh on 11-05-2016.
 *
 */
public class Utils {
    public static final String ERROR_SOMETHING="Something went wrong";
    public static final String PROJECT_ID = "955958891860";

    public interface ErrorAlertCompleted {
        void OkaySelected();
    }

    public static void showErrorAlert(String title, final String msg, final Activity act, final ErrorAlertCompleted alertCompleted) {
        AlertDialog.Builder builder = new AlertDialog.Builder(act);
        if(title!=null)
            builder.setTitle(title);
        else
            builder.create().requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.setMessage(msg);
        if (msg.equals(UtilsServer.MSG_AUTH_FAILED)) {
            parseJsonFeed(act);
        }
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                if (alertCompleted != null)
                    alertCompleted.OkaySelected();

            }
        });
        builder.setCancelable(false);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.show();
    }

    public static void showSnackBar(Activity act,String msg){
        if (msg.contains(UtilsServer.MSG_AUTH_FAILED)) {
            parseJsonFeed(act);
        }
        final Snackbar bar=Snackbar.make(act.findViewById(android.R.id.content),msg,Snackbar.LENGTH_INDEFINITE);
        bar.setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bar.dismiss();
            }
        });
        bar.show();
    }
    public static void showSnackBarLongTime(Activity act,String msg){
        if(act==null)
            act= MyApplication.getInstance().getCurrentActivity();
        if (msg.contains(UtilsServer.MSG_AUTH_FAILED)) {
            parseJsonFeed(act);
        }
        Snackbar.make(act.findViewById(android.R.id.content),msg,Snackbar.LENGTH_LONG).show();

    }
    public static Snackbar showSnackBarLongTime2(Activity act,String msg){
        if (msg.equals(UtilsServer.MSG_AUTH_FAILED)) {
            parseJsonFeed(act);
        }
        return  Snackbar.make(act.findViewById(android.R.id.content),msg,Snackbar.LENGTH_LONG);

    }


    public static void showConformationDialog(Activity act, String title, String msg, final ConformationListener cl) {
        AlertDialog.Builder builder = new AlertDialog.Builder(act);
        if(title!=null)
            builder.setTitle(title);
        else
            builder.create().requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.setMessage(msg);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (cl != null)
                    cl.conformed();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        builder.setCancelable(false);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.show();
    }

    public static void parseJsonFeed(Activity act) {
        Intent in = new Intent(act, MainActivity.class);
        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
     PreferenceData.putLoginStatus(act, false);
        act.startActivity(in);

    }

    public static SpannableStringBuilder getBlueText(String str, Context c)
    {
        if(str!=null)
        {
            int color =c.getResources().getColor(R.color.bluee);
            SpannableStringBuilder bold=new SpannableStringBuilder(str);
            bold.setSpan(new ForegroundColorSpan(color), 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return bold;
        }
        return new SpannableStringBuilder();
    }
}
