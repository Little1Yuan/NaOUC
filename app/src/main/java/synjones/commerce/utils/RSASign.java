package synjones.commerce.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;

public class RSASign {
    private static String f16254a = "SHA1withRSA";
    private static String f16256c = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDil3PZhXSwuPZZ9M4oRARVCyvNjV6Ogw2ndE+1uIzt4SAsI5SlWwp3EL37es++FWd73lLX74h5NYxrzUo+yLPc1E0eqIw6MANPtd+mAFY+SsA2FdjJN91iNorqL+HKzYGzMn3yQ8BbiNWtX3L692smTRcVsimeeNm2/EdOAddrpQIDAQAB";
    private static String f16257d = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAOKXc9mFdLC49ln0zihEBFULK82NXo6DDad0T7W4jO3hICwjlKVbCncQvft6z74VZ3veUtfviHk1jGvNSj7Is9zUTR6ojDowA0+136YAVj5KwDYV2Mk33WI2iuov4crNgbMyffJDwFuI1a1fcvr3ayZNFxWyKZ542bb8R04B12ulAgMBAAECgYAwiEnq/DerJmK1j8acPz1CTds68p2fHpjNFg+Al5+vz7lJWvGanS5XpEFc3MgkKYd5s3vA/nAXrg1+hYDyg6BqM+lI6OHRZxPynaeQQeOiMDdTvR+pZ3b8uSx386riD1DM5d1oGRg9tMvJxQVCQU0bLrxViv2+KJf44dT3yqLJ3QJBAOwmsjpZi1BT+13IREjmdwEuTZRf7Ksffa1SvNyI+EoIpHZrM6V1w6vhRmJsENWe75qvJYy3hhem3i77O6z84oMCQQD1ow/fPTNxtM5qQKxMux3BEixp/j23ib+MIkoswmd+ARGtUEKqwnjXJWoXneXfWhMQTTJhBb5REwOEjOmv8IC3AkB09dlyMuVgHKgz07uWS6cHS7Ka2UOzoX4yePcXVzN6H3utNv02ZvRJzeJ5XsKbuwM7HqI/ZqogTsJejIoK7JkXAkAODxoudctG+8lArZju/1qxnT+rhWC0645qD+Bc9XeE77y6Rbi7G0xdTAfpeCEbCoXCzhhPE0wUSdlOsd4CMuq7AkBUD8WlEup0Ypa7oXTPGIdCOPZ+gxIWfEKBMAvi1paik+HIRfBdeaIPG1PyXrYDg8QWcZ3IrzMqLd9+jZ7XdYUF";
    public static String sign(HashMap<String, String> map) {
        String str = "";
        Object[] array = map.keySet().toArray();
        Arrays.sort(array);
        for (Object obj : array) {
            try {
                str = (obj.equals("request") || obj.equals("timestamp")) ? str + obj + URLDecoder.decode(map.get(obj), "UTF-8") : str + obj + map.get(obj);
            } catch (UnsupportedEncodingException e2) {
                e2.printStackTrace();
            }
        }
        System.out.println("sourcestr=" + str);
        String strB = new SignUtils().getPrivateKey(str, f16257d, f16254a);
        System.out.println("sign=" + strB);
        return strB;

    }
}
