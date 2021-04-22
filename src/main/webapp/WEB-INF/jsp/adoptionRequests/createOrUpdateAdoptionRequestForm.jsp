<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>


<petclinic:layout pageName="adoptionRequest">

    <jsp:body>
        <h2><c:if test="${adoptionRequest['new']}"><fmt:message key="adoptionRequest.new"/></c:if></h2>

        <form:form modelAttribute="adoptionRequest" class="form-horizontal">
        	<input type="hidden" name="ownerId" value="${adoptionRequest.owner.id}"/>
            <div class="form-group has-feedback">
            <fmt:message var="pet" key="pet"/>
                <div class="control-group">
                    <petclinic:selectField2 name="pet" label="${pet}" items="${adoptablePets}" itemsLabel="name" itemsValue="id" size="5"/>
                </div>
            </div>

            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    
                    <button class="btn btn-default" type="submit"><fmt:message key="adoptionRequest.publish"/></button>
                </div>
            </div>
        </form:form>

    </jsp:body>

</petclinic:layout>
