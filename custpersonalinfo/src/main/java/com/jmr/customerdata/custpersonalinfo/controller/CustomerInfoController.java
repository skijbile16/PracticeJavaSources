package com.jmr.customerdata.custpersonalinfo.controller;

import java.sql.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.jmr.customerdata.custpersonalinfo.entity.AccountListInfo;
import com.jmr.customerdata.custpersonalinfo.entity.CustomerInfo;
import com.jmr.customerdata.custpersonalinfo.pojo.CustInfoRequestDto;
import com.jmr.customerdata.custpersonalinfo.pojo.CustInfoResponseDto;
import com.jmr.customerdata.custpersonalinfo.service.AccountListInfoService;
import com.jmr.customerdata.custpersonalinfo.service.CustomerInfoService;
import com.jmr.customerdata.custpersonalinfo.service.SourceInfoService;
import com.jmr.customerdata.custpersonalinfo.service.UserInfoService;

@RestController
@RequestMapping("/customer")
public class CustomerInfoController {

	@Autowired
	private CustomerInfoService customerInfoService;

	@Autowired
	private SourceInfoService sourceInfoService;

	@Autowired
	private UserInfoService userInfoService;

	@Autowired
	private AccountListInfoService accountListInfoService;

	HttpStatus userStatus;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public List<CustomerInfo> getCustomer() {

		return customerInfoService.getCustomer();

	}

	@RequestMapping(value = "/customerinfo", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<Object> getCustInfo(@Valid @RequestBody CustInfoRequestDto custInfoRequestDto,
			@RequestHeader(name = "source", required = true) String source,
			@RequestHeader(name = "userBranch", required = true) String userBranch,
			@RequestHeader(name = "userId", required = true) String userId, Errors errors) {

		System.out.println("Source: " + source);
		System.out.println("User Branch: " + userBranch);
		System.out.println("User ID: " + userId);

		CustInfoResponseDto custInfoResponseDto = new CustInfoResponseDto();

		boolean existUser = userInfoService.validateSourceInfo(userId, userBranch);
		boolean existSource = sourceInfoService.validateSourceInfo(source);

		if (existSource) {

			if (existUser) {
				System.out.println("::Source is validated::");
				String mobileNumber = custInfoRequestDto.getMobileno();

				System.out.println("Mobile No: " + mobileNumber);

				if (mobileNumber == "" || mobileNumber.length() < 8) {
					custInfoResponseDto.setReturnMsg("Mobile Number Provided in the request is not Valid");
					custInfoResponseDto.setReturnCode(2);
					custInfoResponseDto.setErrorMsg(true);
					// custInfoResponseDto.setReturnData(customerInfo);
					userStatus = HttpStatus.BAD_REQUEST;

					return new ResponseEntity<Object>(custInfoResponseDto, userStatus);
				}

				CustomerInfo customerInfo = customerInfoService.getCustomerInfo(mobileNumber);

				System.out.println("customerInfo.getCustomerNo : " + customerInfo.getCustomerNo());

				String customerNo = customerInfo.getCustomerNo();
				List<AccountListInfo> accountListInfo = accountListInfoService.getAccountListById(customerNo);
				// CustomerInfo customerInfo =
				// custInfoService.getCustomerInfoCustom(custInfoRequestDto);

				// if (customerInfo == null) {
				// throw new RecordNotFoundException("Invalid mobile number : "
				// +
				// mobileNumber);
				// }

				// CustInfoResponseDto custInfoResponseDto = new
				// CustInfoResponseDto();

				System.out.println("custInfoRequestDto toString: " + customerInfo.toString());

				// JSONObject jsonObject = new
				// JSONObject(dmsUploadRequestDto.body().string());

				// System.out.println("jsonObject: " + jsonObject);
				// String uploadFileId = jsonObject.getString("id");

				// System.out.println("uploadFileId: " + uploadFileId);
				Map<String, Object> map = new LinkedHashMap<String, Object>();
				if (customerInfo != null) {
					/*
					 * custInfoResponseDto.
					 * setReturnMsg("Customer data fetched successfully");
					 * custInfoResponseDto.setReturnCode(0);
					 * custInfoResponseDto.setErrorMsg(false);
					 * custInfoResponseDto.setReturnData(customerInfo);
					 * userStatus = HttpStatus.OK;
					 */
					map.put("customerDetails", customerInfo);
					map.put("accountList", accountListInfo);
					/*map.put("timestamp", new Date(0));*/
					map.put("returnCode", 0);
					map.put("retunMsg", "Customer data fetched successfully");
					map.put("errorMsg", false);
					userStatus = HttpStatus.OK;

				}
				// return new
				// ResponseEntity<CustInfoResponseDto>(custInfoResponseDto,
				// userStatus);
				return new ResponseEntity<Object>(map, userStatus);

			} else {

				custInfoResponseDto.setReturnMsg("User Id/User Branch Provided in the request is not Valid");
				custInfoResponseDto.setReturnCode(2);
				custInfoResponseDto.setErrorMsg(true);
				// custInfoResponseDto.setReturnData(customerInfo);
				userStatus = HttpStatus.BAD_REQUEST;

				return new ResponseEntity<Object>(custInfoResponseDto, userStatus);

			}

		} else {
			custInfoResponseDto.setReturnMsg("Source Provided in the request is not Valid");
			custInfoResponseDto.setReturnCode(2);
			custInfoResponseDto.setErrorMsg(true);
			// custInfoResponseDto.setReturnData(customerInfo);
			userStatus = HttpStatus.BAD_REQUEST;

			return new ResponseEntity<Object>(custInfoResponseDto, userStatus);

		}
	}

}