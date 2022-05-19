package com.jmr.customerdata.custpersonalinfo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import com.jmr.customerdata.custpersonalinfo.pojo.CustInfoResponseDto;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResponseStatusException.class)
	public ResponseEntity<?> handleResponseStatusException(ResponseStatusException exception) {

		CustInfoResponseDto custInfoResponseDto = new CustInfoResponseDto();

		custInfoResponseDto.setReturnMsg(exception.getMessage());
		custInfoResponseDto.setReturnCode(1);
		custInfoResponseDto.setErrorMsg(true);
		// custInfoResponseDto.setReturnData(customerInfo);
		HttpStatus userStatus = HttpStatus.NOT_FOUND;

		return new ResponseEntity(custInfoResponseDto, userStatus);

	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleGlobalException(Exception exception) {

		CustInfoResponseDto custInfoResponseDto = new CustInfoResponseDto();

		custInfoResponseDto.setReturnMsg(exception.getMessage());
		custInfoResponseDto.setReturnCode(1);
		custInfoResponseDto.setErrorMsg(true);
		// custInfoResponseDto.setReturnData(customerInfo);
		HttpStatus userStatus = HttpStatus.INTERNAL_SERVER_ERROR;

		return new ResponseEntity(custInfoResponseDto, userStatus);

	}

}
