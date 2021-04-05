<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%@ attribute name="message" required="false"%>
<%@ attribute name="messageType" required="false"%>
<%@ attribute name="buttonMessage" required="false"%>
<%@ attribute name="buttonURL" required="false"%>
<%@ attribute name="messageCode" required="false"%>
<%@ attribute name="messageArgument" required="false"%>

<c:set var="messageType"
	value="${not empty messageType ? messageType : 'info'}" />

<c:set var="boldMessage" value="Info: " />
<%
String auxBoldMessage;
switch (messageType) {
case "info":
	auxBoldMessage = "Info: ";
	break;
case "success":
	auxBoldMessage = "Success! ";
	break;
case "warning":
	auxBoldMessage = "Warning: ";
	break;
case "danger":
	auxBoldMessage = "Error! ";
	break;
default:
	messageType = "info";
	auxBoldMessage = "Info: ";
}
jspContext.setAttribute("boldMessage", auxBoldMessage);
%>

<c:if test="${not empty message ||not empty messageCode}">

	<div
		class="alert alert-${not empty messageType ? messageType : 'info'} alert-dismissible"
		role="alert">
		<c:if test="${empty buttonMessage && empty buttonURL}">
			<!--<strong><c:out value="${boldMessage}" /></strong>-->
		</c:if>

		
		
		
		
		<c:choose>
			<c:when test="${not empty message}">
				<c:out value="${message}" />

			</c:when>
			<c:otherwise>
				<spring:message code="${messageCode}" arguments="${messageArgument}" />
			</c:otherwise>
		</c:choose>

		<c:if test="${not empty buttonMessage && not empty buttonURL}">
			<a role="button"
				class="btn btn-${not empty messageType ? messageType : 'default'}"
				href="${fn:escapeXml(buttonURL)}"> <fmt:message
					key="${buttonMessage}" />
			</a>
		</c:if>
		<button type="button" class="close" data-dismiss="alert"
			aria-label="Close">
			<span aria-hidden="true">&times;</span>
		</button>
	</div>
</c:if>
