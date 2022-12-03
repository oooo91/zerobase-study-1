<%@page import="project.service.WifiService"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="project.service.WifiService" %>    
<%@ page import="project.dto.WifiInfoDTO" %> 
  
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
  <style>
    div {
      text-align: center;
    }
    a {
      text-decoration: none;
    }
  </style>
</head>

<body>
  <!--동적으로-->
  <%
  	WifiService wifiService = new WifiService();
  	int getTotal = wifiService.getTotal();
  	pageContext.setAttribute("getTotal", getTotal);
  %>
  
  <div>
    <h1>
    
    <c:out value="${getTotal}"/>
    
    개의 WIFI 정보를 정상적으로 저장하였습니다</h1>
    <a href="/project/index.jsp">홈으로 가기</a>
  </div>
</body>
</html>