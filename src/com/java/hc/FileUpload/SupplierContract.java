package com.java.hc.FileUpload;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;

import com.java.SystemFunction.FileUpLoad.FileUpload;

import net.sf.json.JSONObject;


@WebServlet("/Upload/SupplierContract")


public class SupplierContract extends FileUpload {

	/**
	 * 
	 */

	private String LocalFileName;
	private String LocalPathName;
//	private static final String FilePathName="SupplierContract";
	private static final long serialVersionUID = 1L;

	@Override
	public File GetUploadPath(HttpServletRequest request,
			HttpServletResponse resp) {
		// TODO Auto-generated method stub
		File uploadPath=null;
		String UploadPath=getServletContext().getInitParameter("FileServerPath");
        System.out.println("FileServerPath:"+UploadPath);  
        if (UploadPath==null)
        {        	
        	String WebAppsPath=GetWebAppsPath(request,resp);
        	UploadPath = WebAppsPath+"UploadFile";  
            //获取根目录对应的真实物理路径  
            System.out.println("未配置默认路径！" + UploadPath);  
        }

        uploadPath = new File(UploadPath);  
        //如果目录不存在  
        if (!uploadPath.exists()) {  
            //创建目录  
            uploadPath.mkdir();  
        }  

        uploadPath = new File(UploadPath+"/"+FilePathName()+"/");  
        //如果目录不存在  
        if (!uploadPath.exists()) {  
            //创建目录  
            uploadPath.mkdir();  
        }  
        
        
        
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyyMMdd");   
		java.util.Date currentTime = new java.util.Date();   
		String str_date1 = formatter.format(currentTime);  
		
		LocalPathName="/"+FilePathName()+"/"+str_date1+"/";
		
		UploadPath=UploadPath+LocalPathName;		

        uploadPath = new File(UploadPath);  
		
        //如果目录不存在  
        if (!uploadPath.exists()) {  
            //创建目录  
            uploadPath.mkdir();  
        }  
        return uploadPath;
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPost(request, resp);
	}

	@Override
	public String GetLocalFileName(HttpServletRequest request, HttpServletResponse resp, FileItem item) {
		// TODO Auto-generated method stub
		String fileName = item.getName();    
        String ExeName = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());        
        UUID uuid = UUID.randomUUID();
        fileName= uuid.toString().replaceAll("-","").substring(0, 31)+"."+ExeName;		
        fileName=fileName.toUpperCase();
        LocalFileName=fileName;
		return fileName;
	}

	@Override
	public void doUploadOK(HttpServletRequest request, HttpServletResponse resp) throws IOException {
		// TODO Auto-generated method stub		
        JSONObject json = new JSONObject();
        json.put("FileName", LocalPathName+LocalFileName);
        json.put("MsgID", 1);
        resp.getWriter().print(json.toString());
        resp.getWriter().flush();	
	}
	
	public String FilePathName()
	{
		return "SupplierContract";
	}
	

}
