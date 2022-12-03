<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="project.service.WifiService" %>
<%@ page import="project.dto.WifiInfoDTO" %> 
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
	    padding: 5px 0;
	  }
	  th {
	    padding: 5px 10px;
	    background-color: aquamarine;
	  }
	  .a-deco {
	    text-decoration: none;
	  }
	   td {
	  	font-size: 10px;
			text-align: center;
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
	String lat = request.getParameter("my-location-lat");
	String lnt = request.getParameter("my-location-lnt");

	List<WifiInfoDTO> list = new ArrayList<>();
	
	pageContext.setAttribute("lat", lat);
	pageContext.setAttribute("lnt", lnt);
%>


  <div class="btn-collection">
	    <span>
	      LAT: 
	    </span>
	    <input type="text" id="my-location-lat" name="my-location-lat" value="${lat}"> 
	    
	    <span>
	      LNT: 
	    </span>
	    <input type="text" id="my-location-lnt" name="my-location-lnt" value="${lnt}">
	
	    <input type="button" onclick="myLocationSearch()" value="내 위치 가져오기">
	    <input type="button" onClick="letSubmit()" value="근처 WIFI 정보 보기">
  </div>

  <table id="inputInfo">
    <tr>
      <th>거리(Km)</th>
      <th>관리번호</th>
      <th>자치구</th>
      <th>와이파이명</th>
      <th>도로명주소</th>
      <th>상세주소</th>
      <th>설치위치(층)</th>
      <th>설치유형</th>
      <th>설치기관</th>
      <th>서비스구분</th>
      <th>망종류</th>
      <th>설치년도</th>
      <th>실내외구분</th>
      <th>WIFI접속환경</th>
      <th>X좌표</th>
      <th>Y좌표</th>
      <th>작업일자</th>
    </tr>

 <%
	if(lat == null && lnt == null || lat == "" && lnt == "") {
			list = null;
		} else {
		
			Double latitude =  Double.parseDouble(lat);
			Double longitude = Double.parseDouble(lnt);
			
			WifiService wifiService = new WifiService(); 
			
			list = wifiService.getLocationList(latitude, longitude);   
			pageContext.setAttribute("list", list);
		}
	%>

	<%if(list != null){%>
    <c:forEach var="boardDTO" items="${list}">
    <tr>
      <td>${boardDTO.DISTANCE}</td>
      <td>${boardDTO.MGRNO}</td>
      <td>${boardDTO.WRDOFC}</td>
      <td>${boardDTO.MAINNM}</td>
   		<td>${boardDTO.ADRES1}</td>
      <td>${boardDTO.ADRES2}</td>
      <td>${boardDTO.INSTLFLOOR}</td>
      <td>${boardDTO.INSTLTY}</td>
      <td>${boardDTO.INSTLMBY}</td>
      <td>${boardDTO.SVCSE}</td>
      <td>${boardDTO.CMCWR}</td>
      <td>${boardDTO.CNSTC_YEAR}</td>
      <td>${boardDTO.INOUT_DOOR}</td>
      <td>${boardDTO.REMARS3}</td>
      <td>${boardDTO.LAT}</td>
      <td>${boardDTO.LNT}</td>
      <td>${boardDTO.WORKDTTM}</td>
    </tr>
    </c:forEach>
   <%} %>
  </table>
  
<script>

  function myLocationSearch() {
	  navigator.geolocation.getCurrentPosition(success, onError)

	  function success(position) {
	    console.log(position);
	    document.getElementById('my-location-lat').value = position.coords.latitude;
	    document.getElementById('my-location-lnt').value = position.coords.longitude;
	  }
	  function onError() {
	    alert("위치를 찾을 수 없습니다")
	  }
	}
	
	function letSubmit() {
		var lat = document.getElementById('my-location-lat').value;
		var lnt = document.getElementById('my-location-lnt').value;
		
		if(lat == "" || lnt == "") {
			alert('값이 비어있습니다');
		} else {
			location.href="/project/index.jsp?my-location-lat=" + lat + "&my-location-lnt=" + lnt;
		}
		
	}
</script>
</body>
</html>