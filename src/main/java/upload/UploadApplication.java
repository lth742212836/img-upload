package upload;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import upload.utils.SystemUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * @author swing
 */
public class UploadApplication {
    public static final String BUCKET_NAME;
    public static final String ACCESS_KEY_SECRET;
    public static final String ACCESS_KEY_ID;
    public static final String ENDPOINT;
    public static final String PATH = "lucode\\img-upload";
    public static final String finalPath;
    public static final String propertyName = "config.properties";

    static {
        String systemPath = SystemUtil.getSystemHome();
        finalPath = systemPath + "\\" + PATH + "\\" + propertyName;
        Properties properties = new Properties();
        try {
            properties.load(new FileReader(finalPath));
        } catch (IOException e) {
            System.out.println("load config.properties error");
        }
        BUCKET_NAME = properties.getProperty("BUCKET_NAME");
        ACCESS_KEY_SECRET = properties.getProperty("ACCESS_KEY_SECRET");
        ACCESS_KEY_ID = properties.getProperty("ACCESS_KEY_ID");
        ENDPOINT = properties.getProperty("ENDPOINT");
    }

    /**
     * 上传文件至阿里云
     *
     * @param bytes   文件字节数组
     * @param extName 文件扩展名
     * @return 文件下载路径
     */
    public static String uploadFile(byte[] bytes, String extName) {
        String objectName = getObjectName(extName);
        OSS ossClient = new OSSClientBuilder().build(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME, objectName, new ByteArrayInputStream(bytes));
        ossClient.putObject(putObjectRequest);
        ossClient.shutdown();
        //修改为你的路径
        return "https://" + BUCKET_NAME + ".oss-cn-beijing.aliyuncs.com/" + objectName;
    }

    public static void main(String[] args) throws IOException {
        if (!checkArgs(args)) {
            System.out.println("upload fail");
            return;
        }
        for (String arg : args) {
            //获取后缀
            String extName = arg.split("\\.")[1];
            File file = new File(arg);
            FileInputStream fileInputStream = new FileInputStream(file);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024 * 1024 * 10];
            int n = 0;
            while ((n = fileInputStream.read(bytes)) != -1) {
                byteArrayOutputStream.write(bytes, 0, n);
            }
            byte[] fileBytes = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();
            fileInputStream.close();
            try {
                System.out.println(uploadFile(fileBytes, extName));
            } catch (Exception e) {
                System.out.println("upload fail");
            }
        }

    }

    /**
     * 获取唯一的文件名
     *
     * @param extName 文件扩展名
     * @return 唯一文件名
     */
    public static String getObjectName(String extName) {
        //使用时间戳作为文件名
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss-SSS");
        String format = simpleDateFormat.format(new Date());
        return "markdown/" + format + "." + extName;
    }

    /**
     * 检查args，文件是否存在
     *
     * @param args args
     */
    public static boolean checkArgs(String[] args) {
        if (args.length == 0) {
            return false;
        }
        for (String filePath : args) {
            if (!new File(filePath).exists()) {
                return false;
            }
        }
        return true;
    }
}