package com.jmr.dms.service;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
//import java.util.Base64;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import sun.misc.BASE64Encoder;
import com.jmr.dms.pojo.DMSUploadRequestDto;

import fr.opensagres.xdocreport.core.io.internal.ByteArrayOutputStream;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedInputStream;

//new

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.hibernate.engine.jdbc.StreamUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import org.apache.http.Header;

@Service
public class DMSService {
	@PersistenceContext(unitName = "persistenceUnitNameOne")
	private EntityManager manager;
	public static String gFileName = "";
	File file;
	int maxFileSize = 5000000 * 1024;
	int maxMemSize = 5000000 * 1024;

	String filePath = "//FCUBS//OCMFileUpload//"; 	//UAT Env Path  
	//String filePath = "//home//oracle//temp//upload//";	//Dev Env Path
	//String filePath = "D:/Users/callicoder/uploads/";	//Local Env Path
	
	String accessCode = null;
	String documentID = null;
	String documentType = null;
	String documentsubType = null;
	String uploadDate = null;
	String loanType = null;
	String documentName = null;
	String CIF = "";
	String lang = "";
	String rowIndex = "";
	String fieldValueAl = null;
	String documentbase64String = null;
	String fileContent = "";
	byte[] byteData = new byte[32767];
	String queryFolderResponse = "";
	String createFolderResponse = "";
	String uploadFileResponse = "";
	String count = "";
	String errorCode = "";
	String folderName = "";
	String folderId = "";
	String errorMessage = "";
	String idValue = "";
	String version = "";
	boolean sucess = false;

	String strAccessCode = null;
	String webServiceResponse = null;
	String uploadStatus = "N";
	OutputStream fiOutputStream = null;

	// Upload file function

