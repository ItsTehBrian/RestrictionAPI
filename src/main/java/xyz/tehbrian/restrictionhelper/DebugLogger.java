package xyz.tehbrian.restrictionhelper;

import java.util.Objects;
import java.util.logging.Logger;

/**
 * A small class to make debug logging easier. If {@link DebugLogger#debug} is
 * set to true, messages logged using {@link DebugLogger#log(String)} will be logged
 * via {@link DebugLogger#logger}, else the message will just be ignored.
 */
public class DebugLogger {

    /**
     * The Logger to be used when debugging is enabled.
     */
    private final Logger logger;
    /**
     * Whether attempted debug logs will be logged via {@link DebugLogger#logger}.
     */
    private boolean debug = false;
    /**
     * The prefix to be prepended to the debug message.
     */
    private String prefix;

    /**
     * Creates a DebugLogger using {@code logger} as the internal Logger.
     *
     * @param logger the Logger to be used when debugging is enabled.
     */
    public DebugLogger(final Logger logger) {
        Objects.requireNonNull(logger, "logger cannot be null");

        this.logger = logger;
    }

    /**
     * Get whether attempted debug logs will be logged via {@link DebugLogger#logger}.
     *
     * @return whether debug is enabled or not
     */
    public boolean isDebug() {
        return this.debug;
    }

    /**
     * Set whether attempted debug logs will be logged via {@link DebugLogger#logger}.
     *
     * @param debug whether debug should be enabled or not
     */
    public void setDebug(final boolean debug) {
        this.debug = debug;
    }

    /**
     * Get the prefix to be prepended to the debug message.
     *
     * @return the prefix
     */
    public String getPrefix() {
        return this.prefix;
    }

    /**
     * Set the prefix to be prepended to the debug message.
     *
     * @param prefix the prefix
     */
    public void setPrefix(final String prefix) {
        this.prefix = prefix;
    }

    /**
     * Logs a message using {@link DebugLogger#logger} if {@link DebugLogger#debug} is true.
     *
     * @param message the message to log
     */
    public void log(final String message) {
        if (!this.debug) {
            return;
        }

        this.logger.info(this.prefix + message);
    }

    /**
     * Convenience method which applies {@link String#format(String, Object...)}
     * onto a message before passing it to {@link DebugLogger#log(String)}.
     *
     * @param message the message to log
     * @param formats formats to apply to the message
     */
    public void log(final String message, final Object... formats) {
        this.log(String.format(message, formats));
    }
}
