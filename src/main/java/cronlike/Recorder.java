package cronlike;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.db.ColumnMapping;
import org.apache.logging.log4j.core.appender.db.jdbc.ConnectionSource;
import org.apache.logging.log4j.core.appender.db.jdbc.FactoryMethodConnectionSource;
import org.apache.logging.log4j.core.appender.db.jdbc.JdbcAppender;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilderFactory;
import org.apache.logging.log4j.core.config.builder.api.LayoutComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.RootLoggerComponentBuilder;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.apache.logging.log4j.message.StringMapMessage;

/**
 * Logging to various destinations
 *
 */
public class Recorder {

    private static final boolean DEBUG = true;

    private static final ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();
    private static final RootLoggerComponentBuilder rootLoggerBuilder = builder.newRootLogger(Level.INFO);
    private static final LayoutComponentBuilder patternLayoutBuilder = builder.newLayout("PatternLayout")
        .addAttribute("pattern", "%date{yyyy-MM-dd HH:mm:ss} [%map{command}] %map{result} %map{details}%n");
    private static final Logger logger;

    static {
        if (DEBUG) {
            addConsoleAppender();
        }

        addFileAppender();

        builder.add(rootLoggerBuilder);
        logger = Configurator.initialize(builder.build()).getRootLogger();

        setupJdbcAppender();
    }

    private static void addConsoleAppender() {
        AppenderComponentBuilder consoleAppenderBuilder = builder.newAppender("Console", "CONSOLE");
        consoleAppenderBuilder.add(patternLayoutBuilder);
        builder.add(consoleAppenderBuilder);
        rootLoggerBuilder.add(builder.newAppenderRef("Console"));
    }

    private static void addFileAppender() {
        AppenderComponentBuilder fileAppenderBuilder = builder.newAppender("File", "FILE").addAttribute("fileName", "cronlike.log");
        fileAppenderBuilder.add(patternLayoutBuilder);
        builder.add(fileAppenderBuilder);
        rootLoggerBuilder.add(builder.newAppenderRef("File"));
    }

    private static void setupJdbcAppender() {
        ConnectionSource source = FactoryMethodConnectionSource.createConnectionSource("cronlike.Database", "getDataSource");
        JdbcAppender jdbcAppender = JdbcAppender.newBuilder()
            .setName("Jdbc")
            .setConnectionSource(source)
            .setTableName("log")
            .setColumnMappings(
                column("date",    "%date{yyyy-MM-dd HH:mm:ss}"),
                column("command", "%map{command}"),
                column("result",  "%map{result}"),
                column("details", "%map{details}")
            ).build();
        jdbcAppender.start();
        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        context.getRootLogger().addAppender(jdbcAppender);
        context.updateLoggers();
    }

    private static ColumnMapping column(String name, String pattern) {
        return ColumnMapping.newBuilder().setName(name).setPattern(pattern).build();
    }

    public static void init() {
        System.out.println("[INFO] Recorder initialized");
    }

    public static void info(String command, String result) {
        StringMapMessage msg = new StringMapMessage();
        msg.put("command", command);
        msg.put("result", result);
        logger.info(msg);
    }

    public static void error(String command, String result, String details) {
        StringMapMessage msg = new StringMapMessage();
        msg.put("command", command);
        msg.put("result", result);
        msg.put("details", details);
        logger.error(msg);
    }

}
