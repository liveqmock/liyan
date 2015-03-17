package com.innofi.framework.dao.dynamicstatement;

import com.innofi.framework.exception.FrameworkRuntimeException;
import com.innofi.framework.utils.xml.DynamicStatementDTDEntityResolver;
import org.dom4j.Document;
import org.dom4j.Element;
import org.hibernate.internal.util.xml.MappingReader;
import org.hibernate.internal.util.xml.OriginImpl;
import org.hibernate.internal.util.xml.XmlDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.util.Iterator;

/**
 * 动态sql/hql语句组装器实现类
 *
 * @author liumy2009@126.com
 * @version 2.0.0 12-11-30
 *          类描述
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
public class DefaultDynamicStatementBuilder implements IDynamicStatementBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultDynamicStatementBuilder.class);

    private String[] fileNames = new String[0];

    private EntityResolver entityResolver = new DynamicStatementDTDEntityResolver();


    public void setFileNames(String[] fileNames) {
        this.fileNames = fileNames;
    }

    /**
     * 获取动态sql/hql文件
     *
     * @return
     */
    public String[] getFileNames() {
        return this.fileNames;
    }


    @Override
    public void loadingDynamicStatement(String dbType, String defFileName) throws IOException {
        ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
        Resource resource = resourceResolver.getResource(defFileName);
        String moduleId = defFileName.substring(defFileName.lastIndexOf("/") + 1, defFileName.lastIndexOf("."));
        buildMap(dbType, resource, moduleId);
    }


    @SuppressWarnings({"rawtypes"})
    private void buildMap(String dbType, Resource resource, String moduleId) {
        InputSource inputSource = null;
        try {
            inputSource = new InputSource(resource.getInputStream());
            XmlDocument metadataXml = MappingReader.INSTANCE.readMappingDocument(entityResolver, inputSource, new OriginImpl("file", resource.getFilename()));
            if (isDynamicStatementXml(metadataXml)) {
                final Document doc = metadataXml.getDocumentTree();
                final Element dynamicHibernateStatement = doc.getRootElement();
                Iterator rootChildren = dynamicHibernateStatement.elementIterator();
                while (rootChildren.hasNext()) {
                    final Element element = (Element) rootChildren.next();
                    final String elementName = element.getName();
                    DynamicStatementContext.put(dbType, moduleId, element.attribute("name").getText(), elementName, element.getText());
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.toString());
        } finally {
            if (inputSource != null && inputSource.getByteStream() != null) {
                try {
                    inputSource.getByteStream().close();
                } catch (IOException e) {
                    LOGGER.error(e.toString());
                    throw new FrameworkRuntimeException(e);
                }
            }
        }

    }


    private static boolean isDynamicStatementXml(XmlDocument xmlDocument) {
        return "dynamic-statement".equals(xmlDocument.getDocumentTree().getRootElement().getName());
    }
}