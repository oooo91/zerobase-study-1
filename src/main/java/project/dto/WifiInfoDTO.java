package project.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class WifiInfoDTO {
	double DISTANCE; //km
	String MGRNO; //관리번호
	String WRDOFC; //자치구
	String MAINNM; //와이파이명
	String ADRES1; //도로명주소
	String ADRES2; //상세주소
	String INSTLFLOOR; //설치위치(층)
	String INSTLTY; //설치유형
	String INSTLMBY; //설치기관
	String SVCSE; //서비스구분
	String CMCWR; //망종류
	String CNSTC_YEAR; //설치년도
	String INOUT_DOOR; //실내외구분
	String REMARS3; //wifi접속환경
	double LAT; //Y좌표
	double LNT; //X좌표
	String WORKDTTM; //작업일자
	
	
	
}
