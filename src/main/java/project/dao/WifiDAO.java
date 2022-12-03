package project.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import project.dto.HistoryInfoDTO;
import project.dto.WifiInfoDTO;

public class WifiDAO {

	private String driver = "org.mariadb.jdbc.Driver";
	private String url = "jdbc:mariadb://localhost:3306/project";
	private String username = "projectuser";
	private String password = "zerobase";

	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	public WifiDAO() {
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	} 
	
	public void getConnection() {
		try {
			conn = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		try {
			if(rs!=null) rs.close();
			if(pstmt!=null) pstmt.close();
            if(conn!=null) conn.close();
         }catch (SQLException e) { 
            e.printStackTrace();
         }
	}
	
	public int getTotal() {
		int totalA = 0;
		String sql = "select count(*) from wifiInfoDB";
		
		getConnection();
		
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			rs.next();
			totalA = rs.getInt(1);
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return totalA;
		
	}
	
	public void saveInfo(List<WifiInfoDTO> list) {
		
		String sql = "insert into wifiInfoDB values"
				+ "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		getConnection();
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			for(WifiInfoDTO dto : list) {
			      pstmt.setString(1, dto.getMGRNO());
			      pstmt.setString(2, dto.getWRDOFC());
			      pstmt.setString(3, dto.getMAINNM());
			      pstmt.setString(4, dto.getADRES1());
			      pstmt.setString(5, dto.getADRES2());
			      pstmt.setString(6, dto.getINSTLFLOOR());
			      pstmt.setString(7, dto.getINSTLTY());
			      pstmt.setString(8, dto.getINSTLMBY());
			      pstmt.setString(9, dto.getSVCSE());
			      pstmt.setString(10, dto.getCMCWR());
			      pstmt.setString(11, dto.getCNSTC_YEAR());
			      pstmt.setString(12, dto.getINOUT_DOOR());
			      pstmt.setString(13, dto.getREMARS3());
			      pstmt.setDouble(14, dto.getLAT());
			      pstmt.setDouble(15, dto.getLNT());
			      pstmt.setTimestamp(16, java.sql.Timestamp.valueOf(dto.getWORKDTTM()));
			      
			      pstmt.executeUpdate();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		

		
	}

	public List<WifiInfoDTO> getLocationList(double lat, double lnt) {
		
		List<WifiInfoDTO> list = new ArrayList<>();
		
		String sql = "select *, " +
					"(6371*acos(cos(radians(" + lat + "))*cos(radians(w.lat))*cos(radians(w.lnt)" + 
			    	"-radians(" + lnt + "))+sin(radians(" + lat + "))*sin(radians(w.lat)))) " +   		
			    	"AS distance " +
			    	"FROM wifiinfodb w order by distance asc limit 20";
		
		getConnection();
		
		try {
			
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				
				WifiInfoDTO dto = new WifiInfoDTO();
			
				dto.setDISTANCE(rs.getDouble("distance")); //거리
				System.out.println(rs.getDouble("distance"));
				dto.setMGRNO(rs.getString("mgr_no"));
				dto.setWRDOFC(rs.getString("wrdofc"));
				dto.setMAINNM(rs.getString("main_nm"));
				dto.setADRES1(rs.getString("address1"));
	            dto.setADRES2(rs.getString("address2"));
	            dto.setINSTLFLOOR(rs.getString("install_floor"));
	            dto.setINSTLTY(rs.getString("install_ty"));
				dto.setINSTLMBY(rs.getString("install_mby"));
				dto.setSVCSE(rs.getString("svc_se"));
				dto.setCMCWR(rs.getString("cmcwr"));
				dto.setCNSTC_YEAR(rs.getString("cnstc_year"));
	            dto.setINOUT_DOOR(rs.getString("inout_door"));
	            dto.setREMARS3(rs.getString("remars3"));
				dto.setLAT(rs.getDouble("lat"));
	            dto.setLNT(rs.getDouble("lnt"));
	            
	            Timestamp ts = rs.getTimestamp("dttm");
	            SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
	     
	            dto.setWORKDTTM(sdf.format(ts));
	            
				list.add(dto);

			}//while
			
		}  catch (SQLException e) {
			e.printStackTrace();
			list = null;
		} finally {
			close();
		}

		return list;
	}
	
	public void saveLocation(double lat, double lnt) {
		String sql = "insert into historyDB values (NULL," + lat + ", " + lnt + ", now())";
		
		getConnection();
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		
	}
	
	public List<HistoryInfoDTO> getHistory() {
		
		List<HistoryInfoDTO> list = new ArrayList<>();
		
		String sql = "select * from historydb order by historyId desc";
		
		getConnection();
		
		
		try {
		
		pstmt = conn.prepareStatement(sql);
		rs = pstmt.executeQuery();
		
		while(rs.next()) {
			
			HistoryInfoDTO dto = new HistoryInfoDTO();
		
			dto.setHistoryId(rs.getInt("historyId"));
			dto.setXCoordinate(rs.getDouble("xCoordinate"));
			dto.setYCoordinate(rs.getDouble("yCoordinate"));
			
            Timestamp ts = rs.getTimestamp("historyDate");
            SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
     
            dto.setHistoryDate(sdf.format(ts));
            
			list.add(dto);
	
			}//while
		
		}  catch (SQLException e) {
			e.printStackTrace();
			list = null;
		} finally {
			close();
		}
	
		return list;
	}

	public void removeHistory(int id) {
		
		String sql = "delete from historydb where historyId =" + id;
		
		getConnection();
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}
	
	
}
