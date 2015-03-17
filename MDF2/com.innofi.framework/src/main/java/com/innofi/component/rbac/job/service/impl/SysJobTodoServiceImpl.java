package com.innofi.component.rbac.job.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.innofi.component.metadata.commons.MetadataConstants;
import com.innofi.component.rbac.RBACConstants;
import com.innofi.component.rbac.job.pojo.SysJobTodo;
import com.innofi.component.rbac.job.service.ISysJobTodoService;
import javax.annotation.Resource;

import com.innofi.framework.service.impl.BaseServiceImpl;
import com.innofi.framework.FrameworkConstants;
import com.innofi.component.rbac.post.pojo.SysPost;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.innofi.framework.dao.DaoSupport;
import com.innofi.framework.dao.pagination.Page;
import com.innofi.component.rbac.privilage.pojo.SysFunAction;
import com.innofi.component.rbac.role.pojo.SysRole;
import com.innofi.component.rbac.user.pojo.SysUser;
import com.innofi.framework.spring.context.ContextHolder;
import com.innofi.framework.utils.console.ConsoleUtil;

/**
 * 功能/ 模块：todo 模块中文名称
 * 
 * @author todo 开发人员姓名 邮箱
 * @version todo 当前版本 日期 todo 类描述 修订历史： 日期 作者 参考 描述 北京名晟信息技术有限公司版权所有.
 */
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
@Service(value = "sysJobTodoService")
public class SysJobTodoServiceImpl extends BaseServiceImpl<SysJobTodo, String> implements ISysJobTodoService {
 
	@Resource(name="mdDataTitleTableMappingDaoSupport")
	protected DaoSupport daoSupport;

	public DaoSupport getDaoSupport() {
		return daoSupport;
	}

	public void findSysJobTodoByUserRole(String jobId, Page innofiPage) {
		if (jobId != null && !StringUtils.isBlank(jobId)) {// 测试
			String hql = "select c from SysJobTodo c where c.id=? and c.status='"
					+ FrameworkConstants.COMM_Y + "'";
			this.findByHql_Page(hql, innofiPage, jobId);
		} else {// 主页待办
			List<SysFunAction> userActions = (List<SysFunAction>) ContextHolder
					.getRequest().getSession()
					.getAttribute(MetadataConstants.SESSION_USER_ACTION);// 权限按钮
			if (userActions.size() > 0) {
				List<String> actionIds = new ArrayList<String>();
				for (SysFunAction action : userActions) {
					actionIds.add(action.getId());
				}
				String hql = "select distinct c from SysJobTodo c where c.actionId in (:actionId) and c.status='"
						+ FrameworkConstants.COMM_Y + "'";
				HashMap paramMap = new HashMap();
				ConsoleUtil.info("action list is " + actionIds.toString());
				paramMap.put("actionId", actionIds);
				getDaoSupport().getHibernateDao().findByNamedHql_Page(
						getEntityType(), hql, innofiPage, paramMap);
			}
		}
		List<SysJobTodo> jobs = innofiPage.getEntities();
		List<SysJobTodo> zeroJobs = new ArrayList<SysJobTodo>();
		for (SysJobTodo job : jobs) {
			ConsoleUtil.info("job is :" + job.toString());
			String jobSql = job.getSeleCountSql();
			if (!StringUtils.isBlank(jobSql)) {
				String querySql = parseJobSql(jobSql);
				String privilegeSql = "";//assemblySqlRestrictions(querySql); //todo
				ConsoleUtil.info("job todo query sql is:" + querySql + "\n");
				int jobCount = getDaoSupport().getJdbcDao().queryForInt(querySql);
				job.setJobCount(jobCount + "个");
				if (jobCount == 0) {
					zeroJobs.add(job);
				}
			}
		}
		jobs.removeAll(zeroJobs);
		innofiPage.setEntities(jobs);
		innofiPage.setEntityCount(jobs.size());
	}

	private String parseJobSql(String sql) {
		if (sql.indexOf(RBACConstants.JOB_CURR_USER) > 0) {
			SysUser user = ContextHolder.getContext().getLoginUser();
			sql = sql.replaceAll(RBACConstants.JOB_CURR_USER,
					" '" + user.getId() + "' ");
		}
		if (sql.indexOf(RBACConstants.JOB_CURR_ORG) > 0) {
			SysUser user = ContextHolder.getContext().getLoginUser();
			sql = sql.replaceAll(RBACConstants.JOB_CURR_ORG,
					" '" + user.getOrgCode() + "' ");
		}
		if (sql.indexOf(RBACConstants.JOB_CURR_ROLE) > 0) {
			SysUser user = ContextHolder.getContext().getLoginUser();
			List<SysRole> roles = user.getFunctionRoles();
			StringBuilder buff = new StringBuilder();
			if (roles.size() > 0) {
				for (int i = 0; i < roles.size(); i++) {
					SysRole role = roles.get(i);
					buff.append("'").append(role.getId()).append("'");
					if (i < roles.size() - 1) {
						buff.append(",");
					}
				}
			} else {
				buff.append("''");
			}
			sql = sql.replaceAll(RBACConstants.JOB_CURR_ROLE, buff.toString());
		}

		if (sql.indexOf(RBACConstants.JOB_CURR_POST) > 0) {
			SysUser user = ContextHolder.getContext().getLoginUser();
			List<SysPost> posts = user.getPosts();
			StringBuilder buff = new StringBuilder();
			if (posts.size() > 0) {
				for (int i = 0; i < posts.size(); i++) {
					SysPost post = posts.get(i);
					buff.append("'").append(post.getId()).append("'");
					if (i < posts.size() - 1) {
						buff.append(",");
					}
				}
			} else {
				buff.append("''");
			}
			sql = sql.replaceAll(RBACConstants.JOB_CURR_POST, buff.toString());
		}

		return sql;
	}
}
