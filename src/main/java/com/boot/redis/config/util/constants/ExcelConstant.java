package com.boot.redis.config.util.constants;

/************
 * @info : Excel Constant Class
 * @name : ExcelConstant
 * @date : 2024. 6. 17. 오후 9:32
 * @author : SeokJun Kang(swings134@gmail.com)
 * @version : 1.0.0
 * @Description :
 ************/
public class ExcelConstant {

    private ExcelConstant() {
    }

    // Excel 관련 상수
    public static final String EX_CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"; // 다운로드시 엑셀 컨텐츠 타입 헤더값
    public static final String EX_FILE_NAME = "fileName"; // 다운로드시 엑셀 파일명(확장자 제외)
    public static final String EX_EXCEL_DATA = "excelData"; // ExcelView로 넘겨줄 데이터 Model 이름
    public static final String EX_HEADER_BG_COLOR = "bgColor"; // 컬럼 헤더 배경색 값
    public static final String EX_HEADER_LIST = "headerList"; // 컬럼 헤더 및 데이터 정보
    public static final String EX_ROW_LIST = "rowList"; // 데이터 List
    public static final String EX_SHEET_NAME = "sheetName"; // Excel Sheet 명

}