	public String DMSUpload(MultipartFile uploadfile, String cifid) throws JSONException, IOException {

		String token = getAuthToken();

		OkHttpClient client = new OkHttpClient().newBuilder()
				.readTimeout(90, TimeUnit.SECONDS)
				.writeTimeout(90, TimeUnit.SECONDS)
				.connectTimeout(90, TimeUnit.SECONDS)
				.build();

		/*
		 * OkHttpClient.Builder builder = new OkHttpClient.Builder();
		 * builder.connectTimeout(30, TimeUnit.SECONDS); builder.readTimeout(30,
		 * TimeUnit.SECONDS); builder.writeTimeout(30, TimeUnit.SECONDS); client
		 * = builder.build();
		 */

		MediaType mediaType = MediaType.parse("text/plain");
		byte[] content = uploadfile.getBytes();

		String documentName = uploadfile.getOriginalFilename();
		System.out.println("fileName ==> " + documentName);
		
		CIF = cifid.toString();
		String fileUpload = filePath + documentName;

		System.out.println("CIF ==> " + CIF);
		System.out.println("fileUpload ==> " + fileUpload);
		
		File file = new File(fileUpload);

		try (OutputStream os = new FileOutputStream(file)) {
			os.write(uploadfile.getBytes());
			file.setReadable(true);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		System.out.println("About to build Request Body");

		RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
				.addFormDataPart("file", documentName,
						RequestBody.create(MediaType.parse("application/octet-stream"), content))
				.addFormDataPart("item",
						"{\"name\":\"" + documentName + "\",\"type\":\"File\",\"description\":\"Document Uploaded For :"
								+ CIF + "\",\"repositoryId\":\"D5BB4D5F908945EFA8F8DB84C6A0DBE6\"}")
															   
				.build();

		// System.out.println("RequestBody ==> " +body);

		System.out.println("About to build Request ");

		Request request = new Request.Builder()
				//.url("https://nonprodocm1-waafibank.cec.ocp.oraclecloud.com/content/management/api/v1.1/items")
				.url("https://prodocm1-waafibank.cec.ocp.oraclecloud.com/content/management/api/v1.1/items")
				.method("POST", body).addHeader("X-Requested-With", "XMLHttpRequest")
				.addHeader("Authorization", "Bearer " + token).build();

		System.out.println("Request to post ==> " + request.toString());
		try {
			Response response = client.newCall(request).execute();
			//String strngconvresp = response.body().string();
			System.out.println("response ==> " + response);

			JSONObject jsonObject = new JSONObject(response.body().string());
			System.out.println("jsonObject: " + jsonObject);
			String uploadFileId = jsonObject.getString("id");
			System.out.println("uploadFileId: " + uploadFileId);
			return uploadFileId;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

		/*
		 * List<DMSUploadRequestDto> docRefNoList = new
		 * ArrayList<DMSUploadRequestDto>(); DMSUploadRequestDto obj = new
		 * DMSUploadRequestDto();
		 * 
		 * System.out.println("uploadfile" + uploadfile);
		 * System.out.println("cifid" + cifid);
		 * 
		 * documentName = uploadfile.getOriginalFilename(); CIF =
		 * cifid.toString();
		 * 
		 * String fileUpload = filePath + "/" + documentName; String dmsFolderId
		 * = getFolderId();
		 * 
		 * URI url = null; File file = new File(fileUpload);
		 * 
		 * try (OutputStream os = new FileOutputStream(file)) {
		 * os.write(uploadfile.getBytes()); } catch (FileNotFoundException e1) {
		 * // TODO Auto-generated catch block e1.printStackTrace(); } catch
		 * (IOException e1) { // TODO Auto-generated catch block
		 * e1.printStackTrace(); }
		 * 
		 * String responseBody = ""; System.out.println("gFileName: " +
		 * gFileName); try {
		 * 
		 * // url = new URI( //
		 * "https://conmgmtinstance-sagarkijbile02.cec.ocp.oraclecloud.com:443/documents/api/1.2/files/data"
		 * ); //url = new URI(
		 * "https://nonprodocm1-waafibank.cec.ocp.oraclecloud.com/documents/api/1.2/files/data"
		 * );
		 * 
		 * url = new URI(
		 * "https://nonprodocm1-waafibank.cec.ocp.oraclecloud.com/content/management/api/v1.1/items"
		 * );
		 * 
		 * String token = getAuthToken();
		 * 
		 * System.out.println("gFileName: " + gFileName);
		 * 
		 * CloseableHttpClient httpClient = HttpClients.createDefault();
		 * HttpPost postReq = new HttpPost(url);
		 * 
		 * // postReq.setHeader("Authorization", "Basic //
		 * ZG1zaW50ZWdyYXRpb25AY2FyaXRvcnNvbHV0aW9ucy5pbjpEbXNGY3ViQDMyMQ==");
		 * // postReq.setHeader("Authorization", "Basic //
		 * c2FnYXIua2lqYmlsZUB5YWhvby5jb206QWNjZW50dXJlQDAxMjM=");
		 * 
		 * // Bearer Authorization //postReq.setHeader("Authorization", //
		 * "Bearer eyJ4NXQjUzI1NiI6IktPelBlSWRaalJqQ05lT01EdkUxbExYTkxaM2RoaVZ1TFVCMm5KVWVWWUUiLCJ4NXQiOiJxM20xS002ay1PNF9iVjdLaGlsXzNfem0yRHMiLCJraWQiOiJTSUdOSU5HX0tFWSIsImFsZyI6IlJTMjU2In0.eyJzdWIiOiI4NTdkYzkxMzNjODg0YTk4ODJjNmQ1MDI3NDEyMGZhYiIsInNpZGxlIjo0ODAsImd0cCI6ImNjIiwidXNlci50ZW5hbnQubmFtZSI6ImlkY3MtNzA5NTE3ZDM2YThiNDRmN2FmNTk5ZjJiYzA2NWIzMzAiLCJvcGMiOmZhbHNlLCJzdWJfbWFwcGluZ2F0dHIiOiJ1c2VyTmFtZSIsInByaW1UZW5hbnQiOnRydWUsImlzcyI6Imh0dHBzOlwvXC9pZGVudGl0eS5vcmFjbGVjbG91ZC5jb21cLyIsInRva190eXBlIjoiQVQiLCJjbGllbnRfaWQiOiI4NTdkYzkxMzNjODg0YTk4ODJjNmQ1MDI3NDEyMGZhYiIsImNhX2d1aWQiOiJjYWNjdC05Y2FmZTM3NzFlZWQ0YjJjOWRlZGEwNjUyNTkwNDQzYSIsImF1ZCI6WyJodHRwczpcL1wvbm9ucHJvZG9jbTEtd2FhZmliYW5rLmNlYy5vY3Aub3JhY2xlY2xvdWQuY29tXC8iLCJodHRwczpcL1wvbm9ucHJvZG9jbTEtd2FhZmliYW5rLmNlYy5vY3Aub3JhY2xlY2xvdWQuY29tXC9kb2N1bWVudHNcL2ludGVncmF0aW9uXC9zb2NpYWwiLCJodHRwczpcL1wvbm9ucHJvZG9jbTEtd2FhZmliYW5rLmNlYy5vY3Aub3JhY2xlY2xvdWQuY29tXC9vc25cL3NvY2lhbFwvYXBpIiwidXJuOm9wYzpsYmFhczpsb2dpY2FsZ3VpZD1GQ0YzNzczN0ZGQ0I0QkI5OTRCODYzQ0VGMTdBQjI0NSJdLCJzdWJfdHlwZSI6ImNsaWVudCIsInNjb3BlIjoidXJuOm9wYzpjZWM6YWxsIiwiY2xpZW50X3RlbmFudG5hbWUiOiJpZGNzLTcwOTUxN2QzNmE4YjQ0ZjdhZjU5OWYyYmMwNjViMzMwIiwicmVnaW9uX25hbWUiOiJ1ay1sb25kb24taWRjcy0xIiwiZXhwIjoxNjQxMzY1NjY2LCJpYXQiOjE2NDA3NjA4NjYsImNsaWVudF9ndWlkIjoiMjllOThlOGJjODJmNDQ2ZmI5MTFkYTE0ZDhhMjY3M2MiLCJjbGllbnRfbmFtZSI6IkZDVUJTIE5vbi1Qcm9kIiwidGVuYW50X2lzcyI6Imh0dHBzOlwvXC9pZGNzLTcwOTUxN2QzNmE4YjQ0ZjdhZjU5OWYyYmMwNjViMzMwLmlkZW50aXR5Lm9yYWNsZWNsb3VkLmNvbTo0NDMiLCJ0ZW5hbnQiOiJpZGNzLTcwOTUxN2QzNmE4YjQ0ZjdhZjU5OWYyYmMwNjViMzMwIiwianRpIjoiMDM0NjEyZDUyM2FiNDUyNTlkMGNiYWY5NGU3NjQ0NmYiLCJyZXNvdXJjZV9hcHBfaWQiOiJkOTk1ZGZmMjM4YTM0ZjRhYjljMjU0ZDFkMGQxMTRjNCJ9.D1aXMI3Mnc0ZKzTeqOW97YZNmtXuRmTXuQh-dxr24P_NVYmwHJBZkX_wEhDrMO4A0XvdSRQV1bszrbG2nVzRkwXg5ws4RUCteAIpsyn9fDlsPJldvN-n5nuVHYB8Yvnj5brhu56Wy8BeIAuI0ENq4FhgadYqA3MwQvC7X9zpGFmdFcLdkoEloHQi7Z-nVA-3UswFJyIpm68rugrMt7tRA8tYPtX7qRtE5nnuaro_fb4m-6AwQy2CL0-RGGbnB1tiq2E6BZSraAa5GmjkgcDVRrlEf00wcitqQV4zgCxUeES8v568vNH4Plk6dUaDjfNf6c8ZN93X5H52vKrTmpmJ1w"
		 * );
		 * 
		 * postReq.setHeader("Authorization", "Bearer " + token);
		 * //postReq.addHeader("content-type",
		 * "multipart/form-data; boundary=---WebKitFormBoundarycgF8WZXpKAwjbiPS"
		 * ); postReq.addHeader("content-type", "application/json");
		 * 
		 * File f1 = null; String fileName = filePath + "/" + documentName;
		 * 
		 * System.out.println("fileName: " + fileName);
		 * System.out.println("fileUpload:" + fileUpload);
		 * 
		 * f1 = new File(fileUpload); FileInputStream inputStream = null; try {
		 * inputStream = new FileInputStream(fileUpload);
		 * 
		 * // inputStream=uploadfile.getInputStream(); gFileName = "";
		 * StringBuilder strngBuilder = new StringBuilder();
		 * 
		 * // Create a FileInputStream to read the file.
		 * 
		 * int data; // Read the entire file data. When -1 is returned it //
		 * means no more content to read. while ((data = inputStream.read()) !=
		 * -1) { strngBuilder.append((char) data); }
		 * 
		 * // Print the content of the file //
		 * System.out.println("File Contents = " + // strngBuilder.toString());
		 * 
		 * } catch (FileNotFoundException e2) { // TODO Auto-generated catch
		 * block e2.printStackTrace(); }
		 * 
		 * MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		 * 
		 * builder.setBoundary("-----WebKitFormBoundarycgF8WZXpKAwjbiPS");
		 * builder.addTextBody("jsonInputParameters", "{\"parentID\":\"" +
		 * dmsFolderId + "\"}");
		 * 
		 * System.out.println("builder: " + builder.toString());
		 * 
		 * builder.addBinaryBody("primaryFile", f1, ContentType.TEXT_PLAIN,
		 * f1.getName());
		 * 
		 * HttpEntity multipart = builder.build(); postReq.setEntity(multipart);
		 * 
		 * CloseableHttpResponse response1 = null; response1 =
		 * httpClient.execute(postReq);
		 * 
		 * System.out.println("response: " + response1.toString());
		 * 
		 * HttpEntity responseEntity = response1.getEntity();
		 * 
		 * System.out.println("responseEntity: " + responseEntity.toString());
		 * 
		 * responseBody = EntityUtils.toString(response1.getEntity());
		 * 
		 * } catch (Exception e) {
		 * 
		 * responseBody = "{\"errorCode\": \"-1\",\"errorMessage\": " +
		 * e.getMessage() +
		 * ",\"type\": \"https://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html\"}"
		 * ; e.printStackTrace(); }
		 * 
		 * // logger.info("Content Type"+contentType); return responseBody;
		 */

	}

	public String uploadFile(String dmsFolderId, String fileUpload, String filePath, String documentName) {
		URI url = null;
		String responseBody = "";
		System.out.println("gFileName: " + gFileName);
		try {

			// url = new URI(
			// "https://conmgmtinstance-sagarkijbile02.cec.ocp.oraclecloud.com:443/documents/api/1.2/files/data");

			//url = new URI("https://nonprodocm1-waafibank.cec.ocp.oraclecloud.com/documents/api/1.2/files/data");
			url = new URI("https://prodocm1-waafibank.cec.ocp.oraclecloud.com/documents/api/1.2/files/data");
			 
			String token = getAuthToken();

			System.out.println("gFileName: " + gFileName);
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpPost postReq = new HttpPost(url);

			// postReq.setHeader("Authorization", "Basic
			// ZG1zaW50ZWdyYXRpb25AY2FyaXRvcnNvbHV0aW9ucy5pbjpEbXNGY3ViQDMyMQ==");
			// postReq.setHeader("Authorization", "Basic
			// c2FnYXIua2lqYmlsZUB5YWhvby5jb206QWNjZW50dXJlQDAxMjM=");

			// postReq.setHeader("Authorization",
			// "Bearer
			// eyJ4NXQjUzI1NiI6IktPelBlSWRaalJqQ05lT01EdkUxbExYTkxaM2RoaVZ1TFVCMm5KVWVWWUUiLCJ4NXQiOiJxM20xS002ay1PNF9iVjdLaGlsXzNfem0yRHMiLCJraWQiOiJTSUdOSU5HX0tFWSIsImFsZyI6IlJTMjU2In0.eyJzdWIiOiI4NTdkYzkxMzNjODg0YTk4ODJjNmQ1MDI3NDEyMGZhYiIsInNpZGxlIjo0ODAsImd0cCI6ImNjIiwidXNlci50ZW5hbnQubmFtZSI6ImlkY3MtNzA5NTE3ZDM2YThiNDRmN2FmNTk5ZjJiYzA2NWIzMzAiLCJvcGMiOmZhbHNlLCJzdWJfbWFwcGluZ2F0dHIiOiJ1c2VyTmFtZSIsInByaW1UZW5hbnQiOnRydWUsImlzcyI6Imh0dHBzOlwvXC9pZGVudGl0eS5vcmFjbGVjbG91ZC5jb21cLyIsInRva190eXBlIjoiQVQiLCJjbGllbnRfaWQiOiI4NTdkYzkxMzNjODg0YTk4ODJjNmQ1MDI3NDEyMGZhYiIsImNhX2d1aWQiOiJjYWNjdC05Y2FmZTM3NzFlZWQ0YjJjOWRlZGEwNjUyNTkwNDQzYSIsImF1ZCI6WyJodHRwczpcL1wvbm9ucHJvZG9jbTEtd2FhZmliYW5rLmNlYy5vY3Aub3JhY2xlY2xvdWQuY29tXC8iLCJodHRwczpcL1wvbm9ucHJvZG9jbTEtd2FhZmliYW5rLmNlYy5vY3Aub3JhY2xlY2xvdWQuY29tXC9kb2N1bWVudHNcL2ludGVncmF0aW9uXC9zb2NpYWwiLCJodHRwczpcL1wvbm9ucHJvZG9jbTEtd2FhZmliYW5rLmNlYy5vY3Aub3JhY2xlY2xvdWQuY29tXC9vc25cL3NvY2lhbFwvYXBpIiwidXJuOm9wYzpsYmFhczpsb2dpY2FsZ3VpZD1GQ0YzNzczN0ZGQ0I0QkI5OTRCODYzQ0VGMTdBQjI0NSJdLCJzdWJfdHlwZSI6ImNsaWVudCIsInNjb3BlIjoidXJuOm9wYzpjZWM6YWxsIiwiY2xpZW50X3RlbmFudG5hbWUiOiJpZGNzLTcwOTUxN2QzNmE4YjQ0ZjdhZjU5OWYyYmMwNjViMzMwIiwicmVnaW9uX25hbWUiOiJ1ay1sb25kb24taWRjcy0xIiwiZXhwIjoxNjQxMzY1NjY2LCJpYXQiOjE2NDA3NjA4NjYsImNsaWVudF9ndWlkIjoiMjllOThlOGJjODJmNDQ2ZmI5MTFkYTE0ZDhhMjY3M2MiLCJjbGllbnRfbmFtZSI6IkZDVUJTIE5vbi1Qcm9kIiwidGVuYW50X2lzcyI6Imh0dHBzOlwvXC9pZGNzLTcwOTUxN2QzNmE4YjQ0ZjdhZjU5OWYyYmMwNjViMzMwLmlkZW50aXR5Lm9yYWNsZWNsb3VkLmNvbTo0NDMiLCJ0ZW5hbnQiOiJpZGNzLTcwOTUxN2QzNmE4YjQ0ZjdhZjU5OWYyYmMwNjViMzMwIiwianRpIjoiMDM0NjEyZDUyM2FiNDUyNTlkMGNiYWY5NGU3NjQ0NmYiLCJyZXNvdXJjZV9hcHBfaWQiOiJkOTk1ZGZmMjM4YTM0ZjRhYjljMjU0ZDFkMGQxMTRjNCJ9.D1aXMI3Mnc0ZKzTeqOW97YZNmtXuRmTXuQh-dxr24P_NVYmwHJBZkX_wEhDrMO4A0XvdSRQV1bszrbG2nVzRkwXg5ws4RUCteAIpsyn9fDlsPJldvN-n5nuVHYB8Yvnj5brhu56Wy8BeIAuI0ENq4FhgadYqA3MwQvC7X9zpGFmdFcLdkoEloHQi7Z-nVA-3UswFJyIpm68rugrMt7tRA8tYPtX7qRtE5nnuaro_fb4m-6AwQy2CL0-RGGbnB1tiq2E6BZSraAa5GmjkgcDVRrlEf00wcitqQV4zgCxUeES8v568vNH4Plk6dUaDjfNf6c8ZN93X5H52vKrTmpmJ1w");

			postReq.setHeader("Authorization", "Bearer " + token);
			postReq.addHeader("content-type", "multipart/form-data; boundary=---WebKitFormBoundarycgF8WZXpKAwjbiPS");

			File f1 = null;
			String fileName = filePath + documentName;

			System.out.println("fileName: " + fileName);
			System.out.println("fileUpload:" + fileUpload);

			f1 = new File(fileUpload);
			FileInputStream inputStream = null;
			try {
				inputStream = new FileInputStream(fileUpload);
				gFileName = "";
				StringBuilder strngBuilder = new StringBuilder();

				// Create a FileInputStream to read the file.

				int data;
				// Read the entire file data. When -1 is returned it
				// means no more content to read.
				while ((data = inputStream.read()) != -1) {
					strngBuilder.append((char) data);
				}

				// Print the content of the file
				// System.out.println("File Contents = " +
				// strngBuilder.toString());

			} catch (FileNotFoundException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}

			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.setBoundary("-----WebKitFormBoundarycgF8WZXpKAwjbiPS");
			builder.addTextBody("jsonInputParameters", "{\"parentID\":\"" + dmsFolderId + "\"}");

			builder.addBinaryBody("primaryFile", f1, ContentType.TEXT_PLAIN, f1.getName());

			HttpEntity multipart = builder.build();
			postReq.setEntity(multipart);

			CloseableHttpResponse response1 = null;
			response1 = httpClient.execute(postReq);

			System.out.println("response: " + response1.toString());

			HttpEntity responseEntity = response1.getEntity();

			System.out.println("responseEntity: " + responseEntity.toString());

			responseBody = EntityUtils.toString(response1.getEntity());

		} catch (Exception e) {

			responseBody = "{\"errorCode\": \"-1\",\"errorMessage\": " + e.getMessage()
					+ ",\"type\": \"https://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html\"}";
			e.printStackTrace();
		}
		return responseBody;
	}

	public String getValue(String result, String key, String separator, String endOperator, int occurrence) {
		String value = "";
		int posStartFrom = 0;
		int pos1 = 0, pos2 = 0;
		try {
			for (int i = 0; i < occurrence; i++) {
				posStartFrom = result.indexOf(key, posStartFrom + 1);
				System.out.println("posStartFrom: " + posStartFrom);
			}
			if (posStartFrom > 0) {

				pos1 = result.indexOf(key);
				pos2 = result.indexOf(separator, posStartFrom);
				int pos3 = result.indexOf(endOperator, posStartFrom);

				System.out.println("pos1: " + pos1 + " pos2: " + pos2 + " pos3:" + pos3);

				value = result.substring(pos2 + 1, pos3 - 1);
				value = value.replace("\"", "");
				value = value.replace(" ", "");
				System.out.println("value: " + value);
			} else {
				value = "";
			}
		} catch (Exception e) {
			value = "";
			e.printStackTrace();
		}
		return value;
	}

	public String getBoundary(String result) {
		String boundary = "";

		int pos1 = 0, pos2 = 0;

		result = "multipart/form-data; boundary=----WebKitFormBoundaryLqRsKSzg1iAWTWv3";

		pos1 = result.indexOf("boundary");

		boundary = result.substring(pos1 + "boundary".length() + 2);
		System.out.println("Boundary: " + boundary);

		return boundary;
	}

	// Call to Check if Folder Name exists
	public String queryFolder(String folderName) {
		String responseBody = null;
		try {

			// String URL =
			// "https://conmgmtinstance-sagarkijbile02.cec.ocp.oraclecloud.com/documents/api/1.1/folders/search/items?fulltext=";
			//String URL = "https://nonprodocm1-waafibank.cec.ocp.oraclecloud.com/documents/api/1.1/folders/search/items?fulltext=";
			String URL = "https://prodocm1-waafibank.cec.ocp.oraclecloud.com/documents/api/1.1/folders/search/items?fulltext=";

			String token = getAuthToken();

			URL = URL + folderName;
			System.out.println("URL:" + URL);
			URI url = null;
			try {
				url = new URI(URL);
			} catch (URISyntaxException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}

			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpGet httpGet = new HttpGet(url);

			// Basic Authorization
			// httpGet.setHeader("Authorization", "Basic
			// c2FnYXIua2lqYmlsZUB5YWhvby5jb206QWNjZW50dXJlQDAxMjM=");

			// Bearer Authorization
			// postReq.setHeader("Authorization",
			// "Bearer
			// eyJ4NXQjUzI1NiI6IktPelBlSWRaalJqQ05lT01EdkUxbExYTkxaM2RoaVZ1TFVCMm5KVWVWWUUiLCJ4NXQiOiJxM20xS002ay1PNF9iVjdLaGlsXzNfem0yRHMiLCJraWQiOiJTSUdOSU5HX0tFWSIsImFsZyI6IlJTMjU2In0.eyJzdWIiOiI4NTdkYzkxMzNjODg0YTk4ODJjNmQ1MDI3NDEyMGZhYiIsInNpZGxlIjo0ODAsImd0cCI6ImNjIiwidXNlci50ZW5hbnQubmFtZSI6ImlkY3MtNzA5NTE3ZDM2YThiNDRmN2FmNTk5ZjJiYzA2NWIzMzAiLCJvcGMiOmZhbHNlLCJzdWJfbWFwcGluZ2F0dHIiOiJ1c2VyTmFtZSIsInByaW1UZW5hbnQiOnRydWUsImlzcyI6Imh0dHBzOlwvXC9pZGVudGl0eS5vcmFjbGVjbG91ZC5jb21cLyIsInRva190eXBlIjoiQVQiLCJjbGllbnRfaWQiOiI4NTdkYzkxMzNjODg0YTk4ODJjNmQ1MDI3NDEyMGZhYiIsImNhX2d1aWQiOiJjYWNjdC05Y2FmZTM3NzFlZWQ0YjJjOWRlZGEwNjUyNTkwNDQzYSIsImF1ZCI6WyJodHRwczpcL1wvbm9ucHJvZG9jbTEtd2FhZmliYW5rLmNlYy5vY3Aub3JhY2xlY2xvdWQuY29tXC8iLCJodHRwczpcL1wvbm9ucHJvZG9jbTEtd2FhZmliYW5rLmNlYy5vY3Aub3JhY2xlY2xvdWQuY29tXC9kb2N1bWVudHNcL2ludGVncmF0aW9uXC9zb2NpYWwiLCJodHRwczpcL1wvbm9ucHJvZG9jbTEtd2FhZmliYW5rLmNlYy5vY3Aub3JhY2xlY2xvdWQuY29tXC9vc25cL3NvY2lhbFwvYXBpIiwidXJuOm9wYzpsYmFhczpsb2dpY2FsZ3VpZD1GQ0YzNzczN0ZGQ0I0QkI5OTRCODYzQ0VGMTdBQjI0NSJdLCJzdWJfdHlwZSI6ImNsaWVudCIsInNjb3BlIjoidXJuOm9wYzpjZWM6YWxsIiwiY2xpZW50X3RlbmFudG5hbWUiOiJpZGNzLTcwOTUxN2QzNmE4YjQ0ZjdhZjU5OWYyYmMwNjViMzMwIiwicmVnaW9uX25hbWUiOiJ1ay1sb25kb24taWRjcy0xIiwiZXhwIjoxNjQxMzY1NjY2LCJpYXQiOjE2NDA3NjA4NjYsImNsaWVudF9ndWlkIjoiMjllOThlOGJjODJmNDQ2ZmI5MTFkYTE0ZDhhMjY3M2MiLCJjbGllbnRfbmFtZSI6IkZDVUJTIE5vbi1Qcm9kIiwidGVuYW50X2lzcyI6Imh0dHBzOlwvXC9pZGNzLTcwOTUxN2QzNmE4YjQ0ZjdhZjU5OWYyYmMwNjViMzMwLmlkZW50aXR5Lm9yYWNsZWNsb3VkLmNvbTo0NDMiLCJ0ZW5hbnQiOiJpZGNzLTcwOTUxN2QzNmE4YjQ0ZjdhZjU5OWYyYmMwNjViMzMwIiwianRpIjoiMDM0NjEyZDUyM2FiNDUyNTlkMGNiYWY5NGU3NjQ0NmYiLCJyZXNvdXJjZV9hcHBfaWQiOiJkOTk1ZGZmMjM4YTM0ZjRhYjljMjU0ZDFkMGQxMTRjNCJ9.D1aXMI3Mnc0ZKzTeqOW97YZNmtXuRmTXuQh-dxr24P_NVYmwHJBZkX_wEhDrMO4A0XvdSRQV1bszrbG2nVzRkwXg5ws4RUCteAIpsyn9fDlsPJldvN-n5nuVHYB8Yvnj5brhu56Wy8BeIAuI0ENq4FhgadYqA3MwQvC7X9zpGFmdFcLdkoEloHQi7Z-nVA-3UswFJyIpm68rugrMt7tRA8tYPtX7qRtE5nnuaro_fb4m-6AwQy2CL0-RGGbnB1tiq2E6BZSraAa5GmjkgcDVRrlEf00wcitqQV4zgCxUeES8v568vNH4Plk6dUaDjfNf6c8ZN93X5H52vKrTmpmJ1w");

			httpGet.setHeader("Authorization", "Bearer " + token);

			CloseableHttpResponse httpResponse = httpClient.execute(httpGet);

			System.out.println("GET Response Status:: " + httpResponse.getStatusLine().getStatusCode());

			BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));

			System.out.println("httpResponse.toString(): " + httpResponse.toString());
			responseBody = EntityUtils.toString(httpResponse.getEntity());
			System.out.println("responseBody: " + responseBody);

			String count = getValue(responseBody, "count", ":", ",", 1);
			String errorCode = getValue(responseBody, "errorCode", ":", ",", 1);

			System.out.println("count: " + count);
			System.out.println("errorCode: " + errorCode);
			// Object obj = new JSONParser().parse(responseBody);
			// JSONObject jo = (JSONObject) obj;

			// String errorCode = (String) jo.get("errorCode");
			// String count = (String) jo.get("count");

			// JSONObject obj = new JSONObject(responseBody);
			// String errorCode = obj.getString("errorCode");

			// System.out.println("ErrorCode: " + errorCode);

		} catch (Exception e) {

			responseBody = "";
		}

		return responseBody;
	}

