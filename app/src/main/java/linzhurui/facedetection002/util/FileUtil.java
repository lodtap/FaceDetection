package linzhurui.facedetection002.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

public class FileUtil {
	private static final  String TAG = "FileUtil";
	private static final File parentPath = Environment.getExternalStorageDirectory();
	private static   String storagePath = "";
	private static final String DST_FOLDER_NAME = "PlayCamera";

	/**初始化保存路径
	 * @return
	 */
	private static String initPath(){
		if(storagePath.equals("")){
			storagePath = parentPath.getAbsolutePath()+"/" + DST_FOLDER_NAME;
			File f = new File(storagePath);
			if(!f.exists()){
				f.mkdir();
			}
		}
		return storagePath;
	}

	/**保存Bitmap到sdcard
	 * @param b
	 */
	public static void saveBitmap(Bitmap b){

		String path = initPath();
		long dataTake = System.currentTimeMillis();
		String jpegName = path + "/" + dataTake +".jpg";
		Log.i(TAG, "saveBitmap:jpegName = " + jpegName);
		try {
			FileOutputStream fout = new FileOutputStream(jpegName);
			BufferedOutputStream bos = new BufferedOutputStream(fout);
			b.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();
			Log.i(TAG, "saveBitmap成功");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.i(TAG, "saveBitmap:失败");
			e.printStackTrace();
		}

	}

	/**保存yuv到sdcard
	 * @param data
	 */
	public static void saveYuv(byte[] data){

		String path = initPath();
		long dataTake = System.currentTimeMillis();
		String yuvName = path + "/" + dataTake +".yuv";
		Log.i(TAG, "saveYuv:yuvName = " + yuvName);
		try {
			FileOutputStream fout = new FileOutputStream(yuvName);
			BufferedOutputStream bos = new BufferedOutputStream(fout);
			bos.write(data);
			bos.flush();
			bos.close();
			Log.i(TAG, "saveYuv成功");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.i(TAG, "saveYuv:失败");
			e.printStackTrace();
		}

	}

    /**保存Bitmap 为Webp 到sdcard
     * @param b
     */
    public static void saveWebp(Bitmap b){

        String path = initPath();
        long dataTake = System.currentTimeMillis();
        String webpName = path + "/" + dataTake +".webp";
        Log.i(TAG, "saveWebp:webpName = " + webpName);
        try {
            FileOutputStream fout = new FileOutputStream(webpName);
            BufferedOutputStream bos = new BufferedOutputStream(fout);
            b.compress(Bitmap.CompressFormat.WEBP, 100, bos);
            bos.flush();
            bos.close();
            Log.i(TAG, "saveWebp成功");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.i(TAG, "saveWebp:失败");
            e.printStackTrace();
        }

    }




}
