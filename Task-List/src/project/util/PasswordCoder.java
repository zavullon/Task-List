package project.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordCoder
{
    public static String code(String password)
    {
        if(password == null) password = " ";
        String salt = "1a2b3c4d11jaL;';99832l/;;e,3245gj;8qv'jhg72d";
        int d = (int) ((double) salt.toCharArray()[password.length()] / password.length() % 1 * (salt.length() - 10));
        salt = salt.substring(d , d + (int) (((double) salt.toCharArray()[password.length()] /
                password.length() % 1 * password.length() *
                (salt.length() + d))) % (salt.length() - d - 5));
        password = password + salt;
        StringBuilder code = new StringBuilder();
        try
        {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] bytes = password.getBytes();
            byte[] digest = messageDigest.digest(messageDigest.digest(bytes));
            for(int i = 0 ; i < digest.length ; i++)
            {
                code.append(Integer.toHexString(0x0100 + (digest[i] & 0x00FF) + i).substring(1));
            }
        }
        catch(NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return code.toString();
    }
}
