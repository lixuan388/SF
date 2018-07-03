package com.java.SystemFunction.FileUpLoad;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;


@WebServlet("/GetUploadProgress")

public class UploadProgressServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		response.setHeader("Cache-Control", "no-cache");
		response.setContentType("application/json;charset=utf-8");       

        String Key = request.getSession().getId();
        //使用sessionid + 文件名生成文件号，与上传的文件保持一致        
        Object Type = ProgressSingleton.get(Key + "Type");
        Type = Type == null ? "null" : Type;         
        Object size = ProgressSingleton.get(Key + "ContentLength");
        size = size == null ? 100 : size;
        Object progress = ProgressSingleton.get(Key + "BytesRead");        
        progress = progress == null ? 0 : progress; 
        
        JSONObject json = new JSONObject();
        json.put("type", Type);
        json.put("size", size);
        json.put("progress", progress);
        response.getWriter().print(json.toString());
        response.getWriter().flush();			
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(req, resp);
	}
	
	

}
