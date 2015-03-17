/*
 * This file is part of BDF
 * BDF，Bstek Development Framework
 * Copyright 2002-2012, BSTEK
 * Dual licensed under the Bstek Commercial or GPL Version 2 licenses.
 * http://www.bstek.com/
 */
package com.innofi.component.report.job.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.innofi.framework.spring.context.ContextHolder;
import org.apache.derby.iapi.services.i18n.MessageService;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.innofi.component.dbconsole.ConnectionDocument.Connection.User;
import com.innofi.component.report.domain.ReportJob;
import com.innofi.component.report.domain.ReportJobConfig;
import com.innofi.component.report.domain.ReportMember;
import com.innofi.component.report.job.ReportJobListener;
import com.innofi.component.report.service.IReportService;
import com.saxonica.bytecode.util.MessageTemplate;

/**
 * 默认报表生成监听器实现，用于在发送邮件给指定的用户
 *
 * @author tode.yu@bstek.com
 * @version 1.0
 */
public class SendMailReportJobListener extends ReportJobListener {

    /*
      * (non-Javadoc)
      * @see com.bstek.bdf.report.job.ReportJobListener#getUrl()
      */
    public String getUrl() {
        return "d7.report.config.UserDeptGroupConfig";
    }

    /*
      * (non-Javadoc)
      * @see com.bstek.bdf.report.job.ReportJobListener#getDesc()
      */
    public String getDesc() {
        return "发送邮件接收者选择";

    }

    /*
      * (non-Javadoc)
      * @see com.bstek.bdf.report.job.ReportJobListener#afterGenerateReport(com.bstek.bdf.report.domain.ReportJobConfig, java.util.Collection, java.util.ArrayList)
      */
    public void afterGenerateReport(ReportJobConfig reportJobConfig, Collection<ReportJob> reports, ArrayList<File> reportFiles) {
        Collection<ReportMember> reportMembers = this.reportService.findReportMembers(reportJobConfig.getId(), null);
        Set<User> users = new HashSet<User>();
        for (ReportMember reportMember : reportMembers) {
            if (reportMember.getType().equals(ReportMember.USER_MEMBER)) {
                this.userMember(reportMember.getMemberId(), users);
            } else if (reportMember.getType().equals(ReportMember.DEPT_MEMBER)) {
                this.deptMember(reportMember.getMemberId(), users);
            } else if (reportMember.getType().equals(ReportMember.GROUP_MEMBER)) {
                this.groupMember(reportMember.getMemberId(), users);
            }
        }
        MessageTemplate messageTemplate = this.getMessageTemplate(reportJobConfig);
        this.sendMail(users, reportFiles, messageTemplate);
    }

    /*
      * (non-Javadoc)
      * @see com.bstek.bdf.report.job.ReportJobListener#afterDeleteConfig(com.bstek.bdf.report.domain.ReportJobConfig)
      */
    public void afterDeleteConfig(ReportJobConfig reportJobConfig) {
        Collection<ReportMember> list = this.reportService.findReportMembers(reportJobConfig.getId(), null);
        List<String> ids = new ArrayList<String>();
        for (ReportMember rm : list) {
            ids.add(rm.getId());
        }
        this.reportService.deleteReportJobMember(ids);
    }

    /**
     * 获取用户对象
     *
     * @param memberId 用户名
     * @param users    用户集合
     */
    private void userMember(String memberId, Set<User> users) {
//		users.add(this.frameworkService.findUserByUsername(memberId));
    }

    /**
     * 获取部门对象
     *
     * @param memberId 部门编号
     * @param users    用户集合
     */
    private void deptMember(String memberId, Set<User> users) {
//		Collection<User> tempUsers = frameworkService.findUsersByDeptId(memberId);
//		for(User user : tempUsers){
//			users.add(user);
//		}
    }

    /**
     * 获取群组对象
     *
     * @param memberId 群组编号
     * @param users    用户集合
     */
    private void groupMember(String memberId, Set<User> users) {
//		Collection<GroupMember> groupMembers = groupService.findGroupMemberByGroupId(memberId);
//		for(GroupMember groupMember : groupMembers){
//			if(groupMember.getType().equals(GroupMember.USER_MEMBER)){
//				this.userMember(groupMember.getMemberId(),users);
//			}else if(groupMember.getType().equals(GroupMember.DEPT_MEMBER)){
//				this.deptMember(groupMember.getMemberId(),users);
//			}
//		}
    }

    private MessageTemplate getMessageTemplate(ReportJobConfig reportJobConfig) {
//		String module = ReportMessageVariableRegister.class.getName().replace(".","_");
//		Collection<MessageTemplateUse> uses = this.messageTemplateService.findMessageTemplateUse(reportJobConfig.getId(), null, module, null, null, null, null);
//		if(uses.size() > 0){
//			return this.messageTemplateService.findMessageTempate(uses.iterator().next().getMessageTemplate().getId());
//		}else {
//			return null;
//		}
        return null;
    }

    /**
     * 发送邮件
     *
     * @param users 邮件接收用户列表
     * @param files 发送的邮件的附件列表
     */
    private void sendMail(Set<User> users, ArrayList<File> files, MessageTemplate messageTemplate) {
//		MessagePackage mp;		
//		
//		Set<String> fileNames = new HashSet<String>();
//		for(File file : files){
//			fileNames.add(file.getAbsolutePath());
//		}
//		String content = null;
//		String title = null;
//		if(messageTemplate != null){
//			content = messageTemplate.getContent();
//			title = messageTemplate.getTitle();
//		}
//		if(title == null)
//			title="报表提醒邮件";
//		if(content == null)
//			content ="查看附件中的报表";
//		mp= messageService.buildPackage();
//		mp.sender(this.getSystemAdminUserName()).title(title).addReceivers(users).content(content).attachment(fileNames).addChannel(EmailChannel.class).send();
    }

    /**
     * 获取系统管理员名称
     *
     * @return 返回获取系统管理员名称
     */
    private String getSystemAdminUserName() {
        String userNames = ContextHolder.getSystemProperties().getString("mdf.system.admin.account");
        if (StringUtils.hasText(userNames)) {
            String[] strs = userNames.split(",");
            Assert.state(strs.length > 0, "未配置系统管理员，无法发送消息.");
            return strs[0];
        } else {
            throw new RuntimeException("未配置系统管理员，无法发送消息.");
        }
    }

    private IReportService reportService;
    //private FrameworkService frameworkService;
    //private GroupService groupService;
    private MessageService messageService;
    //private MessageTemplateService messageTemplateService;

    public void setReportService(IReportService reportService) {
        this.reportService = reportService;
    }
//	public void setFrameworkService(FrameworkService frameworkService) {
//		this.frameworkService = frameworkService;
//	}
//	public void setGroupService(GroupService groupService) {
//		this.groupService = groupService;
//	}
//	public void setMessageService(MessageService messageService) {
//		this.messageService = messageService;
//	}
//	public void setPreferenceService(PreferenceService preferenceService) {
//		this.preferenceService = preferenceService;
//	}
//	public void setMessageTemplateService(
//			MessageTemplateService messageTemplateService) {
//		this.messageTemplateService = messageTemplateService;
//	}
}
