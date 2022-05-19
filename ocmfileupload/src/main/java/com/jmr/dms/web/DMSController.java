package com.jmr.dms.web;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.ArrayList;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.FilenameUtils;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.jmr.dms.pojo.DMSUploadRequestDto;
import com.jmr.dms.pojo.ResultDto;
import com.jmr.dms.service.DMSService;
import com.jmr.dms.util.Messages;

import okhttp3.Response;

@Controller
@RequestMapping(value = "/DMSChannel")
public class DMSController extends DMSAbstractController {
	@Autowired
	DMSService DMSservice;

	// testing added service
	@RequestMapping(value = "/test", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody ResponseEntity<ResultDto> gettest1(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		ResultDto resultDto = new ResultDto();
		try {
			resultDto.setErrorMsg(false);
			resultDto.setReturnCode(1);
			resultDto.setReturnData("SUCCESS");
			HttpStatus userStatus = HttpStatus.ACCEPTED;
			return new ResponseEntity<ResultDto>(resultDto, userStatus);
		} catch (Exception e) {
			resultDto.setErrorMsg(true);
			resultDto.setReturnCode(0);
			resultDto.setReturnData(null);
			return new ResponseEntity<ResultDto>(resultDto, HttpStatus.ACCEPTED);
		}
	}

	// Test DMS

	@RequestMapping(value = "/contents", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<ResultDto> DMSUpload(@RequestParam("file") MultipartFile uploadfile,
			@RequestParam("cif") String cifid) throws JSONException, IOException {
		String dmsUploadRequestDto = DMSservice.DMSUpload(uploadfile, cifid);
		ResultDto resultDto = new ResultDto();
		HttpStatus userStatus = HttpStatus.ACCEPTED;
		
		System.out.println("dmsUploadRequestDto: " + dmsUploadRequestDto);
		
		//JSONObject jsonObject = new JSONObject(dmsUploadRequestDto.body().string());
		
		//System.out.println("jsonObject: " + jsonObject);
		//String uploadFileId = jsonObject.getString("id");
		
		//System.out.println("uploadFileId: " + uploadFileId);
		
		if (dmsUploadRequestDto != null) {
			resultDto.setReturnMsg("File Uploaded Successfully");
			resultDto.setReturnCode(1);
			resultDto.setReturnData(dmsUploadRequestDto);
		}
		if (dmsUploadRequestDto == null) {
			resultDto.setErrorMsg(true);
			resultDto.setReturnCode(0);
		}
		return new ResponseEntity<ResultDto>(resultDto, userStatus);
	}

	final String EXTERNAL_FILE_PATH = "//FCUBS//OCMFileDownload//";  	//UAT Env Path
	//final String EXTERNAL_FILE_PATH = "//home//oracle//temp//download//"; 	//Dev Env Path
	//final String EXTERNAL_FILE_PATH = "D:/Users/callicoder/downloads/"; 	//Local Env Path
		
	@RequestMapping(value = "/filedownload", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<ResultDto> DMSFiledownload(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, @RequestParam("docRef") String docRef)
			throws ClientProtocolException, IOException {   
		
		String fileName = DMSservice.DMSFiledownload(httpServletResponse, docRef,EXTERNAL_FILE_PATH);
		
		System.out.println("DMSController Filename ==>"+fileName); 
		
		ResultDto resultDto = new ResultDto();
		
		HttpStatus userStatus = HttpStatus.ACCEPTED;
		if (fileName != null) {
			{
				resultDto.setErrorMsg(true);	
				resultDto.setReturnMsg("File Ready for download");
				resultDto.setReturnData(fileName);
				resultDto.setReturnCode(1);
			}
		}
		if (fileName == null) {
			resultDto.setErrorMsg(true);
			resultDto.setReturnCode(0);
			resultDto.setReturnMsg("Something went wrong while downloading, please try again..");
		}
		return new ResponseEntity<ResultDto>(resultDto, userStatus);
	}
	File file = null;
	@RequestMapping(value = "/download", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<ResultDto> excelDownload(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,@RequestParam("fileName") String fileName) 
	{
		
		System.out.println("Inside excelDownload method ==>");	
		ResultDto resultDto = new ResultDto();
		try {
			file = new File(EXTERNAL_FILE_PATH+fileName);
			
			System.out.println("File Destination:"+EXTERNAL_FILE_PATH+fileName);
			if(!file.exists()){
			resultDto.setErrorMsg(false);
			resultDto.setReturnCode(0);
			resultDto.setReturnData("Sorry, file not exist");  			
			}else{			
				
				System.out.println("File Name ==> "+file.getName());
				
				String mimeType= URLConnection.guessContentTypeFromName(file.getName());
				
				if(mimeType==null){
					System.out.println("mimetype is not detectable, will take default");
					mimeType = "application/octet-stream";
				}			
				System.out.println("mimetype : "+mimeType);
				System.out.println("file.getName() File name : "+fileName);
				String str = fileName;
				
				//String[] arrOfStr = str.split("_", 5);
				//System.out.println("arrOfStr[1]: "+arrOfStr[1]);
				
				//String ext = FilenameUtils.getExtension(fileName);
				httpServletResponse.setContentType(mimeType);     
				
		       	//httpServletResponse.setHeader("Content-Disposition", String.format("attachment; filename=\"" + arrOfStr[1]+"."+ext+"\""));
		       	
				httpServletResponse.setHeader("Content-Disposition", String.format("attachment; filename=\"" +fileName+"\""));
				httpServletResponse.setContentLength((int)file.length());
				InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
		        FileCopyUtils.copy(inputStream, httpServletResponse.getOutputStream());
		        resultDto.setErrorMsg(false);
				resultDto.setReturnCode(1);
				resultDto.setReturnData("File downloaded...");
			}
			HttpStatus userStatus = HttpStatus.ACCEPTED;	
			return new ResponseEntity<ResultDto>(resultDto, userStatus);
		} catch (Exception e) {
			resultDto.setErrorMsg(true);
			resultDto.setReturnCode(2);
			resultDto.setReturnData(null);
			return new ResponseEntity<ResultDto>(resultDto, HttpStatus.ACCEPTED);
			}
	
	}
}