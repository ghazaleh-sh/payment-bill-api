package ir.co.sadad.paymentBill.common;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.*;

public class TripleDES {

    private static String TRIPLE_DES_TRANSFORMATION = "DESede/ECB/PKCS5Padding";
    private static String ALGORITHM = "DESede";
    private static String BOUNCY_CASTLE_PROVIDER = "BC";
    private static final String UNICODE_FORMAT = "UTF8";
    public static final String PASSWORD_HASH_ALGORITHM = "SHA";

    /* To do : initialize bouncy castle provide
     *
     */
    private static void init() {
        Security.addProvider(new BouncyCastleProvider());
    }

    /*
     * To do : encrypt plaintext using 3Des algorithm
     */
    private static byte[] encode(byte[] input, byte[] key) throws IllegalBlockSizeException, BadPaddingException,
            NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException {
        init();
        Cipher encrypter = Cipher.getInstance(TRIPLE_DES_TRANSFORMATION, BOUNCY_CASTLE_PROVIDER);
        //hash key to sha, and init encrypter
        encrypter.init(Cipher.ENCRYPT_MODE, buildKey(key));
        //encrypt
        return encrypter.doFinal(input);
    }

    /*
     * To do : decrypt plaintext using 3Des algorithm
     */
    private static byte[] decode(byte[] input, byte[] key) throws IllegalBlockSizeException, BadPaddingException,
            NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException {
        init();
        Cipher decrypter = Cipher.getInstance(TRIPLE_DES_TRANSFORMATION, BOUNCY_CASTLE_PROVIDER);
        //hash key to sha, and init decrypter
        decrypter.init(Cipher.DECRYPT_MODE, buildKey(key));
        //decrypt
        return decrypter.doFinal(input);
    }

    /*
     *to do : string to byte , UTF-8 format
     */
    private static byte[] getByte(String string) throws UnsupportedEncodingException {
        return string.getBytes(UNICODE_FORMAT);
    }

    /*
     * to do : byte to String
     */
    private static String getString(byte[] byteText) {
        return new String(byteText);
    }

    /*
     * generate has key using SHA
     */
    private static Key buildKey(byte[] key) throws NoSuchAlgorithmException, UnsupportedEncodingException {
//    private static Key buildKey(char[] password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        init();
//        MessageDigest digester = MessageDigest.getInstance(PASSWORD_HASH_ALGORITHM);
//        digester.update(String.valueOf(password).getBytes(UNICODE_FORMAT));
//        byte[] key = digester.digest();

        //3des key using 24 byte, convert to 24 byte
//        byte[] keyDes = Arrays.copyOf(key, 24);
        SecretKeySpec spec = new SecretKeySpec(key, ALGORITHM);
        return spec;
    }

    /*
     * encrypt using 3 des
     */
    public static byte[] encrypt(String plainText, String workingkey) throws IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException {
        byte[] key = Base64.decode(workingkey);
        byte[] encryptedByte = TripleDES.encode(getByte(plainText), key);
        return Base64.encode(encryptedByte);
    }

    /*
     * decrypt using 3 des
     */
    public static String decrypt(String cipherText, String workingkey) throws IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, UnsupportedEncodingException {
        byte[] key = Base64.decode(workingkey);
        byte[] decryptedByte = TripleDES.decode(Base64.decode(cipherText.getBytes()), key);
        return getString(decryptedByte);
    }

    /*
     * generate has key using SHA
     */
    public byte[] generateSHA(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        init();
        MessageDigest digester = MessageDigest.getInstance(PASSWORD_HASH_ALGORITHM);
        digester.update(String.valueOf(password.toCharArray()).getBytes(UNICODE_FORMAT));
        byte[] key = digester.digest();
        return Base64.encode(key);
    }
}

/*
 * How to use:
 * String encryptedHex = TripleDES.encrypt(plainText, encryptionKey);
 * String decryptedHex = TripleDES.decrypt(plainText, encryptionKey);
 * */
