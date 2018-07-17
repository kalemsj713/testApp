/*
 * To change this license header, choose License Headers in Project AppProperties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kalemsj.testapp.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import static kalemsj.testapp.utils.Utils.getReportDate;
import kalemsj.testapp.xml.wrappers.FirstEntry;

/**
 *
 * @author Karagaev S.A.
 */
public class JDBCService {

    private static final String POSTGRESQL_DRIVER = "org.postgresql.Driver";
    private static final String POSTGRESQL_URL_PREFIX = "jdbc:postgresql://";

    private static final String SELECT_SQL = "SELECT FIELD FROM TEST ORDER BY FIELD";
    private static final String DELETE_SQL = "DELETE FROM TEST";
    private static final String INSERT_SQL = "INSERT INTO TEST (FIELD) VALUES (?)";

    private static Logger log = Logger.getLogger(JDBCService.class.getName());

    private static Connection getDBConnection(String dbUrl, String dbLogin, String dbPassword) {
        Connection dbConnection = null;
        try {
            dbConnection = getDataSource(dbUrl, dbLogin, dbPassword).getConnection();
            return dbConnection;
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "", ex);
        }
        return dbConnection;
    }

    private static DataSource getDataSource(String dbUrl, String dbLogin, String dbPassword) {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(POSTGRESQL_DRIVER);
        dataSource.setUrl(POSTGRESQL_URL_PREFIX + dbUrl);
        dataSource.setUsername(dbLogin);
        dataSource.setPassword(dbPassword);
        dataSource.setPoolPreparedStatements(true);
        dataSource.setMaxOpenPreparedStatements(25);
        return dataSource;
    }

    public static void deleteRecordsFromTestTable(String dbUrl, String dbLogin, String dbPassword) {
        Connection dbConnection = null;
        Statement statement = null;
        try {
            dbConnection = getDBConnection(dbUrl, dbLogin, dbPassword);
            statement = dbConnection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet r = statement.executeQuery(SELECT_SQL);
            r.last();
            int count = r.getRow();
            if (count > 0) {
                statement.execute(DELETE_SQL);
                log.log(Level.FINE, "Old records is deleted TEST table. Current time: {0}", getReportDate());
            }

        } catch (SQLException ex) {
            log.log(Level.SEVERE, "", ex);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    log.log(Level.SEVERE, "", ex);
                }
            }
            if (dbConnection != null) {
                try {
                    dbConnection.close();
                } catch (SQLException ex) {
                    log.log(Level.SEVERE, "", ex);
                }
            }
        }
    }

    public static List<FirstEntry> selectFieldsFromTestTable(String dbUrl, String dbLogin, String dbPassword) {
        Connection dbConnection = null;
        PreparedStatement preparedStatement = null;
        List<FirstEntry> testFieldList = new ArrayList<>();

        try {
            dbConnection = getDBConnection(dbUrl, dbLogin, dbPassword);
            preparedStatement = dbConnection.prepareStatement(SELECT_SQL);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                testFieldList.add(new FirstEntry(rs.getInt("FIELD")));
            }
            log.log(Level.FINE, "Records is selected into TEST table. Current time: {0}", getReportDate());

        } catch (SQLException ex) {
            log.log(Level.SEVERE, "", ex);
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException ex) {
                    log.log(Level.SEVERE, "", ex);
                }
            }
            if (dbConnection != null) {
                try {
                    dbConnection.close();
                } catch (SQLException ex) {
                    log.log(Level.SEVERE, "", ex);
                }
            }
        }
        return testFieldList;
    }

    public static void batchInsertRecordsIntoTable(String dbUrl, String dbLogin, String dbPassword, Integer nField) {
        Connection dbConnection = null;
        PreparedStatement preparedStatement = null;
        final int batchSize = 1000;
        int count = 0;
        try {
            dbConnection = getDBConnection(dbUrl, dbLogin, dbPassword);
            preparedStatement = dbConnection.prepareStatement(INSERT_SQL);
            dbConnection.setAutoCommit(false);
            for (int i = 1; i <= nField; i++) {
                preparedStatement.setInt(1, i);
                preparedStatement.addBatch();
                if (++count % batchSize == 0) {
                    preparedStatement.executeBatch();
                }
            }
            preparedStatement.executeBatch();
            dbConnection.commit();
            log.log(Level.FINE, "Records is inserted into TEST table. Current time: {0}", getReportDate());
        } catch (SQLException e) {
            log.log(Level.SEVERE, "", e);
            try {
                dbConnection.rollback();
            } catch (SQLException ex) {
                log.log(Level.SEVERE, "", ex);
            }
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException ex) {
                    log.log(Level.SEVERE, "", ex);
                }
            }
            if (dbConnection != null) {
                try {
                    dbConnection.close();
                } catch (SQLException ex) {
                    log.log(Level.SEVERE, "", ex);
                }
            }
        }
    }
}
