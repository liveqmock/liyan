/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.component.metadata.datatitle.service;

import com.innofi.framework.pojo.metadata.MdDataTitle;
import com.innofi.framework.service.IBaseService;

import java.util.List;

/**
 * 功能/ 模块：数据主题模块
 *
 * @author 张磊 Alex.zhang@innofi.com.cn
 * @version 2.0.0 12-11-30
 *          数据主题Service接口
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
public interface IMdDataTitleService extends IBaseService<MdDataTitle, String> {
    /**
     * 通过父ID查询子集
     *
     * @param parentId
     * @return
     */
    public List<MdDataTitle> findMdDataTitlesByParentId(String parentId);


    /**
     * 删除数据主题
     *
     * @param mdDataTitle 数据主题对象
     */
    public void deleteDataTitle(MdDataTitle mdDataTitle);


}