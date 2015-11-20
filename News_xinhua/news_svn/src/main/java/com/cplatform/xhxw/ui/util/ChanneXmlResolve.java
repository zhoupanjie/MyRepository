package com.cplatform.xhxw.ui.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.db.dao.ChanneDao;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import android.util.Xml;

/**
 * 栏目XML解析
 */
public class ChanneXmlResolve {

    public static List<ChanneDao> doParse(InputStream in) {
        List<ChanneDao> list = null;
    	String tagName;
		XmlPullParser parser=Xml.newPullParser();
		ChanneDao tmp = null;
        String key = null;
		try {
			parser.setInput(in, "utf-8");
			int eventType=parser.getEventType();
			int order = 1;
			while(eventType!=XmlPullParser.END_DOCUMENT){
				switch(eventType){
				case XmlPullParser.START_DOCUMENT:
                    list = new ArrayList<ChanneDao>();
					 break;
				case XmlPullParser.START_TAG:
					tagName = parser.getName();
					if("dict".equals(tagName)){
                        tmp = new ChanneDao();
                        tmp.setListorder(order);
                        order++;
                        tmp.setUserId(Constants.DB_DEFAULT_USER_ID); // 默认用户
						LogUtil.i("<dict","dict开始");
					} else if("key".equals(tagName)){
                        key = parser.nextText();
						LogUtil.i("<key", key);
					} else if("string".equals(tagName)){
                        String value = parser.nextText();
                        setKeyValue(tmp, key, value);
                    }
					break;
				case XmlPullParser.END_TAG:
					if("dict".equals(parser.getName())){
						LogUtil.i("dict/>", "dict结束");
                        list.add(tmp);
					} else if("string".equals(parser.getName())){
                        LogUtil.i("string/>", "string结束");
                    }else if("string".equals(parser.getName())){
						LogUtil.i("string/>", "string结束");
					}
					break;
				}
				eventType=parser.next();
			}
			
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        return list;
    }

    private static void setKeyValue(ChanneDao item, String key, String value) {
        if ("ChannelID".endsWith(key)) {
            item.setChannelID(value);
        } else if ("ChannelName".endsWith(key)) {
            item.setChannelName(value);
        } else if ("show".endsWith(key)) {
            item.setShow(Integer.valueOf(value));
        }
    }
	
}
