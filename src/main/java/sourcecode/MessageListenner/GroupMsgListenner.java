package sourcecode.MessageListenner;

import com.baidu.aip.speech.AipSpeech;
import com.baidu.aip.speech.TtsResponse;
import com.forte.qqrobot.anno.Listen;
import com.forte.qqrobot.beans.messages.msgget.GroupMsg;
import com.forte.qqrobot.beans.messages.types.MsgGetTypes;
import com.forte.qqrobot.sender.MsgSender;
import com.forte.qqrobot.utils.CQCodeUtil;
import sourcecode.Util.BdAipSpeech;
import sourcecode.Util.Tlc;

import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GroupMsgListenner {

    /*被 at*/
    /*
    @Filter(at = true)
    @Listen(MsgGetTypes.groupMsg)
    */

    @Listen(MsgGetTypes.groupMsg)
    public void onGroupMessage(GroupMsg groupMsg, MsgSender msgSender, CQCodeUtil util) {

        /*保存图片与日志输出*/
        String groupMsgString = groupMsg.toString();
        if(groupMsgString.indexOf("CQ:image")>=0){
            Tlc.getMessageInImage(groupMsgString);
            System.out.print(" 群: "+ Tlc.groupMap.get(groupMsg.getGroup()));
            System.out.println(" "+"成员: "+ Tlc.groupMembers.get(groupMsg.getGroup()).get(groupMsg.getQQ())+"\n");
        }
        else{
            System.out.print("群: "+ Tlc.groupMap.get(groupMsg.getGroup()));
            System.out.println(" "+"群名片: "+ Tlc.groupMembers.get(groupMsg.getGroup()).get(groupMsg.getQQ()));
            System.out.println("消息: "+groupMsg.getMsg()+"\n");
        }

        /*管理群*/
        if(groupMsg.getGroup().equals(Tlc.hyyGroup)){

            /*命令消息*/
            char[] commendMsg = groupMsg.getMsg().toCharArray();
            if(commendMsg[0] == '#'){

                /*格式: [#命令:命令参数]*/
                Matcher matcher = Pattern.compile("#\\S+:").matcher(groupMsg.getMsg());
                ArrayList<String> commendAry = new ArrayList<>();
                while(matcher.find())
                    commendAry.add(matcher.group());

                /*命令*/
                String commend = commendAry.get(0).substring(1,commendAry.get(0).length()-1);

                matcher = Pattern.compile(":[\\S+\\s]+").matcher(groupMsg.getMsg());
                commendAry.clear();
                while(matcher.find())
                    commendAry.add(matcher.group());

                /*参数*/
                String parameter = commendAry.get(0).substring(1,commendAry.get(0).length());

                /*语音合成*/
                if(commend.equals("语音合成")){
                    TtsResponse ttsResponse = Tlc.baiduAipSpeech.synthesis(parameter, "zh", 1, Tlc.options);
                    byte[] data = ttsResponse.getData();
                    String voiceFileName = String.valueOf(new Random().nextLong())+".mp3";
                    BdAipSpeech.getFile(data,  voiceFileName);
                    msgSender.SENDER.sendGroupMsg(groupMsg.getGroup(),
                            "[CQ:record,"+"file="+voiceFileName+"]");
                }

            }

            /*复读机*/
            Tlc.cache(groupMsg, msgSender);

            /*被at则被图灵回复*/
            if(Tlc.returnAt(util,groupMsg)){
                if(groupMsg.getGroup().equals(Tlc.hyyGroup)) {
                    for (String tulingMsg : Tlc.tuling.getMessage(groupMsg.getMsg())) {
                        if (tulingMsg.equals("亲爱的，当天请求次数已用完。")) {
                            if (!Tlc.banQQlist.contains(groupMsg.getQQ())) {
                                msgSender.SENDER.sendGroupMsg(groupMsg.getGroup(), "如果我发送了这条消息就说明图灵没有" +
                                        "给我授权接口，或者是我没充钱今天的一百次已经用完了...,所以呢你就别再at我了,否则后续我考虑" +
                                        "将你禁言。");
                                Tlc.banQQlist.add(groupMsg.getQQ());
                            } else { /*永远都为true所以永远都执行else*/
                                if (!msgSender.SETTER.setGroupBan(groupMsg.getGroup(), groupMsg.getQQ(),
                                        (60 * 1000) + (new Random().nextInt(10 + 1) * (60 * 1000))) &&
                                        Tlc.banQQlist.contains(groupMsg.getQQ()))
                                    msgSender.SENDER.sendGroupMsg(groupMsg.getGroup(), "管理员就算了...");
                                else { /*7999*/
                                    msgSender.SETTER.setGroupBan(groupMsg.getGroup(), groupMsg.getQQ(),
                                            (60 * 10) + (new Random().nextInt(10 + 1) * (60 * 10)));
                                    msgSender.SENDER.sendGroupMsg(groupMsg.getGroup(), "就拿你撒撒气叭~");
                                    Tlc.banQQlist.remove(groupMsg.getQQ());
                                }
                            }
                            return;
                        }
                        /*正常*/
                        else{
                            TtsResponse ttsResponse = Tlc.baiduAipSpeech.synthesis(tulingMsg, "zh", 1, Tlc.options);
                            byte[] data = ttsResponse.getData();
                            String voiceFileName = String.valueOf(new Random().nextLong())+".mp3";
                            BdAipSpeech.getFile(data,  voiceFileName);
                            msgSender.SENDER.sendGroupMsg(groupMsg.getGroup(),
                                    "[CQ:record,"+"file="+voiceFileName+"]"
                            );

                        }
                    }
                    return;
                }
            } /*图灵 End*/
        } /*管理群 End*/
    } // onGroupMessage End
} // 群消息监听 End
