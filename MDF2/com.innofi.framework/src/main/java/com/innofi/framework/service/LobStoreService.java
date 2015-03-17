/** Copyright © 2012-2016 Ming Sheng information technology co., LTD.
 * All rights reserved.*/
package com.innofi.framework.service;

import java.io.InputStream;
import java.io.Reader;
import java.sql.SQLException;

/**
 * 功能/ 模块：工具类
 *
 * @author 刘名寓  liumy2009@126.com
 * @version 2.0.0 12-12-04
 *          <p> 大对象仓库服务接口，对外提供二进制数据，超长文本等的储存、更新、取出等操作。</p>
 *          <p/>
 *          <p>
 *          这里的操作分为两组:<br/>
 *          <ol>
 *          <li>byte及binaryStream相关的为第一组，代表了对二进制数据，如图片、声音、视频等的操作，在数据库中通常以BLOB或IMAGE类型表示</li>
 *          <li>string、asciiString及characterStream相关的为第二组，代表了对大文本对象，如文本文件、新闻公告等的操作，
 *          在数据库中通常以CLOB或TEXT类型表示</li>
 *          </ol>
 *          需要注意的是，不管为哪种类型的大对象，仓库中均不允许其内容为空， 若业务数据允许引用一个空对象，请将业务表中的允许为空的记录的相关字段设置为NULL。<br/>
 *          对于不再需要的大对象，请及时使用delete*函数清理，以免造成空间的浪费。<br/>
 *          此接口中的所有函数均不能保证一定能完成指定的操作，请在使用时处理抛出的异常<br/>
 *          </p>
 *          <p/>
 *          修订历史：
 *          日期  作者  参考  描述
 *          北京名晟信息技术有限公司版权所有.
 */
public interface LobStoreService {
    /**
     * 保存一个byte数组，并返回它在仓库中的主键。
     *
     * @param content 需要保存的内容，不能为空
     * @return 保存后的内容在仓库中的主键，稍后可通过{@link #getBytes(String)}取得仓库中的内容
     * @throws java.sql.SQLException 将byte数组保存到数据库时可能抛出此异常
     */
    public String storeBytes(byte[] content) throws SQLException;

    /**
     * 保存一个byte数组及其名称，并返回它在仓库中的主键。
     *
     * @param content 需要保存的内容，不能为空
     * @param name    内容的名称
     * @return 保存后的内容在仓库中的主键，稍后可通过{@link #getBytes(String)}取得仓库中的内容
     * @throws java.sql.SQLException 将byte数组保存到数据库时可能抛出此异常
     */
    public String storeBytes(byte[] content, String name) throws SQLException;

    /**
     * 根据内容在仓库中的主键，删除对应的记录。
     *
     * @param id 需要删除的内容在仓库中的主键
     * @throws java.sql.SQLException 在从数据库中删除数据时可能抛出此异常； 此外，如果删除不成功，如与主键对应的记录不存在，也抛出此异常。
     */
    public void deleteBytes(String id) throws SQLException;

    /**
     * 更新主键所指的内容。
     * <p>
     * 保证此内容的名称<font color="red">不</font>变。
     * </p>
     *
     * @param id      需要被更新的内容的主键
     * @param content 新的值
     * @throws java.sql.SQLException 在更新数据库的过程中可能抛出此异常；此外，如果更新不成功，如与主键对应的记录不存在，也抛出此异常。
     */
    public void updateBytes(String id, byte[] content) throws SQLException;

    /**
     * 更新主键所指的内容的名称。
     * <p>
     * 保证此内容的值<font color="red">不</font>变。
     * </p>
     *
     * @param id   需要被更新的内容的主键
     * @param name 新的名称
     * @throws java.sql.SQLException 在更新数据库的过程中可能抛出此异常；此外，如果更新不成功，如与主键对应的记录不存在，也抛出此异常。
     */
    public void updateBytesName(String id, String name) throws SQLException;

    /**
     * 更新主键所指的内容及其名称。
     *
     * @param id      需要被更新的内容的主键
     * @param content 新的值
     * @param name    新的名称
     * @throws java.sql.SQLException 在更新数据库的过程中可能抛出此异常；此外，如果更新不成功，如与主键对应的记录不存在，也抛出此异常。
     */
    public void updateBytes(String id, byte[] content, String name)
            throws SQLException;

    /**
     * 根据主键查询内容，若未找到，返回<codebuilder>null</codebuilder>。
     *
     * @param id 内容的主键，通常由业务记录中的某一字段保有
     * @return 与主键相对应的内容
     * @throws java.sql.SQLException 在查询数据库的过程中可能抛出此异常。
     */
    public byte[] getBytes(String id) throws SQLException;

