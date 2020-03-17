package sourcecode;

import com.alibaba.fastjson.JSONArray;
import com.forte.component.forcoolqhttpapi.CoolQHttpApp;
import com.forte.component.forcoolqhttpapi.CoolQHttpConfiguration;
import com.forte.qqrobot.beans.messages.result.FriendList;
import com.forte.qqrobot.beans.messages.result.inner.Friend;
import com.forte.qqrobot.beans.messages.result.inner.Group;
import com.forte.qqrobot.beans.messages.result.inner.GroupMember;
import com.forte.qqrobot.sender.MsgSender;
import com.forte.qqrobot.utils.CQCodeUtil;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import sourcecode.Util.LogBack;
import sourcecode.Util.Tlc;

import java.text.SimpleDateFormat;
import java.util.*;

public class MainFunction implements CoolQHttpApp {

    /* 外部并不会用到,我认为这样会第一时间初始化,但可能java的处理更早..我不是很了解阿 */
    private Tlc tool;

    public MainFunction(){
        this.tool = new Tlc();
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

        /* 获取所有好友的数据 */
        boolean[] state = {false,false,false};

        new Thread(()->{
            FriendList fidGroupList = sender.GETTER.getFriendList();
            if (fidGroupList != null) {
                Map<String, Friend[]> fidListMap = fidGroupList.getFriendList();
                Set<String> mapKeyList = fidListMap.keySet();
                for (String key : mapKeyList) { // key 好友分组
                    Friend[] fidList = fidGroupList.getFriendList().get(key);
                    for (Friend fid : fidList) {
                        Tlc.friendMap.put(fid.getQQ(),fid.getName());
                        ; // fid对每个好友的操作
                    }
                }
            }
            state[0] = true; return;
        }).start();

        /* 获取群的数据 */
        new Thread(()->{
            for (Group group : sender.GETTER.getGroupList()) {
                HashMap<String,String> members = new HashMap<>();
                for (GroupMember member : sender.GETTER.getGroupMemberList(group.getCode())) {
                    members.put(member.getQQ(),member.getName());
                    ; // member 对所有群所有成员的操作
                }
                Tlc.groupMap.put(group.getCode(),group.getName());
                Tlc.groupMembers.put(group.getCode(),members);
                ; // group 对所有群的操作
            }
            state[1] = true; return;
        }).start();

        new Thread(()->{
            while (true){
                try {
                    long groupmembersum = 0;

                    if(state[0] && state[1]){
                        for(String groupmemberkey : Tlc.groupMembers.keySet())
                            groupmembersum += Tlc.groupMembers.get(groupmemberkey).size();

                        System.out.println("-------------------------------------------------");
                            System.out.println(
                                    //"-------------------------------------------------\n" +
                                            "好友数量: \t\t\t\t\t\t\t\t\t" + Tlc.friendMap.size() + "\n" +
                                            "群数量: \t\t\t\t\t\t\t\t\t" + Tlc.groupMap.size() // + "\n" +
                                            /*"所有群成员总和数量: \t\t\t\t\t\t\t" + String.valueOf(groupmembersum)*/
                            );
                            /*for (String key : Tlc.groupMap.keySet()) {
                                String gropusum = String.valueOf(Tlc.groupMembers.get(key).size());
                                System.out.print("\t\t" + gropusum);

                                //int lenth = gropusum.length()/2; // 实现不了...再见 四位打两个 两位打三个...
                                if (gropusum.length() == 4)
                                    System.out.print("\t\t");
                                else
                                    System.out.print("\t\t\t");
                                System.out.println(Tlc.groupMap.get(key));
                            }*/
                        // System.out.println("-------------------------------------------------");
                        /*加载机器人的管理对象*/
                        if(Tlc.jobj != null){
                            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                            JSONArray jary =  Tlc.jobj.getJSONArray("managementGroup");
                            for(Object jobj : jary)
                                if(Tlc.friendMap.keySet().contains(jobj.toString())){
                                    Tlc.administrators.add(jobj.toString());
                                    sender.SENDER.sendPrivateMsg(jobj.toString(),date);
                                }else
                                    System.out.println("没有好友: "+jobj.toString()+" 所以无法设为管理员...");

                            jary =  Tlc.jobj.getJSONArray("administrators");
                            for(Object jobj : jary)
                                if(Tlc.groupMap.keySet().contains(jobj.toString())){
                                    Tlc.managementGroupAry.add(jobj.toString());
                                    sender.SENDER.sendGroupMsg(jobj.toString(),date);
                                }else
                                    System.out.println("没有群组: "+jobj.toString()+" 所以无法管理ta...");

                        }
                        System.out.print("管理员: ");
                        for(String admin : Tlc.administrators)
                            System.out.print("["+Tlc.friendMap.get(admin)+"]");

                        System.out.print("\n管理的群: ");
                        for(String mgtg : Tlc.managementGroupAry){
                            System.out.print("["+Tlc.groupMap.get(mgtg)+"]");
                            Tlc.msgCacheMap.put(mgtg,(new Tlc.MsgCache()));
                        }

                        System.out.println("\n-------------------------------------------------\n");

                        return; /*数据加载完毕*/

                    }
                    Thread.sleep(150);
                }catch (Exception e){}
            }
        }).start();

        System.out.println(
              "\n  ┏┓　　　┏┓\n" +
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
        System.out.println("-------------------------------------------------");
        System.out.println("当前系统: "+ (this.tool.windowsOS ? "Windows":"Linux"));
        System.out.println("消息图片存储路径: "+ tool.messageImageSavePath);
        //System.out.println("管理员: "+ Tlc.administrators.toString());
        System.out.println("执行环境: "+ (tool.windowsOS ? "开发" : "使用"));
        System.out.println("-------------------------------------------------");
    }

}
