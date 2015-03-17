package com.innofi.component.dbconsole.service.impl;

import com.innofi.component.dbconsole.PrefsDocument;
import com.innofi.component.dbconsole.dao.DbConsoleDaoSupport;
import com.innofi.component.dbconsole.executer.QueryTask;
import com.innofi.component.dbconsole.executer.QueryTaskObserver;
import com.innofi.component.dbconsole.pojo.*;
import com.innofi.component.dbconsole.service.IDbConsoleService;
import com.innofi.framework.dao.jdbc.IJdbcDao;
import com.innofi.framework.dao.pagination.Page;
import com.innofi.framework.exception.FrameworkJdbcRuntimeException;
import com.innofi.framework.utils.console.ConsoleUtil;
import com.innofi.framework.utils.resource.ResourceUtil;
import com.innofi.framework.utils.sql.SqlUtil;
import com.innofi.framework.utils.string.StringUtil;
import com.innofi.framework.utils.validate.Validator;
import com.innofi.framework.utils.variable.VariableHelper;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.*;

/**
 * Copyright (c) 2010-2012 北京名晟信息技术有限公司 all rights reserved.
 * 
 * @author liumy
 * @version 2.0
 *          <p/>
 *          数据库控制台服务实现，具有事务操作
 * @found date: 2012-5-20
 * @found time: 下午3:47:07
 */
@Component(value = "dbConsoleService")
public class DbConsoleServiceImpl implements IDbConsoleService {
    @Override
    public DbConsoleDbInfo loadDbInfo(String groupId, String connName) {
        return null;
    }

    @Override
    public List<DbConsoleGroup> loadGroups() {
        return null;
    }

    @Override
    public List<DbConsoleConnection> loadConnections(String groupId) {
        return null;
    }

    @Override
    public List<DbConsoleTemplate> loadDbTemplates() {
        return null;
    }

    @Override
    public List<DbConsoleObjectType> loadObjectTypes(String groupId, String connName) {
        return null;
    }

    @Override
    public List<DbConsoleSchema> loadSchemas(String groupId, String connName) {
        return null;
    }

    @Override
    public List<DbConsoleSchema> loadSchemasForSqlEditor(String groupId, String connName) {
        return null;
    }

    @Override
    public List<DbConsoleTable> loadObjects(String groupId, String connName, String schema, String objectName, String types) {
        return null;
    }

    @Override
    public List<DbConsoleTable> loadTablesBySchema(String groupId, String connName, String schema) {
        return null;
    }

    @Override
    public DbConsoleSequence loadSequence(String groupId, String connName, String schema, String seqName) {
        return null;
    }

    @Override
    public List<DbConsoleColumn> loadColumns(String groupId, String connName, String schema, String tableName) {
        return null;
    }

    @Override
    public List<DbConsoleColumn> loadColumnType(String groupId, String connName) {
        return null;
    }

    @Override
    public String loadObjectDDL(String groupId, String connName, String type, String schema, String objectName) {
        return null;
    }

    @Override
    public void insertColumn(String groupId, String connName, String schema, String tableName, DbConsoleColumn columnInfo) {

    }

    @Override
    public void updateColumn(String groupId, String connName, String schema, String tableName, DbConsoleColumn oldColumnInfo, DbConsoleColumn newColumnInfo) {

    }

    @Override
    public void dropColumn(String groupId, String connName, String schema, String tableName, String columnName) {

    }

    @Override
    public String formatSql(String sql) {
        return null;
    }

    @Override
    public String testConnection(String groupId, String connName) {
        return null;
    }

    @Override
    public PrefsDocument getPrefsDocument() {
        return null;
    }

    @Override
    public void saveConfig(PrefsDocument prefsDocument) throws IOException {

    }

    @Override
    public String openConnection(String groupId, String connName) {
        return null;
    }

    @Override
    public void renameTableName(String groupId, String connName, String schema, String tableName, String newTableName) {

    }

    @Override
    public void renameSequence(String groupId, String connName, String schema, String seqName, String newSeqName) {

    }

    @Override
    public void resetSequence(String groupId, String connName, String schema, String seqName, String newSeqValue) {

    }

    @Override
    public boolean dropTable(String groupId, String connName, String schema, String tableName) {
        return false;
    }

    @Override
    public void clearTableData(String groupId, String connName, String schema, String tableName) {

    }

    @Override
    public boolean dropSequence(String groupId, String connName, String schema, String sequenceName) {
        return false;
    }

    @Override
    public String executeQuery(String groupId, String connName, String querySql, int pageNo, int limitSize) {
        return null;
    }

    @Override
    public ProcessResult<String[]> execute(String groupId, String connName, Reader reader, boolean stopOnError, boolean autoCommit) {
        return null;
    }

    @Override
    public String getQueryStatus(String token) {
        return null;
    }

    @Override
    public List<Map<String, Object>> loadTaskQueryData(String token) {
        return null;
    }

    @Override
    public ProcessResult<DbConsoleTable> getTaskResultMetaInfo(String token) {
        return null;
    }

    @Override
    public void stopQuery(String token) {

    }

    @Override
    public String addTask(QueryTask queryTask) {
        return null;
    }

    @Override
    public QueryTask removeTask(String token) {
        return null;
    }

    @Override
    public int createNewTable(String groupId, String connName, String tableName) {
        return 0;
    }

    @Override
    public SqlWrapper getInsertDataSql(String tableName, Map<String, Object> map) throws Exception {
        return null;
    }

