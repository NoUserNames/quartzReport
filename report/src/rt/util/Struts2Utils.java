package rt.util;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

public class Struts2Utils extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4826137525915836840L;

	public static HttpServletRequest getRequest(){
		return ServletActionContext.getRequest();		
	}
	
	public static HttpServletResponse getResponse(){
		return ServletActionContext.getResponse();		
	}
	
	public static HttpSession getSession(){
		return ServletActionContext.getRequest().getSession();
	}
	
	public static ServletContext getServletContext(){
		return ServletActionContext.getServletContext();
	}
}