    /**
     * 根据主键查询内容的名称，若未找到，返回<codebuilder>null</codebuilder>。
     *
     * @param id 内容的主键，通常由业务记录中的某一字段保有
     * @return 与主键相对应的内容的名称
     * @throws java.sql.SQLException 在查询数据库的过程中可能抛出此异常。
     */
    public String getBytesName(String id) throws SQLException;

    /**
     * 保存一个二进制数据流中的数据，并返回其在仓库中的主键。
     *
     * @param inputStream   需要保存的内容，不能为空
     * @param contentLength 内容的长度。如若保存一个{@link java.io.File}对象，则通常此长度由
     *                      {@link java.io.File#length}取得
     * @return 保存后的内容在仓库中的主键，稍后可通过{@link #getBinaryStream(String)}取得仓库中的内容
     * @throws java.sql.SQLException
     */
    public String storeBinaryStream(InputStream inputStream, int contentLength)
            throws SQLException;

    /**
     * 保存一个二进制数据流中的数据及其名称，并返回其在仓库中的主键。
     *
     * @param inputStream   需要保存的内容，不能为空
     * @param contentLength 内容的长度。如若保存一个{@link java.io.File}对象，则通常此长度由
     *                      {@link java.io.File#length}取得
     * @param name          内容的名称
     * @return 保存后的内容在仓库中的主键，稍后可通过{@link #getBinaryStream(String)}取得仓库中的内容
     * @throws java.sql.SQLException
     */
    public String storeBinaryStream(InputStream inputStream, int contentLength,
                                    String name) throws SQLException;

    /**
     * 根据内容在仓库中的主键，删除对应的记录。
     *
     * @param id 需要被删除的内容的主键
     * @throws java.sql.SQLException 在删除数据库中的记录时可能抛出此异常；此外，如果删除不成功，如与主键对应的记录不存在，也抛出此异常。
     */
    public void deleteBinaryStream(String id) throws SQLException;

    /**
     * 更新指定主键代表的内容。
     * <p>
     * 保证此内容的名称<font color="red">不</font>变。
     * </p>
     *
     * @param id            需要被更新的内容的主键
     * @param inputStream   用于更新内容的二进制输入流
     * @param contentLength 二进制输入流的长度
     * @throws java.sql.SQLException 在更新数据库中的记录时可能抛出此异常；此外，如果更新不成功，如与主键对应的记录不存在，也抛出此异常。;
     */
    public void updateBinaryStream(String id, InputStream inputStream,
                                   int contentLength) throws SQLException;

    /**
     * 更新指定主键代表的内容的名称。
     * <p>
     * 保证此内容的值<font color="red">不</font>变。
     * </p>
     *
     * @param id   需要被更新的内容的主键
     * @param name 新的名称
     * @throws java.sql.SQLException 在更新数据库中的记录时可能抛出此异常；此外，如果更新不成功，如与主键对应的记录不存在，也抛出此异常。;
     */
    public void updateBinaryStreamName(String id, String name)
            throws SQLException;

    /**
     * 更新指定主键代表的内容及其名称。
     *
     * @param id            需要被更新的内容的主键
     * @param inputStream   用于更新内容的二进制输入流
     * @param contentLength 二进制输入流的长度
     * @param name          内容的名称
     * @throws java.sql.SQLException 在更新数据库中的记录时可能抛出此异常；此外，如果更新不成功，如与主键对应的记录不存在，也抛出此异常。;
     */
    public void updateBinaryStream(String id, InputStream inputStream,
                                   int contentLength, String name) throws SQLException;

    /**
     * 根据主键取得内容的二进制输入流，若未找到，返回<codebuilder>null</codebuilder>。
     *
     * @param id 需要查询的内容的主键
     * @return 代表内容的二进制输入流
     * @throws java.sql.SQLException 在查询数据库的过程中可能抛出此异常
     */
    public InputStream getBinaryStream(String id) throws SQLException;

    /**
     * 根据主键取得内容的二进制输入流的名称，若未找到，返回<codebuilder>null</codebuilder>。
     *
     * @param id 需要查询的内容的主键
     * @return 代表内容的二进制输入流的名称
     * @throws java.sql.SQLException 在查询数据库的过程中可能抛出此异常
     */
    public String getBinaryStreamName(String id) throws SQLException;

    /**
     * 保存一个超长的字符串，并返回其在仓库中的主键。
     *
     * @param content 需要保存的内容。
     * @return 保存后的内容在仓库中的主键，稍后可通过{@link #getString(String)}取得仓库中的内容
     * @throws java.sql.SQLException 在将内容保存到数据库的过程中可能抛出此异常。
     */
    public String storeString(String content) throws SQLException;

