
<%@ include file="/html/init.jsp" %>

<portlet:renderURL var="portletURL" />
<h3><a href="${portletURL}">></a>
<c:if test="${not empty course}">
	<portlet:actionURL var="viewCourseURL" name="viewCourse">
    	<portlet:param name="id" value="${course.primaryKey}" />
	</portlet:actionURL>
	<liferay-ui:message key="model.resource.com.liferay.lms.model.Course" />: <a href="${viewCourseURL}">${course.getTitle(themeDisplay.locale)}</a>
 		<c:if test="${not empty la}">
 			> <liferay-ui:message key="model.resource.com.liferay.lms.model.LearningActivity" />: ${la.getTitle(themeDisplay.locale)}
 		</c:if>
</c:if> 
</h3>