<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


	<c:choose>
		<c:when test="${step=='license'}">
			<p>
				<input type="hidden" name="lang" value="${lang}" />
				<input type="hidden" name="step" value="${step}" />
				<input type="hidden" name="action" id="action" value="" />
				 
				<input type="submit" class="bouton" value="<spring:message code="previous"/>" onclick="document.getElementById('action').value='previous'; this.form.submit();" />
				<input type="submit" class="bouton" value="<spring:message code="not_accept"/>" onclick="document.getElementById('action').value='not_accept'; this.form.submit();" />
				<input type="submit" class="bouton" value="<spring:message code="accept"/>" onclick="document.getElementById('action').value='accept'; this.form.submit();" />
			</p>
		</c:when>
		
		<c:when test="${step=='adminInfos'}">
			<p>
				<input type="hidden" name="lang" value="${lang}" />
				<input type="hidden" name="step" value="${step}" />
				<input type="hidden" name="action" id="action" value="" />
				 
				<input type="submit" class="bouton" value="<spring:message code="previous"/>" onclick="document.getElementById('action').value='previous'; this.form.submit();" />
				<input type="submit" class="bouton" value="<spring:message code="end"/>" onclick="document.getElementById('action').value='end'; this.form.submit();" />
			</p>
		</c:when>
		
		<c:otherwise>
			<p>
				<input type="hidden" name="lang" value="${lang}" />
				<input type="hidden" name="step" value="${step}" />
				<input type="hidden" name="action" id="action" value="" /> 

				<input type="submit" class="bouton" value="<spring:message code="previous"/>" onclick="document.getElementById('action').value='previous'; this.form.submit();" />
				<input type="submit" class="bouton" value="<spring:message code="next"/>" onclick="document.getElementById('action').value='next'; this.form.submit(); return loadSubmit()" 
					<c:if test="${step=='infosBD' and (empty bd or bd=='none')}">  disabled="disabled" </c:if> />				
			</p>
		</c:otherwise>
	</c:choose>
