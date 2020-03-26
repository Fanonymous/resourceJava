package com.nugget.common.utils;

//import com.nugget.config.InitService;

import java.util.Properties;

/**
 * 常量
 *
 * @author hb
 * @email 1247898199@qq.com
 * @date 2019-01-09
 */
public class Constant {
	/** 超级管理员ID */
	public static final int SUPER_ADMIN = 1;

 /** 数据权限过滤 */
	public static final String SQL_FILTER = "sql_filter";
    /**
     * 当前页码
     */
    public static final String PAGE = "page";
    /**
     * 每页显示记录数
     */
    public static final String LIMIT = "limit";
    /**
     * 排序字段
     */
    public static final String ORDER_FIELD = "sidx";
    /**
     * 排序方式
     */
    public static final String ORDER = "order";
    /**
     *  升序
     */
    public static final String ASC = "asc";

    /**
     * 部门
     */
    public static final Integer DEPART = 4;
    /**
    * 学校
    */

    public static final Integer SCHOOLDEPT = 3;

    public static final Integer UNITDEPT = 2;


   public static final String MENUNAME = "我的课堂";
    /**
    * 默认机构id
    */
    public static final Long DDEPTID=10001L;

    public static final String INIT_PASSOWORD = "123456";

    public static String BaseURL =  "http://192.168.1.238:8889/api/user/onlineNum";

    public static String ResourceURL =  "http://192.168.1.238:8090/moralDemo/";

//    public static String ResourceURL =  "http://192.168.8.48:8090/moralDemo/";

//    public static String BaseURL =  "http://192.168.7.183:8889/api/user/onlineNum";
	/**
	 * 菜单类型
	 *
	 * @author chenshun
	 * @email sunlightcs@gmail.com
	 * @date 2016年11月15日 下午1:24:29
	 */
    public enum MenuType {
        /**
         * 目录
         */
    	CATALOG("directory"),
        /**
         * 菜单
         */
        MENU("menu"),
        /**
         * 按钮
         */
        BUTTON("operate");

        private String value;

        MenuType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    //客户端更新方式
    public enum UpgradeMethod {

        FORCE(1),

        PATCH(2);

        private int value;

        private UpgradeMethod(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

    }

    //学生异动类型
    public enum stuChangeType{

        CHANGE_CLASS(1),

        CHANGE_GRADE(2),

        CHANGE_SCHOOL(3),

        CHANGE_PROBATION(4),

        CHANGE_STATUS(5);

        private int value;

        private stuChangeType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

    }

    //机构类型
    public enum deptType{

        PLATFORM_DEPT(1),

        EDU_DEPT(2),

        SCHOOL_DEPT(3),

        SECTION_DEPT(4),

        SERVICE_PROVIDER(5),

        DEFAULT_DEPT(6);

        private int value;

        private deptType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

    }

    /**
     * 定时任务状态
     *
     * @author chenshun
     * @email sunlightcs@gmail.com
     * @date 2016年12月3日 上午12:07:22
     */
    public enum ScheduleStatus {
        /**
         * 正常
         */
    	NORMAL(0),
        /**
         * 暂停
         */
    	PAUSE(1);

        private int value;

        ScheduleStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * 云服务商
     */
    public enum CloudService {
        /**
         * 七牛云
         */
        QINIU(1),
        /**
         * 阿里云
         */
        ALIYUN(2),
        /**
         * 腾讯云
         */
        QCLOUD(3);

        private int value;

