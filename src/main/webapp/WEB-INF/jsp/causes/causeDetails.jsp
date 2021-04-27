<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="causes">

    <h2><fmt:message key="causesInfo"/></h2>

    <table class="table table-striped">
        <tr>
            <th><fmt:message key="name"/></th>
            <td><b><c:out value="${cause.name}"/></b></td>
        </tr>
        <tr>
            <th><fmt:message key="description"/></th>
            <td><b><c:out value="${cause.description}"/></b></td>
        </tr>
        <tr>
            <th><fmt:message key="organization"/></th>
            <td><b><c:out value="${cause.organization}"/></b></td>
        </tr>
        <tr>
            <th><fmt:message key="budgetTarget"/></th>
            <td><c:out value="${cause.budgetTarget}"/></td>
        </tr>
        <tr>
            <th><fmt:message key="totalBudget"/></th>
            <td><c:out value="${cause.totalBudget}"/></td>
        </tr>
    </table>
    
        <br/>
    <br/>
    <br/>
  
    <h2><fmt:message key="donations"/></h2>

    <table class="table table-striped">
        <c:forEach var="donation" items="${cause.donations}">

            <tr>
                <td valign="top">
                    <dl class="dl-horizontal">
                        <dt><fmt:message key="owner"/></dt>
                        <spring:url value="/owners/{ownerId}" var="ownerUrl">
                        <spring:param name="ownerId" value="${donation.client.id}"/>
                    	</spring:url>
                        <dd><a href="${fn:escapeXml(ownerUrl)}">
                        <c:out value="${donation.client.firstName} ${donation.client.lastName}"/></a></dd>
                        <dt><fmt:message key="amount"/></dt>
                        <dd><c:out value="${donation.amount}"/></dd>
                   		<dt><fmt:message key="donationDate"/></dt>
                        <dd><petclinic:localDate date="${donation.donationDate}" pattern="yyyy-MM-dd"/></dd>
                        
                    </dl>
                </td>
            </tr>

        </c:forEach>
    </table>

</petclinic:layout>
