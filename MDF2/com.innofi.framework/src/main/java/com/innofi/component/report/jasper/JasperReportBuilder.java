/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.innofi.component.report.jasper;

import java.io.OutputStream;
import java.util.Map;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

import org.springframework.util.Assert;

import com.innofi.component.report.domain.Report;
import com.innofi.component.report.jasper.impl.DataJasperPrintBuilder;
import com.innofi.component.report.jasper.impl.JdbcJasperPrintBuilder;
import com.innofi.component.report.jasper.impl.SpringBeanJasperPrintBuilder;
import com.innofi.component.report.jasper.listener.GlobalJasperReportGenerateListener;
import com.innofi.component.report.jasper.listener.JasperReportGenerateListener;
import com.innofi.framework.spring.context.ContextHolder;

/**
 * JasperReport构建器，实现对指定jasper文件模版装载数据生成JasperPrint对象的操作
 * 
 * @author tode.yu@bstek.com
 * @version 1.0
 */
public class JasperReportBuilder {
	
	public static final String JASPER_SPRING_DATASOURCE_KEY="CUSTOM_DATASOURCE";
	public static final String JASPER_SUBREPORT_DIR="SUBREPORT_DIR";
	public static final String SUB_REPORT_NAMES="subReportNames";
	public static final String PRINT_TYPE_HTML = "HTML";
	public static final String PRINT_TYPE_RTF = "RTF";
	public static final String PRINT_TYPE_EXCEL = "EXCEL";
	public static final String PRINT_TYPE_PDF = "PDF";
	
	/**
	 * 根据模版文件导出指定类型的报表，报表中的数据来源可以是配置在spring中的bean也可以是模版中配置的jdbc数据源.<br>
	 * 如果是jdbc数据源，只需在模版文件中添加一个参数CUSTOM_DATASOURCE,同时参数的值为空即可.<br>
	 * 如果是SpringBean,则需在模版文件中添加一个参数CUSTOM_DATASOURCE,值为springBeanId&methodName即可.<br>
	 * 如果导出的是分页数据，则需指定pageIndex和pageSize，同时SpringBean方法也需要提供这两个参数.
	 * 
	 * @param report 报表配置对象
	 * @param targetType 导出的报表类型
	 * @param outputStream 输出流
	 * @param parameters 报表使用参数和SpringBean方法使用的参数集合
	 * @throws Exception
	 */
	public void buildReportFile(Report report,String targetType,OutputStream outputStream,Map<String,Object> parameters)throws Exception{
		JasperReport jasperReport = loadJasperReport(report);
		this.beforeGenerateReportListener(report, jasperReport, parameters);
		initSubReport(parameters);
		JasperPrint jasperPrint = this.buildJasperPrint(report,jasperReport, parameters);
		this.exportReport(jasperPrint, outputStream, targetType);
	}
	
	/**
	 * 初始化子报表信息，指定子报表存放位置（BDF临时目录）,<br>
	 * 同时将子报表模版文件预先在临时目录中创建好，以备报表运行时使用
	 * @param parameters
	 * @throws Exception
	 */
	private void initSubReport(Map<String,Object> parameters) throws Exception{
		if(!parameters.containsKey(SUB_REPORT_NAMES)){
			return;
		}
		parameters.put(JASPER_SUBREPORT_DIR, ContextHolder.getIdfTempFileStorePath());
		String[] subReportNames=((String)parameters.get(SUB_REPORT_NAMES)).split(",");
//		for(int i=0;i<subReportNames.length;i++){
//			String name=subReportNames[i];
//			Collection<UploadInfo> infos=uploadService.getUploadInfosByName(name,true);
//			for(UploadInfo info:infos){
//				InputStream input=info.getContent();
//				String reportFile=ContextHolder.getBdfTempFileStorePath()+"/"+name;
//				File f=new File(reportFile);
//				if(f.exists()){
//					f.delete();
//				}
//				OutputStream output=new FileOutputStream(reportFile);
//				try{
//					IOUtils.copy(input, output);				
//				}finally{
//					input.close();
//					output.close();
//				}
//			}
//		}
	}

	/**
	 * 根据模版文件导出指定类型的报表，数据集是有调用着设定。
	 * 
	 * @param report 报表配置对象
	 * @param targetType 导出的报表类型
	 * @param outputStream 输出流
	 * @param parameters 报表使用的参数集合
	 * @param data 导出报表装载的数据
	 * @throws Exception
	 */
	public void buildReportFile(Report report,String targetType,OutputStream outputStream,Map<String,Object> parameters,Object data)throws Exception{
		JasperReport jasperReport = loadJasperReport(report);
		this.beforeGenerateReportListener(report, jasperReport, parameters);
		initSubReport(parameters);
		JasperPrint jasperPrint = this.dataJasperPrintBuilder.buildJasperPrint(report, jasperReport, parameters, data);		
		this.exportReport(jasperPrint, outputStream, targetType);
	}
	
