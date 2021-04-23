<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>


<petclinic:layout pageName="adoptionRequest">

    <jsp:body>
        <h2><c:if test="${adoptionRequest['new']}"><fmt:message key="adoptionRequest.new"/></c:if></h2>
		<h3>Pet</h3>
        <table class="table table-striped">
            <thead>
            <tr>
                <th><fmt:message key="name"/></th>
                <th><fmt:message key="birthDate"/></th>
                <th><fmt:message key="type"/></th>
                <th><fmt:message key="owner"/></th>
            </tr>
            </thead>
            <tr>
                <td><c:out value="${pet.name}"/></td>
                <td><petclinic:localDate date="${birthDate}" pattern="yyyy/MM/dd"/></td>
                <td><c:out value="${pet.type.name}"/></td>
                <td><c:out value="${pet.owner.firstName} ${pet.owner.lastName}"/></td>
            </tr>
        </table>

        <form:form modelAttribute="adoptionRequest" class="form-horizontal">
        	<input type="hidden" name="ownerId" value="${adoptionRequest.owner.id}"/>
        	<input type="hidden" name="petId" value="${adoptionRequest.pet.id}"/>
            <div class="form-group has-feedback">
            <fmt:message var="pet" key="pet"/>
                <div class="control-group">
                <c:choose>
                	<c:when test="${not empty adoptionRequest.pet.id}">
                		<input type="hidden" name="petId" value="${adoptionRequest.pet.id}"/>
                	</c:when>
                	<c:otherwise>
                		<petclinic:selectField2 name="pet" label="${pet}" items="${adoptablePets}" itemsLabel="name" itemsValue="id" size="5"/>
                	</c:otherwise>
                </c:choose>
                
                    <!--  -->
                </div>
            </div>

            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                <c:choose>
                    <c:when test="${isFormDisabled}">
                    	<button class="btn btn-default" type="submit" disabled><fmt:message key="adoptionRequest.publish" /></button>
                    </c:when>
	                <c:otherwise>
	                    <button class="btn btn-default" type="submit"><fmt:message key="adoptionRequest.publish" /></button>
	                
	                </c:otherwise>
                </c:choose>
                	    <spring:url value="/owners/{ownerId}" var="cancelUrl">
				        <spring:param name="ownerId" value="${adoptionRequest.pet.owner.id}"/>
				    	</spring:url>
                    <a class="btn btn-link" href="${fn:escapeXml(cancelUrl)}"><fmt:message key="cancel"/></a>
                </div>
            </div>
        </form:form>

    </jsp:body>

</petclinic:layout>
