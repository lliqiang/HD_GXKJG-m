package com.hengda.frame.fileloader.download;

import java.io.File;

public interface DownloadCallBackListener {

    void completed(File file);

    void justPlay(String fileDir);

    void error(Exception e);

}