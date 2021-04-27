<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="applications">
	<h2>
		<fmt:message key="applications" />
	</h2>

	
        <table class="table table-striped">
            <thead>
            <tr>
            	<th><fmt:message key="ownerApplication"/></th>
                <th><fmt:message key="petName"/></th>
                <th><fmt:message key="comment"/></th>
                
                <th><fmt:message key="validate"/></th>
            </tr>
            </thead>
		<tbody>
			<c:forEach items="${applications}" var="application">
			
				<tr>
				<c:if test="${empty application.adoptionRequest.approvedApplication}">
						<td><c:out
								value="${application.owner.firstName} ${application.owner.lastName}" /></td>
						<td><c:out value="${application.adoptionRequest.pet.name}" /></td>
						<td><c:out value="${application.comment}" /></td>

						<td><spring:url
								value="/adoptions/applicationList/owners/{ownerId}/adoptions/{adoptionRequestId}/applications/{adoptionApplicationId}"
								var="applicationUrl">
								<spring:param name="ownerId" value="${application.owner.id}" />
								<spring:param name="adoptionRequestId"
									value="${application.adoptionRequest.id}" />
								<spring:param name="adoptionApplicationId"
									value="${application.id}" />
							</spring:url> <a href="${fn:escapeXml(applicationUrl)}"><fmt:message
									key="application.accept" /></a></td>
					</c:if>
            	</tr>



			</c:forEach>
		</tbody>
	</table>

</petclinic:layout>