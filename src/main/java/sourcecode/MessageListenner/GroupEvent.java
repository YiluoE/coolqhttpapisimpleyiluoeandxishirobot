package sourcecode.MessageListenner;

import com.forte.qqrobot.anno.Listen;
import com.forte.qqrobot.beans.messages.msgget.GroupAddRequest;
import com.forte.qqrobot.beans.messages.msgget.GroupMemberReduce;
import com.forte.qqrobot.beans.messages.types.MsgGetTypes;
import com.forte.qqrobot.sender.MsgSender;
import sourcecode.Util.Tlc;

public class GroupEvent {

    // 入 群 申 请 /*如果必须输入验证信息且需要管理员确认时 群成员是无法邀请的,且管理员邀请收不到事件*/
    @Listen(MsgGetTypes.groupAddRequest)
    public void GroupAdd(GroupAddRequest groupAddRequest, MsgSender sender) {

        if (Tlc.managementGroupAry.contains(groupAddRequest.getGroup())) {

            sender.SENDER.sendGroupMsg(
                    groupAddRequest.getGroup(),
                    "\"" + sender.getPersonInfoByCode(groupAddRequest.getQQ()).getName() + "\"" + " " +
                            " 加入该群," + "大概是本群的第 " + String.valueOf(sender.GETTER.getGroupInfo(groupAddRequest.getGroup()).getMemberNum() + 1) + "个成员呢~"
            );

            // 自 动 审 批
            sender.SETTER.setGroupAddRequest(
                    groupAddRequest.getFlag(),
                    groupAddRequest.getRequestType(),
                    true,
                    "-v-"
            );

            String msg = groupAddRequest.getMsg().indexOf("邀请人")==-1?"G D X 有人通过搜索添加 进入你的群~":"通过群成员邀请进的群呢~";
            String groupAddRequestMsg = groupAddRequest.getMsg();

            for(String admin : Tlc.administrators){
                sender.SENDER.sendPrivateMsg(admin,msg+"\n"+"群: "+Tlc.groupMap.get(groupAddRequest.getGroup()));
                sender.SENDER.sendPrivateMsg(admin,groupAddRequestMsg);
            }

        }

    } // GroupAdd End

    // 群 成 员 减 少
    @Listen(MsgGetTypes.groupMemberReduce)
    public void GroupReduce(GroupMemberReduce groupMemberReduce, MsgSender sender) {

        if (Tlc.managementGroupAry.contains(groupMemberReduce.getGroup())) {
            sender.SENDER.sendGroupMsg(
                    groupMemberReduce.getGroup(),
                    "最终: " + "\"" + sender.getPersonInfoByCode(groupMemberReduce.getBeOperatedQQ()).getName() + "\"" + " 还是选择了离开..."
            );
        }

    }


}
