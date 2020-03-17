package sourcecode.MessageListenner;

import com.forte.qqrobot.anno.Filter;
import com.forte.qqrobot.anno.Listen;
import com.forte.qqrobot.beans.messages.msgget.PrivateMsg;
import com.forte.qqrobot.beans.messages.types.MsgGetTypes;
import com.forte.qqrobot.sender.MsgSender;
import sourcecode.Util.Tlc;

@Listen(MsgGetTypes.privateMsg)
public class PrivateMsgListenner {

    @Filter
    public void onPrivateMessage(PrivateMsg privateMsg, MsgSender sender){

        /*由于我巨菜的原因CQ码一直不会用...*/
        String privateMsgString = privateMsg.toString();
        if(privateMsgString.indexOf("CQ:image")>=0){
            Tlc.getMessageInImage(privateMsgString);
            System.out.println(" 来自: "+ Tlc.friendMap.get(privateMsg.getQQ())+"\n");
        }
        else
            /*LOG*/ /*在这里更快 写个缓存线程可以防止阻塞*/
            System.out.println("昵称: "+ Tlc.friendMap.get(privateMsg.getQQ())+" 消息为: "+privateMsg.getMsg()+"\n");


        if(Tlc.administrators.contains(privateMsg.getQQ()))
            sender.SENDER.sendPrivateMsg(privateMsg.getQQ(),"如你所愿莫得问题!！");

    }

}
