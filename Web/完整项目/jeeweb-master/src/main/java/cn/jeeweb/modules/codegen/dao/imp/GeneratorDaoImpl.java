package cn.jeeweb.modules.codegen.dao.imp;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.sql.DataSource;

import cn.jeeweb.core.common.dao.impl.CommonDaoImpl;
import cn.jeeweb.core.utils.PropertiesUtil;
import cn.jeeweb.core.utils.SpringContextHolder;
import cn.jeeweb.core.utils.StringUtils;
import cn.jeeweb.modules.codegen.codegenerator.data.DbColumnInfo;
import cn.jeeweb.modules.codegen.codegenerator.data.DbTableInfo;
import cn.jeeweb.modules.codegen.codegenerator.utils.CodeGenUtils;
import cn.jeeweb.modules.codegen.codegenerator.utils.sql.SqlUtils;
import cn.jeeweb.modules.codegen.dao.IGeneratorDao;
import oracle.jdbc.driver.OracleConnection;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

/**
 * http://blog.csdn.net/linwei_hello/article/details/21639657
 * http://www.cnblogs.com/csuwangwei/archive/2012/01/30/2331737.html
 * http://www.thinksaas.cn/topics/0/195/195191.html 数据库底层操作（还有HIBREATE的映射）
 * 
 * @author 白猫
 *
 */
@SuppressWarnings({ "resource", "unchecked", "rawtypes" })
@Repository("generatorDao")
public class GeneratorDaoImpl extends CommonDaoImpl implements IGeneratorDao {
	String properiesName = "dbconfig.properties";
	PropertiesUtil propertiesUtil = new PropertiesUtil(properiesName);

	@Override
	public Boolean createTableByXml(String xml) throws HibernateException, SQLException {
		SessionFactoryImpl sessionFactory = (SessionFactoryImpl) SpringContextHolder.getBean(SessionFactory.class);
		// 重新构建一个Configuration
		org.hibernate.cfg.Configuration newconf = new org.hibernate.cfg.Configuration();
		newconf.addXML(xml).setProperty("hibernate.dialect", sessionFactory.getDialect().getClass().getName());

		SchemaExport dbExport = new SchemaExport(newconf,
				SessionFactoryUtils.getDataSource(getSession().getSessionFactory()).getConnection());
		dbExport.execute(true, true, false, true);
		List<Exception> exceptionList = dbExport.getExceptions();
		for (Exception exception : exceptionList) {
			throw new RuntimeException(exception.getMessage());
		}
		if (exceptionList.size() > 0) {
			return false;
		}
		return false;
	}

	/**
	 * 获得数据源 直接设置就可以了
	 * 
	 * @return
	 */
	private DataSource getDataSource() {
		return SessionFactoryUtils.getDataSource(getSession().getSessionFactory());
	}

