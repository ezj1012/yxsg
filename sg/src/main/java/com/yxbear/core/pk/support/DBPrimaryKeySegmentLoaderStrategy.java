package com.yxbear.core.pk.support;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.support.JdbcUtils;

import com.yxbear.core.exception.CoreException;
import com.yxbear.core.pk.PrimaryKeySegment;
import com.yxbear.core.pk.PrimaryKeySegmentLoaderStrategy;
import com.yxbear.core.pk.bean.SimplePrimaryKeySegment;

public class DBPrimaryKeySegmentLoaderStrategy implements PrimaryKeySegmentLoaderStrategy {

    private DataSource dataSource;

    @Value("${project.primaryKey.tableName:SYS_PRIMARY_KEY}")
    private String tableName;

    public DBPrimaryKeySegmentLoaderStrategy(DataSource dataSource) {
        super();
        this.dataSource = dataSource;
    }

    @Override
    public PrimaryKeySegment load(String pkName, int batchSize) {
        Connection conn = null;
        Boolean autoCommit = null;
        Integer tr = null;
        try {
            conn = dataSource.getConnection();
            autoCommit = conn.getAutoCommit();
            tr = conn.getTransactionIsolation();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            PrimaryKeySegment load = load(conn, pkName, batchSize, 0);
            conn.commit();
            return load;
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException e1) {
                // nothing
            }
            fail(e);
            return null;
        } finally {
            if (conn != null) {
                try {
                    if (autoCommit != null) {
                        conn.setAutoCommit(autoCommit);
                    }
                } catch (SQLException e) {
                    // nothing
                }
                try {
                    if (tr != null) {
                        conn.setTransactionIsolation(tr);
                    }
                } catch (SQLException e) {
                    // nothing
                }
            }
            JdbcUtils.closeConnection(conn);
        }
    }

    private PrimaryKeySegment load(Connection conn, String pkName, int batchSize, int tryCount) {
        if (tryCount > 3) {
            fail(null);
        }
        String querySql = "SELECT NAME,CUR_VALUE,VERSION FROM " + this.tableName + " WHERE NAME = '" + pkName + "'";
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(querySql);
            if (resultSet.next()) {
                Long version = resultSet.getLong("VERSION");
                Long value = resultSet.getLong("CUR_VALUE");
                String updateSql = "UPDATE " + this.tableName + " SET CUR_VALUE=CUR_VALUE + " + batchSize + ", VERSION = VERSION + 1" + " WHERE NAME = '" + pkName + "' AND VERSION = '" + version + "'";
                int updateCount = stmt.executeUpdate(updateSql);
                if (updateCount > 0) { return new SimplePrimaryKeySegment(batchSize, value + batchSize, value); }
            } else {
                // 插入一条新的记录.
                long cTime = System.currentTimeMillis();
                String insertSql = "INSERT INTO " + this.tableName + " (NAME,CUR_VALUE,VERSION) values ('" + pkName + "',10000,1) ";
                if (stmt.execute(insertSql)) { return new SimplePrimaryKeySegment(batchSize, 1L + batchSize, 1L); }
            }
        } catch (SQLException e) {
            if (tryCount >= 3) {
                fail(e);
            }
        }
        return load(conn, pkName, batchSize, ++tryCount);
    }

    private void fail(SQLException e) {
        throw new CoreException("数据库加载主键失败!", e);
    }

}
