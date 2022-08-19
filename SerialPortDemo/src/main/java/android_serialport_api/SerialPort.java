package android_serialport_api;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import timber.log.Timber;

/**
 * Google官方代码
 * 此类的作用为，JNI的调用，用来加载.so文件的
 * 获取串口输入输出流
 */

public class SerialPort {

    static {
        System.out.println("==============================");
        //System.loadLibrary("serial_port");
        System.loadLibrary("SerialPort");
        System.out.println("********************************");
    }

    private final BufferedInputStream mFileInputStream;
    private final FileOutputStream mFileOutputStream;

    public SerialPort(File device, int baudrate, int flags) throws SecurityException, IOException {
        /* Check access permission */
        if (!device.canRead() || !device.canWrite()) {
            try {
                /* Missing read/write permission, trying to chmod the file */
                Process su;
                su = Runtime.getRuntime().exec("/system/bin/su");
                String cmd = "chmod 666 " + device.getAbsolutePath() + "\n"
                        + "exit\n";
                su.getOutputStream().write(cmd.getBytes());
                if ((su.waitFor() != 0) || !device.canRead() || !device.canWrite()) {
                    throw new SecurityException();
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new SecurityException();
            }
        }

        System.out.println(device.getAbsolutePath() + "==============================");
        /*
         * Do not remove or rename the field mFd: it is used                                                                                                                                                                            by native method
         * close();
         */
        FileDescriptor mFd = open(device.getAbsolutePath(), baudrate, flags);
        if (mFd == null) {
            Timber.e("native open returns null");
            throw new IOException();
        } else {
            Timber.i("open: %s", mFd.toString());
        }
        mFileInputStream = new BufferedInputStream(new FileInputStream(mFd));
        mFileOutputStream = new FileOutputStream(mFd);
    }

    // JNI
    private native static FileDescriptor open(String path, int baudrate,
                                              int flags);

    // Getters and setters
    public BufferedInputStream getInputStream() {
        return mFileInputStream;
    }

    public OutputStream getOutputStream() {
        return mFileOutputStream;
    }

    public native void close();
}
