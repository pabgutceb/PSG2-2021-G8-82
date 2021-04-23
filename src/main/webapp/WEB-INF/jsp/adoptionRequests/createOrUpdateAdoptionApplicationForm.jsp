<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>


<petclinic:layout pageName="adoptionApplication">

    <jsp:body>
        <h2><c:if test="${adoptionApplication['new']}"><fmt:message key="adoptionApplication.new"/></c:if></h2>
		<h3><fmt:message key="pet"/></h3>
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
                <td><c:out value="${adoptionApplication.adoptionRequest.pet.name}"/></td>
                <td><petclinic:localDate date="${adoptionApplication.adoptionRequest.pet.birthDate}" pattern="yyyy/MM/dd"/></td>
                <td><c:out value="${adoptionApplication.adoptionRequest.pet.type.name}"/></td>
                <td><c:out value="${adoptionApplication.adoptionRequest.pet.owner.firstName} ${pet.owner.lastName}"/></td>
            </tr>
        </table>

        <form:form modelAttribute="adoptionApplication" class="form-horizontal">
        	<input type="hidden" name="adoptionRequest" value="${adoptionApplication.adoptionRequest.id}"/>
            <div class="form-group has-feedback">
            	<fmt:message var="comment" key="comment"/>
                <petclinic:inputField label="${comment}" name="comment"/>
            </div>

            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <button class="btn btn-default" type="submit"><fmt:message key="application.create"/></button>
                </div>
            </div>
        </form:form>

    </jsp:body>

</petclinic:layout>
