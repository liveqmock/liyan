package com.innofi.component.download.servlet;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.innofi.component.download.DownloadTool;
import com.innofi.framework.exception.FileDownloadException;
import com.innofi.framework.utils.string.StringUtil;
import com.innofi.framework.utils.validate.Validator;

public class DownloadServlet extends HttpServlet {

	Logger logger = LoggerFactory.getLogger(DownloadServlet.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse resp)
			throws ServletException, IOException {
		String displayName = request.getParameter("displayName");
		String fileIds = request.getParameter("fileIds");
		String containHidden = request.getParameter("hidden");

		boolean containHiddenFile = Boolean.valueOf(false);
		if (Validator.isNotNull(containHidden)) {
			containHiddenFile = Boolean.valueOf(StringUtil
					.getBoolean(containHidden));
		}
		logger.debug("downloadServlet displayName is [" + displayName + "]");
		logger.debug("downloadServlet fileIds is [" + fileIds + "]");
		String charset = "UTF-8";
		DownloadTool downloadTool = null;
		if (Validator.isNotNull(displayName)) {
			// displayName = new String(new
			// String(displayName.getBytes("ISO-8859-1"), charset).getBytes(),
			// "ISO-8859-1");
			displayName = new String(displayName.getBytes("ISO-8859-1"),
					charset);
		}

		if (Validator.isNull(fileIds)) {
			FileDownloadException fde = new FileDownloadException(
					"未指定下载文件唯一标识!");
			throw new ServletException(fde);
		}
		String[] unDecodeFileIds = StringUtil.split(fileIds, ",");
		String[] decodeFileIds = new String[unDecodeFileIds.length];
		for (int i = 0; i < unDecodeFileIds.length; ++i) {
			String unDecodeFileId = unDecodeFileIds[i];
			decodeFileIds[i] = URLDecoder.decode(unDecodeFileId, charset);
		}
		downloadTool = new DownloadTool(resp);
		try {
			downloadTool.processDownload(decodeFileIds, displayName,
					containHiddenFile);
		} catch (FileDownloadException e) {
			throw new ServletException(e);
		}
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
}
