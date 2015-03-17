package com.innofi.framework.spring.context;

import com.bstek.dorado.web.DoradoContext;

import com.innofi.component.rbac.user.pojo.SysUser;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * 框架上下文包装类,用于包装框架上下文相关信息
 *
 * @author jack.liu@innofi.com
 * @version 2.0
 */
public abstract class FrameworkContext implements Serializable {

    private static final long serialVersionUID = -2554263226946218229L;
    protected SysUser loginUser;
    protected String loginUsername;
    private boolean needSave = false;

    public void resetStatus() {
        this.needSave = false;
    }

    /**
     * 用来替换HttpSession存储功能，这里具体存储到哪里取决于具体的ContextRepository的实现，默认实现是放在HttpSession当中
     */
    private Map<String, Object> contextMap = new HashMap<String, Object>();

    /**
     * 是否系统超级管理员
     * @return
     */
    public boolean isAdmin(){
        SysUser sysUser = getLoginUser();
        if(ContextHolder.getSystemProperties().getString("mdf.system.admin.account").indexOf(sysUser.getUserCode()) == -1)return false;
        return true;
    }

    /**
     * 是否系统超级管理员
     * @return
     */
    public boolean isNotAdmin(){
        return !isAdmin();
    }

    /**
     * 是否系统超级管理员
     * @return
     */
    public boolean isAdmin(SysUser sysUser){
        if(ContextHolder.getSystemProperties().getString("mdf.system.admin.account").indexOf(sysUser.getUserCode()) == -1)return false;
        return true;
    }

    /**
     * 获取Dorado上下文对象
     * @return
     */
    public DoradoContext getDoradoContext(){
        return DoradoContext.getCurrent();
    }

    /**
     * 是否需要保存
     * @return
     */
    public boolean isNeedSave() {
        return this.needSave;
    }

    /**
     * 将某个值放在会话上下文中，生命周期与HttpSession相同
     *
     * @param key   对象的key
     * @param value 具体对象的值
     */
    public void setContextValue(String key, Object value) {
        if (!this.needSave) {
            this.needSave = true;
        }
        this.contextMap.put(key, value);
    }

    /**
     * 从会话上下文中取出某个key对应的值，生命周期与HttpSession相同
     *
     * @param key 对象的key
     * @return 返回与该key对象的值
     */
    @SuppressWarnings("unchecked")
    public <T> T getContextValue(String key) {
        return (T) this.contextMap.get(key);
    }

    /**
     * 从会话上下文中移除某个key对应的值，生命周期与HttpSession相同
     *
     * @param key 对象的key
     */
    public void removeContextValue(String key) {
        if (!this.needSave) {
            this.needSave = true;
        }
        this.contextMap.remove(key);
    }

    /**
     * 得到当前登录用户的User对象
     *
     * @return 返回 当前登录的用户对象
     */
    public SysUser getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(SysUser loginUser) {
        if (!this.needSave) {
            this.needSave = true;
        }
        this.loginUser = loginUser;
    }

    public String getLoginUsername() {
        return loginUsername;
    }

    public void setLoginUsername(String loginUsername) {
        if (!this.needSave) {
            this.needSave = true;
        }
        this.loginUsername = loginUsername;
    }

    public boolean isLogin() {
    	//return getLoginUser()==null?false:true;
    	try{
    		this.getLoginUser();
    	}catch(Exception ex){
    		return false;
    	}
    	return true;
    	
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((contextMap == null) ? 0 : contextMap.hashCode());
        result = prime * result
                + ((loginUser == null) ? 0 : loginUser.hashCode());
        result = prime * result
                + ((loginUsername == null) ? 0 : loginUsername.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        FrameworkContext other = (FrameworkContext) obj;
        if (contextMap == null) {
            if (other.contextMap != null)
                return false;
        } else if (!contextMap.equals(other.contextMap))
            return false;
        if (loginUser == null) {
            if (other.loginUser != null)
                return false;
        } else if (!loginUser.equals(other.loginUser))
            return false;
        if (loginUsername == null) {
            if (other.loginUsername != null)
                return false;
        } else if (!loginUsername.equals(other.loginUsername))
            return false;
        return true;
    }

}
