package com.easytarget.micopi;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by michel on 27/10/14.
 */
public class MediaFileHandler implements MediaScannerConnection.MediaScannerConnectionClient{
    private MediaScannerConnection mConnection;
    private static Context mContext;
    private String mFileName;

    /**
     * Saves the generated image to a file.
     */
    public String saveContactImageFile(Context context, Bitmap bitmap, String name, char md5Char) {
        mContext = context;

        String strFileName = name.replace( ' ', '_' ) + "-" + md5Char + ".png";

        // Files will be stored in the /sdcard/micopi dir.
        File micopiFolder = new File( Environment.getExternalStorageDirectory() + "/micopi/" );
        if( micopiFolder.mkdirs() ) Log.i("New directory created", micopiFolder.getPath());
        else Log.i( "New directory created", "false" );

        // The file name is "FirstName_LastName-x.png".
        File file = new File( micopiFolder.getAbsolutePath(), strFileName );
        FileOutputStream fileOutStream;

        try {
            fileOutStream = new FileOutputStream( file );
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fileOutStream);
            fileOutStream.close();
            performMediaScan( file );
        } catch ( Exception e ) {
            e.printStackTrace();
            return "";
        }
        return strFileName;
    }

    /**
     * Makes the saved picture appear in Android's gallery.
     * @param file  Scan this file for media content
     */
    private void performMediaScan(File file) {
        mFileName = file.getAbsolutePath();
        mConnection = new MediaScannerConnection( mContext, this );
        mConnection.connect();
    }

    /**
     *
     */
    @Override
    public void onMediaScannerConnected() {
        mConnection.scanFile(mFileName, null);
    }

    /**
     *
     * @param path
     * @param uri
     */
    @Override
    public void onScanCompleted(String path, Uri uri) {
        mConnection.disconnect();
    }
}
