package com.nugget.common.utils;

import java.io.*;
import java.util.*;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nugget.common.exception.RRException;
import com.nugget.common.fastdfs.utils.FastDFSUtils;
import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;

/**
 * 解析xml的工具类
 * 1、将多层级xml解析为Map
 * 2、将多层级xml解析为Json
 *
 * @author lmb
 *
 */
public class ParseXmlUtil {

    public static void main(String[] args) throws Exception {
        String xmlStr= readFile("D:/ADA/et/Issue_20130506_back.xml");
        Document doc= DocumentHelper.parseText(xmlStr);
        JSONObject json=new JSONObject();
        dom4j2Json(doc.getRootElement(),json);
        System.out.println("xml2Json:"+json.toString());

    }

    public static String readFile(String path) throws Exception {
        InputStream in= FastDFSUtils.downFile(path);

//        File file=new File(path);
//        FileInputStream fis = new FileInputStream(file);
//
//        FileChannel fc = fis.getChannel();
//        ByteBuffer bb = ByteBuffer.allocate(new Long(file.length()).intValue());
//        //fc向buffer中读入数据
//        fc.read(bb);
//        bb.flip();
//        String str=new String(bb.array(),"UTF8");
//        fc.close();
//        fis.close();


        StringBuffer   out   =   new   StringBuffer();
        byte[]   b   =   new   byte[4096];
        for   (int   n;   (n   =   in.read(b))   !=   -1;)   {
            out.append(new   String(b,   0,   n));
        }
        return   out.toString();

    }
    /**
     * xml转json
     * @param xmlStr
     * @return
     * @throws DocumentException
     */
    public static JSONObject xml2Json(String xmlStr) throws DocumentException{
        Document doc= DocumentHelper.parseText(xmlStr);
        JSONObject json=new JSONObject();
        dom4j2Json(doc.getRootElement(), json);
        return json;
    }

    /**
     * xml转json
     * @param element
     * @param json
     */
    public static void dom4j2Json(Element element,JSONObject json){
        //如果是属性
        for(Object o:element.attributes()){
            Attribute attr=(Attribute)o;
            if(!isEmpty(attr.getValue())){
                json.put("@"+attr.getName(), attr.getValue());
            }
        }
        List<Element> chdEl=element.elements();
        if(chdEl.isEmpty()&&!isEmpty(element.getText())){//如果没有子元素,只有一个值
            json.put(element.getName(), element.getText());
        }

        for(Element e:chdEl){//有子元素
            if(!e.elements().isEmpty()){//子元素也有子元素
                JSONObject chdjson=new JSONObject();
                dom4j2Json(e,chdjson);
                Object o=json.get(e.getName());
                if(o!=null){
                    JSONArray jsona=null;
                    if(o instanceof JSONObject ){//如果此元素已存在,则转为jsonArray
                        JSONObject jsono=(JSONObject)o;
                        json.remove(e.getName());
                        jsona=new JSONArray();
                        jsona.add(jsono);
                        jsona.add(chdjson);
                    }
                    if(o instanceof JSONArray){
                        jsona=(JSONArray)o;
                        jsona.add(chdjson);
                    }
                    json.put(e.getName(), jsona);
                }else{
                    if(!chdjson.isEmpty()){
                        json.put(e.getName(), chdjson);
                    }
                }


            }else{//子元素没有子元素
                for(Object o:element.attributes()){
                    Attribute attr=(Attribute)o;
                    if(!isEmpty(attr.getValue())){
                        json.put("@"+attr.getName(), attr.getValue());
                    }
                }
                if(!e.getText().isEmpty()){
                    json.put(e.getName(), e.getText());
                }
            }
        }
    }

    public static boolean isEmpty(String str) {

        if (str == null || str.trim().isEmpty() || "null".equals(str)) {
            return true;
        }
        return false;
    }




    public static void jsonToMap(JSONObject stObj, Map<String, Object> resultMap)  throws IOException {
        if(stObj==null){
            return;
        }

        Iterator it = stObj.keySet().iterator();
        while(it.hasNext()){
            String key = (String) it.next();
            //得到value的值
            Object value = stObj.get(key);
            //System.out.println(value);
            if(value instanceof JSONObject)
            {
                stObj.putAll((JSONObject)value);
                //递归遍历
                jsonToMap(stObj,resultMap);
            }
            else {
                resultMap.put(key, value);
            }
        }
    }





