'use strict';

var singleUploadForm = document.querySelector('#singleUploadForm');
var singleFileUploadInput = document.querySelector('#singleFileUploadInput');
var singleFileUploadError = document.querySelector('#singleFileUploadError');
var singleFileUploadSuccess = document.querySelector('#singleFileUploadSuccess');

function uploadSingleFile(file) {
    var formData = new FormData();
    
    formData.append("file", file);
	formData.append("cif", document.getElementById("iadp_cif").value);
	
    var xhr = new XMLHttpRequest();
    xhr.open("POST", "/ocmfileupload/DMSChannel/contents");

    xhr.onload = function() {
        console.log(xhr.responseText);
        var response = JSON.stringify (JSON.parse(xhr.responseText));
        var docRef = getValue(response, "id", ":", ",", 2);
        
       //if(xhr.status == 200) {
        if (xhr.status === 0 || (xhr.status >= 200 && xhr.status < 400)) {
            singleFileUploadError.style.display = "none";
            singleFileUploadSuccess.innerHTML = "<p>File Uploaded Successfully.</p><p>Document Reference Number is : " +docRef+ "</p>";
            singleFileUploadSuccess.style.display = "block";
            //document.getElementById('iadp_documentReference').value =response.id;
            window.opener.postMessage({response: docRef }, 'https://192.168.1.243:9003/FCJNeoWeb');
            
       } else {
            singleFileUploadSuccess.style.display = "none";
            singleFileUploadError.innerHTML = (response && response.message) || "File Upload Failed";
        }
    }

    xhr.send(formData);
}


singleUploadForm.addEventListener('submit', function(event){
    var files = singleFileUploadInput.files;
    if(files.length === 0) {
        singleFileUploadError.innerHTML = "Please select a file";
        singleFileUploadError.style.display = "block";
    }
    uploadSingleFile(files[0]);
    event.preventDefault();
}, true);


function getValue(result, key,  separator,  endOperator,  occurrence) {
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
			//value = value.replace("\\\"", "");
			//value = value.replace(" ", "");
			console.log("value: " + value);
		} else {
			value = "";
		}
	} catch (err) {
		console.log(err);
		//value = "";
		//e.printStackTrace();
	}
	return value;
}
