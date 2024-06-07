package com.linyang.energy.utils.yunNanUtil;

import com.linyang.energy.utils.StringUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.*;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class SM4Util {

    public static final String ALGORITHM_NAME = "SM4";
    public static final String ALGORITHM_NAME_ECB_PADDING = "SM4/ECB/PKCS5Padding";
    public static final String ALGORITHM_NAME_ECB_NOPADDING = "SM4/ECB/NoPadding";
    public static final String ALGORITHM_NAME_CBC_PADDING = "SM4/CBC/PKCS5Padding";
    public static final String ALGORITHM_NAME_CBC_NOPADDING = "SM4/CBC/NoPadding";

    static {
        Security.addProvider(new BouncyCastleProvider());
    }


    public static byte[] encrypt_Ecb_Padding(byte[] key, byte[] data)
            throws InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException,
            NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = generateEcbCipher(ALGORITHM_NAME_ECB_PADDING, Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    public static byte[] decrypt_Ecb_Padding(byte[] key, byte[] cipherText)
            throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException,
            NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException {
        Cipher cipher = generateEcbCipher(ALGORITHM_NAME_ECB_PADDING, Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(cipherText);
    }

    public static byte[] encrypt_Ecb_NoPadding(byte[] key, byte[] data)
            throws InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException,
            NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = generateEcbCipher(ALGORITHM_NAME_ECB_NOPADDING, Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    public static byte[] decrypt_Ecb_NoPadding(byte[] key, byte[] cipherText)
            throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException,
            NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException {
        Cipher cipher = generateEcbCipher(ALGORITHM_NAME_ECB_NOPADDING, Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(cipherText);
    }

    public static byte[] encrypt_Cbc_Padding(byte[] key, byte[] iv, byte[] data)
            throws InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException,
            NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException,
            InvalidAlgorithmParameterException {
        Cipher cipher = generateCbcCipher(ALGORITHM_NAME_CBC_PADDING, Cipher.ENCRYPT_MODE, key, iv);
        return cipher.doFinal(data);
    }

    public static byte[] decrypt_Cbc_Padding(byte[] key, byte[] iv, byte[] cipherText)
            throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException,
            NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException,
            InvalidAlgorithmParameterException {
        Cipher cipher = generateCbcCipher(ALGORITHM_NAME_CBC_PADDING, Cipher.DECRYPT_MODE, key, iv);
        return cipher.doFinal(cipherText);
    }

    public static byte[] encrypt_Cbc_NoPadding(byte[] key, byte[] iv, byte[] data)
            throws InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException,
            NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException,
            InvalidAlgorithmParameterException {
        Cipher cipher = generateCbcCipher(ALGORITHM_NAME_CBC_NOPADDING, Cipher.ENCRYPT_MODE, key, iv);
        return cipher.doFinal(data);
    }

    public static byte[] decrypt_Cbc_NoPadding(byte[] key, byte[] iv, byte[] cipherText)
            throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException,
            NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException,
            InvalidAlgorithmParameterException {
        Cipher cipher = generateCbcCipher(ALGORITHM_NAME_CBC_NOPADDING, Cipher.DECRYPT_MODE, key, iv);
        return cipher.doFinal(cipherText);
    }

    private static Cipher generateEcbCipher(String algorithmName, int mode, byte[] key)
            throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException,
            InvalidKeyException {
        Cipher cipher = Cipher.getInstance(algorithmName, BouncyCastleProvider.PROVIDER_NAME);
        Key sm4Key = new SecretKeySpec(key, ALGORITHM_NAME);
        cipher.init(mode, sm4Key);
        return cipher;
    }

    private static Cipher generateCbcCipher(String algorithmName, int mode, byte[] key, byte[] iv)
            throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException,
            NoSuchProviderException, NoSuchPaddingException {
        Cipher cipher = Cipher.getInstance(algorithmName, BouncyCastleProvider.PROVIDER_NAME);
        Key sm4Key = new SecretKeySpec(key, ALGORITHM_NAME);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(mode, sm4Key, ivParameterSpec);
        return cipher;
    }

    /** 云南平台SM4 CBC 加密 (对原始的PIN明文进行加密)*/
    public static String yunNanSm4Encode(String key, String iv, String pin){
        try {
            if(key.length() > 16){
                key = key.substring(0,16);
            }
            if(iv.length() > 16){
                iv = iv.substring(0,16);
            }
            //拼装pinBlock
            int pinLong = pin.length();
            if(pinLong > 14){
                return null;
            }
            String addBefore = String.valueOf(pinLong);
            if(pinLong < 10){
                addBefore = "0" + addBefore;
            }
            String addAfter = "";
            if(pinLong < 14){
                for(int i = 0; i < 14-pinLong; i++){
                    addAfter = addAfter + "F";
                }
            }
            String pinBlock = addBefore + pin + addAfter;
            //key、iv、pinBlock都转成16进制格式
            key = StringUtil.stringToHexString(key);
            iv =  StringUtil.stringToHexString(iv);
            pinBlock = StringUtil.stringToHexString(pinBlock);
            //再转成字节流
            byte[] keyByte = org.bouncycastle.pqc.math.linearalgebra.ByteUtils.fromHexString(key);
            byte[] ivByte = org.bouncycastle.pqc.math.linearalgebra.ByteUtils.fromHexString(iv);
            byte[] pinBlockByte = org.bouncycastle.pqc.math.linearalgebra.ByteUtils.fromHexString(pinBlock);
            //SM4 CBC
            pinBlockByte = encrypt_Cbc_NoPadding(keyByte, ivByte, pinBlockByte);
            return org.bouncycastle.pqc.math.linearalgebra.ByteUtils.toHexString(pinBlockByte);
        }
        catch (Exception ex){
            return null;
        }
    }

    /** 云南平台SM4 CBC 解密 (得到PINBLOCK明文) */
    public static String yunNanSm4Decode(String key, String iv, String enCode){
        try {
            if(key.length() > 16){
                key = key.substring(0,16);
            }
            if(iv.length() > 16){
                iv = iv.substring(0,16);
            }
            //key、iv转成16进制格式
            key = StringUtil.stringToHexString(key);
            iv =  StringUtil.stringToHexString(iv);
            //再转成字节流
            byte[] keyByte = org.bouncycastle.pqc.math.linearalgebra.ByteUtils.fromHexString(key);
            byte[] ivByte = org.bouncycastle.pqc.math.linearalgebra.ByteUtils.fromHexString(iv);
            byte[] enCodeByte = org.bouncycastle.pqc.math.linearalgebra.ByteUtils.fromHexString(enCode);
            //SM4 CBC 解密
            byte[] deCodeByte = decrypt_Cbc_NoPadding(keyByte, ivByte, enCodeByte);
            return new String(deCodeByte, "UTF-8");
        }
        catch (Exception ex){
            return null;
        }
    }

}