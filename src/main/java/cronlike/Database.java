package cronlike;

import javax.sql.DataSource;
import org.sqlite.javax.SQLiteConnectionPoolDataSource;

/**
 * Connection to database
 *
 */
public class Database {

    private static final SQLiteConnectionPoolDataSource datasource = new SQLiteConnectionPoolDataSource();

    static {
        datasource.setUrl("jdbc:sqlite:cronlike.db");
    }

    public static DataSource getDataSource() {
        return datasource;
    }

}
