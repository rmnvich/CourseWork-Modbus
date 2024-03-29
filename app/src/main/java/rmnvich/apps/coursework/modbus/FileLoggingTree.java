package rmnvich.apps.coursework.modbus;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.util.Log;
import timber.log.Timber;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FileLoggingTree extends Timber.DebugTree {

    private static final String TAG = FileLoggingTree.class.getSimpleName();

    private Context context;

    public FileLoggingTree(Context context) {
        this.context = context;
    }

    @Override
    protected void log(int priority, String tag, String message, Throwable t) {
        try {
            File direct = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS) + "/CourseWork");
            if (!direct.exists()) {
                direct.mkdir();
            }
            String fileNameTimeStamp = new SimpleDateFormat("MMM-yyyy",
                    Locale.getDefault()).format(new Date());
            String logTimeStamp = new SimpleDateFormat("dd MMM yyyy 'at' HH:mm:ss",
                    Locale.getDefault()).format(new Date());

            String fileName = fileNameTimeStamp + ".html";
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS) + "/CourseWork" + File.separator + fileName);
            file.createNewFile();

            if (file.exists()) {
                OutputStream fileOutputStream = new FileOutputStream(file, true);

                fileOutputStream.write(("<p style=\"background:lightgray;\">" +
                        "<strong style=\"background:lightblue;\">&nbsp&nbsp" +
                        logTimeStamp +
                        " :&nbsp&nbsp</strong>&nbsp&nbsp" +
                        message +
                        "</p>").getBytes());
                fileOutputStream.close();
            }

            if (context != null)
                MediaScannerConnection.scanFile(context, new String[]{file.getAbsolutePath()},
                        null, null);

        } catch (Exception e) {
            Log.e(TAG, "Error while logging into file : " + e);
        }
    }
}
