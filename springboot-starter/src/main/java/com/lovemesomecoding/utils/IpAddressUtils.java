package com.lovemesomecoding.utils;

import java.math.BigInteger;
import java.net.UnknownHostException;

public final class IpAddressUtils {

	public static BigInteger getIpNumber(String ip) {
		java.math.BigInteger ipNumber = null;
		String paddingForIPV4 = "281470681743360";
		BigInteger offset = new BigInteger(paddingForIPV4);

		try {
			java.net.InetAddress ia = java.net.InetAddress.getByName(ip);
			byte byteArr[] = ia.getAddress();
			ipNumber = new java.math.BigInteger(1, byteArr);
			if (ia instanceof java.net.Inet4Address) {
				System.out.println("its ipv4");
				ipNumber = ipNumber.add(offset);
			} else if (ia instanceof java.net.Inet6Address) {
				System.out.println("its ipv6");
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ipNumber;
	}
}
