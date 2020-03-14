package sourcecode;

import com.forte.component.forcoolqhttpapi.CoolQHttpApp;
import com.forte.component.forcoolqhttpapi.CoolQHttpConfiguration;
import com.forte.qqrobot.sender.MsgSender;
import com.forte.qqrobot.utils.CQCodeUtil;
import sourcecode.Tools.LogBack;
import sourcecode.Tools.ToolClass;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainFunction implements CoolQHttpApp {

    /* 外部并不会用到,我认为这样会第一时间初始化,但可能java的处理更早..我不是很了解阿 */
    private ToolClass tool;

    public MainFunction(){
        this.tool = new ToolClass();
    }

    public static void main(String[] args) {
        new MyCoolQHttpApplication(new LogBack()).run(new MainFunction());
    }

    @Override
    public void before(CoolQHttpConfiguration configuration) {
        /*服务器端直接执行*/ /*因为我也是直接拿到服务器上用的,并不是远程上报*/
        configuration.setJavaPort(5700);
        configuration.setServerPort(5699);
    }

    @Override
    public void after(CQCodeUtil cqCodeUtil, MsgSender sender) {

        System.out.println(
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
                "　　　┗┻┛　┗┻┛");

        System.out.println("当前系统: "+ (this.tool.windowsOS ? "Windows":"Linux"));
        System.out.println("消息图片存储路径: "+ tool.messageImageSavePath);
        System.out.println("管理员: "+ tool.hyy);
        System.out.println("执行环境: "+ (tool.windowsOS ? "开发" : "使用"));

        /*启动成功时用以提示 发送一条信息*/
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        sender.SENDER.sendPrivateMsg(String.valueOf(tool.testQQ),new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        sender.SENDER.sendPrivateMsg(String.valueOf(tool.hyy),new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        sender.SENDER.sendGroupMsg(String.valueOf(tool.hyyGroup),new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }

}
