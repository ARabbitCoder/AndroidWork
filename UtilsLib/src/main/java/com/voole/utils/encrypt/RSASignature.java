package com.voole.utils.encrypt;

/**
 * 
 */

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class RSASignature {

	/**
	 * 
	 * 解密
	 * 
	 * @param content
	 *            密文
	 * 
	 * @param key
	 *            商户私钥
	 * 
	 * @return 解密后的字符串
	 */
	public static String decrypt(String content, String key) throws Exception {
		PrivateKey prikey = getPrivateKey(key);

		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, prikey);

		InputStream ins = new ByteArrayInputStream(Base64.decode(content));
		ByteArrayOutputStream writer = new ByteArrayOutputStream();
		// rsa解密的字节大小最多是128，将需要解密的内容，按128位拆开解密
		byte[] buf = new byte[128];
		int bufl;

		while ((bufl = ins.read(buf)) != -1) {
			byte[] block = null;

			if (buf.length == bufl) {
				block = buf;
			} else {
				block = new byte[bufl];
				for (int i = 0; i < bufl; i++) {
					block[i] = buf[i];
				}
			}

			writer.write(cipher.doFinal(block));
		}

		return new String(writer.toByteArray(), "utf-8");
	}

	/**
	 * 
	 * 得到私钥
	 * 
	 * @param key
	 *            密钥字符串（经过base64编码）
	 * 
	 * @throws Exception
	 */

	public static PrivateKey getPrivateKey(String key) throws Exception {

		byte[] keyBytes;

		keyBytes = Base64.decode(key);

		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);

		KeyFactory keyFactory = KeyFactory.getInstance("RSA");

		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

		return privateKey;

	}

	public static final String SIGN_ALGORITHMS = "SHA1WithRSA";

	/**
	 * RSA签名
	 * 
	 * @param content
	 *            待签名数据
	 * @param privateKey
	 *            商户私钥
	 * @return 签名值
	 */
	public static String sign(String content, String privateKey) {
		String charset = "utf-8";
		try {
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
					Base64.decode(privateKey));
			KeyFactory keyf = KeyFactory.getInstance("RSA");
			PrivateKey priKey = keyf.generatePrivate(priPKCS8);

			java.security.Signature signature = java.security.Signature
					.getInstance(SIGN_ALGORITHMS);

			signature.initSign(priKey);
			signature.update(content.getBytes(charset));

			byte[] signed = signature.sign();

			return Base64.encode(signed);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * RSA验签名检查
	 * 
	 * @param content
	 *            待签名数据
	 * @param sign
	 *            签名值
	 * @param publicKey
	 *            支付宝公钥
	 * @return 布尔值
	 */
	public static boolean doCheck(String content, String sign, String publicKey) {
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			byte[] encodedKey = Base64.decode(publicKey);
			PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));

			java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);

			signature.initVerify(pubKey);
			signature.update(content.getBytes("utf-8"));

			boolean bverify = signature.verify(Base64.decode(sign));
			return bverify;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
	
	public static void main(String[] args) {
		String s = "notify_data=<notify><partner>2088901110181103</partner><discount>0.00</discount><payment_type>1</payment_type><SUBJECT>酷开优朋充值年卡</SUBJECT><trade_no>2013040224519216</trade_no><buyer_email>307590677@qq.com</buyer_email><gmt_create>2013-04-02 17:56:43</gmt_create><quantity>1</quantity><out_trade_no>1-18-26</out_trade_no><seller_id>2088901110181103</seller_id><trade_status>TRADE_FINISHED</trade_status><is_total_fee_adjust>N</is_total_fee_adjust><total_fee>0.01</total_fee><gmt_payment>2013-04-02 17:56:44</gmt_payment><seller_email>movie@coocaa.com</seller_email><gmt_close>2013-04-02 17:56:44</gmt_close><price>0.01</price><buyer_id>2088202867973161</buyer_id><use_coupon>N</use_coupon></notify>";
		String sign = "OxSXqLVW7HQaAT/buK85H2qoZUNGvx8NSh75AwNkrk61tfRCDj4d4BHHbJ8jUSAorZ5alj+90oL5gqTjPvEHi8VdM6PuU0x2ibjpw9jzuisI2ImZ4gnkcpkr3oXaMdYk2LshtB8U5Yl4gk8WcogL8+GuBo1e4q2lf5NKGucQWsY=";
		String key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCC/v+O1SIexMjfxcD328vQ9EcBwhu5NYkIIrcE gQiorimpd1LcoL1/+whZ1g4fvz0+LNtR3aFtYDp27+ZQ+8eAmEB";
		System.out.println(doCheck(s,sign,key));
	}

}
