package cn.lakecode.web.gencode.data;

import cn.lakecode.web.gencode.model.TableField;
import cn.lakecode.web.gencode.model.TableInfo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MysqlData {

    private final String url;

    private final String username;

    private final String password;

    public MysqlData(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    private Connection jdbcConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(url, username, password);
    }

    public Map<String, TableInfo> tableInfo() {
        try (Connection connection = jdbcConnection()) {
            Map<String, TableInfo> tables = new HashMap<String, TableInfo>();
            String sql =
                    """
                                     SELECT
                                         t.TABLE_NAME AS `Table Name`,
                                         t.TABLE_COMMENT AS `Table Comment`,
                                         c.COLUMN_NAME AS `Column Name`,
                                         c.COLUMN_TYPE AS `Column Type`,
                                         c.COLUMN_COMMENT AS `Column Comment`,
                                         IF(k.COLUMN_NAME IS NOT NULL, 'YES', 'NO') AS `Primary Key`
                                     FROM
                                         INFORMATION_SCHEMA.TABLES t
                                             JOIN
                                         INFORMATION_SCHEMA.COLUMNS c
                                         ON t.TABLE_NAME = c.TABLE_NAME AND t.TABLE_SCHEMA = c.TABLE_SCHEMA
                                             LEFT JOIN
                                         INFORMATION_SCHEMA.KEY_COLUMN_USAGE k
                                         ON c.TABLE_NAME = k.TABLE_NAME
                                             AND c.COLUMN_NAME = k.COLUMN_NAME
                                             AND k.CONSTRAINT_NAME = 'PRIMARY'
                                     WHERE
                                         t.TABLE_SCHEMA = ?
                                     ORDER BY
                                         t.TABLE_NAME, c.ORDINAL_POSITION;           \s
                            """;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, getDatabase(url));
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Map<String, String>> result = new ArrayList<>();
            while (resultSet.next()) {
                String columnName = resultSet.getString("Column Name");
                String columnComment = resultSet.getString("Column Comment");
                String primaryKey = resultSet.getString("Primary Key");
                String columnType = resultSet.getString("Column Type");
                String tableName = resultSet.getString("Table Name");
                String tableComment = resultSet.getString("Table Comment");
                Map<String, String> map = new HashMap<>();
                map.put("Column Name", columnName);
                map.put("Column Comment", columnComment);
                map.put("Primary Key", primaryKey);
                map.put("Column Type", columnType);
                map.put("Table Name", tableName);
                map.put("Table Comment", tableComment);
                result.add(map);
            }
            for (Map<String, String> map : result) {
                String tableName = map.get("Table Name");
                String tableComment = map.get("Table Comment");
                tables.putIfAbsent(tableName, new TableInfo(tableName, tableComment));
            }
            for (Map<String, String> map : result) {
                TableInfo tableInfo = tables.get(map.get("Table Name"));
                List<TableField> fields = tableInfo.getFields();
                String primaryKey = map.get("Primary Key");
                String columnType = map.get("Column Type");
                String columnComment = map.get("Column Comment");
                String columnName = map.get("Column Name");
                fields.add(new TableField(primaryKey, columnName, trimColumnType(columnType), columnComment));
            }
            return tables;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String trimColumnType(String columnType) {
        int i = columnType.indexOf("(");
        if (i != -1) {
            columnType = columnType.substring(0, i);
        }
        return columnType;
    }

    private String getDatabase(String jdbcUrl) {
        String databaseName = null;
        int startIndex = jdbcUrl.indexOf("/", jdbcUrl.indexOf("//") + 2) + 1;
        int endIndex = jdbcUrl.indexOf("?", startIndex);
        if (startIndex > 0) {
            if (endIndex > 0) {
                databaseName = jdbcUrl.substring(startIndex, endIndex);
            } else {
                databaseName = jdbcUrl.substring(startIndex);
            }
        }
        return databaseName;
    }
}