	public String createFolder(String folderName, String folderDescription) {

		String responseBody = "";
		String defaultLanguage = "en-US";

		try {

			URI url = null;

			// url = new URI(
			// "https://conmgmtinstance-sagarkijbile02.cec.ocp.oraclecloud.com/documents/api/1.2/folders/FE3152412A9293D69F3DCB6ADCF2A235037748A95F15");

			//url = new URI(
					//"https://nonprodocm1-waafibank.cec.ocp.oraclecloud.com/content/management/api/v1.1/repositories");

			url = new URI(
					"https://prodocm1-waafibank.cec.ocp.oraclecloud.com/content/management/api/v1.1/repositories");
			
			
			String token = getAuthToken();
			System.out.println("url: " + url);
			CloseableHttpClient httpClient = HttpClients.createDefault();
			System.out.println("httpClient BVB: " + httpClient.toString());

			// String json = "{\"name\":\"" + folderName + "\",\"" +
			// folder_description + "\":\"Sample Folder\"}";

			String json = "{\"name\":\"" + folderName + "\", \"description\" :\"" + folderDescription
					+ "\",  \"defaultLanguage\" :\"" + defaultLanguage + "\"}";
			System.out.println("json:" + json);

			StringEntity entity = new StringEntity(json);

			HttpPost postReq = new HttpPost(url);

			// postReq.setHeader("Authorization", "Basic
			// c2FnYXIua2lqYmlsZUB5YWhvby5jb206QWNjZW50dXJlQDAxMjM=");

			// Bearer Authorization
			// postReq.setHeader("Authorization",
			// "Bearer
			// eyJ4NXQjUzI1NiI6IktPelBlSWRaalJqQ05lT01EdkUxbExYTkxaM2RoaVZ1TFVCMm5KVWVWWUUiLCJ4NXQiOiJxM20xS002ay1PNF9iVjdLaGlsXzNfem0yRHMiLCJraWQiOiJTSUdOSU5HX0tFWSIsImFsZyI6IlJTMjU2In0.eyJzdWIiOiI4NTdkYzkxMzNjODg0YTk4ODJjNmQ1MDI3NDEyMGZhYiIsInNpZGxlIjo0ODAsImd0cCI6ImNjIiwidXNlci50ZW5hbnQubmFtZSI6ImlkY3MtNzA5NTE3ZDM2YThiNDRmN2FmNTk5ZjJiYzA2NWIzMzAiLCJvcGMiOmZhbHNlLCJzdWJfbWFwcGluZ2F0dHIiOiJ1c2VyTmFtZSIsInByaW1UZW5hbnQiOnRydWUsImlzcyI6Imh0dHBzOlwvXC9pZGVudGl0eS5vcmFjbGVjbG91ZC5jb21cLyIsInRva190eXBlIjoiQVQiLCJjbGllbnRfaWQiOiI4NTdkYzkxMzNjODg0YTk4ODJjNmQ1MDI3NDEyMGZhYiIsImNhX2d1aWQiOiJjYWNjdC05Y2FmZTM3NzFlZWQ0YjJjOWRlZGEwNjUyNTkwNDQzYSIsImF1ZCI6WyJodHRwczpcL1wvbm9ucHJvZG9jbTEtd2FhZmliYW5rLmNlYy5vY3Aub3JhY2xlY2xvdWQuY29tXC8iLCJodHRwczpcL1wvbm9ucHJvZG9jbTEtd2FhZmliYW5rLmNlYy5vY3Aub3JhY2xlY2xvdWQuY29tXC9kb2N1bWVudHNcL2ludGVncmF0aW9uXC9zb2NpYWwiLCJodHRwczpcL1wvbm9ucHJvZG9jbTEtd2FhZmliYW5rLmNlYy5vY3Aub3JhY2xlY2xvdWQuY29tXC9vc25cL3NvY2lhbFwvYXBpIiwidXJuOm9wYzpsYmFhczpsb2dpY2FsZ3VpZD1GQ0YzNzczN0ZGQ0I0QkI5OTRCODYzQ0VGMTdBQjI0NSJdLCJzdWJfdHlwZSI6ImNsaWVudCIsInNjb3BlIjoidXJuOm9wYzpjZWM6YWxsIiwiY2xpZW50X3RlbmFudG5hbWUiOiJpZGNzLTcwOTUxN2QzNmE4YjQ0ZjdhZjU5OWYyYmMwNjViMzMwIiwicmVnaW9uX25hbWUiOiJ1ay1sb25kb24taWRjcy0xIiwiZXhwIjoxNjQxMzY1NjY2LCJpYXQiOjE2NDA3NjA4NjYsImNsaWVudF9ndWlkIjoiMjllOThlOGJjODJmNDQ2ZmI5MTFkYTE0ZDhhMjY3M2MiLCJjbGllbnRfbmFtZSI6IkZDVUJTIE5vbi1Qcm9kIiwidGVuYW50X2lzcyI6Imh0dHBzOlwvXC9pZGNzLTcwOTUxN2QzNmE4YjQ0ZjdhZjU5OWYyYmMwNjViMzMwLmlkZW50aXR5Lm9yYWNsZWNsb3VkLmNvbTo0NDMiLCJ0ZW5hbnQiOiJpZGNzLTcwOTUxN2QzNmE4YjQ0ZjdhZjU5OWYyYmMwNjViMzMwIiwianRpIjoiMDM0NjEyZDUyM2FiNDUyNTlkMGNiYWY5NGU3NjQ0NmYiLCJyZXNvdXJjZV9hcHBfaWQiOiJkOTk1ZGZmMjM4YTM0ZjRhYjljMjU0ZDFkMGQxMTRjNCJ9.D1aXMI3Mnc0ZKzTeqOW97YZNmtXuRmTXuQh-dxr24P_NVYmwHJBZkX_wEhDrMO4A0XvdSRQV1bszrbG2nVzRkwXg5ws4RUCteAIpsyn9fDlsPJldvN-n5nuVHYB8Yvnj5brhu56Wy8BeIAuI0ENq4FhgadYqA3MwQvC7X9zpGFmdFcLdkoEloHQi7Z-nVA-3UswFJyIpm68rugrMt7tRA8tYPtX7qRtE5nnuaro_fb4m-6AwQy2CL0-RGGbnB1tiq2E6BZSraAa5GmjkgcDVRrlEf00wcitqQV4zgCxUeES8v568vNH4Plk6dUaDjfNf6c8ZN93X5H52vKrTmpmJ1w");

			postReq.setHeader("Authorization", "Bearer " + token);
			postReq.setEntity(entity);

			HttpResponse response = httpClient.execute(postReq);
			System.out.println("Response Code : " + response.getStatusLine().getStatusCode());

			responseBody = EntityUtils.toString(response.getEntity());
			System.out.println("responseBody: " + responseBody);

		} catch (Exception e) {
			responseBody = "";
			e.printStackTrace();
		}
		return responseBody;

	}

