package com.voole.utils.base;


import com.voole.utils.net.NetUtil;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;

public abstract class BaseParser {
	private InputStream stream = null;

	public void setUrl(String url) throws Exception {
		stream = NetUtil.getInstance().getStreamFromServerSyn(url,null);
		if (stream != null) {
			setInputStream(stream);
		}
	}

	public void setHttpsUrl(String url) throws Exception {
		stream = NetUtil.getInstance().getStreamFromServerSyn(url,null);
		if (stream != null) {
			setInputStream(stream);
		}
	}

	public InputStream getStream() {
		return this.stream;
	}

	public void setInputStream(InputStream inputStream) throws Exception {
		if (inputStream != null) {
			XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
			xmlPullParserFactory.setNamespaceAware(true);
			XmlPullParser xpp = xmlPullParserFactory.newPullParser();
			xpp.setInput(inputStream, "UTF-8");
			parse(xpp);
			inputStream.close();
			inputStream = null;
		}
	}
	public abstract void parse(XmlPullParser xpp) throws Exception;

}
