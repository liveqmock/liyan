package com.innofi.component.webservice.handler;

import javax.servlet.http.HttpServletRequest;

import org.apache.axiom.soap.SOAPHeader;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.handlers.AbstractHandler;
import org.apache.axis2.transport.http.HTTPConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.innofi.component.webservice.util.IpManager;
/**
 * 功能/ 模块： webservice
 *
 * @author  liuhuaiyang huaiyang.liu@innofi.com.cn
 * @version 2.0.0 13-5-10
 *          webservice鉴权拦截器
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
public class AuthenticationHander extends AbstractHandler {
	private static final Logger logger = LoggerFactory.getLogger(AuthenticationHander.class);
	private static final String LOCAL_HOST_IP = IpManager.getLocalHostIP(); // 本机IP地址

	public InvocationResponse invoke(MessageContext messageContext)
			throws AxisFault {

		// 根据服务名称获取其调用的方法，并且判断是否校验权限
		String serviceName = messageContext.getAxisService().getName(); // 调用服务名字
		String operationName = messageContext.getOperationContext().getOperationName();// 调用服务方法

		//根据前台的配置，判断此服务是否需要校验
		if (!this.isAuthentication(serviceName, operationName)) {
			return InvocationResponse.CONTINUE;
		}

		SOAPHeader header = (SOAPHeader) messageContext.getEnvelope().getHeader() ; //头信息
		String clientIp = getClientIpAxis(messageContext);
		String endPoint = messageContext.getAxisService().getEndpointName();
		
		//1校验header信息是否为空，如果为空则为非法请求
		if(header == null) {
			logger.info("0: 接收到无HEADER的非安全的访问 来源IP:" + clientIp) ;
			throw new AxisFault("Not have SOAP request header information!") ;
		}
		
		
		logger.info("serviceName>>>>" + serviceName);
		logger.info("clientIp>>>>>" + clientIp);
		logger.info("endpoint>>>>" + endPoint);
		logger.info("operationName>>>>" + operationName);
		return InvocationResponse.CONTINUE;
	}

	/**
	 * 根据客户端请求的服务名称和方法，判断是否需要校验，如果为true则需要校验，false则不需要校验
	 * @param serviceName
	 * @param methodName
	 * @return
	 */
	private boolean isAuthentication(String serviceName, String methodName) {
		return true;
	}

	/**
	 * 
	 * @desc 获取请求端的IP地址<br>
	 * @create Apr 9, 2012 5:14:57 PM by qzp<br>
	 * @param messageContext
	 * @return
	 */
	private static final String getClientIpAxis(MessageContext messageContext) {
		HttpServletRequest request = null;
		String ipAddress = "";
		try {
			if (messageContext != null) {
				request = (HttpServletRequest) messageContext
						.getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST);

				if (request != null) {
					ipAddress = request.getRemoteAddr();

					if (ipAddress == null || ipAddress.length() == 0
							|| ipAddress.equalsIgnoreCase("unknown")) {
						ipAddress = request.getHeader("x-forwarded-for");
					} else if (ipAddress == null || ipAddress.length() == 0
							|| ipAddress.equalsIgnoreCase("unknown")) {
						ipAddress = request.getHeader("Proxy-Client-IP");
					} else if (ipAddress == null || ipAddress.length() == 0
							|| ipAddress.equalsIgnoreCase("unknown")) {
						ipAddress = request.getHeader("WL-Proxy-Client-IP");
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ipAddress;
	}
}
