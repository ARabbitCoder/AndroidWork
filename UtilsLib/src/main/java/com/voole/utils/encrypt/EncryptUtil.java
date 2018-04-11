package com.voole.utils.encrypt;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;


public class EncryptUtil {

	/********** 3DES加解密 Start ******************/
	private static final String Algorithm = "DES"; // 定义 加密算法,可用DES,DESede,Blowfish

	/**
	 * @param keybyte
	 *            加密密钥，长度为24字节
	 * @param src
	 *            被加密的数据缓冲区（源）
	 * @return
	 */
	public static byte[] encryptMode(byte[] keybyte, byte[] src) {
		try {
			// 生成密钥
			SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);

			// 加密
			Cipher c1 = Cipher.getInstance(Algorithm);
			c1.init(Cipher.ENCRYPT_MODE, deskey);
			return c1.doFinal(src);
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (NoSuchPaddingException e2) {
			e2.printStackTrace();
		} catch (Exception e3) {
			e3.printStackTrace();
		}
		return null;
	}

	/**
	 * @param keybyte
	 *            加密密钥，长度为24字节
	 * @param src
	 *            加密后的缓冲区
	 * @return
	 */
	public static byte[] decryptMode(byte[] keybyte, byte[] src) {
		try {
			// 生成密钥
			SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
			// 解密
			Cipher c1 = Cipher.getInstance(Algorithm);
			c1.init(Cipher.DECRYPT_MODE, deskey);
			return c1.doFinal(src);
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (NoSuchPaddingException e2) {
			e2.printStackTrace();
		} catch (Exception e3) {
			e3.printStackTrace();
		}
		return null;
	}

	/**
	 * 转换成十六进制字符串
	 *
	 * @param b
	 * @return
	 */
	public static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
			if (n < b.length - 1)
				hs = hs + ":";
		}
		return hs.toUpperCase();
	}

	/********** 3DES加解密 End ******************/

	public static String byte2HEX(byte b) {
		return ("" + "0123456789ABCDEF".charAt(0xf & b >> 4) + "0123456789ABCDEF".charAt(b & 0xF));
	}

	public static byte stringHEX2bytes(String str) {
		return (byte) ("0123456789ABCDEF".indexOf(str.substring(0, 1)) * 16 + "0123456789ABCDEF".indexOf(str
				.substring(1)));
	}

	public static String md5bytes2string(byte[] bytes) {
		String result = "";
		for (int i = 0; i < bytes.length; i++) {
			result += byte2HEX(bytes[i]);
		}
		return result;
	}

	public static byte[] md5string2bytes(String str) {
		byte[] b = new byte[str.length() / 2];
		for (int i = 0; i < b.length; i++) {
			String s = str.substring(i * 2, i * 2 + 2);
			b[i] = stringHEX2bytes(s);
		}
		return b;
	}

	public static boolean checkValidate(String vnetloginname, String token, String verifycode) {
		try {
			String seed = token + vnetloginname + "vnetvalidate";
			MessageDigest md = MessageDigest.getInstance("MD5");
			String mdresult = md5bytes2string(md.digest(seed.getBytes()));
			return mdresult.equalsIgnoreCase(verifycode);
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 兼容PHP加密算法
	 * 
	 * @param plainText
	 * @return
	 * @throws Exception
	 */
	public static byte[] desEncrypt(byte[] plainText, String key) {
		SecureRandom sr = new SecureRandom();
		try {
			DESKeySpec dks = new DESKeySpec(key.getBytes());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(Algorithm);//DES
			SecretKey sKey = keyFactory.generateSecret(dks);
			Cipher cipher = Cipher.getInstance(Algorithm);//DES
			cipher.init(Cipher.ENCRYPT_MODE, sKey, sr);
			byte data[] = plainText;
			byte encryptedData[] = cipher.doFinal(data);
			return encryptedData;
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 兼容PHP解密算法
	 * 
	 * @param encryptText
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] desDecrypt(byte[] encryptText, String key) {
		SecureRandom sr = new SecureRandom();
		try {
			DESKeySpec dks = new DESKeySpec(key.getBytes());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(Algorithm);//DES
			SecretKey sKey = keyFactory.generateSecret(dks);
			Cipher cipher = Cipher.getInstance(Algorithm);//DES
			cipher.init(Cipher.DECRYPT_MODE, sKey, sr);
			byte encryptedData[] = encryptText;
			byte decryptedData[] = decryptedData = cipher.doFinal(encryptedData);
			return decryptedData;
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 卡认证请求加密
	 * @param seqno
	 * @param createno
	 * @param key
	 * @return 
	 */
	public static String requestEncrypt(String seqno, String createno, String key) {
		String str = seqno + createno;
		String digest = SHA1Util.hex_sha1(str);
		String szSrc = seqno + '$' + createno + '$' + digest;
		try {
			byte[] encoded = desEncrypt(szSrc.getBytes(), key);
			return URLEncoder.encode(Base64.encode(encoded), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 卡认证响应信息批次号解密
	 * @param cardAuthResponseValue
	 * @param key
	 * @return
	 */
	public static String[] responseDecrypt(String cardAuthResponseValue, String key) {
		try {
//        	序列号：respno	必须
//        	卡号：cardno		必须
//			String urlDecoder = URLDecoder.decode(cardAuthResponseValue, "utf-8");
			byte[] baseDecoder = Base64.decode(cardAuthResponseValue);
			byte[] decoded = desDecrypt(baseDecoder, key);
			String responseValue = new String(decoded);
			String[] queryResult = responseValue.split("\\$");
			if (queryResult.length >= 3) {
				String digest = SHA1Util.hex_sha1(queryResult[0] + queryResult[1]);
				if ((digest).equalsIgnoreCase(queryResult[2])) {
					return queryResult;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 卡号信息解密
	 * @param cardno
	 * @param key
	 * @return
	 */
	public static String cardnoDecrypt(String cardno, String key) {
		try {
			byte[] baseDecoder = Base64.decode(cardno);
			byte[] decoded = desDecrypt(baseDecoder, key);
			String responseValue = new String(decoded);
			return responseValue;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		System.out.println(cardnoDecrypt("0KMW/cAh0Yx4l1mBAbw2og==", "UnionVooleCard2012666888"));
		System.out.println(cardnoDecrypt("BqERHJtgODbTbdAC3tqZNgBplYf0i/Io/BU+k8mb4hmh6x3eUWcTPNtau0Zpb2zXUTJ46eUpyVphAgA8/ZD2IUMxedDwB0KZ", "UnionVooleCard2012666888"));
		//111555103505366
		//10000101_20130403163507137$cc$21a196c632be50aa2cd54db0f8ae737d58a103cb
		String[] queryResult = "10000101_20130403163507137$cc$21a196c632be50aa2cd54db0f8ae737d58a103cb".split("\\$");
		String digest = SHA1Util.hex_sha1(queryResult[0] + queryResult[1]);
		System.out.println((digest).equalsIgnoreCase(queryResult[2]));
		System.out.println(MD5.getMD5ofStr(URLDecoder.decode("11836011113632820562吴景密20130407160840112skyworth_mobile_app_1.0", "gbk")));
		//60aa993baf2bc914f670098795828d31
		System.out.println(MD5.getMD5ofStr("11836011113632820562我咯20130407163826946skyworth_mobile_app_1.0"));
		System.out.println(URLEncoder.encode("吴景密", "utf-8"));
		
//		String uid="123456";
//		String oemid="333";
//		String hid="1234567890120000000000000000000000000000";
//		String newuid="654321";
//		String key="UnionVooleCard2012666888";
//
//		String str = uid + oemid + hid + newuid;
//		String digest = SHA1Util.hex_sha1(str);
//		
//		//String szSrc="0$a00123456$1315966912251$84ce97b8c103285739d281b40832cee64fb7c875";
//		String szSrc = uid + '$' + oemid + '$' + hid + '$' + newuid + "$" + digest;
//		
//		System.out.println(str + "--" + digest + "--" + szSrc);
//		byte[] keyBytes = key.getBytes();
//		try {
//			String input = Base64.encode(desEncrypt(szSrc.getBytes(), key));
//			System.out.println("新算法加密:" + input);
//			System.out.println("新算法解密:" + new String(desDecrypt(Base64.decode(input), key)));
//		} catch (Exception e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		System.out.println("加密前的字符串:" + szSrc);
//		byte[] encoded = encryptMode(keyBytes, szSrc.getBytes());
//		System.out.println("3DES加密字符串:" + new String(encoded));
//		String endTxt = "";
//		try {
//			endTxt = URLEncoder.encode(Base64.encode(encoded), "utf-8");
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println("加密后的字符串:" + endTxt);
//		System.out.println(str + "的SHA1的值为：" + SHA1Util.hex_sha1(str) + ", length=" + SHA1Util.hex_sha1(str).length());
//
//		try {
//			System.out.println("解密后的字符串:"
//					+ new String(decryptMode(keyBytes, Base64.decode(URLDecoder.decode(endTxt, "utf-8")))));
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}

}
