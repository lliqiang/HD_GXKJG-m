package com.hengda.frame.fileloader.callback;


import com.hengda.frame.fileloader.HDFileLoader;
import com.hengda.frame.fileloader.db.FileBean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.Response;

/**
 * Created by shiwei on 2017/1/11.
 */
public abstract class FileCallBack extends Callback<File> {
    /**
     * Stored folder path
     */
    private String destFileDir;
    /**
     * Stored file name
     */
    private String destFileName;

    /**
     * source folder path
     */
    private String srcFileDir;
    /**
     * check to update
     */
    private boolean checkUpdate;

    public FileCallBack( String srcFileDir,String destFileDir,String destFileName, boolean checkUpdate) {
        this.srcFileDir = srcFileDir;
        this.destFileDir = destFileDir;
        this.destFileName = destFileName;
        this.checkUpdate = checkUpdate;
    }

    public boolean getCheckUpdate() {
        return checkUpdate;
    }

    public String getSrcFileDir() {
        return srcFileDir;
    }

    @Override
    public File parseNetworkResponse(Response response, int id) throws Exception {
        if (response == null)
            return null;
        String time = format(response.header("Last-Modified"));//get server datetime
        FileBean fb = new FileBean();
        fb.setName(destFileName);
        fb.setDate(time);
        HDFileLoader.getInstance().setFileBean(fb);
        if (new File(srcFileDir).exists()) {//local resource exists
            List<FileBean> localDatas = HDFileLoader.getInstance().select(destFileName);
            if (localDatas.size() > 0) {
                FileBean lfb = localDatas.get(0);
                if (lfb.getDate().equals(time)) {
                    HDFileLoader.getInstance().getDelivery().execute(new Runnable() {
                        @Override
                        public void run() {
                            //if remote date equals local date ,justplay or do something
                            justPlay(srcFileDir);
                        }
                    });
                    return null;
                } else {
                    HDFileLoader.getInstance().delete(destFileName);
                }
            }
        }
        return saveFile(response, id);
    }

    public File saveFile(Response response, final int id) throws IOException {
        final long total = response.body().contentLength();
        HDFileLoader.getInstance().getDelivery().execute(new Runnable() {
            @Override
            public void run() {
                connected(total, id);
            }
        });
        InputStream is = null;
        byte[] buf = new byte[2048];
        int len = 0;
        FileOutputStream fos = null;
        try {
            is = response.body().byteStream();
            long sum = 0;
            File dir = new File(destFileDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, destFileName);
            fos = new FileOutputStream(file);
            while ((len = is.read(buf)) != -1) {
                sum += len;
                fos.write(buf, 0, len);
                final long finalSum = sum;
                HDFileLoader.getInstance().getDelivery().execute(new Runnable() {
                    @Override
                    public void run() {
                        //update progress
                        inProgress(finalSum * 1.0f / total, total, id);
                    }
                });
            }
            fos.flush();
            return file;
        } finally {
            try {
                response.body().close();
                if (is != null) is.close();
            } catch (IOException e) {
            }
            try {
                if (fos != null) fos.close();
            } catch (IOException e) {
            }
        }
    }

    /**
     * format date
     *
     * @param s
     * @return
     */
    private String format(String s) {
        Date date = new Date(s);
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String a = dateformat.format(date);
        return a;
    }

}