package project.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import project.dao.WifiDAO;
import project.dto.HistoryInfoDTO;
import project.dto.WifiInfoDTO;

public class WifiService {
	
	static long wifiInfoTotal;
	
	WifiDAO wifiDAO = new WifiDAO();
	
	//DAO 가서 데이터 유무 확인 (전체수), 없으면 API요청, 있으면 바로 가져오기
	public int getTotal() throws IOException, ParseException {
		
		int total = wifiDAO.getTotal();
		
		if(total == 0) {
			insertWifiInfo(); 
			total = wifiDAO.getTotal();
		}
		return total;
		
	}
	
	
	//API 요청하여 DB 저장하기
	public void insertWifiInfo() throws IOException, ParseException {
		
		long start = 1;
		long end = 1000;
		
		
		callAPI(start, end);

		for(int i=1; i <= (wifiInfoTotal / 1000); i++) {
			start += 1000;
			
			if(end + 1000 >= wifiInfoTotal) {
				end = wifiInfoTotal;
			} else {
				end += 1000;
			}
			
			callAPI(start, end);
			
		}

	
	}
	
	//API 요청해서 DB 저장하기
	public void callAPI(long start, long end) throws IOException, ParseException {
		
		StringBuilder urlBuilder = new StringBuilder("http://openapi.seoul.go.kr:8088");
		
		urlBuilder.append("/" +  URLEncoder.encode("50546a4f7a6879653437615a4b6779","UTF-8") );
		urlBuilder.append("/" +  URLEncoder.encode("json","UTF-8") );
		urlBuilder.append("/" + URLEncoder.encode("TbPublicWifiInfo","UTF-8"));
		urlBuilder.append("/" + URLEncoder.encode(String.valueOf(start),"UTF-8"));
		urlBuilder.append("/" + URLEncoder.encode(String.valueOf(end),"UTF-8"));
		
		URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        
        //System.out.println("Response code: " + conn.getResponseCode()); //연결 확인
        
        BufferedReader rd;
        
        if(conn.getResponseCode() >= 200 &&  conn.getResponseCode() <= 300) {
        	rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
        	rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        
        StringBuilder sb = new StringBuilder();
        String line;
        
        while((line = rd.readLine()) != null) {
        	sb.append(line);
        }

        //System.out.println(sb.toString()); //값 확인
        
        rd.close();
        conn.disconnect();

	    JSONParser parser = new JSONParser();
	    JSONObject obj = (JSONObject)parser.parse(sb.toString());
	
	    JSONObject TbPublicWifiInfo = (JSONObject) obj.get("TbPublicWifiInfo");
	    wifiInfoTotal = (long) TbPublicWifiInfo.get("list_total_count");
	    
	    JSONArray parse_listArr = (JSONArray) TbPublicWifiInfo.get("row");
	    
		List<WifiInfoDTO> list = new ArrayList<>();
		
	    for(int i=0; i<parse_listArr.size(); i++) {
        	JSONObject wifiInfoArr = (JSONObject) parse_listArr.get(i);
        	WifiInfoDTO wifiInfo = new WifiInfoDTO();
        	 
        	wifiInfo.setMGRNO((String)wifiInfoArr.get("X_SWIFI_MGR_NO")); //관리번호
        	wifiInfo.setWRDOFC((String)wifiInfoArr.get("X_SWIFI_WRDOFC")); //자치구
        	wifiInfo.setMAINNM((String)wifiInfoArr.get("X_SWIFI_MAIN_NM")); //와이파이명
        	wifiInfo.setADRES1((String)wifiInfoArr.get("X_SWIFI_ADRES1")); //도로명주소
        	wifiInfo.setADRES2((String)wifiInfoArr.get("X_SWIFI_ADRES2")); //상세주소
        	wifiInfo.setINSTLFLOOR((String)wifiInfoArr.get("X_SWIFI_INSTL_FLOOR")); //설치위치(층)
        	wifiInfo.setINSTLTY((String)wifiInfoArr.get("X_SWIFI_INSTL_TY")); //설치유형
        	wifiInfo.setINSTLMBY((String)wifiInfoArr.get("X_SWIFI_INSTL_MBY")); //설치기관
        	wifiInfo.setSVCSE((String)wifiInfoArr.get("X_SWIFI_SVC_SE")); //서비스 구분
        	wifiInfo.setCMCWR((String)wifiInfoArr.get("X_SWIFI_CMCWR")); //망종류
        	wifiInfo.setCNSTC_YEAR((String)wifiInfoArr.get("X_SWIFI_CNSTC_YEAR")); //설치년도
        	wifiInfo.setINOUT_DOOR((String)wifiInfoArr.get("X_SWIFI_INOUT_DOOR"));; //실내외구분
        	wifiInfo.setREMARS3((String)wifiInfoArr.get("X_SWIFI_REMARS3")); //WIFI 접속환경
        	wifiInfo.setLAT(Double.parseDouble((String)wifiInfoArr.get("LAT"))); //y 좌표, Object값이 String
        	wifiInfo.setLNT(Double.parseDouble((String)wifiInfoArr.get("LNT"))); //x 좌표
			wifiInfo.setWORKDTTM((String)wifiInfoArr.get("WORK_DTTM"));
		
        	list.add(wifiInfo);
	    }
	    
	    wifiDAO.saveInfo(list);
	     
	
	}

	public List<WifiInfoDTO> getLocationList(double lat, double lnt) {
		List<WifiInfoDTO> list =  wifiDAO.getLocationList(lat, lnt);
		wifiDAO.saveLocation(lat, lnt);
		return list;
	}
	
	public void saveLocation(double lat, double lnt) {
		wifiDAO.saveLocation(lat, lnt);
	}
	
	public List<HistoryInfoDTO> getHistory() {
		return wifiDAO.getHistory();
	}
	
	public void removeHistory(int id) {
		wifiDAO.removeHistory(id);
	}
		

}
