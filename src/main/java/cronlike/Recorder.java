package cronlike;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
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

    private static final Logger logger;

    static {
        ConfigurationBuilder<BuiltConfiguration> builder = ConfigurationBuilderFactory.newConfigurationBuilder();
        RootLoggerComponentBuilder rootLoggerBuilder = builder.newRootLogger(Level.INFO);

        LayoutComponentBuilder patternLayoutBuilder = builder.newLayout("PatternLayout")
            .addAttribute("pattern", "%date{yyyy-MM-dd HH:mm:ss} [%map{command}] %map{result} %map{details}%n");

        if (DEBUG) {
            AppenderComponentBuilder consoleAppenderBuilder = builder.newAppender("Console", "CONSOLE");
            consoleAppenderBuilder.add(patternLayoutBuilder);
            builder.add(consoleAppenderBuilder);
            rootLoggerBuilder.add(builder.newAppenderRef("Console"));
        }

        AppenderComponentBuilder fileAppenderBuilder = builder.newAppender("File", "FILE").addAttribute("fileName", "cronlike.log");
        fileAppenderBuilder.add(patternLayoutBuilder);
        builder.add(fileAppenderBuilder);
        rootLoggerBuilder.add(builder.newAppenderRef("File"));

        builder.add(rootLoggerBuilder);

        logger = Configurator.initialize(builder.build()).getRootLogger();
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