        /**
         * 生成菜单模板xml方法
         */
    public static void createXml(){
        try {

            // 5、设置生成xml的格式
            OutputFormat format = OutputFormat.createPrettyPrint();
            // 设置编码格式
//            format.setEncoding("UTF-8");
            format.setEncoding("utf-8");// 设置XML文件的编码格式
            format.setLineSeparator("\r\n");
            format.setTrimText(false);
            format.setIndent("  ");
            // 1、创建document对象
            Document document = DocumentHelper.createDocument();
            document.addComment("\r\n"+
                    "1. 必须填写内容不能为空，如果修改某个菜单，只需修改内容及层级关系即可，" +
                    "menuId不变就可以修改。menuId修改之后就视为新增菜单。"+
                    "\r\n"
            +"2. menuId必须填写，切在此文件内不允许重复，切必须为纯数字,不能设置值为1。"
            +"menuName，url，icon必填。type必填，值必须为directory（目录），menu(菜单)，operate(操作)这三种之一。"
            +"sort可不填，如上传过菜单图标，图标文件名称需与在此填写的icon内容一一对应。"+"\r\n"
            +"3. 除有值内容可删除修改，其他模块内容不能随意修改。"+"\r\n");
            // 2、创建根节点rss
            Element menuStore = document.addElement("menuStore");
            // 3、向节点添加version属性
//            rss.addAttribute("version", "2.0");
            // 4、生成子节点及子节点内容
            Element menu = menuStore.addElement("menu");

            Element menuId = menu.addElement("menuId");
            menuId.setText("10");
            Element menuName = menu.addElement("menuName");
            menuName.setText("test");
            Element url = menu.addElement("url");
            url.setText("test");
            Element type = menu.addElement("type");
            type.setText("directory");
            Element sort = menu.addElement("sort");
            sort.setText("1");
            Element icon = menu.addElement("icon");
            icon.setText("test.png");

            Element menu1 = menuStore.addElement("menu");

            Element menuId1 = menu1.addElement("menuId");
            menuId1.setText("100");
            Element menuName1 = menu1.addElement("menuName");
            menuName1.setText("test100");
            Element url1 = menu1.addElement("url");
            url1.setText("test100");
            Element type1 = menu1.addElement("type");
            type1.setText("directory");
            Element sort1 = menu1.addElement("sort");
            sort1.setText("10");
            Element icon1 = menu1.addElement("icon");
            icon1.setText("test100.png");

            Element menu2 = menuStore.addElement("menu");

            Element menuId2 = menu2.addElement("menuId");
            menuId2.setText("");

            //设置子节点
            Element children1 = menu.addElement("children");
            Element cmenuId = children1.addElement("menuId");
            cmenuId.setText("11");
            Element cmenuName = children1.addElement("menuName");
            cmenuName.setText("test11");
            Element curl = children1.addElement("url");
            curl.setText("test11");
            Element ctype = children1.addElement("type");
            ctype.setText("menu");
            Element csort = children1.addElement("sort");
            csort.setText("2");
            Element cicon = children1.addElement("icon");
            cicon.setText("test11.png");

            Element children2 = menu.addElement("children");
            Element cmenuId1 = children2.addElement("menuId");
            cmenuId1.setText("12");
            Element cmenuName1= children2.addElement("menuName");
            cmenuName1.setText("test12");
            Element curl1 = children2.addElement("url");
            curl1.setText("test12");
            Element ctype1 = children2.addElement("type");
            ctype1.setText("menu");
            Element csort1 = children2.addElement("sort");
            csort1.setText("3");
            Element cicon1 = children2.addElement("icon");
            cicon1.setText("test12.png");
            Element children3 = menu.addElement("children");
            Element cmenuId3 = children3.addElement("menuId");
            cmenuId1.setText("");

            // 6、生成xml文件
            File file = new File(Constant.SYS_FILE_URL+"menuDemo.XML");
            XMLWriter writer = new XMLWriter(new FileOutputStream(file), format);
            // 设置是否转义，默认使用转义字符
            writer.setEscapeText(false);
            writer.write(document);
            writer.close();
        } catch (Exception e) {
            throw new RRException("模板生成失败！");
        }
    }




}
