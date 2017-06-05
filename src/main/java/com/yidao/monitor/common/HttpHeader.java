package com.yidao.monitor.common;

import java.util.ArrayList;
import java.util.List;

public class HttpHeader {

	public static List<String> headerList;
	static{
		headerList = new ArrayList<String>();
		headerList.add("Accept");
		headerList.add("Accept-Charset");
		headerList.add("Accept-Encoding");
		headerList.add("Accept-Language");
		headerList.add("Accept-Ranges");
		headerList.add("Authorization");
		headerList.add("Cache-Control");
		headerList.add("Connection");
		headerList.add("Cookie");
		headerList.add("Content-Length");
		headerList.add("Content-Type");
		headerList.add("Date");
		headerList.add("Expect");
		headerList.add("From");
		headerList.add("Host");
		headerList.add("If-Match");
		headerList.add("If-Modified-Since");
		headerList.add("If-None-Match");
		headerList.add("If-Range");
		headerList.add("If-Unmodified-Since");
		headerList.add("Max-Forwards");
		headerList.add("Pragma");
		headerList.add("Proxy-Authorization");
		headerList.add("Range");
		headerList.add("Referer");
		headerList.add("TE");
		headerList.add("Upgrade");
		headerList.add("User-Agent");
		headerList.add("Via");
		headerList.add("Warning");
	}
}