	/**
	 * 生成JasperPrint报表对象之前，报表的监听处理操作。
	 * @param report
	 * @param jasperReport
	 * @param parameters
	 * @throws Exception
	 */
	private void beforeGenerateReportListener(Report report,JasperReport jasperReport,Map<String,Object> parameters)throws Exception{
		Map<String, GlobalJasperReportGenerateListener> globalListeners = ContextHolder.getSpringBeanFactory().getBeansOfType(GlobalJasperReportGenerateListener.class);
		for(GlobalJasperReportGenerateListener listener : globalListeners.values()){
			listener.beforeGlobalGenerate(report, jasperReport, parameters);
		}
		Map<String, JasperReportGenerateListener> listeners = ContextHolder.getSpringBeanFactory().getBeansOfType(JasperReportGenerateListener.class);
		for(JasperReportGenerateListener listener : listeners.values()){
			if(listener.support(report.getEname())){
				listener.beforeGenerate(report, jasperReport, parameters);
			}
		}
	}
	
	/**
	 * 构建JasperReport对象
	 * 
	 * @param report
	 * @return 返回JasperReport对象
	 * @throws Exception
	 */
	private JasperReport loadJasperReport(Report report)throws Exception{
//		InputStream inputStream = uploadService.get(report.getUploadFileName(), true).getContent();
//		return (JasperReport)JRLoader.loadObject(inputStream);
		return null;
	}
	
	/**
	 * 生成报表文件
	 * 
	 * @param jasperPrint
	 * @param outputStream
	 * @param targetType
	 * @throws Exception
	 */
	private void exportReport(JasperPrint jasperPrint,OutputStream outputStream,String targetType)throws Exception{
		JRAbstractExporter exporter = this.getExporter(targetType);
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);
		exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,Boolean.TRUE);
		exporter.exportReport();
		outputStream.flush();
		outputStream.close();
	}
	
	/**
	 * 构建JasperPrint对象方法，需要指定jasper模版文件，<br>
	 * 参数信息和分页信息可以为空，如果为空，则认为调用装载数据的springBean中的方法是无参方法，<br>
	 * 如果只有参数，则认为springBean方法只有一个Map类型参数，<br>
	 * 否则springBean方法有3个参数分别是Map,int当前页码,int页大小<br>
	 * 
	 * @param report 报表配置
	 * @param jasperReport jasper模版文件
	 * @param parameters 参数集合
	 * @return 返回JasperPrint对象
	 * @throws Exception
	 */
	private JasperPrint buildJasperPrint(Report report, JasperReport jasperReport,Map<String,Object> parameters)throws Exception{
		JRParameter jrParameter = this.getTemplateDataSourceConfig(jasperReport);
		if(jrParameter == null){
			return JasperFillManager.fillReport(jasperReport, parameters);
		}else{
			if(jrParameter.getDefaultValueExpression() == null){
				return jdbcJasperPrintBuilder.buildJasperPrint(report, jasperReport, parameters);
			}else{
				String text = jrParameter.getDefaultValueExpression().getText();
				parameters.put(JASPER_SPRING_DATASOURCE_KEY, text);
				String[] names = text.split("&");
				Assert.state(names.length == 2, "当前选择的数据源方式是SpringBean,需同时指定SpringBeanId和方法名称:springBeanId&methodName");
				return springBeanJasperPrintBuilder.buildJasperPrint(report, jasperReport, parameters, names[0], names[1]);
			}
		}
	}

	/**
	 * Jasper模版文件中数据源获取
	 * 
	 * @param report
	 * @return 返回JRParameter，若没有找到返回null
	 */
	private JRParameter getTemplateDataSourceConfig(JasperReport report){
		JRParameter[] defaultParameters = report.getParameters();
		for(JRParameter p : defaultParameters){
			if( p.getName().equals(JASPER_SPRING_DATASOURCE_KEY)){
				return p;
			}
		}
		return null;
	}
	
	/**
	 * 获取指定类型的导出器
	 * 
	 * @param targetType
	 * @return 返回JRAbstractExporter
	 */
	private JRAbstractExporter getExporter (String targetType){
		targetType=targetType.toUpperCase();
		if(targetType.equals(PRINT_TYPE_PDF)){
			return new JRPdfExporter();
		}else if(targetType.equals(PRINT_TYPE_EXCEL)){
			return new JRXlsExporter();
		} 
		else if(targetType.equals(PRINT_TYPE_HTML)){
			return new JRHtmlExporter();
		} 
		else if(targetType.equals(PRINT_TYPE_RTF)){
			return new JRRtfExporter();
		} else{
			throw new RuntimeException("暂不支持["+targetType+"]格式的导出");
		}
	}
	
	//private UploadService uploadService;
	private SpringBeanJasperPrintBuilder springBeanJasperPrintBuilder;
	private JdbcJasperPrintBuilder jdbcJasperPrintBuilder;
	private DataJasperPrintBuilder dataJasperPrintBuilder;
//	public void setUploadService(UploadService uploadService) {
//		this.uploadService = uploadService;
//	}
	public void setSpringBeanJasperPrintBuilder(
			SpringBeanJasperPrintBuilder springBeanJasperPrintBuilder) {
		this.springBeanJasperPrintBuilder = springBeanJasperPrintBuilder;
	}
	public void setJdbcJasperPrintBuilder(
			JdbcJasperPrintBuilder jdbcJasperPrintBuilder) {
		this.jdbcJasperPrintBuilder = jdbcJasperPrintBuilder;
	}
	public void setDataJasperPrintBuilder(
			DataJasperPrintBuilder dataJasperPrintBuilder) {
		this.dataJasperPrintBuilder = dataJasperPrintBuilder;
	}
}
