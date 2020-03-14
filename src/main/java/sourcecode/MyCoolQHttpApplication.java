package sourcecode;

import com.forte.component.forcoolqhttpapi.CoolQHttpApplication;
import com.forte.qqrobot.log.QQLogBack;

public class MyCoolQHttpApplication extends CoolQHttpApplication {

    public MyCoolQHttpApplication(QQLogBack qqLogBack){
        super(qqLogBack);
    }

    /* 用来去掉群主的彩色日志的因为它在Linux会乱码 */ /*不！我纠正一下在我的黑白控制台上乱码...*/
    @Override
    protected void _hello$() { System.out.println("\n"); }

}
