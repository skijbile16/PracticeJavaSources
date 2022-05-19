'use strict';

var fileDownloadForm = document.querySelector('#fileDownloadForm');
// var singleFileUploadInput = document.querySelector('#singleFileUploadInput');
var fileDownloadError = document.querySelector('#fileDownloadError');
var fileDownloadSuccess = document.querySelector('#fileDownloadSuccess');

function downloadFile(docRef) {
	var formData = new FormData();

	// formData.append("file", file);
	formData.append("docRef", document.getElementById("iadp_docId").value);
	var method = "GET";
	var url = "/ocmfileupload/DMSChannel/filedownload?docRef=" + docRef;

	var xhr = new XMLHttpRequest();
	xhr.open(method, url);

	xhr.onload = function() {
		console.log(xhr.responseText);
    	//var response = JSON.stringify(eval('('+xhr.response+')'));
		//var responseData =JSON.parse(response);
		 var responseData = JSON.parse(xhr.responseText);
		// var docRef = getValue(response, "id", ":", ",", 2);

		// if(xhr.status == 200) {
		if (xhr.status === 0 || (xhr.status >= 200 && xhr.status < 400)) {
        if(xhr.readyState==4 && responseData.returnCode==1 ){
				//var downloadFileURI = "/ocmfileupload/DMSChannel/download?fileName="+docRef+"_"+responseData.returnData;
				var downloadFileURI = "/ocmfileupload/DMSChannel/download?fileName="+responseData.returnData;
				var download = document.createElement("a");
				download.href = downloadFileURI; 
				download.click();
			fileDownloadError.style.display = "none";
			fileDownloadSuccess.innerHTML = "<span style='color: black; font-weight:bolder;'>File Downloaded Successfully.</span>";
			fileDownloadSuccess.style.display = "block";
			}else{
				fileDownloadError.style.display = "none";
				fileDownloadSuccess.innerHTML = "<span style='color: black; font-weight:bolder;'>Something went wrong while downloading.</span>";
				fileDownloadSuccess.style.display = "block";
			}

		} else {
			fileDownloadSuccess.style.display = "none";
			fileDownloadError.innerHTML = (response && response.message)
					|| "File Download Failed";
		}
	}

	xhr.send(formData);
}

fileDownloadForm.addEventListener('submit', function(event) {
	var docRef = document.getElementById("iadp_docId").value;
	if (docRef == '') {
		fileDownloadError.innerHTML = "Document Reference Number is Null";
		fileDownloadError.style.display = "block";
	}
	downloadFile(docRef);
	event.preventDefault();
}, true);

function getValue(result, key, separator, endOperator, occurrence) {
	var value = "";
	var posStartFrom = 0;
	var pos1 = 0, pos2 = 0;
	try {
		for (let i = 0; i < occurrence; i++) {
			posStartFrom = result.indexOf(key, posStartFrom + 1);
			console.log("posStartFrom: " + posStartFrom);
		}
		if (posStartFrom > 0) {

			pos1 = result.indexOf(key);
			pos2 = result.indexOf(separator, posStartFrom);
			var pos3 = result.indexOf(endOperator, posStartFrom);

			console.log("pos1: " + pos1 + " pos2: " + pos2 + " pos3:" + pos3);

			value = result.substring(pos2 + 4, pos3 - 2);
			// value = value.replace("\\\"", "");
			// value = value.replace(" ", "");
			console.log("value: " + value);
		} else {
			value = "";
		}
	} catch (err) {
		console.log(err);
		// value = "";
		// e.printStackTrace();
	}
	return value;
}
