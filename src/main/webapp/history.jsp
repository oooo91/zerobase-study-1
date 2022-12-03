<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="project.service.WifiService" %>
<%@ page import="project.dto.HistoryInfoDTO" %> 
<%@ page import="java.util.*"%>
<%@ page import="java.lang.*"%>
<%@ page import="java.lang.*"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
  <style>
    div {
      margin: 10px 0;
    }
    th {
      background-color: aquamarine;
      padding: 5px 100px;
    }
    td {
   		font-size: 10px;
      padding: 1px 5px;
      text-align: center;
    }
    .a-deco {
      text-decoration: none;
    }
  </style>
</head>

<body>
  <h1>와이파이 정보 구하기</h1>

  <div class="link-collection">
    <span>
      <a href="/project/index.jsp" class="a-deco">홈</a>
      |
    </span>
    <span>
      <a href="/project/history.jsp" class="a-deco">위치 히스토리 목록</a>
      |
    </span>
    <span>
      <a href="/project/load-wifi.jsp" class="a-deco">Open API 와이파이 정보 가져오기</a>
    </span>
  </div>

<%

	List<HistoryInfoDTO> list = new ArrayList<>();
	WifiService wifiService = new WifiService(); 

	String id = request.getParameter("myId");
	
	if(id != null) {
		
		int myId = Integer.valueOf(id);
		wifiService.removeHistory(myId); 
	}

	//가져오기
	list = wifiService.getHistory();
	
	pageContext.setAttribute("list", list);
%>


  <table>
    <tr>
      <th>ID</th>
      <th>X좌표</th>
      <th>Y좌표</th>
      <th>조회일자</th>
      <th>비고</th>
    </tr>
    <!--동적으로 데이터 들어감-->

		<%if(list != null){%>
    <c:forEach var="boardDTO" items="${list}">
    <tr>
      <td>${boardDTO.historyId}</td>
      <td>${boardDTO.XCoordinate}</td>
      <td>${boardDTO.YCoordinate}</td>
      <td>${boardDTO.historyDate}</td>
      <td><input type="button" value="삭제" onClick="letDelete(${boardDTO.historyId})"></td>
    </tr>
    </c:forEach>
   <%} %>
   
  </table>
 
  <script>
  	function letDelete(num) {
  		var con = confirm('삭제하시겠습니까?');
  		
  		if (con == true) {
  			alert('삭제되었습니다');
  			location.href="/project/history.jsp?myId=" + num;
  		} else {
  			alert('취소되었습니다.');
  		}
  	}  
  </script>
</body>
</html>