	public static String getFileExtension(String fileName) {
		// String fileName = file.getName();
		if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
			return fileName.substring(fileName.lastIndexOf(".") + 1);
		else
			return "";
	}

	public String getFolderId() {

		folderName = "IKYC_" + CIF;

		queryFolderResponse = queryFolder(folderName);

		count = getValue(queryFolderResponse, "count", ":", ",", 1);
		errorCode = getValue(queryFolderResponse, "errorCode", ":", ",", 1);

		System.out.println("count: " + count);
		System.out.println("Query Folder errorCode: " + errorCode);

		if (errorCode.equals("0")) {
			if (count.equals("0")) {
				System.out.println("No Folders Found. Going to Create New folder");
				// No Folders Found
				// Create a Folder here and get the folder ID
				// We will create under a specific Parent Folder
				// errorCode == 0 is Sucess and errorCode == -1 is failure

				createFolderResponse = createFolder(folderName, CIF);
				errorCode = getValue(createFolderResponse, "errorCode", ":", ",", 1);
				System.out.println("Create Folder Folder errorCode: " + errorCode);
				if (errorCode.equals("0")) {
					System.out.println("Getting Folder ID ");
					folderId = getValue(createFolderResponse, "id", ":", ",", 2);
					System.out.println("Folder ID is " + folderId);

				} else {

					errorMessage = getValue(createFolderResponse, "errorMessage", ":", ",", 1);
					System.out.println("Failed during creation of Folder with: " + errorMessage);
				}
			} else {
				// Found a Folder
				// Get the ID from the Response Body

				folderId = getValue(queryFolderResponse, "id", ":", ",", 4);
				System.out.println("Folder Exists previously with Folder ID: " + folderId);
			}
			System.out.println("folderId to be uploaded to is: " + folderId);

		}
		return folderId;
	}

