package filter;
import java.util.regex.Matcher; 
import java.util.regex.Pattern;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;


public class XSSFilter implements Filter {
	public String filter(String htmlStr){
		if(htmlStr == null) {
			return null;
		}
        String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式 
        String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式 
        String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式 
         
        Pattern p_script = Pattern.compile(regEx_script,Pattern.CASE_INSENSITIVE); 
        Matcher m_script = p_script.matcher(htmlStr); 
        htmlStr=m_script.replaceAll(""); // 过滤script标签 
         
        Pattern p_style=Pattern.compile(regEx_style,Pattern.CASE_INSENSITIVE); 
        Matcher m_style=p_style.matcher(htmlStr); 
        htmlStr=m_style.replaceAll(""); // 过滤style标签 
         
        Pattern p_html=Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE); 
        Matcher m_html=p_html.matcher(htmlStr); 
        htmlStr=m_html.replaceAll(""); // 过滤html标签 
 
        return htmlStr.trim(); // 返回文本字符串 
    } 
	/**
     * 一般使用ServletRequest对象获取表单提交的数据，
     * （主要通过 getParameter() 和 getParameterValues()
     * 方法获取），再此创建内部类Request,重写getParameter()
     * 和 getParameterValues()，并在重写的两个方法中实现过滤 
     */

    class Request extends HttpServletRequestWrapper{// HttpServletRequest                                                                      //Wrapper是servletRequest的实现类

        public Request(HttpServletRequest request) {
            super(request);
        }

        @Override
        public String getParameter(String name) {
            // 返回过滤后的参数值
            return filter(super.getRequest().getParameter(name));
        }

        @Override
        public String[] getParameterValues(String name) {
            // 获取所有参数值
            String[] values = super.getRequest().getParameterValues(name);
            // 通过循环对所有参数进行进行过滤
            for(int i=0;i<values.length;i++){
                values[i] = filter(values[i]);
            }
            return values;
        }

    }

    /**
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        /*if(encoding != null){
            request.setCharacterEncoding(encoding);
            //将request替换为重写后的request
            request = new Request((HttpServletRequest) request);
            response.setContentType("text/html; charset = "+encoding);
        }*/
    	request = new Request((HttpServletRequest) request);
        chain.doFilter(request, response);
    }

    /**
     * @see Filter#destroy()
     */
    public void destroy() {
    }

}
