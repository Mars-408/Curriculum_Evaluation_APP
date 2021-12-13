package utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
    public static String getMD5Code(String strObj){
        String resultString = null;
        try{
            //加盐值的MD5
            resultString = new String(strObj+"businesssdafaqj23ou89ZXcj@#$@#$#@KJdjk1j;D../dSF.,");
            //生成一个MD5计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            //计算MD5函数
            md.update(strObj.getBytes());
            //md.digest() 该函数返回值为存放哈希值结果的byte数组，为8位字符串。
            //BigInteger函数将8位的字符串转换成16位hex值，用字符串来表示，得到字符串形式的哈希值
            resultString = new BigInteger(1, md.digest()).toString(16);
        }catch(NoSuchAlgorithmException ex){
            ex.printStackTrace();
        }
        return resultString;
    }

    public static void main(String[] args){
        System.out.println(getMD5Code("admin"));
    }
}
