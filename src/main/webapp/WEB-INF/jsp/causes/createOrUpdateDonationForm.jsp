<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>


<petclinic:layout pageName="donations">

        <h2><c:if test="${donation['new']}"><fmt:message key="newDonation"/></c:if></h2>

        
            	
        <form:form modelAttribute="donation" class="form-horizontal" id="add-donation-form">
        <div class="form-group has-feedback">
        	<fmt:message var="amount" key="amount"/>
      		<fmt:message var="client" key="client"/>
      		<form:hidden path="donationDate"/>
      		<input type="hidden" name="id" value="${id}"/>
      		<input type="hidden" name="id" value="${cause.id}"/>
           <petclinic:inputField label="${amount}" name="amount"/>
           <petclinic:inputField label="${client}" name="client"/>
           
                    
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
</petclinic:layout>