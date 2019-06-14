package linzhurui.facedetection002.util;

public class WebpUtil {
    private static WebpUtil instance = null;

    private WebpUtil() {
        System.loadLibrary("webp_evme");
    }

    public static WebpUtil getInstance() {
        if (instance == null) {
            synchronized (WebpUtil.class) {
                if (instance == null) {
                    instance = new WebpUtil();
                }
            }
        }
        return instance;
    }

    public static void saveWebp(){

    }


}
