<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="vets">
    <h2><fmt:message key="veterinarians"/></h2>

    <table id="vetsTable" class="table table-striped">
        <thead>
        <tr>

            <th><fmt:message key="name"/></th>
            <th><fmt:message key="specialties"/></th>
            <th><fmt:message key="buttons"/></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${vets.vetList}" var="vet">
            <tr>
            	<td>
            		<c:out value="${vet.firstName} ${vet.lastName}"/>
            	</td>
                <td>
                    <c:forEach var="specialty" items="${vet.specialties}" varStatus="specialtyLoopState">
                        <c:out value="${specialty.name}"/><c:if test="${not specialtyLoopState.last}">, </c:if>
                    </c:forEach>
                    <c:if test="${vet.nrOfSpecialties == 0}">none</c:if>
                </td>
                
                 <td>
                 	<form modelAttribute="vet" action="/vets/${vet.id}/edit" method="get" class="form-horizontal">
        				    <button type="submit" class="btn btn-default"><fmt:message key="editVet"/></button>
   					      </form>
                  <form modelAttribute="vet" action="/vets/${vet.id}/delete"
							      method="get" class="form-horizontal">
							    <button type="submit" class="btn btn-default"><fmt:message key="deleteVet"/></button>
                   </form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <table class="table-buttons">
    	<tr>
        	<td>
               <form modelAttribute="vet" action="/vets/new" method="get" class="form-horizontal">
        			<button type="submit" class="btn btn-default"><fmt:message key="createVet"/></button>
   				</form>
            </td>      
        </tr>
    	
    </table>
</petclinic:layout>