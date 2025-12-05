package cn.nahco3awa.naouc;

import static android.widget.Toast.LENGTH_LONG;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public class MyCrashHandler implements Thread.UncaughtExceptionHandler {
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private Context mcontext;
    private static MyCrashHandler myCrashHandler;

    private MyCrashHandler() {}

    public static synchronized MyCrashHandler newInstance() {
        if (myCrashHandler == null)
            myCrashHandler = new MyCrashHandler();
        return myCrashHandler;
    }

    public void init(Context context) {
        mcontext = context;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(@NonNull Thread t, Throwable e) {
        Toast.makeText(mcontext, e.getMessage(), LENGTH_LONG).show();
        if (!handleExample(e) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(t, e);
        } else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    private boolean handleExample(Throwable ex) {
        if(ex == null)
            return false;
        saveCrashInfoToFile(ex);
        return true;
    }

    private void saveCrashInfoToFile(Throwable ex) {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable exCause = ex.getCause();
        while (exCause != null) {
            exCause.printStackTrace(printWriter);
            exCause = exCause.getCause();
        }
        printWriter.close();

        long timeMillis = System.currentTimeMillis();
        String fileName = "crash-" + timeMillis + ".log";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String path = "/storage/emulated/0/crash_logInfo/";
            File fl = new File(path);
            if (!fl.exists()) {
                boolean ignored = fl.mkdirs();
            }
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(path + fileName);
                fileOutputStream.write(writer.toString().getBytes());
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}