	private Connection getConnection() {
		try {
			Connection conn = null;
			String dbType = propertiesUtil.getString("connection.dbType");
			String url = propertiesUtil.getString("connection.url");
			String username = propertiesUtil.getString("connection.username");
			String password = propertiesUtil.getString("connection.password");
			String driverClassName = "com.mysql.jdbc.Driver";
			if (dbType.equals("sqlserver")) {
				driverClassName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
			} else if (dbType.equals("mysql")) {
				driverClassName = "com.mysql.jdbc.Driver";
			} else if (dbType.equals("oracle")) {
				driverClassName = "oracle.jdbc.driver.OracleDriver";
			} else {
				return getDataSource().getConnection();
			}
			// 初始化JDBC驱动并让驱动加载到jvm中
			Class.forName(driverClassName);
			conn = DriverManager.getConnection(url, username, password);
			conn.setAutoCommit(true);
			return conn;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<DbTableInfo> getDbTables() {
		ResultSet resultSet = null;
		Connection connection = null;
		List<DbTableInfo> dbTableInfos = new ArrayList<DbTableInfo>();
		try {
			connection = getConnection();
			connection.setAutoCommit(true);

			String[] types = { "TABLE" };
			// 判断是否为MYSQL
			String driverName = connection.getMetaData().getDriverName().toUpperCase();
			if (driverName.contains("ORACLE")) {
				/**
				 * 设置连接属性,使得可获取到表的REMARK(备注)
				 */
				((OracleConnection) connection).setRemarksReporting(true);
				resultSet = connection.getMetaData().getTables(null, propertiesUtil.getString("connection.username"),
						null, types);
			} else {
				resultSet = connection.getMetaData().getTables(null, null, null, types);
			}
			while (resultSet.next()) {
				String tableName = resultSet.getString("TABLE_NAME");
				String remarks = resultSet.getString("REMARKS");
				if (StringUtils.isEmpty(remarks)) {

					if (driverName.contains("MySQL")) {
						// String schemas = getCatalog(connection);
						// remarks = getTableComment("jeeweb", tableName,
						// connection);
					}
				}
				DbTableInfo dbTableInfo = new DbTableInfo();
				dbTableInfo.setTableName(tableName);
				dbTableInfo.setRemarks(remarks);
				dbTableInfos.add(dbTableInfo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (resultSet != null)
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace();

				} finally {
					if (connection != null)
						try {
							connection.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
				}
		}
		return dbTableInfos;
	}

	@Override
	public List<DbColumnInfo> getDbColumnInfo(String tableName) {
		ResultSet resultSet = null;
		Connection connection = null;
		List<DbColumnInfo> columnInfos = new ArrayList<DbColumnInfo>();
		try {
			connection = getConnection();
			connection.setAutoCommit(true);
			// 判断是否为MYSQL
			String driverName = connection.getMetaData().getDriverName().toUpperCase();
			if (driverName.contains("ORACLE")) {
				/**
				 * 设置连接属性,使得可获取到表的REMARK(备注)
				 */
				((OracleConnection) connection).setRemarksReporting(true);
			}
			// 获得列的信息
			resultSet = connection.getMetaData().getColumns(null, null, tableName, null);
			while (resultSet.next()) {
				// 获得字段名称
				String columnName = resultSet.getString("COLUMN_NAME");
				// 获得字段类型名称
				String typeName = resultSet.getString("TYPE_NAME").toUpperCase();
				// 获得字段大小
				String columnSize = resultSet.getString("COLUMN_SIZE");
				// 获得字段备注
				String remarks = resultSet.getString("REMARKS");

				// 该列是否为空
				Boolean nullable = Boolean.FALSE;
				if (driverName.contains("ORACLE")) {
					nullable = resultSet.getBoolean("NULLABLE");
				} else {
					nullable = resultSet.getBoolean("IS_NULLABLE");
				}
				// 小数部分的位数
				String decimalDigits = resultSet.getString("DECIMAL_DIGITS");
				// 默认值
				String columnDef = resultSet.getString("COLUMN_DEF");
				DbColumnInfo info = new DbColumnInfo(columnName, typeName, columnSize, remarks, nullable, false, false,
						columnDef, decimalDigits);
				columnInfos.add(info);
			}

			// 获得主键的信息
			resultSet = connection.getMetaData().getPrimaryKeys(null, null, tableName);
			while (resultSet.next()) {
				String primaryKey = resultSet.getString("COLUMN_NAME");
				// 设置是否为主键
				for (DbColumnInfo dbColumnInfo : columnInfos) {
					if (primaryKey != null && primaryKey.equals(dbColumnInfo.getColumnName()))
						dbColumnInfo.setParmaryKey(true);
					else
						dbColumnInfo.setParmaryKey(false);
				}
			}

			// 获得外键信息
			resultSet = connection.getMetaData().getImportedKeys(null, null, tableName);
			while (resultSet.next()) {
				String exportedKey = resultSet.getString("FKCOLUMN_NAME");
				// 设置是否是外键
				for (DbColumnInfo dbColumnInfo : columnInfos) {
					if (exportedKey != null && exportedKey.equals(dbColumnInfo.getColumnName()))
						dbColumnInfo.setImportedKey(true);
					else
						dbColumnInfo.setImportedKey(false);
				}
			}

		} catch (

		Exception e) {
			e.printStackTrace();
			throw new RuntimeException("获取字段信息的时候失败，请将问题反映到维护人员。" + e.getMessage(), e);
		} finally {
			if (resultSet != null)
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					if (connection != null)
						try {
							connection.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
				}
		}
		Set set = new HashSet();
		set.addAll(columnInfos);
		columnInfos.clear();
		columnInfos.addAll(set);
		return columnInfos;
	}

	@Override
	public void dropTable(String tableName) {
		String dropSql = SqlUtils.getSqlUtils().getSqlByID("dropTable").getContent();
		dropSql = dropSql.replaceAll("\\$\\{tablename\\}", tableName);
		executeSql(dropSql);
	}

	@Override
	public Boolean isExistTable(String tableName) {
		Connection conn = null;
		ResultSet rs = null;
		String tableNamePattern = tableName;
		try {
			String[] types = { "TABLE" };
			conn = getConnection();
			String dbType = CodeGenUtils.getDbType().toLowerCase();
			if ("oracle".equals(dbType)) {
				tableNamePattern = tableName.toUpperCase();
				// 由于PostgreSQL是大小写敏感的，并默认对SQL语句中的数据库对象名称转换为小写
			} else if ("postgresql".equals(dbType)) {
				tableNamePattern = tableName.toLowerCase();
			}
			DatabaseMetaData dbMetaData = conn.getMetaData();
			rs = dbMetaData.getTables(null, null, tableNamePattern, types);
			if (rs.next()) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			throw new RuntimeException();
		} finally {// 关闭连接
			try {
				if (rs != null) {
					rs.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}