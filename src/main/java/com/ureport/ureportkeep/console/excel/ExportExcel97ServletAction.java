/*******************************************************************************
 * Copyright 2017 Bstek
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package com.ureport.ureportkeep.console.excel;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ureport.ureportkeep.console.BaseServletAction;
import com.ureport.ureportkeep.console.cache.TempObjectCache;
import com.ureport.ureportkeep.console.exception.ReportDesignException;
import com.ureport.ureportkeep.core.build.ReportBuilder;
import com.ureport.ureportkeep.core.definition.ReportDefinition;
import com.ureport.ureportkeep.core.exception.ReportComputeException;
import com.ureport.ureportkeep.core.export.ExportConfigure;
import com.ureport.ureportkeep.core.export.ExportConfigureImpl;
import com.ureport.ureportkeep.core.export.ExportManager;
import com.ureport.ureportkeep.core.export.excel.low.Excel97Producer;
import com.ureport.ureportkeep.core.model.Report;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @author Jacky.gao
 * @since 2017年7月3日
 */
@Component
public class ExportExcel97ServletAction extends BaseServletAction {
	@Autowired
	private ReportBuilder reportBuilder;

	@Autowired
	private ExportManager exportManager;
	private Excel97Producer excelProducer=new Excel97Producer();
	
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String method=retriveMethod(req);
		if(method!=null){
			invokeMethod(method, req, resp);
		}else{			
			buildExcel(req, resp,false,false);
		}
	}
	public void paging(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		buildExcel(req, resp, true, false);
	}
	
	public void sheet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		buildExcel(req, resp, false, true);
	}
	
	public void buildExcel(HttpServletRequest req, HttpServletResponse resp,boolean withPage,boolean withSheet) throws IOException {
		String file=req.getParameter("_u");
		if(StringUtils.isBlank(file)){
			throw new ReportComputeException("Report file can not be null.");
		}
		String fileName=req.getParameter("_n");
		if(StringUtils.isNotBlank(fileName)){
			fileName=decode(fileName);
		}else{
			fileName="ureport.xls";
		}
		resp.setContentType("application/octet-stream;charset=ISO8859-1");
		resp.setHeader("Content-Disposition","attachment;filename=\"" + fileName + "\"");
		Map<String, Object> parameters = buildParameters(req);
		OutputStream outputStream=resp.getOutputStream();
		if(file.equals(PREVIEW_KEY)){
			ReportDefinition reportDefinition=(ReportDefinition) TempObjectCache.getObject(PREVIEW_KEY);
			if(reportDefinition==null){
				throw new ReportDesignException("Report data has expired,can not do export excel.");
			}
			Report report=reportBuilder.buildReport(reportDefinition, parameters);
			if(withPage){
				excelProducer.produceWithPaging(report, outputStream);
			}else if(withSheet){
				excelProducer.produceWithSheet(report, outputStream);
			}else{
				excelProducer.produce(report, outputStream);				
			}
		}else{
			ExportConfigure configure=new ExportConfigureImpl(file,parameters,outputStream);
			if(withPage){
				exportManager.exportExcelWithPaging(configure);
			}else if(withSheet){
				exportManager.exportExcelWithPagingSheet(configure);
			}else{
				exportManager.exportExcel(configure);
			}
		}
		outputStream.flush();
		outputStream.close();
	}
	
	public void setReportBuilder(ReportBuilder reportBuilder) {
		this.reportBuilder = reportBuilder;
	}
	public void setExportManager(ExportManager exportManager) {
		this.exportManager = exportManager;
	}

	@Override
	public String url() {
		return "/excel97";
	}
}
