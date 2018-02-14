<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ page session="false"%>

<%
    String cp = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head> 
	<title>LOGIN</title>
	<link rel="stylesheet" href="/resources/css/jquery1.12.1/jquery-ui.min.css" type="text/css">
	<link rel="stylesheet" href="/resources/css/jquery1.12.1/jquery-ui.theme.min.css" type="text/css">

	<link rel="stylesheet" href="/resources/css/table.css" type="text/css">
	<link rel="stylesheet" href="/resources/css/c3.min.css" type="text/css">
	<link rel="stylesheet" href="/resources/css/melon.datepicker.css" type="text/css">
	<link rel="stylesheet" href="/resources/css/login.css" type="text/css">
	
	
	<script type="text/javascript" src="/resources/js/jquery-3.3.1.min.js"></script>
	<script type="text/javascript" src="/resources/js/jquery1.12.1/jquery-ui.min.js"></script>  
	<script type="text/javascript" src="/resources/js/c3.min.js"></script>
	<script type="text/javascript" src="/resources/js/d3.v3.min.js" charset="utf-8"></script>
	<script type="text/javascript" src="/resources/js/common.js"></script>

	<script type="text/javascript">
		$(document).ready(function() {
			$("#login").click(function(e) {
				debugger;
			    e.preventDefault();
				var userId = $('#userid').val();
				var password = $('#password').val();
				
			    if (userId == null || password == null) {
			    	var msg = "Enter Id or Password";
			    	$("#errorMessage").html(msg);
					$("#errorMessageDialog").dialog( {
						model:true,
						buttons: {
							OK : function () {
								$(this).dialog('close');
							}
						}
					});
			    }
			    
			    var parameter = {};
			    parameter['id'] = userId;
			    parameter['password'] = password;
//		         {
//		             id: userId,
//		             password: password,
//		         };
			    
			    commonJson.setUrl("<c:url value='/cmn/login.do' />");
			    commonJson.setParam(parameter);
			    commonJson.setCallback(callBackLogin);
			    commonJson.ajax();			    
			});
			
			$("#test").click(function(e) {
				debugger;
			    e.preventDefault();
			    
			    var parameter = {
			    	    "reqHeader" : {
			    	        "apiKey" : "wfdfafd23asrtqdfgasdfaseaqweradsfasdf",
			    	        "reqFacilLicNo" : "30300129",
			    	        "reqProfLicNo" : "40000283"
			    	    },
			    	    "reqData" : {
			    	        "dscReqId" : "",
			    	        "reqDscItemBitCd" : "11100000000000000000",
			    	        "presIssueNo" : "",
			    	        "reqFacilLicNo" : "30300129",
			    	        "reqProfLicNo" : "40000283",
			    	        "patntCprNo" : "950309988",
			    	        "patntPasspNo" : "",
			    	        "patntNm" : "Ashraf",
			    	        "patntAge" : "25",
			    	        "patntBdate" : "31031995",
			    	        "patntGenderCd" : "001",
			    	        "patntContryCd" : "BHR",
			    	        "pregStatsCd" : "002",
			    	        "g6PdDfcyStatsCd" : "002",
			    	        "mdsbjCd" : "",
			    	        "diseCd" : "A56.8",
			    	        "presValidDuratn" : "3",
			    	        "dispsNote" : "",
			    	        "presbePttnCd" : "002",
			    	        "presbeStatsCd" : "003",
			    	        "presbeCnclReason" : "",
			    	        "presbeStopStatsCd" : "002",
			    	        "presbeStopReason" : "",
			    	        "presbeDate" : "",
			    	        "reqListData" : [
			    	            {
			    	                "dscIngrSeqno" : "1",
			    	                "presbeCtgrCd" : "002",
			    	                "wmGenericCd" : "g8080",
			    	                "drn" : "DRN-5820/02",
			    	                "dose" : "1",
			    	                "freqnc" : "3",
			    	                "duratn" : "3",
			    	                "presbeUsgCd" : "003",
			    	                "adminStartDt" : "20180129"
			    	            },
			    	            {
			    	                "dscIngrSeqno" : "2",
			    	                "presbeCtgrCd" : "001",
			    	                "wmGenericCd" : "",
			    	                "drn" : "",
			    	                "dose" : "1",
			    	                "freqnc" : "3",
			    	                "duratn" : "3",
			    	                "presbeUsgCd" : "003",
			    	                "adminStartDt" : "20180129"
			    	            },
			    	            {
			    	                "dscIngrSeqno" : "3",
			    	                "presbeCtgrCd" : "002",
			    	                "wmGenericCd" : "g5816",
			    	                "drn" : "DRN-5569/01",
			    	                "dose" : "1",
			    	                "freqnc" : "3",
			    	                "duratn" : "3",
			    	                "presbeUsgCd" : "003",
			    	                "adminStartDt" : "20180129"
			    	            }
			    	        ]
			    	    }
			    	};
			    
			    commonJson.setUrl("<c:url value='/cmn/login.do' />");
			    commonJson.setParam(parameter);
			    commonJson.setCallback(callBackLogin);
			    commonJson.ajax();			    
			});

		});
		
		function callBackLogin(response) {
			$.each(response.resMessage, function(k, v) {
				if(k == 'stautsCode') {
					debugger;
					if (v == 'ERROR') {
						debugger;
						var msg = "Enter Id or Password";
				    	$("#errorMessageDialog").html(msg);
						$("#errorMessageDialog").dialog( {
							model:true,
							buttons: {
								OK : function () {
									$(this).dialog('close');
								}
							}
						});
					}
					else {
						debugger;
						commonPageMove.setUrl("<c:url value='/cmn/viewMap.do' />");
						commonPageMove.submit();
//						$(location).attr('href', '');
						//location.href = '/chart/viewChart';
						//$.ajax({
					    //   type: "POST",
					    //    url: '/chart/viewChart',
					    //    dataType : "json",  //Cross domain
					    //    contentType: "application/json"
					    //});
					}

				}
			});
			
		}
	</script>
</head>
        
         
<body>
	<div class="container">
		<div class="main">
			<form class="form" method="post" action="#">
				<h2>SEHATI Interface System</h2>
				<label>ID</label>
				<input type="text" name="userid" id="userid">
				<label>Password</label>
				<input type="password" name="password" id="password">
				<input type="button" name="login" id="login" value="Login">
				<input type="button" name="Test" id="test" value="test">
				<div id="msgbox"> </div>
			</form>
		</div>
	</div>
	<div style="display:none" id="errorMessageDialog"></div>
	<div style="display:none" id="PKIPopup"></div>
	</body>
</html>
