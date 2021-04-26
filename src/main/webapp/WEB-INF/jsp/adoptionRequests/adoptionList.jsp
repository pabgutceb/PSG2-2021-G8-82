<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="adoptions">
	<h2>
		<fmt:message key="adoptions" />
	</h2>


	<table class="table table-striped">
		<thead>
			<tr>
				<th><fmt:message key="owner" /></th>
				<th><fmt:message key="name" /></th>
				<th><fmt:message key="birthDate" /></th>
				<th><fmt:message key="type" /></th>
				<th></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${adoptions}" var="adoption">
				<tr>
					<c:if test="${empty adoption.approvedApplication}">
						<td><c:out
								value="${adoption.pet.owner.firstName} ${adoption.pet.owner.lastName}" /></td>
						<td><c:out value="${adoption.pet.name}" /></td>
						<td><petclinic:localDate date="${adoption.pet.birthDate}"
								pattern="yyyy/MM/dd" /></td>
						<td><c:out value="${adoption.pet.type.name}" /></td>
						<td><spring:url
								value="/adoptions/{adoptionRequestId}/application/new"
								var="applicationUrl">
								<spring:param name="adoptionRequestId" value="${adoption.id}" />
							</spring:url> <a href="${fn:escapeXml(applicationUrl)}"><fmt:message
									key="application.create" /></a></td>
					</c:if>
				</tr>



			</c:forEach>
		</tbody>
	</table>
	<spring:url value="/adoptions/applicationList" var="applicationListUrl">
	</spring:url>
	<a href="${fn:escapeXml(applicationListUrl)}"><fmt:message
			key="application.list" /></a>
</petclinic:layout>