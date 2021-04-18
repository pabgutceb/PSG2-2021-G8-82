<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="causes">
    <h2><fmt:message key="causes"/></h2>

    <table id="causesTable" class="table table-striped">
        <thead>
        <tr>

            <th><fmt:message key="name"/></th>
            <th><fmt:message key="budgetTarget"/></th>
            <th><fmt:message key="totalBudget"/></th>
            <th><fmt:message key="buttons"/></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${causes.causeList}" var="cause">
            <tr>
            	<td>
            		<c:out value="${cause.name}"/>
            	</td>
                <td>
                  
                   <c:out value="${cause.budgetTarget}"/>
                </td>
                
                 <td>
                  
                   <c:out value="${cause.totalBudget}"/>
                </td>
                <td>
               		<form modelAttribute="cause" action="/causes/${cause.id}/donations/new" method="get" class="form-horizontal">
        				<button type="submit" class="btn btn-default"><fmt:message key="createDonation"/></button>
   					</form>
            	</td>   
            </tr>
           
        	   
        	
        </c:forEach>
        </tbody>
    </table>

    <table class="table-buttons">
    	<tr>
        	<td>
               <form modelAttribute="cause" action="/causes/new" method="get" class="form-horizontal">
        			<button type="submit" class="btn btn-default"><fmt:message key="addCause"/></button>
   				</form>
            </td>      
        </tr>
    	
    </table>
</petclinic:layout>