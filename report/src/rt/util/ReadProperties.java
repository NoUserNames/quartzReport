package rt.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ReadProperties {

	public static void main(String[] args){
		TUtil.print(ReadProprety("rate.bv.report"));
	}
	
	/**
	 * 读取配置文件
	 * @param property 属性名
	 * @return 属性对应值
	 */
	public static String ReadProprety(String property){
		Properties pro = new Properties();
		try {
			String url = ReadProperties.class.getResource("/")+"config.properties";
			url = url.substring(url.indexOf("/")+1);
			pro.load(new FileInputStream(url));
			
			property=pro.getProperty(property);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			pro.clear();
		}
		return property;
	}
}