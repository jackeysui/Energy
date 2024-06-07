package com.linyang.energy.utils.yunNanUtil;

import java.io.IOException;

/**
 * Created by Administrator on 20-5-7.
 */
public class SM4Test {

    public static void main(String[] args) throws IOException {

        String enCode1 = SM4Util.yunNanSm4Encode("GP0W8AIF8uuf7Go4", "12345678b0123456", "123456");
        System.out.println("SM4 CBC 加密1:\n" + enCode1);
        String deCode1 = SM4Util.yunNanSm4Decode("GP0W8AIF8uuf7Go4", "12345678b0123456", enCode1);
        System.out.println("SM4 CBC 解密1:\n" + deCode1);


        String enCode2 = SM4Util.yunNanSm4Encode("a10W8AI3duufbGo4", "AB345668b0c34567", "abcdef");
        System.out.println("SM4 CBC 加密2:\n" + enCode2);
        String deCode2 = SM4Util.yunNanSm4Decode("a10W8AI3duufbGo4", "AB345668b0c34567", enCode2);
        System.out.println("SM4 CBC 解密2:\n" + deCode2);

    }

}
