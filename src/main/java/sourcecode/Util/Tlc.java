package sourcecode.Util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.forte.qqrobot.beans.cqcode.CQCode;
import com.forte.qqrobot.beans.messages.msgget.GroupMsg;
import com.forte.qqrobot.sender.MsgSender;
import com.forte.qqrobot.utils.CQCodeUtil;

import javax.swing.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tlc {

    private final String windowsJsonDataPath = "C:/Users/Yiluo/Desktop/jsondata/data.json";
    private final String linuxJsonDataPath = "/root/jsondata/data.json";

    /*系统识别懒得加..对！都到这种地步了*/
    //public static final String voicePath = "C:/Users/Yiluo/Desktop/outdate/";
    public static final String voicePath = "C:/Users/Yiluo/Desktop/cqp/酷Q Pro/data/record/";

    public static boolean windowsOS = false;

    public static String
            testQQ = null,
            testGroup = null,
            hyy = null,
            hyyGroup = null;

    public static String messageImageSavePath = null;

    public static TuLing tuling = null;
    /*未授权接口重复at,即将被禁言列表*/
    public static ArrayList<String> banQQlist = new ArrayList<>();

    /* 登录QQ的好友列表和群列表已经群成员列表 */
    public static final HashMap<String,String> friendMap = new HashMap<>();
    public static final HashMap<String,String> groupMap = new HashMap<>();
    /*群成员*/
    public static final HashMap<String,HashMap<String,String>> groupMembers = new HashMap<>();

    public static BdAipSpeech baiduAipSpeech;
    public  static final HashMap options = new HashMap(){{
        put("spd", "5");
        put("pit", "4");
        put("per", "3"); // 情感逍遥
    }};

    public Tlc(){ /*构造*/

        boolean os = windowsOS = System.getProperty("os.name").toLowerCase().startsWith("windows");

        File jsondatafile = new File(os?windowsJsonDataPath:linuxJsonDataPath);
        byte[] data = new byte[(int)jsondatafile.length()];
        try {
            new FileInputStream(jsondatafile).read(data);
        }catch (Exception e){}

        /*加载配置*/
        JSONObject jobj = JSON.parseObject(new String(data));
        if(jobj != null){
            tuling = new TuLing(jobj.getString("tuling_api_key"));

            baiduAipSpeech = new BdAipSpeech(jobj.getString("baidu_api_id"),jobj.getString("baidu_api_key"),jobj.getString("baidu_api_secertkey"));


            testQQ = jobj.getString("testQQ");
            testGroup = jobj.getString("testGroup");
            if(os){
                messageImageSavePath = jobj.getString("windowsMessageImagesPath");
                hyy = testQQ;
                hyyGroup = testGroup;
            }
            else{
                messageImageSavePath = jobj.getString("linuxMessageImagesPath");
                hyy = jobj.getString("hyy");
                hyyGroup = jobj.getString("hyyGroup");
            }
        }
        else {
            System.out.println("json error");
            /*兄弟出问题了在这里中断叭...不要再让java做无畏的挣扎了,毕竟ta还是个年轻的咖啡*/
        } /* 加载配置 End */

    } /*构造 End*/

    public static String returnMsgLog(String msg){
        String message = msg;
        if(msg.length()>75)
            message = msg.substring(0,70)+" . . ]";
        return  message;
    }

    /*是否被at*/
    public static boolean returnAt(CQCodeUtil util, GroupMsg groupMsg){
        CQCode at = util.getCQCode_At(groupMsg.getThisCode());
        if(groupMsg.getMsg().contains(at))
            return true; /*被 at*/
        return false;
    }

    // 消息缓存用以复读= =
    public static boolean cooling = true;
    public static String msgCache = null;
    public static void cache(GroupMsg groupMsg, MsgSender sender){
        if(cooling){
            if(msgCache == null){
                msgCache = groupMsg.getMsg();
                return;
            }

            if(msgCache.equals(groupMsg.getMsg())){
                sender.SENDER.sendGroupMsg(groupMsg.getGroup(),msgCache);
                msgCache = null;
                cooling = false;
                new Thread(() -> {
                    try {
                        //sender.SENDER.sendGroupMsg(groupMsg.getGroup(), "cooling~");
                        Thread.sleep((1000 * 60) * 5/*最后*分钟*/);
                        cooling = true;
                        //sender.SENDER.sendGroupMsg(groupMsg.getGroup(), "cooling End~");
                    } catch (Exception e) {}
                }).run();
                return;
            }else
                msgCache = groupMsg.getMsg();
        }
    }

    public static void getMessageInImage(String msgString){

        Matcher matcher = Pattern.compile("\\[\\S+\\]").matcher(msgString);


        ArrayList<String> strs = new ArrayList<>();
        while(matcher.find())
            strs.add(matcher.group());
        Matcher matcher2 = Pattern.compile("https:\\S+[\\]]").matcher(strs.get(0));
        String url = null;
        while(matcher2.find())
            url = matcher2.group();
        url = url.substring(0,url.length()-1);

        try {
            System.out.print("保存了一张图片耶: "+GetHttpsImage.getImage(url));
        }catch (Exception e){e.printStackTrace();}
    }


    public static class GetHttpsImage {

        public static GetHttpsImage getImage = new GetHttpsImage();

        public static String getImage(String urlSTR) throws Exception{
            try {
                URL url = new URL(urlSTR);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(3 * 1000);
                InputStream inStream = conn.getInputStream();

                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = 0;
                while( (len = inStream.read(buffer)) != -1 )
                    outStream.write(buffer, 0, len);

                inStream.close();
                byte[] data = outStream.toByteArray();

                /*写入图片到文件*/
                if(true){
                    String msgImageName = String.valueOf(new Random().nextLong())+".jpg";
                    File imageFile = new File(messageImageSavePath + msgImageName);
                    FileOutputStream outStreamSave = new FileOutputStream(imageFile);
                    outStreamSave.write(data);
                    outStreamSave.close();
                    return msgImageName;
                }

            }catch(Exception e){}
            return "如果返回这个我吃屎~"; /*算了不吃了...如果路径错误就返回这个了*/
        }

        /*没实现呢~ 我好懒...不我好菜...*/
        public ImageIcon getRobotHeadImage(String url){ return null; }
    }

    // 随机消息 /*又没用又长的放最下面*/
    public static final ArrayList<String> randomMessage = new ArrayList<String>(){
        {
            this.add("是的我又饿了~");
            this.add("如果现在是白天的话我肯定在睡觉...");
            this.add("喂！你是不是又不听话了?");
            this.add("感觉猫比狗好养点,毕竟吃的少~");
            this.add("复读是不可能复读的这辈子是不可能复读的。");
            this.add("很冒昧的打扰了你们，想说之前倚门卖笑，和愿君以后安好。");
            this.add("三体?那究竟是什么东西...");
            this.add("gdx快出来玩啦哈哈");
            this.add("事实证明，当一个人沉默的时候，才是要鸽的前奏。●ｖ●");
            this.add("建国之后，情况就不一样了，用谷歌都得科学上网");
            this.add("开战前做个约定，你若接的住我的什么什么，几招内如何如何，我就怎样怎样，你就这样那样。");
            this.add("要不...就别拉图灵的接口了吧,好像授权蛮麻烦的...");
            this.add("最致命的异常 Not fonud object...nmb");
            this.add("好吧...我真的想不出来该说些什么了");
            this.add("我们为了光明而组织黑暗。我们...就是刺客");
            this.add("我的刺客原则就是必须用袖剑杀人。");
            this.add("一位大师说道: 要相信科学。 说完便从16米高的楼层跳下去吃饭了");
            this.add(
                    "  ┏┓　　　┏┓\n" +
                            "┏┛┻━━━┛┻┓\n" +
                            "┃　　　　　　　┃\n" +
                            "┃　　　━　　　┃\n" +
                            "┃　┳┛　┗┳　┃\n" +
                            "┃　　　　　　　┃\n" +
                            "┃　　　┻　　　┃\n" +
                            "┃　　　　　　　┃\n" +
                            "┗━┓　　　┏━┛\n" +
                            "　　┃　　　┃\n" +
                            "　　┃　　　┃\n" +
                            "　　┃　　　┗━━━┓\n" +
                            "　　┃　　　　　　　┣┓\n" +
                            "　　┃　　　　　　　┏┛\n" +
                            "　　┗┓┓┏━┳┓┏┛\n" +
                            "　　　┃┫┫　┃┫┫\n" +
                            "　　　┗┻┛　┗┻┛"+"\n毕竟一切总不像是看上去的那么顺利...");
            this.add("投食！\n" +
                    "　 ∧＿∧\n" +
                    " （｡･ω･｡)つ━☆・*。\n" +
                    "  ⊂　　 ノ 　　　・゜+.\n" +
                    "　しーＪ　　　°。+ *´¨)\n" +
                    "　　　       　　.· ´¸.·*´¨) ¸.·*¨)\n" +
                    "　　　　　　　     　(¸.·´ (¸.·’*\n");
        }
    };

}