    /**
     * 根据内容在仓库中的主键删除对应的记录。
     *
     * @param id 需要删除的内容在仓库中的主键
     * @throws java.sql.SQLException 在从数据库中删除记录时可能抛出此异常；此外，如果删除不成功，如与主键对应的记录不存在，也抛出此异常。;
     */
    public void deleteString(String id) throws SQLException;

    /**
     * 更新指定的主键代表的内容。
     *
     * @param id      需要更新的内容的主键
     * @param content 用于更新的值
     * @throws java.sql.SQLException 在更新数据库中的记录时可能抛出此异常；此外，如果更新不成功，如与主键对应的记录不存在，也抛出此异常。
     */
    public void updateString(String id, String content) throws SQLException;

    /**
     * 根据主键查询其在仓库中的内容，若未找到，返回<codebuilder>null</codebuilder>。
     *
     * @param id 需要查询的内容的主键
     * @return 代表查询内容的字符串
     * @throws java.sql.SQLException 在查询数据库的过程中可能抛出此异常
     */
    public String getString(String id) throws SQLException;

    /**
     * 保存一个字节流，并返回其在仓库中的主键。
     *
     * @param asciiSrteam   需要保存到仓库中的字节流。
     * @param contentLength 字节流的长度。
     * @return 保存后的内容在仓库中的主键，稍后可通过{@link #getAsciiStream(String)}取得仓库中的内容
     * @throws java.sql.SQLException 在将内容保存到数据库的过程中可能抛出此异常。
     */
    public String storeAsciiStream(InputStream asciiSrteam, int contentLength)
            throws SQLException;

    /**
     * 根据内容在仓库中的主键删除对应的记录。
     *
     * @param id 需要删除的内容在仓库中的主键
     * @throws java.sql.SQLException 在从数据库中删除记录时可能抛出此异常；此外，如果删除不成功，如与主键对应的记录不存在，也抛出此异常。;
     */
    public void deleteAsciiStream(String id) throws SQLException;

    /**
     * 更新指定的主键代表的内容。
     *
     * @param id            需要更新的内容的主键
     * @param asciiStream   用于更新的字节流
     * @param contentLength 字节流的长度
     * @throws java.sql.SQLException 在更新数据库中的记录时可能抛出此异常；此外，如果更新不成功，如与主键对应的记录不存在，也抛出此异常。;
     */
    public void updateAsciiStream(String id, InputStream asciiStream,
                                  int contentLength) throws SQLException;

    /**
     * 根据主键查询其在仓库中的内容，若未找到，返回<codebuilder>null</codebuilder>。
     *
     * @param id 需要查询的内容的主键
     * @return 代表内容的字节流
     * @throws java.sql.SQLException 在查询数据库的过程中可能抛出此异常
     */
    public InputStream getAsciiStream(String id) throws SQLException;

    /**
     * 保存一个字符流，并返回其在仓库中的主键。
     *
     * @param reader        需要保存的字符流
     * @param contentLength 字符流的长度
     * @return 保存后的内容在仓库中的主键，稍后可通过{@link #getCharacterStream(String)}取得仓库中的内容
     * @throws java.sql.SQLException 在将内容保存到数据库的过程中可能抛出此异常。
     */
    public String storeCharacterStream(Reader reader, int contentLength)
            throws SQLException;

    /**
     * 根据内容在仓库中的主键删除对应的记录。
     *
     * @param id 需要删除的内容在仓库中的主键
     * @throws java.sql.SQLException 在从数据库中删除记录时可能抛出此异常；此外，如果删除不成功，如与主键对应的记录不存在，也抛出此异常。;
     */
    public void deleteCharacterStream(String id) throws SQLException;

    /**
     * 更新指定的主键代表的内容。
     *
     * @param id            需要更新的内容的主键
     * @param reader        用于更新的字符流
     * @param contentLength 字符流的长度
     * @throws java.sql.SQLException 在更新数据库中的记录时可能抛出此异常；此外，如果更新不成功，如与主键对应的记录不存在，也抛出此异常。;
     */
    public void updateCharacterStream(String id, Reader reader,
                                      int contentLength) throws SQLException;

    /**
     * 根据主键查询其在仓库中的内容，若未找到，返回<codebuilder>null</codebuilder>。
     *
     * @param id 需要查询的内容的主键
     * @return 代表内容的字符流
     * @throws java.sql.SQLException 在查询数据库的过程中可能抛出此异常
     */
    public Reader getCharacterStream(String id) throws SQLException;

    /**
     * 服务ID，代表了其在spring中的beanName。
     */
    public static final String SERVICE_ID = "mdf.lobStoreService";
}
