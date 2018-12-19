package com.tarena.util;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;

//@Component("md5Util")
public class Md5Util {
	/**
	 * jdk方式
	 * @param file 源文件
	 * @return string  md5的值
	 */
	public static String getMd5(File file)throws Exception{
		String md5String=null;
		FileInputStream in=new FileInputStream(file);
		MappedByteBuffer byteBuffer=in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
		MessageDigest md5=MessageDigest.getInstance("MD5");
		md5.update(byteBuffer);
		BigInteger bi=new  BigInteger(1,md5.digest());
		md5String=bi.toString(16);
		in.close();
		return md5String;
	}
	/**
	 * commons codec方式
	 * @param path  文件的路径,字符串
	 * @return  md5的值
	 * @throws Exception
	 */
	public static String getMd5(String path)throws Exception{
		String md5String=null;
		FileInputStream is=new  FileInputStream(path);
		md5String=DigestUtils.md5Hex(IOUtils.toByteArray(is));
		IOUtils.closeQuietly(is);
		return md5String;
	}
	/**
	 * 加盐
	 * @param path  文件路径,字符串
	 * @param salt  盐
	 * @return md5的值
	 * @throws Exception
	 */
	public static String getMd5(String str,String salt)throws Exception{
		String md5String=null;
		MessageDigest md5=MessageDigest.getInstance("MD5");
		md5.update(str.getBytes());
		md5.update(salt.getBytes());
		byte[] digest=md5.digest();
		md5String=org.apache.commons.codec.binary.Hex.encodeHexString(digest);
		return md5String;
	}
	public static void main(String[] args) {
		try {
			System.out.println(Md5Util.getMd5(new File("c:/ks.mp4")));
			System.out.println(Md5Util.getMd5("c:/ks.mp4"));
			System.out.println(Md5Util.getMd5("123","admin"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
