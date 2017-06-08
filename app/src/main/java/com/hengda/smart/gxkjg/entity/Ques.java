package com.hengda.smart.gxkjg.entity;

import java.util.List;

/**
 * Created by lenovo on 2017/1/16.
 */

public class Ques {


    /**
     * id : 3
     * ques_title : 企业员工满意度调查问卷
     * uid : 1
     * addtime : 2016-08-26 11:31:27
     * starttime : 2016-08-26 11:35:15
     * endtime : null
     * language : 1
     * desc : 企业员工满意度调查问卷
     * maxcount : 0
     * status : 1
     */

    private QuesBean ques;
    /**
     * tid : 10
     * quesid : 3
     * topic : 从总体而言，你对企业满意程度
     * option : [{"oid":"27","quesid":"3","tid":"10","option":"不满意"},{"oid":"28","quesid":"3","tid":"10","option":"选项3"},{"oid":"29","quesid":"3","tid":"10","option":"选项5"},{"oid":"30","quesid":"3","tid":"10","option":"满意"}]
     */

    private List<TopicBean> topic;

    public QuesBean getQues() {
        return ques;
    }

    public void setQues(QuesBean ques) {
        this.ques = ques;
    }

    public List<TopicBean> getTopic() {
        return topic;
    }

    public void setTopic(List<TopicBean> topic) {
        this.topic = topic;
    }

    public static class QuesBean {
        private String id;
        private String ques_title;
        private String uid;
        private String addtime;
        private String starttime;
        private Object endtime;
        private String language;
        private String desc;
        private String maxcount;
        private String status;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getQues_title() {
            return ques_title;
        }

        public void setQues_title(String ques_title) {
            this.ques_title = ques_title;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getStarttime() {
            return starttime;
        }

        public void setStarttime(String starttime) {
            this.starttime = starttime;
        }

        public Object getEndtime() {
            return endtime;
        }

        public void setEndtime(Object endtime) {
            this.endtime = endtime;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getMaxcount() {
            return maxcount;
        }

        public void setMaxcount(String maxcount) {
            this.maxcount = maxcount;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    public static class TopicBean {
        private String tid;
        private String quesid;
        private String topic;
        /**
         * oid : 27
         * quesid : 3
         * tid : 10
         * option : 不满意
         */

        private List<OptionBean> option;

        public String getTid() {
            return tid;
        }

        public void setTid(String tid) {
            this.tid = tid;
        }

        public String getQuesid() {
            return quesid;
        }

        public void setQuesid(String quesid) {
            this.quesid = quesid;
        }

        public String getTopic() {
            return topic;
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }

        public List<OptionBean> getOption() {
            return option;
        }

        public void setOption(List<OptionBean> option) {
            this.option = option;
        }

        public static class OptionBean {
            private String oid;
            private String quesid;
            private String tid;
            private String option;

            public String getOid() {
                return oid;
            }

            public void setOid(String oid) {
                this.oid = oid;
            }

            public String getQuesid() {
                return quesid;
            }

            public void setQuesid(String quesid) {
                this.quesid = quesid;
            }

            public String getTid() {
                return tid;
            }

            public void setTid(String tid) {
                this.tid = tid;
            }

            public String getOption() {
                return option;
            }

            public void setOption(String option) {
                this.option = option;
            }
        }
    }
}
