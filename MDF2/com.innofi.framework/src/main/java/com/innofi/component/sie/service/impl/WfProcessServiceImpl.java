package com.innofi.component.sie.service.impl;

import com.bstek.dorado.core.el.Expression;
import com.bstek.dorado.core.el.ExpressionHandler;
import com.bstek.dorado.web.DoradoContext;
import com.innofi.component.rbac.RBACConstants;
import com.innofi.framework.service.impl.BaseServiceImpl;
import com.innofi.component.sie.SieConstants;
import com.innofi.component.sie.pojo.WfOperateStatus;
import com.innofi.component.sie.pojo.WfProcess;
import com.innofi.component.sie.pojo.WfProcessCfg;
import com.innofi.component.sie.service.IWfProcessCfgService;
import com.innofi.component.sie.service.IWfProcessInterceptor;
import com.innofi.component.sie.service.IWfProcessService;
import com.innofi.framework.FrameworkConstants;
import com.innofi.framework.exception.BaseException;
import com.innofi.framework.exception.SysMessage;
import com.innofi.framework.dao.DaoSupport;
import com.innofi.framework.dao.hibernate.PropertyFilter;
import com.innofi.framework.dao.hibernate.QueryConstants;
import com.innofi.framework.pojo.BasePojo;
import com.innofi.component.rbac.user.pojo.SysUser;
import com.innofi.framework.service.IBaseService;
import com.innofi.framework.spring.context.ContextHolder;
import com.innofi.framework.utils.console.ConsoleUtil;
import com.innofi.framework.utils.reflect.ReflectionUtil;
import com.innofi.framework.utils.string.StringUtil;
import com.innofi.framework.utils.validate.Validator;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * 功能/ 模块：todo 模块中文名称
 *
 * @author todo 开发人员姓名 邮箱
 * @version todo 当前版本 日期 todo 类描述 修订历史： 日期 作者 参考 描述 北京名晟信息技术有限公司版权所有.
 */
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
@Component(value = "wfProcessService")
public class WfProcessServiceImpl extends
        BaseServiceImpl<WfProcess, String> implements
        IWfProcessService {

    @Resource(name="mdDataTitleTableMappingDaoSupport")
    protected DaoSupport daoSupport;

    @Resource(name = "wfProcessCfgService")
    protected IWfProcessCfgService wfProcessCfgService;

    public DaoSupport getDaoSupport(){
        return daoSupport;
    }

    public WfProcess saveWfProcess(WfProcess wfProcess, WfOperateStatus wfOperateStatus, BasePojo businessObject) {
        String moduleCode = wfProcess.getModuleCode();   // 模块编码

        WfProcess wfProcessNew = null;
        Collection<IWfProcessInterceptor> interceptors = ContextHolder.getSpringBeanFactory().getBeansOfType(IWfProcessInterceptor.class).values();
        for (IWfProcessInterceptor interceptor : interceptors) {

            if (interceptor.support(moduleCode)) { //传入模块编码，判断当前Service是否支持处理，支持则在状态转换之前调用Service的Before方法，转换之后调用afer方法，不支持则什么都不做
                interceptor.before(wfProcess, businessObject);

                wfProcessNew = saveWfProcess(wfOperateStatus, wfProcess);

                interceptor.after(wfProcessNew, businessObject);

            }
        }
        return wfProcessNew;
    }

    
    
    public WfProcess saveWfProcess(WfProcess wfProcess, WfOperateStatus wfOperateStatus) {
        String moduleCode = wfProcess.getModuleCode();   // 模块编码

        WfProcess wfProcessNew = null;
        Collection<IWfProcessInterceptor> interceptors = ContextHolder.getSpringBeanFactory().getBeansOfType(IWfProcessInterceptor.class).values();
        for (IWfProcessInterceptor interceptor : interceptors) {

            if (interceptor.support(moduleCode)) { //传入模块编码，判断当前Service是否支持处理，支持则在状态转换之前调用Service的Before方法，转换之后调用afer方法，不支持则什么都不做
                
            	IBaseService baseService = (IBaseService) interceptor;
            	
            	BasePojo businessObject = baseService.get(wfProcess.getBusinessId());
            	
            	interceptor.before(wfProcess, businessObject);

                wfProcessNew = saveWfProcess(wfOperateStatus, wfProcess);

                interceptor.after(wfProcessNew, businessObject);

            }
        }
        return wfProcessNew;
    }
    
    
    public WfProcess saveWfProcess(WfOperateStatus wfOperateStatus, WfProcess wfProcess) {

        SysUser user = ContextHolder.getContext().getLoginUser();
        String orgLevel = user.getSysOrgInfo().getOrgLevel();
        String businessId = wfProcess.getBusinessId();    // 业务ID
        String moduleCode = wfProcess.getModuleCode();    // 模块编码
        String moduleName = wfProcess.getModuleName();    // 模块名称
        String operType = wfOperateStatus.getOperType();  // 操作类型
        String operDesc = wfOperateStatus.getOperDesc();  // 操作名称
        String dealIdea = wfProcess.getDealIdea();        // 处理意见
        String nodeId = wfProcess.getNodeId();            // 业务处理配置ID
        WfProcess wfProcessNew = new WfProcess();

        if (operType == null) {
            // 操作类型不能为空
        } else if (operType.equals(SieConstants.OPER_TYPE_RETURN)) {
        	
            // 当状态为待审核或待审批并且意见为空时，添加默认意见
            if ((dealIdea == null || dealIdea.equals("")) && (wfProcess.getStatus().equals(SieConstants.BUSI_STATUS_WAIT_ADUIT)
                    || wfProcess.getStatus().equals(SieConstants.BUSI_STATUS_WAIT_APPROVAL))) {
                dealIdea = new SysMessage("default.ReturnIdea").getMsgInfo();
            }
        	
            // 如果操作类型为退回，则通过业务id、模块编码，查询业务处理过程最近的记录，得到上一步业务状态。
            List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
            addPropertyFilter(filters, "businessId", businessId, QueryConstants.EQUAL, true);
            addPropertyFilter(filters, "moduleCode", moduleCode, QueryConstants.EQUAL, true);
            addPropertyFilter(filters, "isNew", FrameworkConstants.COMM_Y, QueryConstants.EQUAL, true);
            List<WfProcess> wfProcessList = this.findByPropertyFilters(filters,null);
            if (wfProcessList != null && wfProcessList.size() > 0) {
                WfProcess wfProcessOld = wfProcessList.get(0);
                String cfgId = wfProcessOld.getNodeId();
                WfProcessCfg wfCfg = wfProcessCfgService.get(cfgId);
                WfProcessCfg preNode = wfProcessCfgService.get(wfCfg.getPrevNodeId());
                ReflectionUtil.copyProperties(preNode, wfProcessNew);
                wfProcessNew.setBusinessId(businessId);
                wfProcessNew.setNodeId(preNode.getId());
            }
            wfProcessNew.setDealIdea(dealIdea);
        } else if (operType.equals(SieConstants.OPER_TYPE_RETURN_EDIT) || operType.equals(SieConstants.OPER_TYPE_CANCEL)) {
            // 如果操作类型为撤消、退回到开始，则下一步业务状态为编辑中.
            
            ArrayList orgLevelList = new ArrayList();
            orgLevelList.add(orgLevel);
            orgLevelList.add(RBACConstants.ORG_LEVEL_ALL);
            WfProcessCfg wfProcessCfg = wfProcessCfgService.findWfProcessCfg(moduleCode, orgLevelList, SieConstants.BUSI_STATUS_EDIT);

            if (wfProcessCfg != null ) {
                ReflectionUtil.copyProperties(wfProcessCfg, wfProcessNew);
                wfProcessNew.setBusinessId(businessId);
                wfProcessNew.setNodeId(wfProcessCfg.getId());
            }

        } else {
            // 当状态为待审核或待审批并且意见为空时，添加默认意见
            if ((dealIdea == null || dealIdea.equals("")) && (wfProcess.getStatus().equals(SieConstants.BUSI_STATUS_WAIT_ADUIT)
                    || wfProcess.getStatus().equals(SieConstants.BUSI_STATUS_WAIT_APPROVAL))) {
                dealIdea = new SysMessage("default.DealIdea").getMsgInfo();
            }

            // 获取下一步业务处理配置
            WfProcessCfg wfProcessCfg = wfProcessCfgService.findWfProcessCfg(nodeId);

            if (wfProcessCfg.getNodeType().equals(SieConstants.NODE_TYPE_MANUAL)) {

            } else if (wfProcessCfg.getNodeType().equals(SieConstants.NODE_TYPE_IF)) {
                if (Validator.isNotEmpty(wfProcessCfg.getCondExpre())) {
                    String expressionStr = wfProcessCfg.getCondExpre();
                    Boolean flag = false;
                    DoradoContext doradoContext = DoradoContext.getCurrent();
                    ExpressionHandler expressionHandler = null;
                    try {
                        expressionHandler = (ExpressionHandler) doradoContext.getServiceBean("expressionHandler");
                    } catch (Exception e) {
                    }

                    String[] expressions = StringUtil.split(expressionStr, ",");//多个表达式之间用逗号分隔

                    for (int i = 0; i < expressions.length; i++) {
                        String expSrc = StringUtil.split(expressions[i], "|")[0];
                        String nextNodeId = StringUtil.split(expressions[i], "|")[1];
                        Expression expression = expressionHandler.compile(expSrc);

                        if (expression != null) {
                            try {
                                flag = (Boolean) expression.evaluate();
                            } catch (Exception e) {
                                throw new BaseException("判断节点[" + wfProcessCfg.getId() + "]表达式结果不为boolean类型，请检查!");
                            }
                        }

                        if (flag) {
                            wfProcessCfg = wfProcessCfgService.get(nextNodeId);
                            break;
                        }
                    }
                } else {
                    throw new BaseException("判断节点[" + wfProcessCfg.getId() + "]表达式为空，请检查!");
                }
            } else if (wfProcessCfg.getNodeType().equals(SieConstants.NODE_TYPE_AUTO)) {

            }

            ReflectionUtil.copyProperties(wfProcessCfg, wfProcessNew);

            wfProcessNew.setId(null);
            wfProcessNew.setBusinessId(businessId);
            wfProcessNew.setModuleCode(moduleCode);
            wfProcessNew.setModuleName(moduleName);
            wfProcessNew.setNodeId(wfProcessCfg.getId());


            //-------------------------------------------设置机构--------------------------------------------------------
            if (wfProcessCfg.getIsUserOrg() != null && wfProcessCfg.getIsUserOrg().equals(FrameworkConstants.COMM_Y)) {
                wfProcessNew.setOperOrgCode(user.getOrgCode());
            } else {
                if (wfProcessCfg.getOperOrgCode() != null && wfProcessCfg.getOperOrgCode().equals(SieConstants.VARIABLE_ORG_KEY)) { //如果为变量则从frameworkContext中取对应参数
                    String orgCode = (String) DoradoContext.getCurrent().getAttribute(SieConstants.VARIABLE_ORG_KEY);
                    if (orgCode == null) throw new BaseException("未知的机构变量[" + SieConstants.VARIABLE_ORG_KEY + "],请检查！");
                    wfProcessNew.setOperOrgCode(orgCode);
                } else {
                    wfProcessNew.setOperOrgCode(wfProcessCfg.getOperOrgCode());
                }
            }

            //-------------------------------------------设置用户--------------------------------------------------------
            if (wfProcessCfg.getIsUser() != null && wfProcessCfg.getIsUser().equals(FrameworkConstants.COMM_Y)) {
                wfProcessNew.setOperUserCode(user.getUserCode());
            } else {
                if (wfProcessCfg.getOperUserCode() != null && wfProcessCfg.getOperUserCode().equals(SieConstants.VARIABLE_USER_KEY)) { //如果为变量则从frameworkContext中取对应参数
                    String userCode = (String) DoradoContext.getCurrent().getAttribute(SieConstants.VARIABLE_USER_KEY);
                    if (userCode == null)
                        throw new BaseException("未知的用户变量[" + SieConstants.VARIABLE_USER_KEY + "],请检查！");
                    wfProcessNew.setOperUserCode(userCode);
                } else {
                    wfProcessNew.setOperUserCode(wfProcessCfg.getOperUserCode());
                }
            }


            if (wfProcessCfg.getOperRoleCode() != null && wfProcessCfg.getOperRoleCode().equals(SieConstants.VARIABLE_ROLE_KEY)) {
                String roleCode = (String) DoradoContext.getCurrent().getAttribute(SieConstants.VARIABLE_ROLE_KEY);
                if (roleCode == null) throw new BaseException("未知的角色变量[" + SieConstants.VARIABLE_ROLE_KEY + "],请检查！");
                wfProcessNew.setOperRoleCode(roleCode);
            } else {
                wfProcessNew.setOperRoleCode(wfProcessCfg.getOperRoleCode());
            }

            if (wfProcessCfg != null) {
                wfProcessNew.setStatus(wfProcessCfg.getStatus());
                wfProcessNew.setIsEnd(wfProcessCfg.getIsEnd());
            }
        }

        // 保存业务处理过程
        // 将最近一条记录的“是否最近”状态更改为否
        updateWfProcess(businessId);

        wfProcessNew.setOperType(operType);
        wfProcessNew.setOperDesc(operDesc);
        wfProcessNew.setOperDate(new Date());
        wfProcessNew.setDealIdea(dealIdea);
        wfProcessNew.setIsNew(FrameworkConstants.COMM_Y);
        wfProcessNew.setId(null);


        Object object = ReflectionUtil.getFieldValue(wfProcessNew, getIdPropertyName());
        if (object != null && object instanceof String) {
            if (Validator.isEmpty((String) object)) {
                ReflectionUtil.setFieldValue(wfProcessNew, getIdPropertyName(), null);
            }
        }
        if (user != null) {
            wfProcessNew.setCrtDate(new Date()); // 设置创建日期
            wfProcessNew.setCrtUserCode(user.getUserCode()); // 设置创建用户
            wfProcessNew.setCrtOrgCode(user.getOrgCode()); // 设置创建机构
        }

        getDaoSupport().getHibernateDao().save(wfProcessNew);

        return wfProcessNew;
    }

    /**
     * 创建流程
     *
     * @param clazz      实体对象class
     * @param businessId 业务实体id
     */
    public void saveWfProcess(Class clazz, String businessId) {

        SysUser user = ContextHolder.getContext().getLoginUser();
        String orgLevel = user.getSysOrgInfo().getOrgLevel();

        String moduleCode = getModuleCode(clazz.getName());
        ConsoleUtil.info("wfprocess query module codebuilder is [" + moduleCode + "]");
        if (moduleCode == null || moduleCode.equals("")) {
            // moduleCode为空，元数据不完整
        } else {
            ArrayList orgLevelList = new ArrayList();
            orgLevelList.add(orgLevel);
            orgLevelList.add(RBACConstants.ORG_LEVEL_ALL);
            WfProcessCfg wfProcessCfg = wfProcessCfgService.findWfProcessCfg(moduleCode, orgLevelList, SieConstants.BUSI_STATUS_EDIT);

            // 判断是否进行流程配置，空为未进行配置，则不创建流程
            if (wfProcessCfg != null) {
                WfProcess wfProcess = new WfProcess();
                ReflectionUtil.copyProperties(wfProcessCfg, wfProcess);
                wfProcess.setId(null);
                wfProcess.setNodeId(wfProcessCfg.getId());
                wfProcess.setBusinessId(businessId);
                wfProcess.setModuleCode(moduleCode);
                wfProcess.setModuleName(wfProcessCfg.getModuleName());
                wfProcess.setOperType(SieConstants.OPER_TYPE_NEWADD);

                //-------------------------------------------设置机构--------------------------------------------------------
                if (wfProcessCfg.getIsUserOrg() != null && wfProcessCfg.getIsUserOrg().equals(FrameworkConstants.COMM_Y)) {
                    wfProcess.setOperOrgCode(user.getOrgCode());
                } else {
                    if (wfProcessCfg.getOperOrgCode() != null && wfProcessCfg.getOperOrgCode().equals(SieConstants.VARIABLE_ORG_KEY)) { //如果为变量则从frameworkContext中取对应参数
                        String orgCode = (String) DoradoContext.getCurrent().getAttribute(SieConstants.VARIABLE_ORG_KEY);
                        if (orgCode == null)
                            throw new BaseException("未知的机构变量[" + SieConstants.VARIABLE_ORG_KEY + "],请检查！");
                        wfProcess.setOperOrgCode(orgCode);
                    } else {
                        wfProcess.setOperOrgCode(wfProcessCfg.getOperOrgCode());
                    }
                }

                //-------------------------------------------设置用户--------------------------------------------------------
                if (wfProcessCfg.getIsUser() != null && wfProcessCfg.getIsUser().equals(FrameworkConstants.COMM_Y)) {
                    wfProcess.setOperUserCode(user.getUserCode());
                } else {
                    if (wfProcessCfg.getOperUserCode() != null && wfProcessCfg.getOperUserCode().equals(SieConstants.VARIABLE_USER_KEY)) { //如果为变量则从frameworkContext中取对应参数
                        String userCode = (String) DoradoContext.getCurrent().getAttribute(SieConstants.VARIABLE_USER_KEY);
                        if (userCode == null)
                            throw new BaseException("未知的用户量[" + SieConstants.VARIABLE_USER_KEY + "],请检查！");
                        wfProcess.setOperUserCode(userCode);
                    } else {
                        wfProcess.setOperUserCode(wfProcessCfg.getOperUserCode());
                    }
                }


                if (wfProcessCfg.getOperRoleCode() != null && wfProcessCfg.getOperRoleCode().equals(SieConstants.VARIABLE_ROLE_KEY)) {
                    String roleCode = (String) DoradoContext.getCurrent().getAttribute(SieConstants.VARIABLE_ROLE_KEY);
                    if (roleCode == null)
                        throw new BaseException("未知的角色变量[" + SieConstants.VARIABLE_ROLE_KEY + "],请检查！");
                    wfProcess.setOperRoleCode(roleCode);
                } else {
                    wfProcess.setOperRoleCode(wfProcessCfg.getOperRoleCode());
                }

                if (wfProcessCfg != null) {
                    wfProcess.setStatus(wfProcessCfg.getStatus());
                    wfProcess.setIsEnd(wfProcessCfg.getIsEnd());
                }


                wfProcess.setStatus(SieConstants.BUSI_STATUS_EDIT);
                wfProcess.setIsEnd(FrameworkConstants.COMM_N);
                wfProcess.setIsNew(FrameworkConstants.COMM_Y);

                SysUser sysUser = ContextHolder.getContext().getLoginUser();
                Object object = ReflectionUtil.getFieldValue(wfProcess, getIdPropertyName());
                if (object != null && object instanceof String) {
                    if (Validator.isEmpty((String) object)) {
                        ReflectionUtil.setFieldValue(wfProcess, getIdPropertyName(), null);
                    }
                }
                if (user != null) {
                    wfProcess.setCrtDate(new Date()); // 设置创建日期
                    wfProcess.setCrtUserCode(sysUser.getUserCode()); // 设置创建用户
                    wfProcess.setCrtOrgCode(sysUser.getOrgCode()); // 设置创建机构
                }

                BasePojo basePojo = (BasePojo) getDaoSupport().getHibernateDao().get(clazz,businessId);
                // 状态字段名
                ReflectionUtil.setFieldValue(basePojo, getStatusFieldName(), wfProcess.getStatus());
                ReflectionUtil.setFieldValue(basePojo, getNodeFieldName(), wfProcess.getNodeName());

                // 保存业务处理过程
                getDaoSupport().getHibernateDao().save(wfProcess);
            }
        }
    }
    /**
     * 创建流程 使用了AOP代理 造成CLASS需要处理
     *
     * @param String     类名
     * @param businessId 业务实体id
     */
    public void saveWfProcess(String className, String businessId) {

        SysUser user = ContextHolder.getContext().getLoginUser();
        String orgLevel = user.getSysOrgInfo().getOrgLevel();

        String moduleCode = getModuleCode(className);
        ConsoleUtil.info("wfprocess query module codebuilder is [" + moduleCode + "]");
        if (moduleCode == null || moduleCode.equals("")) {
            // moduleCode为空，元数据不完整
        } else {
            ArrayList orgLevelList = new ArrayList();
            orgLevelList.add(orgLevel);
            orgLevelList.add(RBACConstants.ORG_LEVEL_ALL);
            WfProcessCfg wfProcessCfg = wfProcessCfgService.findWfProcessCfg(moduleCode, orgLevelList, SieConstants.BUSI_STATUS_EDIT);

            // 判断是否进行流程配置，空为未进行配置，则不创建流程
            if (wfProcessCfg != null) {
                WfProcess wfProcess = new WfProcess();
                ReflectionUtil.copyProperties(wfProcessCfg, wfProcess);
                wfProcess.setId(null);
                wfProcess.setNodeId(wfProcessCfg.getId());
                wfProcess.setBusinessId(businessId);
                wfProcess.setModuleCode(moduleCode);
                wfProcess.setModuleName(wfProcessCfg.getModuleName());
                wfProcess.setOperType(SieConstants.OPER_TYPE_NEWADD);

                //-------------------------------------------设置机构--------------------------------------------------------
                if (wfProcessCfg.getIsUserOrg() != null && wfProcessCfg.getIsUserOrg().equals(FrameworkConstants.COMM_Y)) {
                    wfProcess.setOperOrgCode(user.getOrgCode());
                } else {
                    if (wfProcessCfg.getOperOrgCode() != null && wfProcessCfg.getOperOrgCode().equals(SieConstants.VARIABLE_ORG_KEY)) { //如果为变量则从frameworkContext中取对应参数
                        String orgCode = (String) DoradoContext.getCurrent().getAttribute(SieConstants.VARIABLE_ORG_KEY);
                        if (orgCode == null)
                            throw new BaseException("未知的机构变量[" + SieConstants.VARIABLE_ORG_KEY + "],请检查！");
                        wfProcess.setOperOrgCode(orgCode);
                    } else {
                        wfProcess.setOperOrgCode(wfProcessCfg.getOperOrgCode());
                    }
                }

                //-------------------------------------------设置用户--------------------------------------------------------
                if (wfProcessCfg.getIsUser() != null && wfProcessCfg.getIsUser().equals(FrameworkConstants.COMM_Y)) {
                    wfProcess.setOperUserCode(user.getUserCode());
                } else {
                    if (wfProcessCfg.getOperUserCode() != null && wfProcessCfg.getOperUserCode().equals(SieConstants.VARIABLE_USER_KEY)) { //如果为变量则从frameworkContext中取对应参数
                        String userCode = (String) DoradoContext.getCurrent().getAttribute(SieConstants.VARIABLE_USER_KEY);
                        if (userCode == null)
                            throw new BaseException("未知的用户量[" + SieConstants.VARIABLE_USER_KEY + "],请检查！");
                        wfProcess.setOperUserCode(userCode);
                    } else {
                        wfProcess.setOperUserCode(wfProcessCfg.getOperUserCode());
                    }
                }


                if (wfProcessCfg.getOperRoleCode() != null && wfProcessCfg.getOperRoleCode().equals(SieConstants.VARIABLE_ROLE_KEY)) {
                    String roleCode = (String) DoradoContext.getCurrent().getAttribute(SieConstants.VARIABLE_ROLE_KEY);
                    if (roleCode == null)
                        throw new BaseException("未知的角色变量[" + SieConstants.VARIABLE_ROLE_KEY + "],请检查！");
                    wfProcess.setOperRoleCode(roleCode);
                } else {
                    wfProcess.setOperRoleCode(wfProcessCfg.getOperRoleCode());
                }

                if (wfProcessCfg != null) {
                    wfProcess.setStatus(wfProcessCfg.getStatus());
                    wfProcess.setIsEnd(wfProcessCfg.getIsEnd());
                }


                wfProcess.setStatus(SieConstants.BUSI_STATUS_EDIT);
                wfProcess.setIsEnd(FrameworkConstants.COMM_N);
                wfProcess.setIsNew(FrameworkConstants.COMM_Y);

                SysUser sysUser = ContextHolder.getContext().getLoginUser();
                Object object = ReflectionUtil.getFieldValue(wfProcess, getIdPropertyName());
                if (object != null && object instanceof String) {
                    if (Validator.isEmpty((String) object)) {
                        ReflectionUtil.setFieldValue(wfProcess, getIdPropertyName(), null);
                    }
                }
                Date date = new Date();
                wfProcess.setOperDate(date);
                if (user != null) {
                    wfProcess.setCrtDate(date); // 设置创建日期
                    wfProcess.setCrtUserCode(sysUser.getUserCode()); // 设置创建用户
                    wfProcess.setCrtOrgCode(sysUser.getOrgCode()); // 设置创建机构
                }
                Class clazz = null;
				try {
					clazz = Class.forName(className);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//System.out.println("className  String: "+clazz.getName());
                BasePojo basePojo = (BasePojo) getDaoSupport().getHibernateDao().get(clazz,businessId);
                // 状态字段名
                ReflectionUtil.setFieldValue(basePojo, getStatusFieldName(), wfProcess.getStatus());
                ReflectionUtil.setFieldValue(basePojo, getNodeFieldName(), wfProcess.getNodeName());

                // 保存业务处理过程
                getDaoSupport().getHibernateDao().save(wfProcess);
            }
        }
    }
    /**
     * 删除流程
     *
     * @param entityName 实体对象名称
     * @param businessId 业务实体id
     */
    public void deleteWfProcess(String entityName, String businessId) {
        List<PropertyFilter> propertyFilters = new ArrayList<PropertyFilter>();
        String moduleCode = getModuleCode(entityName);
        propertyFilters.add(new PropertyFilter(
                QueryConstants.RESTRICTION_TYPE_AND, "moduleCode", moduleCode,
                QueryConstants.EQUAL));
        propertyFilters.add(new PropertyFilter(
                QueryConstants.RESTRICTION_TYPE_AND, "businessId", businessId,
                QueryConstants.EQUAL));
        this.deleteByPropertyFilters(propertyFilters);
    }

    /*
     * 将最近一条记录的“是否最近”状态更改为否
     */
    private void updateWfProcess(String businessId) {
        String sql = "UPDATE WF_PROCESS SET IS_NEW = '0' WHERE IS_NEW = '1' AND BUSINESS_ID = '" + businessId + "'";
        getDaoSupport().getJdbcDao().execute(sql);
    }

    /*
     * 通过实体名获得模块编码
     */
    public String getModuleCode(String entityName) {
        String moduleCode = "";

        String sql = "SELECT b.MENU_CODE AS MODULECODE from MD_TABLE a ,SYS_MENU b where b.MENU_URL = a.UI_NAME and a.entity_name = ?";
        List<Map<String, Object>> moduleList = getDaoSupport().getJdbcDao()
                .queryForList(sql, entityName);

        if (moduleList != null && moduleList.size() > 0) {
            moduleCode = (String) ((Map) moduleList.get(0)).get("MODULECODE");
        }

        return moduleCode;
    }

}
