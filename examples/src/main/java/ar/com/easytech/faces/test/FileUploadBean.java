package ar.com.easytech.faces.test;

import java.io.File;
import java.io.Serializable;
import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class FileUploadBean implements Serializable  {

	private static final long serialVersionUID = 5302033264760254652L;
	private File file;

	public String doSubmit() {
		Logger.getLogger(FileUploadBean.class.toString()).info("File Name: " + file.getName());
		return "/upload/uploadImage";
	}
	
	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
	
}
