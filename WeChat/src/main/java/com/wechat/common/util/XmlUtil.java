package com.wechat.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class XmlUtil {

	public static <T> T XmlParse(InputStream in, T t) throws Exception {
		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read(in);

		Element root = document.getRootElement();
        System.out.println("Root: " + root.getName());
        Element foo;//二级节点 
        Field[] properties = t.getClass().getDeclaredFields();//获得实例的属性
        //实例的get方法
        Method getmeth;
        //实例的set方法
        Method setmeth;
    	t = (T)t.getClass().newInstance();//获得对象的新的实例
        for (Iterator i = root.elementIterator(); i.hasNext();) {
        	//遍历t.getClass().getSimpleName()节点
        	foo = (Element) i.next();//下一个二级节点

        	for (int j = 0; j < properties.length; j++) {
        		//遍历所有孙子节点
        		//实例的set方法 
        		String proName = properties[j].getName().substring(0, 1).toUpperCase()+ properties[j].getName().substring(1);
        		setmeth = t.getClass().getMethod("set"+ proName,properties[j].getType());
        		//properties[j].getType()为set方法入口参数的参数类型(Class类型)
        		
        		
        		if(!proName.toLowerCase().equals(foo.getName().toLowerCase())) {
        			continue;
        		}
        		Object setStr = foo.getStringValue(); 
        		if(properties[j].getType()==java.util.Date.class){
        			setStr = new Date((Long.parseLong((String) setStr)));
        		}
        		if(properties[j].getType()==int.class){
        	        if(foo.elementText(proName) != null && !"".equals(foo.elementText(proName))){
        	        	int intts = Integer.parseInt(foo.elementText(proName));
        	        	setStr = intts;
        	        }else{
        	        	setStr = 0;
        	        }
        	    }
        		setmeth.invoke(t, setStr);
        		break;
        	}
        }
		return t;
        
	}
	
	public static <T> String XmlGenerate(T obj) throws Exception {
		Document doc = DocumentHelper.createDocument();
		String rootname = "xml";
		Element root = doc.addElement(rootname);
		Field[] properties = obj.getClass().getDeclaredFields(); 
         
        
        for(Field field : properties) {
        	//反射get方法       
        	
        	Method meth = obj.getClass().getMethod("get" + field.getName().substring(0, 1).toUpperCase()+ field.getName().substring(1));   
        	String text;
        	if (field.getType() == java.util.Date.class) {
        		text = ((Date)meth.invoke(obj)).getTime()+"";
			} else if(field.getType() == int.class) {
				text = meth.invoke(obj).toString();
			}
        	else {
        		text = "<![CDATA[" + meth.invoke(obj).toString() + "]]>";
        	}
        	
        	root.addElement(field.getName().substring(0, 1).toUpperCase()+ field.getName().substring(1)).setText(text);   

        }   

        return formatXml(doc, "utf-8", false);
	}
	
	public static String formatXml(Document document, String charset, boolean istrans) { 
        OutputFormat format = OutputFormat.createPrettyPrint(); 
        format.setEncoding(charset); 
        StringWriter sw = new StringWriter(); 
        XMLWriter xw = new XMLWriter(sw, format); 
        xw.setEscapeText(istrans); 
        try { 
                xw.write(document); 
                xw.flush(); 
                xw.close(); 
        } catch (IOException e) { 
                System.out.println("格式化XML文档发生异常，请检查！"); 
                e.printStackTrace(); 
        } 
        
        return sw.toString(); 
	} 
}
