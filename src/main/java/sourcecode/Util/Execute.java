package sourcecode.Util;

import com.forte.qqrobot.anno.timetask.CronTask;
import com.forte.qqrobot.sender.MsgSender;
import com.forte.qqrobot.timetask.TimeJob;
import com.forte.qqrobot.utils.CQCodeUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@CronTask("0 0 5/5 * * ? ")
public class Execute implements TimeJob {

    int random = 0;
    String msg = null;
    Random randomNextInt = new Random();

    @Override
    public void execute(MsgSender msgSender, CQCodeUtil cqCodeUtil) {

        random = randomNextInt.nextInt(Tlc.randomMessage.size()+1);

        if (true){ /*方便测试*//*据说也是一种设计模式...我这里代码短体现不出来,甚至宛如一个zz*/
            if (random == Tlc.randomMessage.size())
                msg = "我爱你..但现在时间是:\n    "+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            else
                msg = Tlc.randomMessage.get(random);

            for(String admin : Tlc.administrators)
                msgSender.SENDER.sendGroupMsg(admin, msg);
        }
    }

}