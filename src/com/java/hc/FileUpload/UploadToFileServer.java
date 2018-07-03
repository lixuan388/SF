package com.java.hc.FileUpload;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



@WebServlet("/Upload/UploadToFileServer")


public class UploadToFileServer extends SupplierContract {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPost(request, resp);
	}

	@Override
	public String FilePathName() {
		// TODO Auto-generated method stub
		return "UploadFile";
	}

}
