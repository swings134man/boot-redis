package com.boot.redis.config.util;

import com.boot.redis.config.util.constants.ExcelConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/************
 * @info : ExcelUtil Class
 * @name : ExcelUtil
 * @date : 2024. 6. 17. 오후 9:21
 * @author : SeokJun Kang(swings134@gmail.com)
 * @version : 1.0.0
 * @Description :
 ************/
@Slf4j
public class ExcelUtil {

    /**
     * Sxssf 대용량 Stream Download
     * @param model
     * @param request
     * @param response
     * @param addRow
     */
    public static void downloadExcelToSxssf(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response, AddRowToSXSSF addRow) {
        try {
            // 입력받은 정보 가져오기
            Map<String, Object> excelData = (Map<String, Object>) model.get(ExcelConstant.EX_EXCEL_DATA);

            // 다운로드시 사용할 파일명 가져오기
            String rawFileName = (String) model.get(ExcelConstant.EX_FILE_NAME);
            String fileName = "";
            if (StringUtils.isNotEmpty(rawFileName)) {
                fileName = createFileName(rawFileName) + "x";
                setFileNameToResponse(request, response, fileName);
            } else {
                rawFileName = ExcelConstant.EX_EXCEL_DATA;
            }

            // 워크북 생성
            SXSSFWorkbook workbook = new SXSSFWorkbook(300); // memory default maintain 100 rows -> just new SXSSFWorkbook()
            // 워크북에 새로운 시트를 생성
            SXSSFSheet sheet = workbook.createSheet(rawFileName);

            // 헤더 배경색 정보 가져오기
            IndexedColors headerBgColor = IndexedColors.YELLOW;
            if (excelData.containsKey(ExcelConstant.EX_HEADER_BG_COLOR)) {
                headerBgColor = (IndexedColors) excelData.get(ExcelConstant.EX_HEADER_BG_COLOR);
            }

            // 헤더 스타일 입력
            CellStyle headerStyle = workbook.createCellStyle();
//            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setFillPattern(FillPatternType.forInt(FillPatternType.SOLID_FOREGROUND.ordinal()));
            headerStyle.setFillForegroundColor(headerBgColor.getIndex());

            // 헤더 정보 가져오기
            LinkedHashMap<String, String> headerList = (LinkedHashMap<String, String>) excelData.get(ExcelConstant.EX_HEADER_LIST);

            // 헤더 입력
            Row headerRow = sheet.createRow(0);
            Iterator<String> keys = headerList.keySet().iterator();
            int headerCnt = 0;
            while (keys.hasNext()) {
                String headerKey = keys.next();
                Cell headerCell = headerRow.createCell(headerCnt);
                headerCell.setCellValue(headerList.get(headerKey));
                headerCell.setCellStyle(headerStyle);
                headerCnt++;
            }

            // 데이터 목록 가져오기
            addRow.row(sheet);

            response.setContentType(ExcelConstant.EX_CONTENT_TYPE);

            ServletOutputStream out = response.getOutputStream();
            workbook.write(out);

            if (out != null) {
                out.close();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }finally {
            log.info("Excel Download End");
        }
    }

    private static void setFileNameToResponse(HttpServletRequest request, HttpServletResponse response, String fileName) {
        String userAgent = request.getHeader("User-Agent");
        if (userAgent.indexOf("MSIE 5.5") >= 0) {
            response.setContentType("doesn/matter");
            response.setHeader("Content-Disposition", "filename=\"" + fileName + "\"");
        } else {
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        }
        response.setHeader("Content-Transfer-Encoding", "binary");

    }

    private static String createFileName(String fname) throws UnsupportedEncodingException {
        SimpleDateFormat fileFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String rs = new StringBuilder(fname)
                .append("_")
                .append(fileFormat.format(new Date()))
                .append(".xls")
                .toString();
        return URLEncoder.encode(rs, "UTF-8");
    }

    @FunctionalInterface
    public interface AddRow {
        void row(HSSFSheet sheet);
    }

    @FunctionalInterface
    public interface AddRowToXSSF {
        void row(XSSFSheet sheet);
    }
    @FunctionalInterface
    public interface AddRowToSXSSF {
        void row(SXSSFSheet sheet);
    }

}
