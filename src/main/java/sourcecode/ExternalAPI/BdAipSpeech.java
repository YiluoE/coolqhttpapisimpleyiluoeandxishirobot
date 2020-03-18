package sourcecode.ExternalAPI;

import com.baidu.aip.speech.AipSpeech;
import sourcecode.Util.TLC;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class BdAipSpeech extends AipSpeech {

    public BdAipSpeech(String appId, String apiKey, String secretKey) {
        super(appId, apiKey, secretKey);
        
    }

    public static void getFile(byte[] bfile,String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(TLC.voicePath);
            if (!dir.exists() && dir.isDirectory()) { // 判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(TLC.voicePath + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