        CloudService(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum UserType {

        /**
         * 平台管理员
         */
        ADMIN(1),

        /**
         * 教职工/教师
         */
        TEACHER(2),

        /**
         * 学生
         */
        STUDENT(3),

        /**
         * 教育局职工
         */
        EDUCATION_STAFF(4),

        /**
        *   游客
        */
         VISTIOR(7);

        private int value;

        private UserType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

    }

    /**
     * 字符串长度
     */
    public static final Integer LENGTH_ELEVEN  =11;//排序.联系方式输入框允许输入字符长度
    public static final Integer LENGTH_TWENTY  = 20;//名称输入框允许输入字符长度
    public static final Integer LENGTH_FIFTY = 50;//备注 & 简介输入框允许输入字符长度
    public static final Integer LENGTH_ONE_HUNDRED = 100;//标题 || 长一点儿的名称输入框允许输入字符长度
    public static final Integer LENGTH_FIVE_HUNDRED = 500;//内容输入框允许输入字符长度
    public static final Integer LENGTH_Nine = 9;//数字输入框允许输入字符长度

    /** 用户登录id标识 */
    public static final String LOGIN_USER_KEY = "LOGIN_USER_KEY";

    public static String SYS_FILE_URL = "file/";

    /** 系统文件存储路径 */
    public static String SYS_IMG_URL = "img/";

    /** 资源文件存储路径 */
    public static String RE_FILE_URL = "resources/";
    /** 菜单文件存储路径 */
    public static String MENU_FILE_URL = "menu/";

    /** 版本包存储录目录 */
    public static String DEPLOY_FILE_URL = "deploy/";

    /** pdf文件存储路径 */
    public static String PDF_FILE_URL = "pdf/";
//    public static final String SYS_IMG_URL = "/usr/local/ischoolFile/img/";

    /** 资源文件存储路径 */
    public static String FILE_URL = "E:/ischoolFile/";

    /** 系统错误EXCEL下载路径 */
    public static String EXPORT_ERROR_FILE_URL = "errorFile/";

    /** EXCEL导入数据错误 */
    public static final String EXCEL_HEADER_ERROR = "EXCEL第一行内容与系统模板不一致，请下载系统模板对照后重新导入。";

    /**
     * 文件类型不支持时的提示
     */
    public static String UNSUPPORT_FILE_TYPE_MSG = "只支持.xls 或 .xlsx 文件";

    /** EXCEL导入数据不可以超过2000条 */
    public static final String EXCEL_MAX_ERROR = "EXCEL文件条数错误:文件大于2000条！";

    /**
     * 学校
     */
    public static final String SCHOOL = "0";
    /**
     * 教育局
     */
    public static final String EDUCATION = "1";

    public static final String BRAND="品牌";

    public static final String FAULT="故障";

	 /**
     * 批量insert 大小
     */
    public static final int SAVEBACHNUM = 1000;

    /**
     * 课表节次
     */
    public static final int LESSON1 = 1;
    public static final int LESSON2 = 2;
    public static final int LESSON3 = 3;
    public static final int LESSON4 = 4;
    public static final int LESSON5 = 5;
    public static final int LESSON6 = 6;
    public static final int LESSON7 = 7;
    public static final int LESSON8 = 8;
    public static final int LESSON9 = 9;
    public static final int LESSON10 = 10;
    public static final int LESSON11 = 11;
    public static final int LESSON12 = 12;
    public static final int LESSON13 = 13;
    public static final int LESSON14 = 14;
    public static final int LESSON15 = 15;

    /**
     * 星期
     */
    public static final int WEEK1 = 1;
    public static final int WEEK2 = 2;
    public static final int WEEK3 = 3;
    public static final int WEEK4 = 4;
    public static final int WEEK5 = 5;
    public static final int WEEK6 = 6;
    public static final int WEEK7 = 7;
    //演示学校ID
    public static final int DEMO_DEPT_ID = 10003;
    //演示学校用户类型
    public static final int DEMO_DEPT_TYPE = 6;
    //演示学校名称
    public static final String DEMO_DEPT_NAME = "默认机构";
    //演示学校名称
    public static final long ADMIN_USER_ID = 100000000000000001L;

    public static final String SCHOOL_NAME = "学校";

    public static final String UNIT_NAME = "教育局";

    //鼠标键盘采集数据表
    public static final String HBASE_TABLE = "dwd_mk_schoolId";

    /**
     *多媒体教学应用水平（教学时长、交互度、多样性、多媒体教学率） 默认统计近7天数据
     */
    public static final int SCREEN_STARTTIME_DAY = -7;
    public static final int SCREEN_ENDTIME_DAY = -1;

    /**
     * 数据填报项数目
     */
    public static final int REPORT_ITEMS = 20;


    public static final int ADMIN_ID=1;
}
