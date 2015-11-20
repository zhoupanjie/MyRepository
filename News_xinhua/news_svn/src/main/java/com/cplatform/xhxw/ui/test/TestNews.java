package com.cplatform.xhxw.ui.test;

import com.cplatform.xhxw.ui.model.Message;
import com.cplatform.xhxw.ui.model.New;
import com.cplatform.xhxw.ui.model.Photo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cy-love on 13-12-24.
 */
public class TestNews {

    public static List<New> news= new ArrayList<New>();
    static {
        for (int i = 0; i < 50; ++i)
        {
            New item = new New();
            item.setTitle("测试");
            item.setSummary("简介");
            news.add(item);
//            news.add(new New("http://img0.bdstatic.com/img/image/daren/sheying1.jpg","法新社2013年度最佳图片","法新社2013年度最佳图片"));
        }
    }

    public static List<Photo> photos= new ArrayList<Photo>();
    static {
        for (int i = 0; i < 50; ++i)
        {
            photos.add(new Photo("http://img0.bdstatic.com/img/image/daren/sheying1.jpg","法新社2013年度最佳图片","图集"));
        }
    }

    public static final String SpecialTopicRecoURL = "http://img0.bdstatic.com/img/image/daren/sheying1.jpg";

    public static List<Photo> homeSlider= new ArrayList<Photo>();
    static {
        for (int i = 0; i < 5; ++i)
        {
            homeSlider.add(new Photo("http://img0.bdstatic.com/img/image/daren/sheying1.jpg","法新社2013年度最佳图片","图集"));
        }
    }

    public static final String tilte = "{\"code\":0,\"msg\":\"ok\",\"data\":[{\"channelid\":27,\"channelname\":\"\\u70ab\\u95fb\",\"listorder\":0},{\"channelid\":43,\"channelname\":\"\\u65f6\\u653f\",\"listorder\":1},{\"channelid\":29,\"channelname\":\"\\u8d22\\u7ecf\",\"listorder\":2},{\"channelid\":30,\"channelname\":\"\\u5a31\\u4e50\",\"listorder\":3},{\"channelid\":32,\"channelname\":\"\\u793e\\u4f1a\",\"listorder\":4},{\"channelid\":35,\"channelname\":\"\\u519b\\u4e8b\",\"listorder\":5},{\"channelid\":34,\"channelname\":\"\\u4f53\\u80b2\",\"listorder\":6},{\"channelid\":37,\"channelname\":\"\\u70ab\\u56fe\",\"listorder\":7},{\"channelid\":39,\"channelname\":\"\\u79d1\\u6280\",\"listorder\":8},{\"channelid\":47,\"channelname\":\"\\u70ab\\u89c6\",\"listorder\":9},{\"channelid\":41,\"channelname\":\"\\u6c7d\\u8f66\",\"listorder\":10},{\"channelid\":40,\"channelname\":\"\\u623f\\u4ea7\",\"listorder\":11},{\"channelid\":48,\"channelname\":\"\\u70ab\\u89c6\\u91ce\",\"listorder\":12},{\"channelid\":33,\"channelname\":\"\\u6e2f\\u6fb3\\u53f0\",\"listorder\":13},{\"channelid\":38,\"channelname\":\"\\u6559\\u80b2\",\"listorder\":14},{\"channelid\":36,\"channelname\":\"\\u751f\\u6d3b\",\"listorder\":15},{\"channelid\":31,\"channelname\":\"\\u60a6\\u8bfb\",\"listorder\":16},{\"channelid\":28,\"channelname\":\"\\u89c2\\u70b9\",\"listorder\":17},{\"channelid\":42,\"channelname\":\"\\u8bbf\\u8c08\",\"listorder\":18},{\"channelid\":51,\"channelname\":\"\\u70ab\\u542c\",\"listorder\":51}]}";

    public static List<Message> messages= new ArrayList<Message>();
    static {
        for (int i = 0; i < 50; ++i)
        {
            Message  item = new Message();
            item.setTitle("测试");
            item.setSummary("简介");
            messages.add(item);
//            news.add(new New("http://img0.bdstatic.com/img/image/daren/sheying1.jpg","法新社2013年度最佳图片","法新社2013年度最佳图片"));
        }
    }
}
