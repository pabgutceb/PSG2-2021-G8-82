<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="owners">
    <h2>
        <c:if test="${owner['new']}"><fmt:message key="newOwner"/></c:if>
    </h2>
    <form:form modelAttribute="owner" class="form-horizontal" id="add-owner-form">
        <div class="form-group has-feedback">
        	<fmt:message var="firstName" key="firstName"/>
        	<fmt:message var="lastName" key="lastName"/>
        	<fmt:message var="address" key="address"/>
        	<fmt:message var="city" key="city"/>
        	<fmt:message var="telephone" key="telephone"/>
        	<fmt:message var="username" key="username"/>
        	<fmt:message var="password" key="password"/>
            <petclinic:inputField label="${firstName}" name="firstName"/>
            <petclinic:inputField label="${lastName}" name="lastName"/>
            <petclinic:inputField label="${address}" name="address"/>
            <petclinic:inputField label="${city}" name="city"/>
            <petclinic:inputField label="${telephone}" name="telephone"/>
            <petclinic:inputField label="${username}" name="user.username"/>
            <petclinic:inputField label="${password}" name="user.password"/>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <c:choose>
                    <c:when test="${owner['new']}">
                        <button class="btn btn-default" type="submit"><fmt:message key="addOwner"/></button>
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-default" type="submit"><fmt:message key="updateOwner"/></button>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </form:form>
</petclinic:layout>