	// Download File Source Code Started here
	/*
	 * public String DMSFiledownload(HttpServletRequest httpServletRequest,
	 * HttpServletResponse httpServletResponse, String dofRef) throws
	 * ClientProtocolException, IOException{
	 * 
	 * 
	 * String responseBody = "";
	 * 
	 * System.out.println("dofRef ==> " + dofRef);
	 * 
	 * String idValue = ""; String version = "";
	 * 
	 * System.out.println("Entered the DMSFiledownload function"); String
	 * filePat1h = "//home//oracle//temp//";
	 * 
	 * 
	 * // File file; int maxFileSize = 5000000 * 1024; int maxMemSize = 5000000
	 * * 1024;
	 * 
	 * 
	 * System.out.println("Getting the docId: " + dofRef);
	 * 
	 * //idValue = getDocValue(docId, 1);
	 * 
	 * idValue = dofRef;
	 * 
	 * //version = getDocValue(docId, 2);
	 * 
	 * String reponseContentDep = "";
	 * 
	 * String URL =
	 * "https://conmgmtinstance-sagarkijbile02.cec.ocp.oraclecloud.com:443/documents/api/1.2/files/";
	 * URL = URL + idValue + "/data";
	 * 
	 * URI url = null; try { url = new URI(URL); } catch (URISyntaxException e2)
	 * { // TODO Auto-generated catch block e2.printStackTrace(); }
	 * 
	 * CloseableHttpClient httpClient = HttpClients.createDefault(); HttpGet
	 * httpGet = new HttpGet(url); httpGet.setHeader("Authorization",
	 * "Basic c2FnYXIua2lqYmlsZUB5YWhvby5jb206QWNjZW50dXJlQDAxMjM=");
	 * 
	 * CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
	 * 
	 * System.out.println("GET Response Status:: " +
	 * httpResponse.getStatusLine().getStatusCode());
	 * 
	 * DataHandler fileData = new DataHandler( new
	 * ByteArrayDataSource(httpResponse.getEntity().getContent(),httpResponse.
	 * getEntity().getContentType().getValue()));
	 * 
	 * ByteArrayOutputStream output = new ByteArrayOutputStream();
	 * 
	 * fileData.writeTo(output);
	 * 
	 * byte[] content = output.toByteArray();
	 * 
	 * String contentType= httpResponse.getEntity().getContentType().getValue();
	 * 
	 * HttpEntity responseEntity = httpResponse.getEntity();
	 * 
	 * System.out.println("responseEntity: " + responseEntity.toString());
	 * 
	 * String url1=
	 * "https://conmgmtinstance-sagarkijbile02.cec.ocp.oraclecloud.com:443/documents/api/1.2/files/"
	 * +dofRef; CloseableHttpClient httpClient1 = HttpClients.createDefault();
	 * 
	 * HttpGet httpGet1 = new HttpGet(url1); httpGet1.setHeader("Authorization",
	 * "Basic c2FnYXIua2lqYmlsZUB5YWhvby5jb206QWNjZW50dXJlQDAxMjM=");
	 * 
	 * CloseableHttpResponse httpResponse1 = httpClient1.execute(httpGet1);
	 * HttpEntity responseEntity1 = httpResponse1.getEntity();
	 * 
	 * System.out.println("responseEntity: " + responseEntity1.toString());
	 * 
	 * String responseBody1 = EntityUtils.toString(httpResponse1.getEntity());
	 * JSONObject jsonObject = new JSONObject(responseBody1.toString()); String
	 * filename = jsonObject.getString("name");
	 * 
	 * System.out.println("filename: " + filename);
	 * 
	 * return responseBody1;
	 * 
	 * 
	 * }
	 */

