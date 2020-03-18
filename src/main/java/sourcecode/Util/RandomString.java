package sourcecode.Util;

import java.util.Random;

public class RandomString {

    // [A-Z 65-90] [0-0 48-57]

    public static String get(int digit){

        String str = new String();

        for(int i = 0; i < digit; i++){

            /*为1字符 反之数字*/
            if(new Random().nextInt(2)!=0?true:false)
                str += (char)(new Random().nextInt(26)+65); // [65 ASCLL基值] [26 大写英文字符]
            else
                str += (char)(new Random().nextInt(10)+48);
        }


        return str;
    }

}
