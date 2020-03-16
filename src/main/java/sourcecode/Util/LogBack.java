package sourcecode.Util;

import com.forte.qqrobot.log.LogLevel;
import com.forte.qqrobot.log.QQLogBack;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class LogBack implements QQLogBack {

        /*sorry java的枚举不会用...*/
        public static final HashMap<Short,String> levelMap = new HashMap<Short,String>(){
            {
                put((short) 0,"D");
                put((short) 1,"I");
                put((short) 2,"W");
                put((short) 3,"E");
            }
        };

    @Override
    public boolean onLog(String msg, LogLevel level, Throwable e) {

        msg = msg.replaceAll("\u001b\\[\\d+m", "");

        String log = "["+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"]";

        log += " " + levelMap.get((short)level.getLevel()) + " " + msg;

        System.out.println(log);

        return false; // false 抑制原版输出
    }


}
