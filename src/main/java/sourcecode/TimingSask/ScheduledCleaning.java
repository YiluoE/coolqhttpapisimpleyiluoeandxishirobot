package sourcecode.TimingSask;

import com.forte.qqrobot.anno.timetask.CronTask;
import com.forte.qqrobot.sender.MsgSender;
import com.forte.qqrobot.timetask.TimeJob;
import com.forte.qqrobot.utils.CQCodeUtil;
import org.apache.commons.codec.digest.DigestUtils;
import sourcecode.Util.TLC;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@CronTask("0 0 0 1/1 * ? ") /*一天清理一次吧...*/
public class ScheduledCleaning implements TimeJob {

    @Override
    public void execute(MsgSender msgSender, CQCodeUtil cqCodeUtil) {
        try {

            new Thread(()->{
                TLC.reMD5();
            }).start();

            HashMap<String, File> imgfilesmap = new HashMap<>();
            File[] imgfiles = new File(TLC.messageImageSavePath).listFiles();
            for(File imgfile : imgfiles){
                byte[] bytes = new byte[(int)imgfile.length()];
                FileInputStream fileinputstream = new FileInputStream(imgfile);
                fileinputstream.read(bytes);
                fileinputstream.close(); /*我在这里学会了随手关门的好习惯*/ /*以后的再关吧...之前的不关了*/

                Matcher matcher = Pattern.compile("[.]\\S+").matcher(imgfile.getName());
                ArrayList<String> strs = new ArrayList<>();
                while(matcher.find())
                    strs.add(matcher.group());

                /*重复删除*/ /*非图片文件删除*/
                if(!imgfilesmap.containsKey(DigestUtils.md5Hex(bytes)) && strs.get(0).equals(".jpg") )
                    imgfilesmap.put(DigestUtils.md5Hex(bytes),imgfile);
                else{
                    System.out.println("delete: "+imgfile.getName());
                    imgfile.delete();
                }

            }

        }catch (Exception e){e.printStackTrace();}
    }

}
