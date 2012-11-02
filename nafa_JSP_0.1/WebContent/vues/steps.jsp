<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
	<p id="titreEtape"><spring:message code="etapes" /></p>
	<ol>
    	<li>
	  		<c:choose>
	    		<c:when test="${step=='choixLangue'}"><span class="etapeCourant"><spring:message code="choixLangue" /></span></c:when>
				<c:otherwise><spring:message code="choixLangue" /></c:otherwise>
	  		</c:choose>
		</li>
		<li>
			<c:choose>
	    		<c:when test="${step=='precheck'}"><span class="etapeCourant"><spring:message code="precheck" /></span></c:when>
				<c:otherwise><spring:message code="precheck" /></c:otherwise>
	  		</c:choose>
		</li>
		<li>
			<c:choose>
	    		<c:when test="${step=='license'}"><span class="etapeCourant"><spring:message code="license" /></span></c:when>
				<c:otherwise><spring:message code="license" /></c:otherwise>
	  		</c:choose>
		</li>
		<li>
			<c:choose>
	    		<c:when test="${step=='infosBD'}"><span class="etapeCourant"><spring:message code="infosBD" /></span></c:when>
				<c:otherwise><spring:message code="infosBD" /></c:otherwise>
	  		</c:choose>
		</li>
		<li>
			<c:choose>
	    		<c:when test="${step=='adminInfos'}"><span class="etapeCourant"><spring:message code="adminInfos" /></span></c:when>
				<c:otherwise><spring:message code="adminInfos" /></c:otherwise>
	  		</c:choose>
		</li>
	</ol>		