    @Override
    public SqlWrapper getUpdateDataSql(String tableName, Map<String, Object> map, Map<String, Object> oldMap) throws Exception {
        return null;
    }

    @Override
    public SqlWrapper getDeleteDataSql(String tableName, Map<String, Object> oldMap) throws Exception {
        return null;
    }

    @Override
    public void loadQueryTableData(Page page, Map<String, Object> map) throws Exception {

    }

    @Override
    public int[] updateSql(String groupId, String connName, List<SqlWrapper> listSqlWrapper) {
        return new int[0];
    }

	/*private Logger _log = LoggerFactory.getLogger(DbConsoleServiceImpl.class);

	*//*
	 * 查询任务列表 唯一的token ， value任务
	 *//*
	private static Map<String, QueryTask> taskTokenMapping = new HashMap<String, QueryTask>();

	*//*
	 * 数据库管理器数据库访问接口
	 *//*
	@Resource(name = "dbConsoleDaoSupport", type = DbConsoleDaoSupport.class)
	private DbConsoleDaoSupport dbConsoleJdbcSupport;
	*//*
	 * 数据库属性设置
	 *//*
	private Properties dbConsoleProperties = new Properties();

	public DbConsoleServiceImpl() throws Exception {
	}

	*//**
	 * (non-Javadoc)
	 * 
	 * @see com.innofi.component.dbconsole.app.service.IDbConsoleService#loadDbInfo(String,
	 *      String)
	 *//*
	public DbConsoleDbInfo loadDbInfo(String groupId, String connName) {
		IJdbcDao jdbcDao = getJdbcDao(groupId, connName);
		DbConsoleDbInfo dbConsoleDbInfo = jdbcDao.loadDbInfo();
		return dbConsoleDbInfo;
	}

	*//**
	 * (non-Javadoc)
	 * 
	 * @see com.innofi.component.dbconsole.app.service.IDbConsoleService#loadGroups()
	 *//*
	public List<DbConsoleGroup> loadGroups() {
		List<DbConsoleGroup> dbConsoleGroupList = new ArrayList<DbConsoleGroup>();
		try {
			List<com.innofi.component.dbconsole.GroupDocument.Group> groups = Arrays
					.asList(prefsDocument.getPrefs().getDatabase()
							.getGroupArray());
			for (com.innofi.component.dbconsole.GroupDocument.Group dg : groups) {
				DbConsoleGroup dbConsoleGroup = new DbConsoleGroup();
				BeanUtils.copyProperties(dbConsoleGroup, dg);
				dbConsoleGroupList.add(dbConsoleGroup);
			}
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return dbConsoleGroupList;
	}

	*//**
	 * (non-Javadoc)
	 * 
	 * @see com.innofi.component.dbconsole.app.service.IDbConsoleService#loadConnections(String)
	 *//*
	public List<DbConsoleConnection> loadConnections(String groupId) {
		List<DbConsoleConnection> dbConsoleConnections = new ArrayList<DbConsoleConnection>();
		try {
			List<com.innofi.component.dbconsole.GroupDocument.Group> groups = Arrays
					.asList(prefsDocument.getPrefs().getDatabase()
							.getGroupArray());
			for (com.innofi.component.dbconsole.GroupDocument.Group dg : groups) {
				if (dg.getId().equals(groupId)) {
					List<com.innofi.component.dbconsole.ConnectionDocument.Connection> dcs = Arrays
							.asList(dg.getConnectionArray());
					for (com.innofi.component.dbconsole.ConnectionDocument.Connection dc : dcs) {
						DbConsoleConnection dbConsoleConnection = new DbConsoleConnection();
						BeanUtils.copyProperties(dbConsoleConnection, dc);
						dbConsoleConnection.setGroupId(dg.getId());
						dbConsoleConnection.setDefaults(dc.getDefault());
						dbConsoleConnections.add(dbConsoleConnection);
					}
				}
			}
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return dbConsoleConnections;
	}

	*//**
	 * (non-Javadoc)
	 * 
	 * @see com.innofi.component.dbconsole.app.service.IDbConsoleService#loadDbTemplates()
	 *//*
	public List<DbConsoleTemplate> loadDbTemplates() {
		List<DbConsoleTemplate> dbConsoleTemplates = new ArrayList<DbConsoleTemplate>();
		try {
			List<com.innofi.component.dbconsole.TemplateDocument.Template> templates = Arrays
					.asList(prefsDocument.getPrefs().getDbTemplate()
							.getTemplateArray());
			for (com.innofi.component.dbconsole.TemplateDocument.Template ts : templates) {
				DbConsoleTemplate dbConsoleTemplate = new DbConsoleTemplate();
				BeanUtils.copyProperties(dbConsoleTemplate, ts);
				dbConsoleTemplates.add(dbConsoleTemplate);
			}
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return dbConsoleTemplates;
	}

	*//**
	 * (non-Javadoc)
	 * 
	 * @see 
	 *      com.innofi.component.dbconsole.app.service.IDbConsoleService#updateSql
	 *      (String, String, List<SqlWrapper>)
	 *//*
	public int[] updateSql(String groupId, String connName,
			final List<SqlWrapper> listSqlWrapper) {
		IJdbcDao jdbcDao = getJdbcDao(groupId, connName);
		List<Integer> list = new ArrayList<Integer>();
		if (_log.isDebugEnabled()) {
			for (SqlWrapper sw : listSqlWrapper) {
				_log.debug(sw.getSql());
				if (StringUtil.hasText(sw.getSql().trim())) {
					int res[] = jdbcDao.executeUpdateSql(sw);
					list.add(res[0]);
				}
			}
		}
		int[] ints = ArrayUtils.toPrimitive(list.toArray(new Integer[list
				.size()]));
		return ints;
	}

	*//**
	 * (non-Javadoc)
	 * 
	 * @see com.innofi.component.dbconsole.app.service.IDbConsoleService#loadDbTemplates()
	 *//*
	public List<DbConsoleObjectType> loadObjectTypes(String groupId,
			String connName) {

		IJdbcDao jdbcDao = getJdbcDao(groupId, connName);

		List<String> objectTypes = jdbcDao.loadObjectTypes();

		List<DbConsoleObjectType> dbConsoleObjectTypes = new ArrayList<DbConsoleObjectType>();

		DbConsoleObjectType dbConsoleObjectType = new DbConsoleObjectType();
		dbConsoleObjectType.setTypeName("*");
		dbConsoleObjectTypes.add(dbConsoleObjectType);
		for (String objectType : objectTypes) {
			dbConsoleObjectType = new DbConsoleObjectType();
			dbConsoleObjectType.setTypeName(objectType);
			dbConsoleObjectTypes.add(dbConsoleObjectType);
		}

		if (objectTypes.contains("TABLE") && objectTypes.contains("VIEW")) {
			dbConsoleObjectType = new DbConsoleObjectType();
			dbConsoleObjectType.setTypeName("TABLE,VIEW");
			dbConsoleObjectTypes.add(dbConsoleObjectType);
		}

		return dbConsoleObjectTypes;
	}

	*//**
	 * (non-Javadoc)
	 * 
	 * @see com.innofi.component.dbconsole.app.service.IDbConsoleService#loadObjects(String,
	 *      String, String, String, String)
	 *//*
	public List<DbConsoleTable> loadObjects(String groupId, String connName,
			String schema, String objectName, String types) {
		IJdbcDao jdbcDao = getJdbcDao(groupId, connName);
		List<DbConsoleTable> tables = jdbcDao.loadTableList(schema, objectName,
				types, true);

		if (!StringUtil.hasText(types) || types.equals("*")
				|| types.indexOf("SEQUENCE") > -1) {
			List<DbConsoleSequence> sequences = jdbcDao.loadSequences(schema,
					objectName);
			for (DbConsoleSequence sequence : sequences) {
				DbConsoleTable table = new DbConsoleTable();
				table.setCatalog(sequence.getCatalog());
				table.setType(sequence.getObjectType());
				table.setComment(sequence.getComment());
				table.setFullQualifyName(sequence.getSchema() + "."
						+ sequence.getObjectName());
				table.setSchema(sequence.getSchema());
				table.setTable(sequence.getObjectName());
				tables.add(table);
			}
		}
		return tables;
	}

	*//**
	 * (non-Javadoc)
	 * 
	 * @see com.innofi.component.dbconsole.app.service.IDbConsoleService#loadTablesBySchema(String,
	 *      String, String)
	 *//*
	public List<DbConsoleTable> loadTablesBySchema(String groupId,
			String connName, String schema) {
		IJdbcDao jdbcDao = getJdbcDao(groupId, connName);
		List<DbConsoleTable> tables = jdbcDao.loadTableList(schema, null,
				"TABLE", true);
		return tables;
	}

	*//**
	 * (non-Javadoc)
	 * 
	 * @see com.innofi.component.dbconsole.app.service.IDbConsoleService#loadSequence(String,
	 *      String, String, String)
	 *//*
	public DbConsoleSequence loadSequence(String groupId, String connName,
			String schema, String seqName) {
		IJdbcDao jdbcDao = getJdbcDao(groupId, connName);
		List<DbConsoleSequence> sequences = jdbcDao.loadSequences(schema,
				seqName);
		for (DbConsoleSequence sequence : sequences) {
			if (StringUtil.hasText(schema)) {
				if (sequence.getSchema().equals(schema)
						&& sequence.getSequenceName().equals(seqName)) {
					return sequence;
				}
			} else if (sequence.getSequenceName().equals(seqName)) {
				return sequence;
			}
		}
		return null;
	}

	*//**
	 * (non-Javadoc)
	 * 
	 * @see com.innofi.component.dbconsole.app.service.IDbConsoleService#loadColumns(String,
	 *      String, String, String)
	 *//*
	public List<DbConsoleColumn> loadColumns(String groupId, String connName,
			String schema, String tableName) {
		IJdbcDao jdbcDao = getJdbcDao(groupId, connName);
		return jdbcDao.loadColumns(schema, tableName);
	}

	*//**
	 * (non-Javadoc)
	 * 
	 * @see com.innofi.component.dbconsole.app.service.IDbConsoleService#loadColumnType(String,
	 *      String)
	 *//*
	public List<DbConsoleColumn> loadColumnType(String groupId, String connName) {
		List<DbConsoleColumn> columns = new ArrayList<DbConsoleColumn>();
		IJdbcDao jdbcDao = getJdbcDao(groupId, connName);
		List<String> colTypes = jdbcDao.loadColumnType();
		for (String type : colTypes) {
			DbConsoleColumn column = new DbConsoleColumn();
			if (type.equals("OBJECT"))
				continue;
			column.setColumnType(type);
			columns.add(column);
		}
		return columns;
	}

	*//**
	 * @see com.innofi.component.dbconsole.app.service.IDbConsoleService#loadObjectDDL(String,
	 *      String, String, String, String)
	 *//*
	public String loadObjectDDL(String groupId, String connName, String type,
			String schema, String objectName) {
		if (Validator.isNotNull(type)) {
			if (type.equals("SEQUENCE")) {
				DbConsoleSequence sequence = loadSequence(groupId, connName,
						schema, objectName);
				return SqlUtil.formatDDLSql(sequence.getSource().toString()
						+ ";");
			} else if (type.equals("VIEW")) {

			} else if (type.equals("TABLE")) {
				IJdbcDao jdbcDao = getJdbcDao(groupId, connName);
				List<DbConsoleTable> tables = jdbcDao.loadTableList(schema,
						objectName, "*", false);
				for (DbConsoleTable table : tables) {
					if (table.getTableName().equals(objectName)) {
						return jdbcDao.getDBDialect().generatorDDL(table);
					}
				}
			}
		}
		return null;
	}

	*//**
	 * (non-Javadoc)
	 * 
	 * @see com.innofi.component.dbconsole.app.service.IDbConsoleService#insertColumn(String,
	 *      String, String, String,
	 *      com.innofi.component.dbconsole.pojo.DbConsoleColumn)
	 *//*
	public void insertColumn(String groupId, String connName, String schema,
			String tableName, DbConsoleColumn columnInfo) {
		IJdbcDao jdbcDao = getJdbcDao(groupId, connName);
		jdbcDao.insertColumn(schema, tableName, columnInfo);
	}

	*//**
	 * (non-Javadoc)
	 * 
	 * @see com.innofi.component.dbconsole.app.service.IDbConsoleService#updateColumn(String,
	 *      String, String, String,
	 *      com.innofi.component.dbconsole.pojo.DbConsoleColumn,
	 *      com.innofi.component.dbconsole.pojo.DbConsoleColumn)
	 *//*
	public void updateColumn(String groupId, String connName, String schema,
			String tableName, DbConsoleColumn oldColumnInfo,
			DbConsoleColumn newColumnInfo) {
		IJdbcDao jdbcDao = getJdbcDao(groupId, connName);
		jdbcDao.updateColumn(schema, tableName, oldColumnInfo, newColumnInfo);
	}

	*//**
	 * (non-Javadoc)
	 * 
	 * @see com.innofi.component.dbconsole.app.service.IDbConsoleService#dropColumn(String,
	 *      String, String, String, String)
	 *//*
	public void dropColumn(String groupId, String connName, String schema,
			String tableName, String columnName) {
		IJdbcDao jdbcDao = getJdbcDao(groupId, connName);
		jdbcDao.dropColumn(schema, tableName, columnName);
	}

	*//**
	 * (non-Javadoc)
	 * 
	 * @see com.innofi.component.dbconsole.app.service.IDbConsoleService#loadSchemas
	 *//*
	public List<DbConsoleSchema> loadSchemas(String groupId, String connName) {
		List<DbConsoleSchema> consoleSchemas = loadSchemasForSqlEditor(groupId,
				connName);
		DbConsoleSchema dbConsoleDbConsoleSchema = new DbConsoleSchema();
		dbConsoleDbConsoleSchema.setConnName(connName);
		dbConsoleDbConsoleSchema.setSchemaName("*");
		consoleSchemas.add(dbConsoleDbConsoleSchema);
		return consoleSchemas;
	}

	*//**
	 * (non-Javadoc)
	 * 
	 * @see com.innofi.component.dbconsole.app.service.IDbConsoleService#loadSchemas
	 *//*
	public List<DbConsoleSchema> loadSchemasForSqlEditor(String groupId,
			String connName) {
		IJdbcDao jdbcDao = getJdbcDao(groupId, connName);
		List<DbConsoleSchema> consoleSchemas = new ArrayList<DbConsoleSchema>();
		List<String> schemasList = jdbcDao.loadSchemas();

		// 只加载当前用户对应的schema
		String dbUser = getDbConnectionByName(getDbConsoleGroup(groupId),
				connName).getUser();

		schemasList.remove(dbUser.toUpperCase());// 删除用户模式

		DbConsoleSchema dbConsoleDbConsoleSchema = new DbConsoleSchema();
		dbConsoleDbConsoleSchema.setConnName(connName);
		dbConsoleDbConsoleSchema.setSchemaName(dbUser.toUpperCase());
		consoleSchemas.add(0, dbConsoleDbConsoleSchema);

		for (String schema : schemasList) {
			dbConsoleDbConsoleSchema = new DbConsoleSchema();
			dbConsoleDbConsoleSchema.setConnName(connName);
			dbConsoleDbConsoleSchema.setSchemaName(schema);
			consoleSchemas.add(dbConsoleDbConsoleSchema);
		}

		return consoleSchemas;
	}

	*//**
	 * (non-Javadoc)
	 * 
	 * @see com.innofi.component.dbconsole.app.service.IDbConsoleService#saveConfig(com.innofi.component.dbconsole.PrefsDocument)
	 *//*
	public void saveConfig(
			com.innofi.component.dbconsole.PrefsDocument newPrefsDocument)
			throws IOException {
		savePrefsDocument(newPrefsDocument);
	}

	*//**
	 * (non-Javadoc)
	 * 
	 * @see com.innofi.component.dbconsole.app.service.IDbConsoleService#openConnection(String,
	 *      String)
	 *//*
	public String openConnection(String groupId, String connName) {
		String message = testConnection(groupId, connName);
		if (StringUtil.hasText(message)) {
			return message;
		}
		DataSource dataSource = getDataSource(groupId, connName);
		if (dataSource == null) {
			DbConsoleGroup dbConsoleGroup = getDbConsoleGroup(groupId);
			DbConsoleConnection dbConsoleConnection = getDbConnectionByName(
					dbConsoleGroup, connName);
			dataSource = newDataSource(dbConsoleConnection);
			sysCacheConfigService.putCacheObject(
					"$" + groupId + "_" + connName, dataSource);
		}
		return null;
	}

	*//**
	 * (non-Javadoc)
	 * 
	 * @see com.innofi.component.dbconsole.app.service.IDbConsoleService#renameTableName(String,
	 *      String, String, String, String)
	 *//*
	public void renameTableName(String groupId, String connName, String schema,
			String tableName, String newTableName) {
		IJdbcDao jdbcDao = getJdbcDao(groupId, connName);
		jdbcDao.renameTableName(schema, tableName, newTableName);
	}

	*//**
	 * (non-Javadoc)
	 * 
	 * @see com.innofi.component.dbconsole.app.service.IDbConsoleService#renameSequence(String,
	 *      String, String, String, String)
	 *//*
	public void renameSequence(String groupId, String connName, String schema,
			String seqName, String newSeqName) {
		IJdbcDao jdbcDao = getJdbcDao(groupId, connName);
		DbConsoleSequence sequence = jdbcDao.loadSequence(schema, seqName);

		String currentValue = sequence.getCurrentValue();
		String minvalue = sequence.getMinValue();
		String maxValue = sequence.getMaxValue();
		String increment = sequence.getIncrement();
		String cycle = sequence.getCycle();
		String dataType = sequence.getDataType();

		StringBuilder sql = new StringBuilder(" CREATE SEQUENCE " + newSeqName);

		if (StringUtil.hasText(dataType)) {
			sql.append(" DATATYPE " + dataType);
		}

		sql.append(" START WITH " + currentValue);

		sql.append(" INCREMENT BY " + increment);

		if (StringUtil.hasText(minvalue)) {
			sql.append(" MINVALUE " + minvalue);
		}

		if (StringUtil.hasText(maxValue)) {
			sql.append(" MAXVALUE " + maxValue);
		}

		*//*
		 * if (StringUtil.hasText(cycle) && cycle.equalsIgnoreCase("Y")) {
		 * sql.append(" CYCLE "); } else { sql.append(" NO CYCLE "); }
		 *//*

		dropSequence(groupId, connName, schema, sequence.getSequenceName());
		jdbcDao.execute(sql.toString());

	}

	*//**
	 * (non-Javadoc)
	 * 
	 * @see com.innofi.component.dbconsole.app.service.IDbConsoleService#renameSequence(String,
	 *      String, String, String, String)
	 *//*
	public void resetSequence(String groupId, String connName, String schema,
			String seqName, String newSeqValue) {
		IJdbcDao jdbcDao = getJdbcDao(groupId, connName);
		DbConsoleSequence sequence = jdbcDao.loadSequence(schema, seqName);

		String startValue = sequence.getStartValue();
		String minvalue = sequence.getMinValue();
		String maxValue = sequence.getMaxValue();
		String increment = sequence.getIncrement();
		String cycle = sequence.getCycle();
		String dataType = sequence.getDataType();

		StringBuilder sql = new StringBuilder(" CREATE SEQUENCE " + seqName);

		if (StringUtil.hasText(dataType)) {
			sql.append(" DATATYPE " + dataType);
		}

		sql.append(" START WITH " + newSeqValue);

		sql.append(" INCREMENT BY " + increment);

		if (StringUtil.hasText(minvalue)) {
			sql.append(" MINVALUE " + minvalue);
		}

		if (StringUtil.hasText(maxValue)) {
			sql.append(" MAXVALUE " + maxValue);
		}

		*//*
		 * if (StringUtil.hasText(cycle) && cycle.equalsIgnoreCase("Y")) {
		 * sql.append(" CYCLE "); } else { sql.append(" NO CYCLE "); }
		 *//*

		dropSequence(groupId, connName, schema, sequence.getSequenceName());
		jdbcDao.execute(sql.toString());
	}

	*//**
	 * (non-Javadoc)
	 * 
	 * @see com.innofi.component.dbconsole.app.service.IDbConsoleService#dropTable(String,
	 *      String, String, String)
	 *//*
	public boolean dropTable(String groupId, String connName, String schema,
			String tableName) {
		IJdbcDao jdbcDao = getJdbcDao(groupId, connName);
		return jdbcDao.dropTable(schema, tableName);
	}

	*//**
	 * (non-Javadoc)
	 * 
	 * @see com.innofi.component.dbconsole.app.service.IDbConsoleService#clearTableData(String,
	 *      String, String, String)
	 *//*
	public void clearTableData(String groupId, String connName, String schema,
			String tableName) {
		IJdbcDao jdbcDao = getJdbcDao(groupId, connName);
		jdbcDao.clearTableData(schema, tableName);
	}

	*//**
	 * (non-Javadoc)
	 * 
	 * @see com.innofi.component.dbconsole.app.service.IDbConsoleService#dropTable(String,
	 *      String, String, String)
	 *//*
	public boolean dropSequence(String groupId, String connName, String schema,
			String sequenceName) {
		IJdbcDao jdbcDao = getJdbcDao(groupId, connName);
		return jdbcDao.dropSequence(schema, sequenceName);
	}

	*//**
	 * (non-Javadoc)
	 * 
	 * @see com.innofi.component.dbconsole.app.service.IDbConsoleService#dropTable(String,
	 *      String, String, String)
	 *//*
	public synchronized String executeQuery(String groupId, String connName,
			String querySql, int pageNo, int limitSize) {
		DataSource dataSource = getDataSource(groupId, connName);
		ProcessResult<DbConsoleTable> result = new ProcessResult<DbConsoleTable>();
		result.setTimeOut(60000);
		result.setQuerySql(querySql);
		result.setPageNo(pageNo);
		result.setLimitSize(limitSize);
		QueryTaskObserver observer = new QueryTaskObserver(result);

		if (pageNo < 1) {
			pageNo = 1;
		}
		if (limitSize < 1) {
			limitSize = VariableHelper.parseInt(dbConsoleProperties
					.get("pageSize"));
		}

		result.setPageNo(pageNo);
		result.setLimitSize(limitSize);

		QueryTask queryTask = null;
		try {
			queryTask = new QueryTask(observer, "dbconsole_"
					+ System.currentTimeMillis(), dataSource.getConnection(),
					result);
			Thread taskThread = new Thread(queryTask, "dbconsole_"
					+ System.currentTimeMillis());
			taskThread.start();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new FrameworkJdbcRuntimeException(e);
		}
		return addTask(queryTask);
	}

	*//**
	 * (non-Javadoc)
	 * 
	 * @see com.innofi.component.dbconsole.app.service.IDbConsoleService#execute(String,
	 *      String, java.io.Reader, boolean, boolean)
	 *//*
	public ProcessResult<String[]> execute(String groupId, String connName,
			Reader reader, boolean stopOnError, boolean autoCommit) {
		IJdbcDao jdbcDao = getJdbcDao(groupId, connName);
		return jdbcDao.runScript(reader, stopOnError, autoCommit);
	}

	*//**
	 * (non-Javadoc)
	 * 
	 * @see com.innofi.component.dbconsole.app.service.IDbConsoleService#getQueryStatus(String)
	 *//*
	public String getQueryStatus(String token) {
		QueryTask task = taskTokenMapping.get(token);
		if (task != null) {
			String message = task.getResult().getMessage();
			int status = task.getCurrentStatus();
			if (status == QueryTask.TASK_STATUS_ERROR
					|| status == QueryTask.TASK_STATUS_STOP
					|| status == QueryTask.TASK_STATUS_TIMEOUT) {
				removeTask(token);
			}
			if (StringUtil.hasText(message)) {
				return task.getCurrentStatusName() + "|" + message;
			}
			_log.debug("task current status is [" + task.getCurrentStatusName()
					+ "]");
			return task.getCurrentStatusName();
		}
		return null;
	}

	*//**
	 * (non-Javadoc)
	 * 
	 * @see com.innofi.component.dbconsole.app.service.IDbConsoleService#loadTaskQueryData(String)
	 *//*
	public List<Map<String, Object>> loadTaskQueryData(String token) {
		QueryTask task = taskTokenMapping.get(token);
		if (task == null)
			throw new FrameworkJdbcRuntimeException("请检查，查询结果已不存！");
		removeTask(token);
		return task.getResult().getData().getData();
	}

	*//**
	 * (non-Javadoc)
	 * 
	 * @see com.innofi.component.dbconsole.app.service.IDbConsoleService#getTaskResultMetaInfo(String)
	 *//*
	public ProcessResult<DbConsoleTable> getTaskResultMetaInfo(String token) {
		QueryTask task = taskTokenMapping.get(token);
		if (task == null)
			throw new FrameworkJdbcRuntimeException("请检查，查询结果已不存！");
		return task.getResult();
	}

	*//**
	 * (non-Javadoc)
	 * 
	 * @see com.innofi.component.dbconsole.app.service.IDbConsoleService#stopQuery(String)
	 *//*
	public void stopQuery(String token) {
		_log.debug("command is stop task");
		QueryTask task = taskTokenMapping.get(token);
		task.stopTask();
	}

	*//**
	 * (non-Javadoc)
	 * 
	 * @see com.innofi.component.dbconsole.app.service.IDbConsoleService#formatSql(String)
	 *//*
	public String formatSql(String sql) {
		return SqlUtil.selectFormatSql(sql);
	}

	*//**
	 * (non-Javadoc)
	 * 
	 * @see com.innofi.component.dbconsole.app.service.IDbConsoleService#testConnection(String,
	 *      String)
	 *//*
	public String testConnection(String groupId, String connName) {
		DbConsoleGroup dbConsoleGroup = getDbConsoleGroup(groupId);
		DbConsoleConnection dbConsoleConnection = getDbConnectionByName(
				dbConsoleGroup, connName);

		if (!StringUtil.hasText(dbConsoleConnection.getUrl())) {
			return MessageFormat.format("[{0}]数据库连接的URL为空，请重新配置数据库连接！",
					connName);
		}
		if (!StringUtil.hasText(dbConsoleConnection.getDriver())) {
			return MessageFormat.format("[{0}]数据库连接的驱动类名称，请重新配置数据库连接！",
					connName);
		}
		try {
			Class.forName(dbConsoleConnection.getDriver());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return MessageFormat.format("名称为[{0}]的驱动类没有找到!",
					dbConsoleConnection.getDriver());
		}
		java.sql.Connection conn = null;
		try {
			_log.debug("url=" + dbConsoleConnection.getUrl() + " user="
					+ dbConsoleConnection.getUser() + " password="
					+ dbConsoleConnection.getPassword());
			conn = DriverManager.getConnection(dbConsoleConnection.getUrl(),
					dbConsoleConnection.getUser(),
					dbConsoleConnection.getPassword());
			DatabaseMetaData metaData = conn.getMetaData();
			_log.debug(String
					.format("connection info:[DatabaseProductName=%s,DatabaseProductVersion=%s,DatabaseMajorVersion=%s,DatabaseMinorVersion=%s,DriverName=%s]",
							metaData.getDatabaseProductName(),
							metaData.getDatabaseProductVersion(),
							metaData.getDatabaseMajorVersion(),
							metaData.getDatabaseMinorVersion(),
							metaData.getDriverName()));
		} catch (SQLException e) {
			e.printStackTrace();
			return MessageFormat.format("连接失败,失败原因:[{0}]", e.getMessage());
		} finally {
			JdbcUtils.closeConnection(conn);
		}
		return null;
	}

	*//**
	 * (non-Javadoc)
	 * 
	 * @see com.innofi.component.dbconsole.app.service.IDbConsoleService#addTask(com.innofi.component.dbconsole.app.executer.QueryTask)
	 *//*
	public String addTask(QueryTask queryTask) {
		final String token = System.currentTimeMillis() + "";
		taskTokenMapping.put(token, queryTask);
		_log.debug("add query task task name is [" + queryTask.getName()
				+ "] totalTaskSize is [" + taskTokenMapping.size() + "]");
		return token;
	}

	*//**
	 * (non-Javadoc)
	 * 
	 * @see com.innofi.component.dbconsole.app.service.IDbConsoleService#removeTask(String)
	 *//*
	public QueryTask removeTask(String token) {
		QueryTask task = taskTokenMapping.remove(token);
		_log.debug("remove query task token is [" + token
				+ "] totalTaskSize is [" + taskTokenMapping.size() + "]");
		return task;
	}

	*//**
	 * (non-Javadoc)
	 * 
	 * @see com.innofi.component.dbconsole.app.service.IDbConsoleService#createNewTable(String,
	 *      String, String)
	 *//*
	public int createNewTable(String groupId, String connName, String tableName) {
		IJdbcDao jdbcDao = getJdbcDao(groupId, connName);
		return jdbcDao.createNewTable(tableName);
	}

	*//**
	 * (non-Javadoc)
	 * 
	 * @see com.innofi.component.dbconsole.app.service.IDbConsoleService#getInsertDataSql(String,
	 *      java.util.Map)
	 *//*
	public SqlWrapper getInsertDataSql(String tableName, Map<String, Object> map)
			throws Exception {
		StringBuilder columnName = new StringBuilder();
		StringBuilder values = new StringBuilder();
		List<Object> list = new ArrayList<Object>();
		int i = 1;
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			list.add(value);
			if (i == map.size()) {
				columnName.append(key);
				values.append("?");
			} else {
				columnName.append(key + ",");
				values.append("?,");
			}
			i++;
		}
		String sql = "insert into " + tableName + "(" + columnName + ")values("
				+ values + ")";
		return new SqlWrapper(sql, list.toArray());
	}

	*//**
	 * (non-Javadoc)
	 * 
	 * @see com.innofi.component.dbconsole.app.service.IDbConsoleService#getUpdateDataSql(String,
	 *      java.util.Map, java.util.Map)
	 *//*
	public SqlWrapper getUpdateDataSql(String tableName,
			Map<String, Object> map, Map<String, Object> oldMap)
			throws Exception {
		StringBuilder newParameter = new StringBuilder();
		List<Object> list = new ArrayList<Object>();
		int i = 1;
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			list.add(value);
			if (i == map.size()) {
				newParameter.append(key + "=?");
			} else {
				newParameter.append(key + "=?,");
			}
			i++;
		}
		String whereParameter = this.generateWhereSql(list, oldMap);
		String sql = "update " + tableName + " set " + newParameter + " where "
				+ whereParameter;
		return new SqlWrapper(sql, list.toArray());

	}

	*//**
	 * (non-Javadoc)
	 * 
	 * @see com.innofi.component.dbconsole.app.service.IDbConsoleService#getDeleteDataSql(String,
	 *      java.util.Map)
	 *//*
	public SqlWrapper getDeleteDataSql(String tableName,
			Map<String, Object> oldMap) throws Exception {
		List<Object> list = new ArrayList<Object>();
		String whereParameter = this.generateWhereSql(list, oldMap);
		String sql = " delete from  " + tableName + " where " + whereParameter;
		return new SqlWrapper(sql, list.toArray());
	}

	*//**
	 * (non-Javadoc)
	 * 
	 * @see com.innofi.component.dbconsole.app.service.IDbConsoleService#loadQueryTableData(com.innofi.framework.dao.pagination.Page,
	 *      java.util.Map)
	 *//*
	public void loadQueryTableData(Page page, Map<String, Object> map)
			throws Exception {
		String groupId = (String) map.get("groupId");
		String connName = (String) map.get("connName");
		String schemaName = (String) map.get("schemaName");
		String tableName = (String) map.get("tableName");

		StringBuilder selectSql = new StringBuilder();
		if (StringUtil.hasText(tableName)) {
			selectSql.append("select * from " + schemaName + "." + tableName);
		}
		_log.debug("select sql:" + selectSql);
		IJdbcDao jdbcDao = getJdbcDao(groupId, connName);

		jdbcDao.dynamicQueryMapForList(selectSql.toString(), page);
	}

	*//**
	 * 拼接where sql
	 * 
	 * @param list
	 * @param map
	 * @return 返回生成的SQL语句
	 *//*
	private String generateWhereSql(List<Object> list, Map<String, Object> map) {
		StringBuilder sql = new StringBuilder(" 1=1 ");
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();

			if (value != null) {
				list.add(value);
				sql.append(" and " + key + "=? ");
			}
		}
		return sql.toString();
	}

	*//*
	 * 加载innofi_dbconsole_prefs配置文件
	 *//*
	private PrefsDocument loadPrefsDocument() {

		org.springframework.core.io.Resource resource = null;
		try {
			PathMatchingResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
			org.springframework.core.io.Resource[] resources = resourceResolver
					.getResources(DBCONSOLE_PREFS_FILE);

			resource = resources[0];
		} catch (IOException e) {
			e.printStackTrace();
			throw new DbConsoleRuntimeException(
					"在类路径【classpath:META-INF】下未找到【innofi_dbconsole_prefs.xml】配置文件，请检查！");
		}
		if (resource != null) {
			try {
				return PrefsDocument.Factory.parse(resource.getURL());
			} catch (Exception e) {
				e.printStackTrace();
				throw new DbConsoleRuntimeException(
						"加载配置文件【innofi_dbconsole_prefs.xml】出现异常，请检查文件内容是否合法！");
			}
		}
		return null;
	}

	*//*
	 * 保存首选项配置文件
	 * 
	 * @param prefsDocument
	 * 
	 * @throws IOException
	 *//*
	private void savePrefsDocument(PrefsDocument prefsDocument)
			throws IOException {
		File file = ResourceUtil.getFile(DBCONSOLE_PREFS_FILE);
		_log.info("**********save the dbconsole prefs file to path:["
				+ file.getAbsoluteFile() + "]");
		prefsDocument.save(file);
		this.prefsDocument = prefsDocument;
	}

	*//*
	 * 根据连接信息创建数据源对象
	 * 
	 * @param dbConsoleConnection
	 * 
	 * @return 数据源对象
	 *//*
	private DataSource newDataSource(DbConsoleConnection dbConsoleConnection) {
		ComboPooledDataSource ds = new ComboPooledDataSource();
		ConsoleUtil.info(String.format(
				"connect to db [%s] url [%s] username [%s] password [%s] ",
				dbConsoleConnection.getName(), dbConsoleConnection.getUrl(),
				dbConsoleConnection.getUser(),
				dbConsoleConnection.getPassword()));
		ds.setUser(dbConsoleConnection.getUser());
		ds.setPassword(dbConsoleConnection.getPassword());
		ds.setJdbcUrl(dbConsoleConnection.getUrl());
		try {
			ds.setDriverClass(dbConsoleConnection.getDriver());
		} catch (PropertyVetoException e) {
			e.printStackTrace();
			throw new FrameworkJdbcRuntimeException(e);
		}
		return ds;
	}

	*//*
	 * 根据工作组编号获得工作组对象
	 * 
	 * @param groupId 工作组编号
	 * 
	 * @return 编号对应的工作组对象
	 *//*
	private DbConsoleGroup getDbConsoleGroup(String groupId) {
		List<com.innofi.component.dbconsole.GroupDocument.Group> groups = Arrays
				.asList(prefsDocument.getPrefs().getDatabase().getGroupArray());
		for (com.innofi.component.dbconsole.GroupDocument.Group dg : groups) {
			if (dg.getId().equals(groupId)) {
				DbConsoleGroup dbConsoleGroup = new DbConsoleGroup();
				try {
					BeanUtils.copyProperties(dbConsoleGroup, dg);
					List<com.innofi.component.dbconsole.ConnectionDocument.Connection> dcs = Arrays
							.asList(dg.getConnectionArray());
					List<DbConsoleConnection> dbConnectionList = new ArrayList<DbConsoleConnection>();
					for (com.innofi.component.dbconsole.ConnectionDocument.Connection dc : dcs) {
						DbConsoleConnection dbConsoleConnection = new DbConsoleConnection();
						BeanUtils.copyProperties(dbConsoleConnection, dc);
						dbConsoleConnection.setGroupId(dg.getId());
						dbConnectionList.add(dbConsoleConnection);
					}
					dbConsoleGroup.setDbConsoleConnections(dbConnectionList);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
				return dbConsoleGroup;
			}
		}
		return null;
	}

	*//*
	 * 根据连接名称获得对应连接名称
	 * 
	 * @param dbConsoleGroup 工作组对象
	 * 
	 * @param connName 连接名称
	 * 
	 * @return 数据库连接对象
	 *//*
	private DbConsoleConnection getDbConnectionByName(
			DbConsoleGroup dbConsoleGroup, String connName) {
		List<DbConsoleConnection> connections = dbConsoleGroup
				.getDbConsoleConnections();
		for (DbConsoleConnection dc : connections) {
			if (dc.getName().equals(connName)) {
				return dc;
			}
		}
		return null;
	}

	*//*
	 * 获得数据源
	 * 
	 * @param groupId 工作组编号
	 * 
	 * @param connName 连接名称
	 * 
	 * @return
	 *//*
	private DataSource getDataSource(String groupId, String connName) {
		DataSource dataSource = (DataSource) sysCacheConfigService
				.getCacheObject("$" + groupId + "_" + connName);
		if (dataSource == null) {
			DbConsoleGroup dbConsoleGroup = getDbConsoleGroup(groupId);
			DbConsoleConnection dbConsoleConnection = getDbConnectionByName(
					dbConsoleGroup, connName);
			dataSource = newDataSource(dbConsoleConnection);
			sysCacheConfigService.putCacheObject(
					"$" + groupId + "_" + connName, dataSource);
		}
		return dataSource;
	}*/
}
