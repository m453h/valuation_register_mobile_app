package com.aru.valuationregister.Rest;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;


/**
 * Created by Michael Hudson on 4/16/2017.
 */

public class Action {
    private static Action mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;
    private static SharedPreferences prefs;
    private static String PARENT_URL;


    private Action(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized void initialize(SharedPreferences prefs) {
        Action.setPrefs(prefs);
        Action.setParentUrl();
    }


    public static synchronized Action getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new Action(context);
        }
        return mInstance;
    }

    public static void setPrefs(SharedPreferences prefs) {
        Action.prefs = prefs;
    }

    public static void setParentUrl() {

        String apiURL = prefs.getString("apiURL", null);

        if (apiURL == null || 1 == 1) {
            String defaultURL = "http://192.168.43.71:8000/api/client";
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("apiURL", defaultURL);
            editor.apply();
            apiURL = defaultURL;
        }

        Action.PARENT_URL = apiURL;
    }

    public static String getParentUrl() {
        return Action.PARENT_URL;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public static String getRequestURL(String url) {
        return PARENT_URL + url;
    }

    public static synchronized String parseErrorResponse(VolleyError ex) {

        String message;

        if (ex instanceof TimeoutError || ex instanceof NoConnectionError) {
            message = "Connection could not be established";
        } else if (ex instanceof AuthFailureError) {
            message = "HTTP Authentication Could not be done";
        } else if (ex instanceof ServerError) {
            message = "Server error";
        } else if (ex instanceof NetworkError) {
            message = "Network error";
        } else if (ex instanceof ParseError) {
            message = "Parse error";
        } else {
            message = "Unknown error";
        }
        return message;
    }

    public static synchronized String getValue(JSONObject object, String key) {
        try {
            return object.get(key).toString();
        } catch (JSONException e) {
            // e.printStackTrace();
        }

        return null;
    }

    public static synchronized String getFieldValue(JSONObject response, String field) {
        String responseCode = "";
        try {

            responseCode = response.getString(field);
        } catch (JSONException ex) {

        }
        return responseCode;

    }

    public static String padRight(String s, int n) {
        return String.format("%1$-" + n + "s", s);
    }

    public static String padLeft(String s, int n) {
        return String.format("%1$" + n + "s", s);
    }

    public static boolean arrayContains(String[] arr, String targetValue) {
        return Arrays.asList(arr).contains(targetValue);
    }


    public static boolean strCompare(String strOne, String strTwo) {
        try {
            if (strOne.equals(strTwo)) {
                return true;
            }
        } catch (NullPointerException e) {

        }

        return false;
    }

    public static synchronized String encodeBmp(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    public static Bitmap getProperImage(Uri fileUri) {
        BitmapFactory.Options opts = new BitmapFactory.Options();

        opts.inSampleSize = 4;

        Bitmap bm = BitmapFactory.decodeFile(fileUri.getPath(), opts);

        ExifInterface exif = null;

        try {
            exif = new ExifInterface(fileUri.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);

        int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;

        int rotationAngle = 0;

        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;

        Matrix matrix = new Matrix();

        matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);

        Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, opts.outWidth, opts.outHeight, matrix, true);

        rotatedBitmap = getResizedBitmap(rotatedBitmap, 800);

        return rotatedBitmap;
    }

    public static Bitmap getResizedBitmap(Bitmap image, int maxWidth) {

        int currentBitmapWidth = image.getWidth();
        int currentBitmapHeight = image.getHeight();

        int newHeight = 0;

        if (currentBitmapWidth > maxWidth) {
            newHeight = (currentBitmapHeight * maxWidth) / currentBitmapWidth;
        } else {
            newHeight = currentBitmapHeight;

            maxWidth = currentBitmapWidth;
        }

        return Bitmap.createScaledBitmap(image, maxWidth, newHeight, true);
    }

    public static String getApplicationVersion(Context ctx) {
        PackageManager manager = ctx.getPackageManager();
        PackageInfo info;
        try {
            info = manager.getPackageInfo(ctx.getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return "";
    }

}