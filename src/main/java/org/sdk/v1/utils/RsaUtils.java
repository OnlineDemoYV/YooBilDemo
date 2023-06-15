package org.sdk.v1.utils;

import cn.hutool.core.codec.Base64;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author YooBil
 */
@Slf4j
public class RsaUtils {
    
    private static final String ALGORITHM = "RSA";
    private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    
    
    /**
     * BASE6 Encoder
     *
     * @param data data
     * @return encoded string
     */
    private static String encodeWithBase64(byte[] data) {
        return Base64.encode(data);
    }
    
    /**
     * BASE64 Decoder
     *
     * @param data string to decode
     * @return decoded content
     */
    public static byte[] decodeWithBase64(String data) {
        return Base64.decode(data);
    }
    
    
    /**
     * Load public or private keys
     *
     * @param key key string
     * @throws IOException load stream exception
     */
    public static byte[] formatRsaKeyStr(final String key) throws IOException {
        
        InputStream in = new ByteArrayInputStream(key.getBytes(DEFAULT_CHARSET));
        InputStreamReader isr = new InputStreamReader(in, DEFAULT_CHARSET);
        
        BufferedReader br = new BufferedReader(isr);
        String s = br.readLine();
        StringBuilder res = new StringBuilder();
        while (null != s) {
            if (!s.startsWith("-----")) {
                res.append(s);
            }
            s = br.readLine();
        }
        return decodeWithBase64(res.toString());
    }
    
    
    /**
     * Load private key from a file
     *
     * @param path file path
     */
    public static RSAPrivateKey loadPrivateKeyFromPath(String path) {
        
        try {
            String key = readFile(new File(path));
            return newRsaPrivateKey(key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Load a public key from a file
     *
     * @param path file path
     */
    public static RSAPublicKey loadPublicKeyFromPath(String path) {
        try {
            String key = readFile(new File(path));
            return newRsaPublicKey(key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    
    /**
     * Sign data with private key
     *
     * @param data       data to sign
     * @param privateKey private key
     * @return Signature
     */
    public static String signWithPrivateKey(byte[] data, RSAPrivateKey privateKey) {
        try {
            // load key first
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initSign(privateKey);
            signature.update(data);
            return encodeWithBase64(signature.sign());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    
    /**
     * Verify signature with public key
     *
     * @param data      Data to sign
     * @param sign      Signature to verify
     * @param publicKey public key
     * @return result
     */
    public static boolean verifyWithPublicKey(byte[] data, String sign, RSAPublicKey publicKey) {
        // 验签
        
        try {
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initVerify(publicKey);
            signature.update(data);
            // 验证签名
            return signature.verify(decodeWithBase64(sign));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
    }
    
    /**
     * Encrypt with public key
     *
     * @param str       string to encrypt
     * @param charset   char set
     * @param publicKey public key
     * @return encryption
     * @throws Exception all
     */
    public static String encryptWithPublicKey(String str, Charset charset, RSAPublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        
        byte[] data = str.getBytes(charset);
        return encodeWithBase64(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data, publicKey.getModulus().bitLength()));
    }
    
    
    /**
     *
     *
     * @param encryptStr encrypted string
     * @param charset    char set
     * @param privateKey private key path
     * @return decrypted string
     * @throws Exception all
     */
    public static String decryptWithPrivateKey(String encryptStr, Charset charset, RSAPrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        byte[] data = rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, decodeWithBase64(encryptStr), privateKey.getModulus().bitLength());
        return new String(data, charset);
    }
    
    private static byte[] rsaSplitCodec(Cipher cipher, int mode, byte[] data, int keySize) throws IOException {
        int maxBlock;
        if (mode == Cipher.DECRYPT_MODE) {
            maxBlock = keySize / 8;
        } else {
            maxBlock = keySize / 8 - 11;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] buff;
        int i = 0;
        try {
            while (data.length > offSet) {
                if (data.length - offSet > maxBlock) {
                    buff = cipher.doFinal(data, offSet, maxBlock);
                } else {
                    buff = cipher.doFinal(data, offSet, data.length - offSet);
                }
                out.write(buff, 0, buff.length);
                i++;
                offSet = i * maxBlock;
            }
        } catch (Exception e) {
            throw new RuntimeException("加解密区间为[" + maxBlock + "]的数据时发生异常", e);
        }
        byte[] resultData = out.toByteArray();
        out.close();
        return resultData;
    }
    
    
    /**
     * Read a file
     *
     * @throws IOException all
     */
    public static String readFile(File file) throws IOException {
        InputStream in = null;
        ByteArrayOutputStream out = null;
        try {
            in = Files.newInputStream(file.toPath());
            out = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
            out.flush();
            return out.toString();
        } finally {
            close(in);
            close(out);
        }
    }
    
    private static void close(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static RSAPublicKey newRsaPublicKey(String publicKeyStr) throws Exception {
        byte[] bytes = formatRsaKeyStr(publicKeyStr);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) keyFactory.generatePublic(new X509EncodedKeySpec(bytes));
    }
    
    
    public static RSAPrivateKey newRsaPrivateKey(String privateKeyStr) throws Exception {
        byte[] bytes = formatRsaKeyStr(privateKeyStr);
        java.security.Security.addProvider(new BouncyCastleProvider());
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(bytes);
        return (RSAPrivateKey) KeyFactory.getInstance(ALGORITHM).generatePrivate(spec);
    }
    
}
