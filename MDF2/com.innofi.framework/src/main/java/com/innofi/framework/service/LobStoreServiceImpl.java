/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.framework.service;

import com.innofi.framework.dao.DaoSupport;
import com.innofi.framework.spring.context.ContextHolder;
import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.AbstractLobCreatingPreparedStatementCallback;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.jdbc.support.lob.OracleLobHandler;
import org.springframework.jdbc.support.nativejdbc.CommonsDbcpNativeJdbcExtractor;
import org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor;

import java.io.InputStream;
import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

/**
 * 功能/ 模块：工具类
 *
 * @author 刘名寓  liumy2009@126.com
 * @version 2.0.0 12-12-04
 *          <p> 大对象仓库服务接口，对外提供二进制数据，超长文本等的储存、更新、取出等操作。</p>
 *          {@link JdbcDaoSupport}接口的默认实现。
 *          <p>
 *          以{@link LobHandler}和{@link LobCreator}为基础，实现了跨数据库的大对象存储操作。<br/>
 *          以下为兼容的数据库及其版本列表:</br> <strong>*注:BLOB在SQLServer系列中的类型可能称为IMAGE</strong><br/>
 *          <table style="border-width: 1px;border-color: #FFC555;border-style: solid;padding: 4;margin: 0;">
 *          <tr style = "background-color: #CEFCCD;">
 *          <td>数据库</td>
 *          <td>版本</td>
 *          <td>驱动版本</td>
 *          </tr>
 *          <tr>
 *          <td>MySQL</td>
 *          <td>5.5</td>
 *          <td>mysql-connector-java-5.1.0-bin.jar</td>
 *          </tr>
 *          <tr style = "background-color: #FEFCCD;">
 *          <td>Oracle</td>
 *          <td>11g</td>
 *          <td>ojdbc14.jar</td>
 *          </tr>
 *          <tr>  getFormatHql
 *          <td>DB2</td>
 *          <td>9.7</td>
 *          <td>db2jcc.jar</td>
 *          </tr>
 *          <tr style = "background-color: #FEFCCD;">
 *          <td>SQLServer</td>
 *          <td>2005</td>
 *          <td>sqljdbc4.jar</td>
 *          </tr>
 *          </table>
 *          </p>
 *          <p/>
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
//@Component(value="lobStoreService")
public class LobStoreServiceImpl implements LobStoreService {

	@Resource(name="mdDataTitleTableMappingDaoSupport")
    protected DaoSupport daoSupport;

    public DaoSupport getDaoSupport(){
        return daoSupport;
    }

    /*
      * (non-Javadoc)
      *
      * @see com.innofi.framework.service.LobStoreService#storeBytes(byte[])
      */
    public String storeBytes(final byte[] content) throws SQLException {
        return this.storeBytes(content, null);
    }

    /*
      * (non-Javadoc)
      *
      * @see com.innofi.framework.service.LobStoreService#storeBytes(byte[],
      * java.lang.String)
      */
    public String storeBytes(final byte[] content, final String name)
            throws SQLException {
        final String sql = "INSERT INTO idf_blob_store (id_,content_,name_) VALUES (?,?,?)";
        final String id = this.generateId();
        int updatedRowCount = getDaoSupport().getJdbcDao().execute(
                sql,
                new AbstractLobCreatingPreparedStatementCallback(this
                        .getLobHandler()) {

                    @Override
                    protected void setValues(
                            PreparedStatement preparedstatement,
                            LobCreator lobcreator) throws SQLException,
                            DataAccessException {
                        preparedstatement.setString(1, id);
                        lobcreator
                                .setBlobAsBytes(preparedstatement, 2, content);
                        preparedstatement.setString(3, name);

                    }
                });
        if (0 == updatedRowCount) {
            throw new SQLException("未能成功存储Byte");
        }
        return id;
    }

    /*
      * (non-Javadoc)
      *
      * @see com.innofi.framework.service.LobStoreService#deleteBytes(java.lang.String)
      */
    public void deleteBytes(final String id) throws SQLException {
        deleteFromBlob(id);
    }

    /**
     * 从删除一条代表二进制数据的记录
     *
     * @param id 主键
     * @throws java.sql.SQLException
     */
    protected void deleteFromBlob(final String id) throws SQLException {
        final String sql = "DELETE FROM idf_blob_store WHERE id_=?";
        int updatedRowCount = getDaoSupport().getJdbcDao().execute(sql,
                new PreparedStatementCallback<Integer>() {

                    public Integer doInPreparedStatement(
                            PreparedStatement preparedstatement)
                            throws SQLException, DataAccessException {
                        preparedstatement.setString(1, id);
                        return preparedstatement.executeUpdate();
                    }

                });
        if (0 == updatedRowCount) {
            throw new SQLException(String.format(
                    "未能成功删除大二进制对象[id=%s],请检查其是否存在", id));
        }
    }

    /**
     * 删除一条代表文本的记录。
     *
     * @param id 主键
     * @throws java.sql.SQLException
     */
    protected void deleteFromClob(final String id) throws SQLException {
        final String sql = "DELETE FROM idf_clob_store WHERE id_=?";
        int updatedRowCount = getDaoSupport().getJdbcDao().execute(sql,
                new PreparedStatementCallback<Integer>() {

                    public Integer doInPreparedStatement(
                            PreparedStatement preparedstatement)
                            throws SQLException, DataAccessException {
                        preparedstatement.setString(1, id);
                        return preparedstatement.executeUpdate();
                    }

                });
        if (0 == updatedRowCount) {
            throw new SQLException(String.format("未能成功删除大字符对象[id=%s],请检查其是否存在",
                    id));
        }
    }

    /*
      * (non-Javadoc)
      *
      * @see com.innofi.framework.service.LobStoreService#updateBytes(java.lang.String,
      * byte[])
      */
    public void updateBytes(final String id, final byte[] content)
            throws SQLException {
        final String sql = "UPDATE idf_blob_store SET content_=? WHERE id_=?";
        int updatedRowCount = getDaoSupport().getJdbcDao().execute(
                sql,
                new AbstractLobCreatingPreparedStatementCallback(this
                        .getLobHandler()) {

                    @Override
                    protected void setValues(
                            PreparedStatement preparedstatement,
                            LobCreator lobcreator) throws SQLException,
                            DataAccessException {
                        lobcreator
                                .setBlobAsBytes(preparedstatement, 1, content);
                        preparedstatement.setString(2, id);

                    }
                });
        if (0 == updatedRowCount) {
            throw new SQLException(String.format(
                    "未能成功更新Byte[id=%s],请检查此记录是否存在", id));
        }
    }

    public void updateBytesName(final String id, final String name)
            throws SQLException {
        final String sql = "UPDATE idf_blob_store SET name_=? WHERE id_=?";
        int updatedRowCount = getDaoSupport().getJdbcDao().execute(sql,
                new PreparedStatementCallback<Integer>() {

                    public Integer doInPreparedStatement(
                            PreparedStatement preparedStatement)
                            throws SQLException, DataAccessException {
                        preparedStatement.setString(1, name);
                        preparedStatement.setString(2, id);
                        int updatedRowCount = preparedStatement.executeUpdate();
                        return updatedRowCount;
                    }

                });
        if (0 == updatedRowCount) {
            throw new SQLException(String.format(
                    "未能成功更新Byte[id=%s],请检查此记录是否存在", id));
        }

    }

    /*
      * (non-Javadoc)
      *
      * @see com.innofi.framework.service.LobStoreService#updateBytes(java.lang.String,
      * byte[], java.lang.String)
      */
    public void updateBytes(final String id, final byte[] content,
                            final String name) throws SQLException {
        final String sql = "UPDATE idf_blob_store SET content_=?,name_=? WHERE id_=?";
        int updatedRowCount = getDaoSupport().getJdbcDao().execute(
                sql,
                new AbstractLobCreatingPreparedStatementCallback(this
                        .getLobHandler()) {

                    @Override
                    protected void setValues(
                            PreparedStatement preparedstatement,
                            LobCreator lobcreator) throws SQLException,
                            DataAccessException {
                        lobcreator
                                .setBlobAsBytes(preparedstatement, 1, content);
                        preparedstatement.setString(2, name);
                        preparedstatement.setString(3, id);

                    }
                });
        if (0 == updatedRowCount) {
            throw new SQLException(String.format(
                    "未能成功更新Byte[id=%s],请检查此记录是否存在", id));
        }

    }

    /*
      * (non-Javadoc)
      *
      * @see com.innofi.framework.service.LobStoreService#getBytes(java.lang.String)
      */
    public byte[] getBytes(String id) throws SQLException {
        final String sql = "SELECT content_ FROM idf_blob_store WHERE id_=?";
        List<byte[]> list = getDaoSupport().getJdbcDao().query(sql,
                new Object[]{id}, new RowMapper<byte[]>() {

            public byte[] mapRow(ResultSet resultset, int i)
                    throws SQLException {
                byte[] content = LobStoreServiceImpl.this
                        .getLobHandler().getBlobAsBytes(resultset, 1);
                return content;
            }

        });
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    /*
      * (non-Javadoc)
      *
      * @see com.innofi.framework.service.LobStoreService#getBytesName(java.lang.String)
      */
    public String getBytesName(String id) throws SQLException {
        final String sql = "SELECT name_ FROM idf_blob_store WHERE id_=?";
        List<String> list = getDaoSupport().getJdbcDao().query(sql,
                new Object[]{id}, new RowMapper<String>() {

            public String mapRow(ResultSet resultset, int i)
                    throws SQLException {
                String content = resultset.getString(1);
                return content;
            }

        });
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    /*
      * (non-Javadoc)
      *
      * @see
      * com.innofi.framework.service.LobStoreService#storeBinaryStream(java.io.InputStream
      * , int)
      */
    public String storeBinaryStream(final InputStream inputStream,
                                    final int contentLength) throws SQLException {
        return this.storeBinaryStream(inputStream, contentLength, null);
    }

    /*
      * (non-Javadoc)
      *
      * @see
      * com.innofi.framework.service.LobStoreService#storeBinaryStream(java.io.InputStream
      * , int, java.lang.String)
      */
    public String storeBinaryStream(final InputStream inputStream,
                                    final int contentLength, final String name) throws SQLException {
        final String sql = "INSERT INTO idf_blob_store (id_,content_,name_) VALUES (?,?,?)";
        final String id = this.generateId();
        int updatedRowCount = getDaoSupport().getJdbcDao().execute(
                sql,
                new AbstractLobCreatingPreparedStatementCallback(this
                        .getLobHandler()) {

                    @Override
                    protected void setValues(
                            PreparedStatement preparedstatement,
                            LobCreator lobcreator) throws SQLException,
                            DataAccessException {
                        preparedstatement.setString(1, id);
                        lobcreator.setBlobAsBinaryStream(preparedstatement, 2,
                                inputStream, contentLength);
                        preparedstatement.setString(3, name);

                    }
                });
        if (0 == updatedRowCount) {
            throw new SQLException("未能成功存储二进制流");
        }
        return id;
    }

    /*
      * (non-Javadoc)
      *
      * @see
      * com.innofi.framework.service.LobStoreService#deleteBinaryStream(java.lang.String
      * )
      */
    public void deleteBinaryStream(String id) throws SQLException {
        this.deleteFromBlob(id);
    }

    /*
      * (non-Javadoc)
      *
      * @see
      * com.innofi.framework.service.LobStoreService#updateBinaryStream(java.lang.String
      * , java.io.InputStream, int)
      */
    public void updateBinaryStream(final String id,
                                   final InputStream inputStream, final int contentLength)
            throws SQLException {
        final String sql = "UPDATE idf_blob_store SET content_=? WHERE id_=?";
        int updatedRowCount = getDaoSupport().getJdbcDao().execute(
                sql,
                new AbstractLobCreatingPreparedStatementCallback(this
                        .getLobHandler()) {

                    @Override
                    protected void setValues(
                            PreparedStatement preparedstatement,
                            LobCreator lobcreator) throws SQLException,
                            DataAccessException {
                        lobcreator.setBlobAsBinaryStream(preparedstatement, 1,
                                inputStream, contentLength);
                        preparedstatement.setString(2, id);

                    }
                });
        if (0 == updatedRowCount) {
            throw new SQLException(String.format(
                    "未能成功更新Byte[id=%s],请检查此记录是否存在", id));
        }
    }

    public void updateBinaryStreamName(final String id, final String name)
            throws SQLException {
        final String sql = "UPDATE idf_blob_store SET name_=? WHERE id_=?";
        int updatedRowCount = getDaoSupport().getJdbcDao().execute(sql,
                new PreparedStatementCallback<Integer>() {

                    public Integer doInPreparedStatement(
                            PreparedStatement preparedStatement)
                            throws SQLException, DataAccessException {
                        preparedStatement.setString(1, name);
                        preparedStatement.setString(2, id);
                        int updatedRowCount = preparedStatement.executeUpdate();
                        return updatedRowCount;
                    }

                });
        if (0 == updatedRowCount) {
            throw new SQLException(String.format(
                    "未能成功更新Byte[id=%s],请检查此记录是否存在", id));
        }

    }

    /*
      * (non-Javadoc)
      *
      * @see
      * com.innofi.framework.service.LobStoreService#updateBinaryStream(java.lang.String
      * , java.io.InputStream, int, java.lang.String)
      */
    public void updateBinaryStream(final String id,
                                   final InputStream inputStream, final int contentLength,
                                   final String name) throws SQLException {
        final String sql = "UPDATE idf_blob_store SET content_=?,name_=? WHERE id_=?";
        int updatedRowCount = getDaoSupport().getJdbcDao().execute(
                sql,
                new AbstractLobCreatingPreparedStatementCallback(this
                        .getLobHandler()) {

                    @Override
                    protected void setValues(
                            PreparedStatement preparedstatement,
                            LobCreator lobcreator) throws SQLException,
                            DataAccessException {
                        lobcreator.setBlobAsBinaryStream(preparedstatement, 1,
                                inputStream, contentLength);
                        preparedstatement.setString(2, name);
                        preparedstatement.setString(3, id);

                    }
                });
        if (0 == updatedRowCount) {
            throw new SQLException(String.format(
                    "未能成功更新Byte[id=%s],请检查此记录是否存在", id));
        }

    }

    /*
      * (non-Javadoc)
      *
      * @see
      * com.innofi.framework.service.LobStoreService#getBinaryStream(java.lang.String)
      */
    public InputStream getBinaryStream(String id) throws SQLException {
        final String sql = "SELECT content_ FROM idf_blob_store WHERE id_=?";
        List<InputStream> list = getDaoSupport().getJdbcDao().query(sql,
                new Object[]{id}, new RowMapper<InputStream>() {

            public InputStream mapRow(ResultSet resultset, int i)
                    throws SQLException {
                InputStream content = LobStoreServiceImpl.this
                        .getLobHandler().getBlobAsBinaryStream(
                                resultset, 1);
                return content;
            }
        });
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    /*
      * (non-Javadoc)
      *
      * @see
      * com.innofi.framework.service.LobStoreService#getBinaryStreamName(java.lang.String
      * )
      */
    public String getBinaryStreamName(String id) throws SQLException {
        final String sql = "SELECT name_ FROM idf_blob_store WHERE id_=?";
        List<String> list = getDaoSupport().getJdbcDao().query(sql,
                new Object[]{id}, new RowMapper<String>() {

            public String mapRow(ResultSet resultset, int i)
                    throws SQLException {
                String content = resultset.getString(1);
                return content;
            }

        });
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    /*
      * (non-Javadoc)
      *
      * @see com.innofi.framework.service.LobStoreService#storeString(java.lang.String)
      */
    public String storeString(final String content) throws SQLException {
        final String sql = "INSERT INTO idf_clob_store (id_,content_) values(?,?)";
        final String id = this.generateId();
        int updatedRowCount = getDaoSupport().getJdbcDao().execute(
                sql,
                new AbstractLobCreatingPreparedStatementCallback(this
                        .getLobHandler()) {

                    @Override
                    protected void setValues(
                            PreparedStatement preparedstatement,
                            LobCreator lobcreator) throws SQLException,
                            DataAccessException {
                        preparedstatement.setString(1, id);
                        lobcreator.setClobAsString(preparedstatement, 2,
                                content);
                    }
                });
        if (0 == updatedRowCount) {
            throw new SQLException("未能成功储存大字符串");
        }
        return id;
    }

    /*
      * (non-Javadoc)
      *
      * @see com.innofi.framework.service.LobStoreService#deleteString(java.lang.String)
      */
    public void deleteString(String id) throws SQLException {
        this.deleteFromClob(id);
    }

    /*
      * (non-Javadoc)
      *
      * @see com.innofi.framework.service.LobStoreService#updateString(java.lang.String,
      * java.lang.String)
      */
    public void updateString(final String id, final String content)
            throws SQLException {
        final String sql = "UPDATE idf_clob_store SET content_=? WHERE id_=?";
        int updatedRowCount = getDaoSupport().getJdbcDao().execute(
                sql,
                new AbstractLobCreatingPreparedStatementCallback(this
                        .getLobHandler()) {

                    @Override
                    protected void setValues(
                            PreparedStatement preparedstatement,
                            LobCreator lobcreator) throws SQLException,
                            DataAccessException {
                        preparedstatement.setString(2, id);
                        lobcreator.setClobAsString(preparedstatement, 1,
                                content);

                    }

                });
        if (0 == updatedRowCount) {
            throw new SQLException(String.format("未能成功更新大字符串[%s],请检查其是否存在", id));
        }
    }

    /*
      * (non-Javadoc)
      *
      * @see com.innofi.framework.service.LobStoreService#getString(java.lang.String)
      */
    public String getString(String id) throws SQLException {
        final String sql = "SELECT content_ FROM idf_clob_store WHERE id_=?";
        List<String> list = getDaoSupport().getJdbcDao().query(sql, new Object[]{id}, new RowMapper<String>() {
            public String mapRow(ResultSet resultset, int i)
                    throws SQLException {
                String content = LobStoreServiceImpl.this
                        .getLobHandler().getClobAsString(resultset, 1);
                return content;
            }
        });
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    /*
      * (non-Javadoc)
      *
      * @see
      * com.innofi.framework.service.LobStoreService#storeAsciiStream(java.io.InputStream
      * , int)
      */
    public String storeAsciiStream(final InputStream asciiStream,
                                   final int contentLength) throws SQLException {
        final String sql = "INSERT INTO idf_clob_store (id_,content_) values(?,?)";
        final String id = this.generateId();
        int updatedRowCount = getDaoSupport().getJdbcDao().execute(
                sql,
                new AbstractLobCreatingPreparedStatementCallback(this
                        .getLobHandler()) {

                    @Override
                    protected void setValues(
                            PreparedStatement preparedstatement,
                            LobCreator lobcreator) throws SQLException,
                            DataAccessException {
                        preparedstatement.setString(1, id);
                        lobcreator.setClobAsAsciiStream(preparedstatement, 2,
                                asciiStream, contentLength);
                    }
                });
        if (0 == updatedRowCount) {
            throw new SQLException("未能成功储存大字符串");
        }
        return id;
    }

    /*
      * (non-Javadoc)
      *
      * @see
      * com.innofi.framework.service.LobStoreService#deleteAsciiStream(java.lang.String)
      */
    public void deleteAsciiStream(String id) throws SQLException {
        this.deleteFromClob(id);
    }

    /*
      * (non-Javadoc)
      *
      * @see
      * com.innofi.framework.service.LobStoreService#updateAsciiStream(java.lang.String,
      * java.io.InputStream, int)
      */
    public void updateAsciiStream(final String id,
                                  final InputStream asciiStream, final int contentLength)
            throws SQLException {
        final String sql = "UPDATE idf_clob_store SET content_=? WHERE id_=?";
        int updatedRowCount = getDaoSupport().getJdbcDao().execute(
                sql,
                new AbstractLobCreatingPreparedStatementCallback(this
                        .getLobHandler()) {

                    @Override
                    protected void setValues(
                            PreparedStatement preparedstatement,
                            LobCreator lobcreator) throws SQLException,
                            DataAccessException {
                        preparedstatement.setString(2, id);
                        lobcreator.setClobAsAsciiStream(preparedstatement, 1,
                                asciiStream, contentLength);

                    }

                });
        if (0 == updatedRowCount) {
            throw new SQLException(String.format("未能成功更新大字符串[%s],请检查其是否存在", id));
        }

    }

    /*
      * (non-Javadoc)
      *
      * @see
      * com.innofi.framework.service.LobStoreService#getAsciiStream(java.lang.String)
      */
    public InputStream getAsciiStream(String id) throws SQLException {
        final String sql = "SELECT content_ FROM idf_clob_store WHERE id_=?";
        List<InputStream> list = getDaoSupport().getJdbcDao().query(sql,
                new Object[]{id}, new RowMapper<InputStream>() {

            public InputStream mapRow(ResultSet resultset, int i)
                    throws SQLException {
                InputStream content = LobStoreServiceImpl.this
                        .getLobHandler().getClobAsAsciiStream(
                                resultset, 1);
                return content;
            }

        });
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    /*
      * (non-Javadoc)
      *
      * @see
      * com.innofi.framework.service.LobStoreService#storeCharacterStream(java.io.Reader
      * , int)
      */
    public String storeCharacterStream(final Reader reader,
                                       final int contentLength) throws SQLException {
        final String sql = "INSERT INTO idf_clob_store (id_,content_) values(?,?)";
        final String id = this.generateId();
        int updatedRowCount = getDaoSupport().getJdbcDao().execute(
                sql,
                new AbstractLobCreatingPreparedStatementCallback(this
                        .getLobHandler()) {

                    @Override
                    protected void setValues(
                            PreparedStatement preparedstatement,
                            LobCreator lobcreator) throws SQLException,
                            DataAccessException {
                        preparedstatement.setString(1, id);
                        lobcreator.setClobAsCharacterStream(preparedstatement,
                                2, reader, contentLength);
                    }
                });
        if (0 == updatedRowCount) {
            throw new SQLException("未能成功储存大字符串");
        }
        return id;
    }

    /*
      * (non-Javadoc)
      *
      * @see
      * com.innofi.framework.service.LobStoreService#deleteCharacterStream(java.lang
      * .String)
      */
    public void deleteCharacterStream(String id) throws SQLException {
        this.deleteFromClob(id);
    }

    /*
      * (non-Javadoc)
      *
      * @see
      * com.innofi.framework.service.LobStoreService#updateCharacterStream(java.lang
      * .String, java.io.Reader, int)
      */
    public void updateCharacterStream(final String id, final Reader reader,
                                      final int contentLength) throws SQLException {
        final String sql = "UPDATE idf_clob_store SET content_=? WHERE id_=?";
        int updatedRowCount = getDaoSupport().getJdbcDao().execute(
                sql,
                new AbstractLobCreatingPreparedStatementCallback(this
                        .getLobHandler()) {

                    @Override
                    protected void setValues(
                            PreparedStatement preparedstatement,
                            LobCreator lobcreator) throws SQLException,
                            DataAccessException {
                        preparedstatement.setString(2, id);
                        lobcreator.setClobAsCharacterStream(preparedstatement,
                                1, reader, contentLength);

                    }

                });
        if (0 == updatedRowCount) {
            throw new SQLException(String.format("未能成功更新大字符串[%s],请检查其是否存在", id));
        }

    }

    /*
      * (non-Javadoc)
      *
      * @see
      * com.innofi.framework.service.LobStoreService#getCharacterStream(java.lang.String
      * )
      */
    public Reader getCharacterStream(String id) throws SQLException {
        final String sql = "SELECT content_ FROM idf_clob_store WHERE id_=?";
        List<Reader> list = getDaoSupport().getJdbcDao().query(sql,
                new Object[]{id}, new RowMapper<Reader>() {

            public Reader mapRow(ResultSet resultset, int i)
                    throws SQLException {
                Reader content = LobStoreServiceImpl.this
                        .getLobHandler().getClobAsCharacterStream(
                                resultset, 1);
                return content;
            }

        });
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 生成一个主键，唯一标识一条仓库中的记录。
     * <p>
     * 默认以{@link java.rmi.dgc.VMID}产生，子类可覆盖此函数。
     * </p>
     *
     * @return 一个唯一的主键
     */
    protected String generateId() {
        return UUID.randomUUID().toString();
        //return vmid.toString();
    }


    public void setLobHandler(LobHandler lobHandler) {
        this.lobHandler = lobHandler;
    }

    /**
     * 根据数据库类型取得LobHander接口的具体实现。
     * <p>
     * <li>MySQL、MSSQL、DB2等都遵循JDBC标准的BLOB和CLOB操作API，因此使用DefaultLobHander即可</li>
     * <li>Oracle9i及以前的版本需要使用OracleLobHandler</li>
     * </p>
     *
     * @return LobHander 特定于数据库的实例
     */
    protected LobHandler getLobHandler() {
        if (null == this.lobHandler) {
            synchronized (this) {
                if (null == this.lobHandler) {
                    LobHandlerResolver lobHandlerResolver = new LobHandlerResolver(
                            this);
                    getDaoSupport().getJdbcDao().execute(lobHandlerResolver);
                }
            }
        }
        return this.lobHandler;
    }

    private LobHandler lobHandler = null;
}

class LobHandlerResolver implements ConnectionCallback<Void> {
    public LobHandlerResolver(LobStoreServiceImpl lobStoreServiceImpl) {
        this.lobStoreServiceImpl = lobStoreServiceImpl;
    }

    public Void doInConnection(Connection connection) throws SQLException,
            DataAccessException {
        String productName = connection.getMetaData().getDatabaseProductName();
        LobHandler lobHandler = null;
        if (StringUtils.containsIgnoreCase(productName, ORACLE)) {
            lobHandler = createOracleLobHandler();
        } else {
            lobHandler = createDefaultLobHandler(productName);
        }
        this.lobStoreServiceImpl.setLobHandler(lobHandler);
        return null;
    }

    /**
     * 创建一个通用的的LobHandler,但对SqlServer数据库做了特殊处理.
     *
     * @param productName 数据库驱动的名称
     * @return 返回DefaultLobHandler对象
     */
    private DefaultLobHandler createDefaultLobHandler(String productName) {
        DefaultLobHandler defaultLobHandler = new DefaultLobHandler();
        // 为支持sqljdbc4的处理方式,需要将其大对象流包装到BLOB/CLOB里,否则流将被自动关闭
        if (StringUtils.containsIgnoreCase(productName, SQLSERVER)) {
            defaultLobHandler.setWrapAsLob(true);
        }
        return defaultLobHandler;
    }

    /**
     * 创建一个特定于Oracle的LobHandler,用于适配Oracle的大对象字段.
     *
     * @return 返回OracleLobHandler对象
     */
    private OracleLobHandler createOracleLobHandler() {
        OracleLobHandler oracleLobHandler = new OracleLobHandler();
        Map<String, NativeJdbcExtractor> jdbcExtractors = ContextHolder.getSpringBeanFactory().getBeansOfType(
                NativeJdbcExtractor.class);
        NativeJdbcExtractor jdbcExtractor;
        if (null == jdbcExtractors || 0 == jdbcExtractors.size()) {
            jdbcExtractor = new CommonsDbcpNativeJdbcExtractor();
        } else {
            jdbcExtractor = jdbcExtractors.values().iterator().next();
        }
        oracleLobHandler.setNativeJdbcExtractor(jdbcExtractor);
        return oracleLobHandler;
    }

    private LobStoreServiceImpl lobStoreServiceImpl = null;
    private static final String ORACLE = "oracle";
    private static final String SQLSERVER = "sql server";

}