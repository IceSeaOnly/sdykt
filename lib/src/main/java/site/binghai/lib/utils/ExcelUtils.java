package site.binghai.lib.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.hssf.usermodel.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.LinkedHashMap;

public class ExcelUtils {

    public static Builder builder(){
        return new Builder();
    }

    public static void json2Excel(OutputStream outputStream, LinkedHashMap<String,String> mapper, JSONArray data) throws IOException {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet();
        HSSFRow row = sheet.createRow((short) 0);
        sheet.createFreezePane(0, 1);
        short col = 0;
        for (String key : mapper.keySet()) {
            cteateCell(wb, row, col++, mapper.get(key));
        }

        for (int i = 0; i < data.size(); i++) {
            HSSFRow rowi = sheet.createRow((short) (i+1));
            JSONObject obj = data.getJSONObject(i);
            col = 0;
            for (String key : mapper.keySet()) {
                cteateCell(wb, rowi, col++, obj.getString(key));
            }
        }
        wb.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }

    private static void cteateCell(HSSFWorkbook wb, HSSFRow row, short col, String val) {
        HSSFCell cell = row.createCell(col);
        cell.setCellValue(val);
        HSSFCellStyle cellstyle = wb.createCellStyle();
        cellstyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        cell.setCellStyle(cellstyle);
    }

    public static class Builder {
        private LinkedHashMap<String,String> mapper;
        private JSONArray data;
        private OutputStream os;


        public Builder() {
            this.mapper = new LinkedHashMap<>();
            this.data = new JSONArray();
            this.os = null;
        }

        public Builder outputStream(OutputStream os){
            this.os = os;
            return this;
        }

        public Builder mapper(String key,String name){
            this.mapper.put(key,name);
            return this;
        }

        public Builder addData(JSONObject object){
            this.data.add(object);
            return this;
        }

        public Builder putAll(JSONArray array){
            this.data.addAll(array);
            return this;
        }

        public void build() throws IOException {
            json2Excel(this.os,this.mapper,this.data);
        }

        public void webBuild(HttpServletResponse response,String fileName) throws IOException {
            this.os = response.getOutputStream();
            response.reset();// 清空输出流
            // 设定输出文件头,该方法有两个参数，分别表示应答头的名字和值。
            if(fileName == null){
                fileName = TimeTools.now() + "__" + data.size();
            }
            response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xls");
            build();
        }
    }

}
