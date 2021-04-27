<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>


<petclinic:layout pageName="donations">
	<jsp:body>
        <h2><c:if test="${donation['new']}"><fmt:message key="newDonation"/></c:if></h2>

        
            	
        <form:form modelAttribute="donation" class="form-horizontal" id="add-donation-form">
        <div class="form-group has-feedback">
        	<fmt:message var="amount" key="amount"/>
      		<form:hidden path="donationDate"/>
      		<input type="hidden" name="id" value="${id}"/>
      		
           <petclinic:inputField label="${amount}" name="amount"/>
       
           
                    
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <c:choose>
                    <c:when test="${donation['new']}">
                    	
                        <button class="btn btn-default" type="submit"><fmt:message key="addDonation"/></button>
                    </c:when>
                </c:choose>
            </div>
        </div>
    </form:form>
    </jsp:body>
</petclinic:layout>