<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<base href="<%=basePath%>">
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
	<link rel="stylesheet" href="vues/style/install.css" type="text/css" />
	<title><spring:message code="step1"/></title>
</head>
<body>
	<h1 id=logo>
		<img title="Future Logo De NAFA" src="vues/images/logo.gif" alt="Logo NAFA" />
	</h1>
	<div id="page">
		<div id="etapes">
			<ol>
				<li>
					<c:choose>
						<c:when test="${lang=='fr'}">
							<spring:message code="step1" />
						</c:when>
						<c:otherwise><b><spring:message code="step1" /></b></c:otherwise>
					</c:choose>
					<c:out value="${lang}"></c:out>
				</li>
				<li><spring:message code="step2" /></li>
				<li><spring:message code="step3" /></li>
				<li><spring:message code="step4" /></li>
			</ol>		
		</div>
		<div id="content">
			<span class="desc">
				<p><b><spring:message code="step1"/></b></p>
				<p><spring:message code="step1.text"/></p>
			</span>
			<form action="choixLangue.htm" method="get">
				<select size="4" name="lang" onchange="this.form.submit();">
					<option value='fr'><spring:message code="step1.french"/></option>
					<option value='en'><spring:message code="step1.english"/></option>
					<option value='es'><spring:message code="step1.spanish"/></option>
					<option value='wo'><spring:message code="step1.wolof"/></option>
					<option>${lang}</option>
				</select>
			</form>
		</div>
		<div id="nav">
			<p>
				<a href="<%=path%>/install.htm" class="bouton"><spring:message code="previous"/></a>
		   		<a href="<%=path%>/install.htm" class="bouton"><spring:message code="next"/></a>
			</p>
		</div>
	</div>
</body>
</html>