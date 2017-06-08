package com.hengda.smart.common.util;


import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Response;

/**
 * 作者：Tailyou （祝文飞）
 * 时间：2016/5/11 08:27
 * 邮箱：tailyou@163.com
 * 描述：文件下载回调，下载并解压文件
 */
public abstract class HFileCallBack extends Callback<File> {

    private String destFileDir;
    private String destFileName;

    public HFileCallBack(String destFileDir, String destFileName) {
        this.destFileDir = destFileDir;
        this.destFileName = destFileName;
    }

    public abstract void inProgress(float progress, long total);

    @Override
    public File parseNetworkResponse(Response response) throws Exception {
        InputStream is = null;
        byte[] buf = new byte[2048];
        FileOutputStream fos = null;
        try {
            is = response.body().byteStream();
            final long total = response.body().contentLength();
            long sum = 0;
            File dir = new File(destFileDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, destFileName);
            fos = new FileOutputStream(file);
            int len;
            while ((len = is.read(buf)) != -1) {
                sum += len;
                fos.write(buf, 0, len);
                final long finalSum = sum;
                OkHttpUtils.getInstance().getDelivery().post(new Runnable() {
                    @Override
                    public void run() {
                        inProgress(finalSum * 1.0f / total, total);
                    }
                });
            }
            fos.flush();
            //改
//            HdAppConfig.setIsLoading(false);
            unzipFile(file);
            return file;
        } finally {
            try {
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
     * 解压文件
     *
     * @param file
     */
    private void unzipFile(File file) {
        try {
            ZipUtil.unZipFolder(file.getAbsolutePath(), destFileDir);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (file.exists()) {
            file.delete();
        }
    }

}