	/*
	 * public String DMSFiledownload(HttpServletResponse httpServletResponse,
	 * String dofRef, String EXTERNAL_FILE_PATH) throws ClientProtocolException,
	 * IOException { System.out.println("Getting the docId: " + dofRef); dofRef
	 * = "CONTD00117D062B3443092B7A3D0CA26FCF8";
	 * 
	 * FileOutputStream fos = null;
	 * 
	 * // String URL = //
	 * "https://conmgmtinstance-sagarkijbile02.cec.ocp.oraclecloud.com:443/documents/api/1.2/files/";
	 * 
	 * String URL =
	 * "https://nonprodocm1-waafibank.cec.ocp.oraclecloud.com/content/management/api/v1.1/assets/";
	 * 
	 * //{id}/native //String URL =
	 * "https://nonprodocm1-waafibank.cec.ocp.oraclecloud.com/content/management/api/v1.1/items/";
	 * 
	 * String token = getAuthToken();
	 * 
	 * URL = URL + dofRef +"/native" ; URI url = null; try { url = new URI(URL);
	 * } catch (URISyntaxException e2) { // TODO Auto-generated catch block
	 * e2.printStackTrace(); }
	 * 
	 * CloseableHttpClient httpClient = HttpClients.createDefault(); HttpGet
	 * httpGet = new HttpGet(url);
	 * 
	 * // httpGet.setHeader("Authorization", "Basic //
	 * c2FnYXIua2lqYmlsZUB5YWhvby5jb206QWNjZW50dXJlQDAxMjM=");
	 * 
	 * // Bearer Authorization //postReq.setHeader("Authorization", //
	 * "Bearer eyJ4NXQjUzI1NiI6IktPelBlSWRaalJqQ05lT01EdkUxbExYTkxaM2RoaVZ1TFVCMm5KVWVWWUUiLCJ4NXQiOiJxM20xS002ay1PNF9iVjdLaGlsXzNfem0yRHMiLCJraWQiOiJTSUdOSU5HX0tFWSIsImFsZyI6IlJTMjU2In0.eyJzdWIiOiI4NTdkYzkxMzNjODg0YTk4ODJjNmQ1MDI3NDEyMGZhYiIsInNpZGxlIjo0ODAsImd0cCI6ImNjIiwidXNlci50ZW5hbnQubmFtZSI6ImlkY3MtNzA5NTE3ZDM2YThiNDRmN2FmNTk5ZjJiYzA2NWIzMzAiLCJvcGMiOmZhbHNlLCJzdWJfbWFwcGluZ2F0dHIiOiJ1c2VyTmFtZSIsInByaW1UZW5hbnQiOnRydWUsImlzcyI6Imh0dHBzOlwvXC9pZGVudGl0eS5vcmFjbGVjbG91ZC5jb21cLyIsInRva190eXBlIjoiQVQiLCJjbGllbnRfaWQiOiI4NTdkYzkxMzNjODg0YTk4ODJjNmQ1MDI3NDEyMGZhYiIsImNhX2d1aWQiOiJjYWNjdC05Y2FmZTM3NzFlZWQ0YjJjOWRlZGEwNjUyNTkwNDQzYSIsImF1ZCI6WyJodHRwczpcL1wvbm9ucHJvZG9jbTEtd2FhZmliYW5rLmNlYy5vY3Aub3JhY2xlY2xvdWQuY29tXC8iLCJodHRwczpcL1wvbm9ucHJvZG9jbTEtd2FhZmliYW5rLmNlYy5vY3Aub3JhY2xlY2xvdWQuY29tXC9kb2N1bWVudHNcL2ludGVncmF0aW9uXC9zb2NpYWwiLCJodHRwczpcL1wvbm9ucHJvZG9jbTEtd2FhZmliYW5rLmNlYy5vY3Aub3JhY2xlY2xvdWQuY29tXC9vc25cL3NvY2lhbFwvYXBpIiwidXJuOm9wYzpsYmFhczpsb2dpY2FsZ3VpZD1GQ0YzNzczN0ZGQ0I0QkI5OTRCODYzQ0VGMTdBQjI0NSJdLCJzdWJfdHlwZSI6ImNsaWVudCIsInNjb3BlIjoidXJuOm9wYzpjZWM6YWxsIiwiY2xpZW50X3RlbmFudG5hbWUiOiJpZGNzLTcwOTUxN2QzNmE4YjQ0ZjdhZjU5OWYyYmMwNjViMzMwIiwicmVnaW9uX25hbWUiOiJ1ay1sb25kb24taWRjcy0xIiwiZXhwIjoxNjQxMzY1NjY2LCJpYXQiOjE2NDA3NjA4NjYsImNsaWVudF9ndWlkIjoiMjllOThlOGJjODJmNDQ2ZmI5MTFkYTE0ZDhhMjY3M2MiLCJjbGllbnRfbmFtZSI6IkZDVUJTIE5vbi1Qcm9kIiwidGVuYW50X2lzcyI6Imh0dHBzOlwvXC9pZGNzLTcwOTUxN2QzNmE4YjQ0ZjdhZjU5OWYyYmMwNjViMzMwLmlkZW50aXR5Lm9yYWNsZWNsb3VkLmNvbTo0NDMiLCJ0ZW5hbnQiOiJpZGNzLTcwOTUxN2QzNmE4YjQ0ZjdhZjU5OWYyYmMwNjViMzMwIiwianRpIjoiMDM0NjEyZDUyM2FiNDUyNTlkMGNiYWY5NGU3NjQ0NmYiLCJyZXNvdXJjZV9hcHBfaWQiOiJkOTk1ZGZmMjM4YTM0ZjRhYjljMjU0ZDFkMGQxMTRjNCJ9.D1aXMI3Mnc0ZKzTeqOW97YZNmtXuRmTXuQh-dxr24P_NVYmwHJBZkX_wEhDrMO4A0XvdSRQV1bszrbG2nVzRkwXg5ws4RUCteAIpsyn9fDlsPJldvN-n5nuVHYB8Yvnj5brhu56Wy8BeIAuI0ENq4FhgadYqA3MwQvC7X9zpGFmdFcLdkoEloHQi7Z-nVA-3UswFJyIpm68rugrMt7tRA8tYPtX7qRtE5nnuaro_fb4m-6AwQy2CL0-RGGbnB1tiq2E6BZSraAa5GmjkgcDVRrlEf00wcitqQV4zgCxUeES8v568vNH4Plk6dUaDjfNf6c8ZN93X5H52vKrTmpmJ1w"
	 * );
	 * 
	 * httpGet.setHeader("Authorization", "Bearer " + token);
	 * CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
	 * 
	 * System.out.println("GET Response Status:: " +
	 * httpResponse.getStatusLine().getStatusCode());
	 * 
	 * ByteArrayOutputStream result = new ByteArrayOutputStream(); byte[] buffer
	 * = new byte[httpResponse.getEntity().getContent().available()];
	 * 
	 * int length = buffer.length;
	 * 
	 * System.out.println("Length:: " + length);
	 * 
	 * while ((length = httpResponse.getEntity().getContent().read(buffer)) !=
	 * -1) { System.out.println("getContent ==> :: " +
	 * httpResponse.getEntity().getContent().read(buffer)); result.write(buffer,
	 * 0, length); }
	 * 
	 * String imageStr = Base64.encodeBase64String(result.toByteArray()); byte[]
	 * image = Base64.decodeBase64(imageStr.getBytes()); try {
	 * 
	 * String url1 =
	 * "https://nonprodocm1-waafibank.cec.ocp.oraclecloud.com/content/management/api/v1.1/assets/"
	 * +dofRef+"/native";
	 * 
	 * CloseableHttpClient httpClient1 = HttpClients.createDefault(); HttpGet
	 * httpGet1 = new HttpGet(url1);
	 * 
	 * // httpGet1.setHeader("Authorization", "Basic //
	 * c2FnYXIua2lqYmlsZUB5YWhvby5jb206QWNjZW50dXJlQDAxMjM=");
	 * 
	 * // Bearer Authorization //postReq.setHeader("Authorization", //
	 * "Bearer eyJ4NXQjUzI1NiI6IktPelBlSWRaalJqQ05lT01EdkUxbExYTkxaM2RoaVZ1TFVCMm5KVWVWWUUiLCJ4NXQiOiJxM20xS002ay1PNF9iVjdLaGlsXzNfem0yRHMiLCJraWQiOiJTSUdOSU5HX0tFWSIsImFsZyI6IlJTMjU2In0.eyJzdWIiOiI4NTdkYzkxMzNjODg0YTk4ODJjNmQ1MDI3NDEyMGZhYiIsInNpZGxlIjo0ODAsImd0cCI6ImNjIiwidXNlci50ZW5hbnQubmFtZSI6ImlkY3MtNzA5NTE3ZDM2YThiNDRmN2FmNTk5ZjJiYzA2NWIzMzAiLCJvcGMiOmZhbHNlLCJzdWJfbWFwcGluZ2F0dHIiOiJ1c2VyTmFtZSIsInByaW1UZW5hbnQiOnRydWUsImlzcyI6Imh0dHBzOlwvXC9pZGVudGl0eS5vcmFjbGVjbG91ZC5jb21cLyIsInRva190eXBlIjoiQVQiLCJjbGllbnRfaWQiOiI4NTdkYzkxMzNjODg0YTk4ODJjNmQ1MDI3NDEyMGZhYiIsImNhX2d1aWQiOiJjYWNjdC05Y2FmZTM3NzFlZWQ0YjJjOWRlZGEwNjUyNTkwNDQzYSIsImF1ZCI6WyJodHRwczpcL1wvbm9ucHJvZG9jbTEtd2FhZmliYW5rLmNlYy5vY3Aub3JhY2xlY2xvdWQuY29tXC8iLCJodHRwczpcL1wvbm9ucHJvZG9jbTEtd2FhZmliYW5rLmNlYy5vY3Aub3JhY2xlY2xvdWQuY29tXC9kb2N1bWVudHNcL2ludGVncmF0aW9uXC9zb2NpYWwiLCJodHRwczpcL1wvbm9ucHJvZG9jbTEtd2FhZmliYW5rLmNlYy5vY3Aub3JhY2xlY2xvdWQuY29tXC9vc25cL3NvY2lhbFwvYXBpIiwidXJuOm9wYzpsYmFhczpsb2dpY2FsZ3VpZD1GQ0YzNzczN0ZGQ0I0QkI5OTRCODYzQ0VGMTdBQjI0NSJdLCJzdWJfdHlwZSI6ImNsaWVudCIsInNjb3BlIjoidXJuOm9wYzpjZWM6YWxsIiwiY2xpZW50X3RlbmFudG5hbWUiOiJpZGNzLTcwOTUxN2QzNmE4YjQ0ZjdhZjU5OWYyYmMwNjViMzMwIiwicmVnaW9uX25hbWUiOiJ1ay1sb25kb24taWRjcy0xIiwiZXhwIjoxNjQxMzY1NjY2LCJpYXQiOjE2NDA3NjA4NjYsImNsaWVudF9ndWlkIjoiMjllOThlOGJjODJmNDQ2ZmI5MTFkYTE0ZDhhMjY3M2MiLCJjbGllbnRfbmFtZSI6IkZDVUJTIE5vbi1Qcm9kIiwidGVuYW50X2lzcyI6Imh0dHBzOlwvXC9pZGNzLTcwOTUxN2QzNmE4YjQ0ZjdhZjU5OWYyYmMwNjViMzMwLmlkZW50aXR5Lm9yYWNsZWNsb3VkLmNvbTo0NDMiLCJ0ZW5hbnQiOiJpZGNzLTcwOTUxN2QzNmE4YjQ0ZjdhZjU5OWYyYmMwNjViMzMwIiwianRpIjoiMDM0NjEyZDUyM2FiNDUyNTlkMGNiYWY5NGU3NjQ0NmYiLCJyZXNvdXJjZV9hcHBfaWQiOiJkOTk1ZGZmMjM4YTM0ZjRhYjljMjU0ZDFkMGQxMTRjNCJ9.D1aXMI3Mnc0ZKzTeqOW97YZNmtXuRmTXuQh-dxr24P_NVYmwHJBZkX_wEhDrMO4A0XvdSRQV1bszrbG2nVzRkwXg5ws4RUCteAIpsyn9fDlsPJldvN-n5nuVHYB8Yvnj5brhu56Wy8BeIAuI0ENq4FhgadYqA3MwQvC7X9zpGFmdFcLdkoEloHQi7Z-nVA-3UswFJyIpm68rugrMt7tRA8tYPtX7qRtE5nnuaro_fb4m-6AwQy2CL0-RGGbnB1tiq2E6BZSraAa5GmjkgcDVRrlEf00wcitqQV4zgCxUeES8v568vNH4Plk6dUaDjfNf6c8ZN93X5H52vKrTmpmJ1w"
	 * );
	 * 
	 * httpGet.setHeader("Authorization", "Bearer " + token);
	 * CloseableHttpResponse httpResponse1 = httpClient1.execute(httpGet1);
	 * 
	 * HttpEntity responseEntity1 = httpResponse1.getEntity();
	 * 
	 * System.out.println("responseEntity: " + responseEntity1.toString());
	 * String responseBody1 = EntityUtils.toString(httpResponse1.getEntity());
	 * JSONObject jsonObject = new JSONObject(responseBody1.toString());
	 * 
	 * String filename = jsonObject.getString("name");
	 * System.out.println("filename: " + filename);
	 * 
	 * File file = new File(EXTERNAL_FILE_PATH + filename); fos = new
	 * FileOutputStream(file); fos.write(image);
	 * 
	 * System.out.println("File Saved"); return filename;
	 * 
	 * } catch (Exception excp) { // handle error
	 * System.out.println("Enterer here with error: " + excp.getMessage());
	 * return null; } finally { if (fos != null) { fos.close(); } else { return
	 * null; } result.close(); System.out.println("Finally.. !!"); } }
	 * 
	 */

