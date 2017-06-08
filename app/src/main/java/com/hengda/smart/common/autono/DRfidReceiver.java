package com.hengda.smart.common.autono;


import com.hengda.smart.gxkjg.app.HdApplication;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;



public abstract class DRfidReceiver   {

    public SerialPort mSerialPort;
    public ReadThread mReadThread;
    public InputStream mInputStream;
    public OutputStream mOutputStream;

    // 回传参数字符串
    private byte[] tem1 = new byte[4];
    private byte[] tem2 = new byte[4];

    /**
     * 注册收号器
     */
    public void registerRFIDReceiver() {
//        Logger.d("开启RFID收号");
        try {
            mSerialPort = HdApplication.getSerialPort();
            mInputStream = mSerialPort.getInputStream();
            mOutputStream = mSerialPort.getOutputStream();
            mReadThread = new ReadThread();
            mReadThread.start();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidParameterException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解除注册收号器
     */
    public void unregisterRFIDReceiver() {
//        Logger.d("关闭RFID收号");
        if (mReadThread != null) {
            mReadThread.interrupt();
        }
        HdApplication.closeSerialPort();
        mSerialPort = null;
    }

    /**
     * 收号处理部分
     *
     * @param buffer
     * @param size
     */
    protected abstract void onDataReceived(final byte[] buffer, final int size);

    private class ReadThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (true) {
                int size;
                byte[] buffer = new byte[16];
                try {
                    if (mInputStream == null) {
                        return;
                    }
                    size = mInputStream.read(buffer);
                    if (size > 0) {
                        if (size == 4) {
                            if (buffer[0] == (byte) 0xAA) {
                                tem1 = subBytes(buffer, 0, 4);
                            }
                        } else if (size == 3) {
                            tem2 = subBytes(buffer, 0, 4);
                            if (tem1 != null) {
                                onDataReceived(byteMerger(tem1, tem2), 8);
                            }
                        } else {
                            onDataReceived(buffer, size);
                        }
                    }
                    sleep(200);
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @param byte_1
     * @param byte_2
     * @return byte[]
     * @throws
     * @Description: 由于STC原因，合并两个字节数组
     * @autour wzq
     * @date 2015-6-23 下午6:50:50
     * @update (date)
     */
    public byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
        byte[] byte_3 = new byte[byte_1.length + byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }

    /**
     * 从一个byte[]数组中截取一部分
     *
     * @param src
     * @param begin
     * @param count
     * @return
     */
    public byte[] subBytes(byte[] src, int begin, int count) {
        byte[] bs = new byte[count];
        for (int i = begin; i < begin + count; i++)
            bs[i - begin] = src[i];
        return bs;
    }

}
