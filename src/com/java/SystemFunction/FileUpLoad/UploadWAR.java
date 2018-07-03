package com.java.SystemFunction.FileUpLoad;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/UploadWAR")


public class UploadWAR extends FileUpload {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public File GetUploadPath(HttpServletRequest request,
			HttpServletResponse resp) {
		// TODO Auto-generated method stub
		return new File(GetWebAppsPath(request, resp));
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

        System.out.println("doPost");  
		super.doPost(request, resp);
	}
	
	

}
