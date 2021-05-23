<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="owners">

    <h2><fmt:message key="ownersInfo"/></h2>

    <table class="table table-striped">
        <tr>
            <th><fmt:message key="name"/></th>
            <td><b><c:out value="${owner.firstName} ${owner.lastName}"/></b></td>
        </tr>
        <tr>
            <th><fmt:message key="address"/></th>
            <td><c:out value="${owner.address}"/></td>
        </tr>
        <tr>
            <th><fmt:message key="city"/></th>
            <td><c:out value="${owner.city}"/></td>
        </tr>
        <tr>
            <th><fmt:message key="telephone"/></th>
            <td><c:out value="${owner.telephone}"/></td>
        </tr>
    </table>

    <spring:url value="{ownerId}/edit" var="editUrl">
        <spring:param name="ownerId" value="${owner.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(editUrl)}" class="btn btn-default"><fmt:message key="editOwner"/></a>

    <spring:url value="{ownerId}/pets/new" var="addUrl">
        <spring:param name="ownerId" value="${owner.id}"/>
    </spring:url>
    <a href="${fn:escapeXml(addUrl)}" class="btn btn-default"><fmt:message key="addNewPet"/></a>

    <br/>
    <br/>
    <br/>
  
    <h2><fmt:message key="petsAndVisits"/></h2>

    <table class="table table-striped">
        <c:forEach var="pet" items="${owner.pets}">

            <tr>
                <td valign="top">
                    <dl class="dl-horizontal">
                        <dt><fmt:message key="name"/></dt>
                        <dd><c:out value="${pet.name}"/></dd>
                        <dt><fmt:message key="birthDate"/></dt>
                        <dd><petclinic:localDate date="${pet.birthDate}" pattern="yyyy-MM-dd"/></dd>
                        <dt><fmt:message key="type"/></dt>
                        <dd><c:out value="${pet.type.name}"/></dd>
                    </dl>
                    
					<sec:authorize access="hasAnyAuthority('owner')">
						<c:if test="${pet.owner.user.username == principalUsername}">
							<spring:url value="/adoptions/requests/pet/{petId}/new" var="newAdoptionRequest">
		                               <spring:param name="petId" value="${pet.id}"/>
		                    </spring:url>
		                   	<a href="${fn:escapeXml(newAdoptionRequest)}" class="btn btn-default"><fmt:message key="adoptions.newAdoptionRequest"/></a>
						
						<spring:url value="/owners/{ownerId}/pets/{petId}/delete" var="deletePetUrl">
	                               <spring:param name="ownerId" value="${owner.id}"/>
	                               <spring:param name="petId" value="${pet.id}"/>
	                    </spring:url>
	                   	<a href="${fn:escapeXml(deletePetUrl)}" class="btn btn-default"><fmt:message key="deletePet"/></a>
	                   	</c:if>
                   	</sec:authorize>
                    
                    <sec:authorize access="hasAnyAuthority('admin')">
	                   	
						<spring:url value="/owners/{ownerId}/pets/{petId}/delete" var="deletePetUrl">
	                               <spring:param name="ownerId" value="${owner.id}"/>
	                               <spring:param name="petId" value="${pet.id}"/>
	                    </spring:url>
	                   	<a href="${fn:escapeXml(deletePetUrl)}" class="btn btn-default"><fmt:message key="deletePet"/></a>
	             		      	
                   	</sec:authorize>
                </td>
                <td valign="top">
                    <table class="table-condensed">
    					<caption style="text-align:center">Visits</caption>
                        <thead>
                        <tr>
                            <th><fmt:message key="visitDay"/></th>
                            <th><fmt:message key="description"/></th>
                        </tr>
                        </thead>
                        <c:forEach var="visit" items="${pet.visits}">
                            <tr>
                                <td><petclinic:localDate date="${visit.date}" pattern="yyyy-MM-dd"/></td>
                                <td><c:out value="${visit.description}"/></td>
                            
                            <td><spring:url value="/owners/{ownerId}/pets/{petId}/visits/{visitId}/delete" var="deleteVisitUrl">
                                    <spring:param name="ownerId" value="${owner.id}"/>
                                    <spring:param name="petId" value="${pet.id}"/>
                                    <spring:param name="visitId" value="${visit.id}"/>
                             </spring:url>
                             <a href="${fn:escapeXml(deleteVisitUrl)}"><fmt:message key="deleteVisit"/></a>
                             </td>
                             </tr>
                        </c:forEach>
                        <tr>
                        	
                            <td>
                                <spring:url value="/owners/{ownerId}/pets/{petId}/edit" var="petUrl">
                                    <spring:param name="ownerId" value="${owner.id}"/>
                                    <spring:param name="petId" value="${pet.id}"/>
                                </spring:url>
                                <a href="${fn:escapeXml(petUrl)}"><fmt:message key="editPet"/></a>
                            </td>
                            <td>
                                <spring:url value="/owners/{ownerId}/pets/{petId}/visits/new" var="visitUrl">
                                    <spring:param name="ownerId" value="${owner.id}"/>
                                    <spring:param name="petId" value="${pet.id}"/>
                                </spring:url>
                                <a href="${fn:escapeXml(visitUrl)}"><fmt:message key="addVisit"/></a>
                            </td>
						</tr>
                    </table>
                </td>
                <td>
 					<table class="table-condensed">
    					<caption style="text-align:center"><fmt:message key="bookings"/></caption>
                        <thead>
                        <tr>
                           <th><fmt:message key="startDate"/></th>
                           <th><fmt:message key="finishDate"/></th>
                        </tr>
                        </thead>
                        <c:forEach var="booking" items="${pet.bookings}">
                            <tr>
                                <td><petclinic:localDate date="${booking.startDate}" pattern="yyyy-MM-dd"/></td>
                                <td><petclinic:localDate date="${booking.finishDate}" pattern="yyyy-MM-dd"/></td>
                                
                                <td><spring:url value="/owners/{ownerId}/pets/{petId}/booking/{bookingId}/delete" var="deleteBookingUrl">
                                    <spring:param name="ownerId" value="${owner.id}"/>
                                    <spring:param name="petId" value="${pet.id}"/>
                                    <spring:param name="bookingId" value="${booking.id}"/>
                             </spring:url>
                             <a href="${fn:escapeXml(deleteBookingUrl)}"><fmt:message key="deleteBooking"/></a>


                             </td>
                            </tr>
                        </c:forEach>
                        <tr>
                            <td colspan="2" align="center">
                                 <spring:url value="/owners/{ownerId}/pets/{petId}/booking/new" var="bookingUrl">
                                    <spring:param name="ownerId" value="${owner.id}"/>
                                    <spring:param name="petId" value="${pet.id}"/>
                                </spring:url>
                                <a href="${fn:escapeXml(bookingUrl)}"><fmt:message key="bookingRoom"/></a>
                            </td>
                        </tr>
                    </table>                
                </td>
            </tr>

        </c:forEach>
    </table>

</petclinic:layout>
