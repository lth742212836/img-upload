package upload.utils;

/**
 * @author liutianhao
 * @date 2022/05/13
 **/
public class SystemUtil {

    /**
     * 判断系统是否是Linux
     *
     * @return boolean：true: linux系统   false:不是linux系统
     */
    public static boolean isLinux() {
        return getSystemName().toLowerCase().contains("linux");
    }

    public static String getSystemName() {
        return System.getProperty("os.name");
    }

    public static String getSystemHome() {
        return System.getProperty("user.home");
    }

}
