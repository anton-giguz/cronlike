package cronlike;

import javax.sql.DataSource;
import org.sqlite.javax.SQLiteConnectionPoolDataSource;

/**
 * Connection to database
 *
 */
public class Database {

    private static final SQLiteConnectionPoolDataSource datasource = new SQLiteConnectionPoolDataSource();

    public static void init(String database) {
        datasource.setUrl("jdbc:sqlite:" + database);
    }

    public static DataSource getDataSource() {
        return datasource;
    }

}