	public String DMSFiledownload(HttpServletResponse httpServletResponse, String docRef, String EXTERNAL_FILE_PATH)
			throws JSONException, IOException {

		System.out.println("Getting the docId: " + docRef);
		//docRef = "CONTD00117D062B3443092B7A3D0CA26FCF8";

		//String URL = "https://nonprodocm1-waafibank.cec.ocp.oraclecloud.com:443/content/management/api/v1.1/assets/";
		String URL = "https://prodocm1-waafibank.cec.ocp.oraclecloud.com/content/management/api/v1.1/assets/";
		String token = getAuthToken();

		URL = URL + docRef + "/native";
		URI url = null;
		try {
			url = new URI(URL);
		} catch (URISyntaxException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);

		httpGet.setHeader("Authorization", "Bearer " + token);
		CloseableHttpResponse httpResponse = httpClient.execute(httpGet);

		System.out.println("Response Body httpResponse headers ==>: " + httpResponse.getAllHeaders());

		// Print all headers
		List<Header> httpHeaders = Arrays.asList(httpResponse.getAllHeaders());
		for (Header header : httpHeaders) {
			System.out.println("Headers.. name,value:" + header.getName() + "," + header.getValue());

		}

		String fnamefromheader = httpResponse.getFirstHeader("Content-Disposition").getValue();

		System.out.println("fnamefromheader: " + fnamefromheader);

		String filename = fnamefromheader.replaceFirst("(?i)^.*filename=\"?([^\"]+)\"?.*$", "$1");

		System.out.println("filename: " + filename);

		String responseBody = EntityUtils.toString(httpResponse.getEntity());

		System.out.println("responseBody ==> " + responseBody);
		byte[] image = responseBody.getBytes(StandardCharsets.UTF_8);

		String filePath = EXTERNAL_FILE_PATH + "/" + filename;
		System.out.println("filePath ==>" + filePath);

		FileOutputStream fos = new FileOutputStream(new File(filePath));
		fos.write(image);

		System.out.println("Write bytes to file.");
		// printContent(file);
		fos.close();

		// System.out.println("fos.toString() ==>"+fos.toString());

		httpClient.close();
		System.out.println("File Download Completed!!!");

		System.out.println("GET Response Status:: " + httpResponse.getStatusLine().getStatusCode());
		return filename;

	}

