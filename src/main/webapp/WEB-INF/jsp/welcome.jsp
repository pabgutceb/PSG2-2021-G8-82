<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>
<!-- %@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %-->  

<petclinic:layout pageName="home">
    <h2><fmt:message key="welcome"/></h2>
    <div class="row">
        <div class="col-md-12">
            <spring:url value="/resources/images/pets2.png" htmlEscape="true" var="petsImage"/>
            <img class="img-responsive" src="${petsImage}"/>
            
        </div>
     
    </div>
    
    <div class="col-md-40">
    Petclinic es un servicio donde los propietarios pueden poner en adopción sus mascotas,
    donar a alguna de las causas que hay disponibles e incluso crear alguna propia.
    Además, pueden ver todos los propietarios de la clínica y los veterinarios 
    que se encargarán de cuidar de sus mascotas. También es posible hacer visitas a mascotas de otros dueños
    y reservar habitaciones para las mascotas.
    </div>
</petclinic:layout>
