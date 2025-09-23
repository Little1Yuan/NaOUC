package cn.nahco3awa.naouc;

import static android.widget.Toast.LENGTH_LONG;

import android.content.Context;
import android.os.Environment;
import android.os.Looper;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import kotlinx.serialization.descriptors.PrimitiveKind;

public class MyCrashHandler implements Thread.UncaughtExceptionHandler {

    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private Context mcontext;
    private static MyCrashHandler myCrashHandler;

    public MyCrashHandler(){}

    public static synchronized MyCrashHandler newInstance() {
        if(myCrashHandler == null)
            myCrashHandler = new MyCrashHandler();
        return myCrashHandler;
    }

    /**
     * 初始化
     * @param context
     */
    public void init(Context context){
        mcontext = context;
        //系统默认处理类
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该类为系统默认处理类
        Thread.setDefaultUncaughtExceptionHandler(this);
    }



    @Override
    public void uncaughtException(Thread t, Throwable e) {
        Toast.makeText(mcontext, e.getMessage(), LENGTH_LONG).show();
        if(!handleExample(e) && mDefaultHandler != null) { //判断异常是否已经被处理
            mDefaultHandler.uncaughtException(t, e);
        }else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            //退出程序
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }

    /**
     * 提示用户出现异常
     * 将异常信息保存
     * @param ex
     * @return
     */
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
        //错误日志文件名称
        String fileName = "crash-" + timeMillis + ".log";
        //判断sd卡可正常使用
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //文件存储位置
            String path = "/storage/emulated/0/crash_logInfo/";
            File fl = new File(path);
            //创建文件夹
            if(!fl.exists()) {
                fl.mkdirs();
            }
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(path + fileName);
                fileOutputStream.write(writer.toString().getBytes());
                fileOutputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}