	public String getAuthToken() throws IOException, JSONException {

		String TOKEN_REQUEST_URL = "https://idcs-709517d36a8b44f7af599f2bc065b330.identity.oraclecloud.com/oauth2/v1/token";
		// String TOKEN_REQUEST_URL =
		// "https://nonprodocm1-waafibank.identity.oraclecloud.com/oauth2/v1/token";
		// String CLIENT_ID = "857dc9133c884a9882c6d50274120fab";
		// String CLIENT_SECRET = "e5ac9290-7b7b-4aa4-9da9-1674d6cb82ef";
		// String SCOPE =
		// "https://nonprodocm1-waafibank.cec.ocp.oraclecloud.com/urn:opc:cec:all";
		// String CLIENT_CREDENTIALS = "Client Credentials";

		URL obj = new URL(TOKEN_REQUEST_URL);
		// String auth = client_app_key + ":" + client_app_secret;

		// System.out.println("URL ==> " + URL);
		// System.out.println("Auth ==> " + auth);

		// byte[] encodedAuth =
		// Base64.getEncoder().encode(auth.getBytes(StandardCharsets.UTF_8));
		// System.out.println("Encode Auth ==> " + encodedAuth);
		// String authHeaderValue = "Basic " + new String(encodedAuth);

		//String authHeaderValue = "Basic ODU3ZGM5MTMzYzg4NGE5ODgyYzZkNTAyNzQxMjBmYWI6ZTVhYzkyOTAtN2I3Yi00YWE0LTlkYTktMTY3NGQ2Y2I4MmVm";
		String authHeaderValue = "Basic OTE3MDMzODcyMWI2NDdlY2E0ZTQ0YWIxNzFmYmUxNDg6MzM4ZWEyYWUtM2UwNi00ZDdkLTg0OTYtOWE3ZGYxMzJhNjA1";
		

		System.out.println("Auth Header Value ==> " + authHeaderValue);

		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("Authorization", authHeaderValue);

		// con.setRequestProperty("Accept", "application/json");
		//https: // nonprodocm1-waafibank.cec.ocp.oraclecloud.com/

		con.setRequestProperty("Host", "idcs-709517d36a8b44f7af599f2bc065b330.identity.oraclecloud.com");
		// con.setRequestProperty("Host",
		// "nonprodocm1-waafibank.identity.oraclecloud.com");
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

		// con.setRequestProperty("User-Agent", USER_AGENT);

		// final String POST_PARAMS = "grant_type=client_credentials";

		//final String POST_PARAMS = "grant_type=client_credentials&scope=https%3A%2F%2Fnonprodocm1-waafibank.cec.ocp.oraclecloud.com%2Furn%3Aopc%3Acec%3Aall";
		final String POST_PARAMS = "grant_type=client_credentials&scope=https%3A%2F%2Fprodocm1-waafibank.cec.ocp.oraclecloud.com%2Furn%3Aopc%3Acec%3Aall";

		// For POST only - START
		con.setDoOutput(true);
		OutputStream os = con.getOutputStream();
		os.write(POST_PARAMS.getBytes());
		os.flush();
		os.close();
		// For POST only - END
		int responseCode = con.getResponseCode();
		String responsemsg = con.getResponseMessage();

		System.out.println("POST Response Code :: " + responseCode);
		// System.out.println("Response Message :: " + con.getInputStream());

		if (responseCode == HttpURLConnection.HTTP_OK) { // success
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			System.out.println("Response Message ==>" + response.toString());
			in.close();
			JSONObject jsonObject = new JSONObject(response.toString());
			System.out.println("jsonObject: " + jsonObject);
			String access_token = jsonObject.getString("access_token");
			System.out.println("access_token: " + access_token);

			return access_token;
		} else {
			System.out.println("POST request not worked");
			throw new IOException("POST request not worked");
		}
	}

}
