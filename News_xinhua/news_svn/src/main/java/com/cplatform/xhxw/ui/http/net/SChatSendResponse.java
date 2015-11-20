package com.cplatform.xhxw.ui.http.net;

import java.io.Serializable;

/**
 * S信发送解析
 */
public class SChatSendResponse extends BaseResponse {


    private SChatSendInfo data;

    public SChatSendInfo getData() {
        return data;
    }

    public void setData(SChatSendInfo data) {
        this.data = data;
    }

    public class SChatSendInfo implements Serializable {

        private String infoid;
        private String ctime;

        public String getInfoid() {
            return infoid;
        }

        public void setInfoid(String infoid) {
            this.infoid = infoid;
        }

        public String getCtime() {
            return ctime;
        }

        public void setCtime(String ctime) {
            this.ctime = ctime;
        }
    }
}
