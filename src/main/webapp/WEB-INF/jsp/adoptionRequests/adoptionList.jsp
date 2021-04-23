<%@ page session="false" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags"%>

<petclinic:layout pageName="adoptions">
	<h2>
		<fmt:message key="adoptions" />
	</h2>

	
        <table class="table table-striped">
            <thead>
            <tr>
            	<th><fmt:message key="owner"/></th>
                <th><fmt:message key="name"/></th>
                <th><fmt:message key="birthDate"/></th>
                <th><fmt:message key="type"/></th>
                
            </tr>
            </thead>
		<tbody>
			<c:forEach items="${adoptions}" var="adoption">
				<tr>
            	<td><c:out value="${adoption.pet.owner.firstName} ${adoption.pet.owner.lastName}"/></td>
                <td><c:out value="${adoption.pet.name}"/></td>
                <td><petclinic:localDate date="${adoption.pet.birthDate}" pattern="yyyy/MM/dd"/></td>
                <td><c:out value="${adoption.pet.type.name}"/></td>
               
            	</tr>



			</c:forEach>
		</tbody>
	</table>

</petclinic:layout>