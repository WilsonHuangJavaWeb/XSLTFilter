package com.filter;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * Created by ki264 on 2017/6/26.
 */
@WebFilter(filterName = "XSLTFilter", urlPatterns = {"/msn/*"})
public class XSLTFilter implements Filter {

    private ServletContext servletContext;

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        System.out.println("Filter.....doFilter...");
        HttpServletRequest request = (HttpServletRequest) req;  //request物件
        HttpServletResponse response = (HttpServletResponse) resp;  //response物件

        //格式樣本檔案：/book.xsl
        Source styleSource = new StreamSource(servletContext.getRealPath("/book.xsl"));

        //請求的 XML 檔案
        Source xmlSource = new StreamSource(servletContext.getRealPath(
                request.getRequestURI().replace(request.getContextPath() + "", "")));

        try {
            //轉換器工廠
            TransformerFactory transformerFactory = TransformerFactory.newInstance();

            //轉換器
            Transformer transformer = transformerFactory.newTransformer(styleSource);

            //將轉換的結果儲存到該物件中
            CharArrayWriter charArrayWriter = new CharArrayWriter();    //快取記憶體writer
            StreamResult result = new StreamResult(charArrayWriter);    //輸出結果

            transformer.transform(xmlSource, result);   //轉換
            response.setContentType("text/html");   //輸出轉換後的結果
            response.setContentLength(charArrayWriter.toString().length()); //設定長度
            PrintWriter out = response.getWriter(); //獲得原來的response
            out.write(charArrayWriter.toString());  //輸出到用戶端

        } catch (Exception e) {
        }
    }

    public void init(FilterConfig config) throws ServletException {
        servletContext = config.getServletContext();
        System.out.println("Filter.....init....");
    }

}
