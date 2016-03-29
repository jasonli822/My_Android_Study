package com.upchina.financialnews.support;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Define a toast utility class.
 */
public class ToastUtil {

    private static Toast defaultStyleToast;
    private static final int DEFAULT_DURATION = Toast.LENGTH_LONG;
    private static final int DEFAULT_GRAVITY = Gravity.CENTER;

    /**
     * Show toast text by default style.
     * 
     * @param context
     *            The context to use. Usually your {@link android.app.Application} or {@link android.app.Activity} object.
     * @param text
     *            The text to show. Can be formatted text.
     */
    public static void showToast(Context context, String text) {

        if (null == defaultStyleToast) {

            defaultStyleToast = makeToast(context, text, DEFAULT_DURATION, DEFAULT_GRAVITY);

        } else {
            defaultStyleToast.setText(text);
        }
        
        if (null != defaultStyleToast) {
            defaultStyleToast.show();
        }
    }

    /**
     * Show toast text.
     * 
     * @param context
     *            The context to use. Usually your {@link android.app.Application} or {@link android.app.Activity} object.
     * @param text
     *            The text to show. Can be formatted text.
     * @param duration
     *            How long to display the message. Either {@link #LENGTH_SHORT} or {@link #LENGTH_LONG}
     */
    public static void showToast(Context context, String text, int duration) {
        showToast(context, text, duration, DEFAULT_GRAVITY);
    }

    /**
     * Show toast text.
     * 
     * @param context
     *            The context to use. Usually your {@link android.app.Application} or {@link android.app.Activity} object.
     * @param gravity
     *            The alignment of the text. Refer {@link Gravity}
     * @param text
     *            The text to show. Can be formatted text.
     */
    public static void showToast(Context context, int gravity, String text) {
        showToast(context, text, DEFAULT_DURATION, gravity);
    }

    /**
     * Show toast text.
     * 
     * @param context
     *            The context to use. Usually your {@link android.app.Application} or {@link android.app.Activity} object.
     * @param text
     *            The text to show. Can be formatted text.
     * @param duration
     *            How long to display the message. Either {@link #LENGTH_SHORT} or {@link #LENGTH_LONG}
     * @param gravity
     *            The alignment of the text. Refer {@link Gravity}
     */
    public static void showToast(Context context, String text, int duration, int gravity) {
        Toast toast = makeToast(context, text, duration, gravity);
        if (null != toast) {
            toast.show();
        }
    }

    /**
     * Make the toast.
     * 
     * @param context
     *            The context to use. Usually your {@link android.app.Application} or {@link android.app.Activity} object.
     * @param text
     *            The text to show. Can be formatted text.
     * @param duration
     *            How long to display the message. Either {@link #LENGTH_SHORT} or {@link #LENGTH_LONG}
     * @param gravity
     *            The alignment of the text. Refer {@link Gravity}
     * @return
     */
    @SuppressLint("ShowToast")
    private static Toast makeToast(Context context, String text, int duration, int gravity) {

        Toast toast = null;
        if (null != context && !TextUtils.isEmpty(text)) {

            Context appContext = context.getApplicationContext();
            if (null != appContext) {
                toast = Toast.makeText(appContext, text, duration);
                TextView textView = (TextView) toast.getView().findViewById(android.R.id.message);
                if (null != textView) {
                    textView.setGravity(gravity);
                }
            }
        }
        return toast;
    }
}
