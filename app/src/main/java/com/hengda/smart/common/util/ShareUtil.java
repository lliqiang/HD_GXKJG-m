package com.hengda.smart.common.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;

public class ShareUtil {

	/**
	 * 分享文字
	 *
	 * @param context
	 * @param activityTitle
	 * @param msgTitle
	 * @param msgText
	 */
	public static void shareText(Context context, String activityTitle,
								 String msgTitle, String msgText) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);
		intent.putExtra(Intent.EXTRA_TEXT, msgText);
		context.startActivity(Intent.createChooser(intent, activityTitle));
	}

	/**
	 * 分享图片
	 *
	 * @param context
	 * @param activityTitle
	 * @param imgPath
	 */
	public static void sharePic(Context context, String activityTitle,
								String imgPath) {
		File f = new File(imgPath);
		sharePic(context, activityTitle, f);
	}

	/**
	 * 分享图片
	 *
	 * @param context
	 * @param activityTitle
	 * @param file
	 */
	public static void sharePic(Context context, String activityTitle, File file) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		if (file != null && file.exists() && file.isFile()) {
			intent.setType("image/*");
			Uri u = Uri.fromFile(file);
			intent.putExtra(Intent.EXTRA_STREAM, u);
		}
		context.startActivity(Intent.createChooser(intent, activityTitle));
	}
}
