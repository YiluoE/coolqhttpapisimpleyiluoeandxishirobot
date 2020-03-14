package sourcecode.MessageListenner;

import com.forte.qqrobot.anno.Listen;
import com.forte.qqrobot.beans.messages.msgget.GroupMsg;
import com.forte.qqrobot.beans.messages.types.MsgGetTypes;
import com.forte.qqrobot.sender.MsgSender;
import com.forte.qqrobot.utils.CQCodeUtil;
import sourcecode.Tools.ToolClass;

import java.util.Random;

public class GroupMsgListenner {

    /*被 at*/
    /*
    @Filter(at = true)
    @Listen(MsgGetTypes.groupMsg)
    */


    @Listen(MsgGetTypes.groupMsg)
    public void onGroupMessage(GroupMsg groupMsg, MsgSender msgSender, CQCodeUtil util) {

        System.out.println("群消息:\n\t"+"Group: "+groupMsg.getGroup()+"\n\t"+"Msg: "+ ToolClass.returnMsgLog(groupMsg.getMsg()));

        String groupMsgString = groupMsg.toString();
        if(groupMsgString.indexOf("CQ:image")>=0)
            ToolClass.getMessageInImage(groupMsgString);

        if(groupMsg.getGroup().equals(ToolClass.hyyGroup))
            ToolClass.cache(groupMsg, msgSender); /*复读机*/

        /*被at则被图灵回复*/
        if(ToolClass.returnAt(util,groupMsg))
            if(groupMsg.getGroup().equals(ToolClass.hyyGroup)) {
                for(String tulingMsg :  ToolClass.tuling.getMessage(groupMsg.getMsg())){
                    if(tulingMsg.equals("亲爱的，当天请求次数已用完。")){
                        if(!ToolClass.banQQlist.contains(groupMsg.getQQ())){
                            msgSender.SENDER.sendGroupMsg(groupMsg.getGroup(),"如果我发送了这条消息就说明图灵没有" +
                                    "给我授权接口，或者是我没充钱今天的一百次已经用完了...,所以呢你就别再at我了,否则后续我考虑" +
                                    "将你禁言。");
                            ToolClass.banQQlist.add(groupMsg.getQQ());
                        }
                        else{ /*永远都为true所以永远都执行else*/
                            if(!msgSender.SETTER.setGroupBan(groupMsg.getGroup(),groupMsg.getQQ(),
                                    (60*1000) + (new Random().nextInt(10+1)*(60*1000))) &&
                                    ToolClass.banQQlist.contains(groupMsg.getQQ()))
                                msgSender.SENDER.sendGroupMsg(groupMsg.getGroup(),"管理员就算了...");
                            else
                            { /*7999*/
                                msgSender.SETTER.setGroupBan(groupMsg.getGroup(),groupMsg.getQQ(),
                                        (60*10) + (new Random().nextInt(10+1)*(60*10)));
                                msgSender.SENDER.sendGroupMsg(groupMsg.getGroup(),"就拿你撒撒气叭~");
                                ToolClass.banQQlist.remove(groupMsg.getQQ());
                            }
                        }
                        return;
                    }
                    msgSender.SENDER.sendGroupMsg(groupMsg.getGroup(), tulingMsg);
                }
                return;

            } // tuling End
    } // onGroupMessage End
} // 群消息监听 End
