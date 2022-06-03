<%@page language="java"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="urn:jsptagdir:/WEB-INF/tags"%>

<acme:form>
	<acme:input-textbox code="inventor.brid.form.label.theme" path="theme" />
	<acme:input-textbox code="inventor.brid.form.label.code" path="code" readonly="true"/>
	<jstl:if test="${isNew == false}">
	<acme:input-moment code="inventor.brid.form.label.creationMoment" path="creationMoment" readonly="true"/>
	</jstl:if>
	<acme:input-textarea code="inventor.brid.form.label.summary" path="summary" />
	<acme:input-moment code="inventor.brid.form.label.period" path="period" />
	<acme:input-money code="inventor.brid.form.label.helping" path="helping" />
	<acme:input-url code="inventor.brid.form.label.furtherInfo" path="furtherInfo" />
	<acme:input-select code="inventor.brid.form.label.artifact" path="artifactId">
	<jstl:forEach items="${artifact}" var="artifact">
			<acme:input-option code="${artifact.code}" value="${artifact.getId()}" selected="${artifact.getId() == artifactId}"/>
	</jstl:forEach>
	</acme:input-select>
	<jstl:choose>
		<jstl:when test="${isNew == true}">
			<acme:submit code="inventor.brid.form.label.create" action="/inventor/brid/create" />
		</jstl:when>
		<jstl:otherwise>
				<acme:submit code="inventor.brid.form.label.update"
					action="/inventor/brid/update" />
				<acme:submit code="inventor.brid.form.label.delete"
					action="/inventor/brid/delete" />
		</jstl:otherwise>
	</jstl:choose>
</acme:form>
