package com.java.SystemFunction.FileUpLoad;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;





@WebServlet("/FileUpload")


public class FileUpload extends HttpServlet {
	
	private static final long serialVersionUID = 3655349618159330684L;	
//    private File uploadPath;  
//    private File tempPath;

	private static final int MaxStoreSize=4*1024;
	private static final int MaxFileUploadExceptionSize=1024*1024*1024;
    
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(req, resp);
	}
	
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		

    	//使用sessionid + 文件名生成文件号

		resp.setHeader("Cache-Control", "no-cache");
		

		final String SessionID= request.getSession().getId();
        

        ProgressSingleton.put(SessionID + "Type", "doPost");
        ProgressSingleton.put(SessionID + "ContentLength", 0);
        ProgressSingleton.put(SessionID + "BytesRead", 0);     
        
        
           
        //获取根目录对应的真实物理路径  
        File uploadPath = GetUploadPath(request, resp);  
        //临时目录  
        File tempPath =GetTempPath(request, resp);

        //从item_upload.jsp中拿取数据，因为上传页的编码格式跟一般的不同，使用的是enctype="multipart/form-data"  
        //form提交采用multipart/form-data,无法采用req.getParameter()取得数据  
        //String itemNo = req.getParameter("itemNo");  
        //System.out.println("itemNo======" + itemNo);  
              
          
    /********************************使用 FileUpload 组件解析表单********************/  

        System.out.println("DiskFileItemFactory");  
        //DiskFileItemFactory：创建 FileItem 对象的工厂，在这个工厂类中可以配置内存缓冲区大小和存放临时文件的目录。  
        DiskFileItemFactory factory = new DiskFileItemFactory();  
        // maximum size that will be stored in memory  
        factory.setSizeThreshold(MaxStoreSize);  
        // the location for saving data that is larger than getSizeThreshold()  
        factory.setRepository(tempPath);  
          
        //ServletFileUpload：负责处理上传的文件数据，并将每部分的数据封装成一到 FileItem 对象中。  
        //在接收上传文件数据时，会将内容保存到内存缓存区中，如果文件内容超过了 DiskFileItemFactory 指定的缓冲区的大小，  
        //那么文件将被保存到磁盘上，存储为 DiskFileItemFactory 指定目录中的临时文件。  
        //等文件数据都接收完毕后，ServletUpload再从文件中将数据写入到上传文件目录下的文件中

        System.out.println("ServletFileUpload");  
        
        ServletFileUpload upload = new ServletFileUpload(factory);  
        // maximum size before a FileUploadException will be thrown  
        upload.setSizeMax(MaxFileUploadExceptionSize);        
        upload.setProgressListener(doProgressListener(request,resp,SessionID));
        
          
        /*******************************解析表单传递过来的数据，返回List集合数据-类型:FileItem***********/  
          
        try {                


            System.out.println("upload.parseRequest(request)");  
            List fileItems = upload.parseRequest(request);         
            System.out.println("upload.parseRequest(request) OK");       
            String itemNo = "";  
            

            
            

            for (Iterator iter = fileItems.iterator(); iter.hasNext();) {
                //获得序列中的下一个元素  
                FileItem item = (FileItem) iter.next();  
                //判断是文件还是文本信息  
                //是普通的表单输入域  
                if(item.isFormField()) {  
                    if ("itemNo".equals(item.getFieldName())) {  
                        itemNo = item.getString();  
                    }
                    doFormField(request, resp, item);            	    
                }  
                if (!item.isFormField()) {     
                	doFileUpload(request, resp, item,uploadPath,SessionID);
                }  
            }  

        } catch (Exception e) {  
            e.printStackTrace();  
        }     
        doUploadOK(request, resp);
	    resp.getWriter().flush();			
	    
	    
		
	}  
    
	
	public String GetWebAppsPath(HttpServletRequest request, HttpServletResponse resp)
	{
		return getServletContext().getRealPath("/").replace(request.getContextPath().replace("/", "")+"\\", "");
	}

    public File GetUploadPath(HttpServletRequest request, HttpServletResponse resp)
    {
    	String WebAppsPath=GetWebAppsPath(request,resp);
    	
        //获取根目录对应的真实物理路径  
        File uploadPath = new File(WebAppsPath+"UploadFile");  
        System.out.println("uploadPath:" + uploadPath);  
        //如果目录不存在  
        if (!uploadPath.exists()) {  
            //创建目录  
            uploadPath.mkdir();  
        }  
        return uploadPath;
    }
    
    public File GetTempPath(HttpServletRequest request, HttpServletResponse resp)
    {

    	String WebAppsPath=GetWebAppsPath(request,resp);
        //临时目录  
        //File tempFile = new File(item.getName())构造临时对象  
        File tempPath = new File(WebAppsPath+"UploadTempFile");  
        if (!tempPath.exists()) {  
            tempPath.mkdir();  
        }  
        return tempPath;
    }
    
    @Override
	protected void service(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.service(arg0, arg1);
	}


	@Override
	public void service(ServletRequest arg0, ServletResponse arg1)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.service(arg0, arg1);
	}


	public void doFormField(HttpServletRequest request, HttpServletResponse resp,FileItem item)
    {
        System.out.println("getFieldName:" + item.getFieldName()+":"+item.getString());  
    }
    public String doFileUpload(HttpServletRequest request, HttpServletResponse resp,FileItem item,File uploadPath,String Key) throws Exception
    {
        //上传文件的名称和完整路径  
        String fileName =GetLocalFileName(request, resp,item);                        
        long size = item.getSize();  
        //判断是否选择了文件  
        if ((fileName == null || fileName.equals("")) && size == 0) {  
            return "";  
        }  
        InputStream in = item.getInputStream();

        File file = new File(uploadPath, fileName);
        FileOutputStream out = new FileOutputStream(file);
        
        byte[] buffer = new byte[1024];
        int readNumber = 0;
        int Progress=0;
        while((readNumber = in.read(buffer)) != -1){
            //每读取一次，更新一次进度大小
        	Progress=Progress+readNumber;
            ProgressSingleton.put(Key + "Type", "Loacl");
            ProgressSingleton.put(Key + "ContentLength", size);
            ProgressSingleton.put(Key + "BytesRead", Progress);     
            out.write(buffer);
        }
        in.close();
        out.close();

        ProgressSingleton.put(Key + "Type", "success");
        System.out.println("upload ok ");
		return fileName;
    }
    
    public String GetLocalFileName(HttpServletRequest request, HttpServletResponse resp,FileItem item)
    {
        String fileName = item.getName();                        
        long size = item.getSize();  
        System.out.println("FileName:" + fileName+";FileSize:"+size);          
        fileName = fileName.substring(fileName.lastIndexOf("\\") + 1, fileName.length());
        return fileName;

    }
    

    public void doUploadOK(HttpServletRequest request, HttpServletResponse resp) throws IOException
    {

	    resp.getWriter().print("File Upload OK");
    }
    
    public ProgressListener doProgressListener(HttpServletRequest request, HttpServletResponse resp,final String Key) throws IOException
    {
    	ProgressListener p =new ProgressListener(){
        	
            public void update(long pBytesRead, long pContentLength, int arg2) {
//                System.out.println("文件大小为：" + pContentLength + ",当前已处理：" + pBytesRead);
                //向单例哈希表写入文件长度和初始进度
                ProgressSingleton.put(Key + "Type", "Temp");
                ProgressSingleton.put(Key + "ContentLength", pContentLength);
                ProgressSingleton.put(Key + "BytesRead", pBytesRead);     
            }
            
        };
        return p;
    }
    
}
