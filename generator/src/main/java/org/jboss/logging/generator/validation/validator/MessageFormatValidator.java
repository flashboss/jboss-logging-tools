package org.jboss.logging.generator.validation.validator;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a {@link java.text.MessageFormat} string.
 * <p/>
 * <i><b>**Note:</b> Currently the format type and format style are not validated</i>
 * <p/>
 * Date: 14.06.2011
 *
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 */
class MessageFormatValidator implements FormatValidator {
    public final String PATTERN = "\\{}|\\{.+?}";

    private final Set<FormatPart> formatParts = new TreeSet<FormatPart>();
    private final Set<MessageFormatPart> formats = new TreeSet<MessageFormatPart>();
    private int argumentCount;
    private boolean valid;
    private String summary;
    private String detail;
    private final String format;


    MessageFormatValidator(final String format) {
        this.format = format;
        valid = true;
        detail = "";
        summary = "";
    }

    public static MessageFormatValidator of(final String format) {
        final MessageFormatValidator result = new MessageFormatValidator(format);
        result.init();
        result.validate();
        return result;
    }

    public static MessageFormatValidator of(final String format, final Object... parameters) {
        final MessageFormatValidator result = new MessageFormatValidator(format);
        result.init();
        result.validate();
        result.parameterCheck(parameters);
        return result;
    }

    public static MessageFormatValidator of(final String format, final int parameterCount) {
        final MessageFormatValidator result = new MessageFormatValidator(format);
        result.init();
        result.validate();
        result.parameterCheck(parameterCount);
        return result;
    }

    @Override
    public int argumentCount() {
        return argumentCount;
    }

    @Override
    public boolean isValid() {
        return valid;
    }

    @Override
    public String detailMessage() {
        return detail;
    }

    @Override
    public String summaryMessage() {
        return summary;
    }

    private void validate() {
        // Simple argument parser
        int start = format.indexOf("{");
        int end = format.indexOf("}");
        while (start != -1 && valid) {
            if (end < start) {
                valid = false;
                summary = String.format("Format %s appears to be missing an ending bracket.", format);
                detail = summary;
            }
            start = format.indexOf("{", end);
            if (start > 0) {
                end = format.indexOf("}", start);
            }
        }
    }

    private void parameterCheck(final Object... parameters) {
        if (argumentCount > 0 && parameters == null) {
            valid = false;
            summary = String.format("Invalid parameter count. Required %d provided null for format '%s'.", argumentCount, format);
            detail = String.format("Required %d parameters, but none were provided for format %s.", argumentCount, format);
        } else {
            parameterCheck(parameters.length);
        }

    }

    private void parameterCheck(final int parameterCount) {
        if (argumentCount != parameterCount) {
            valid = false;
            summary = String.format("Invalid parameter count. Required: %d provided %d for format '%s'.", argumentCount, parameterCount, format);
            detail = String.format("Required %d parameters, but %d were provided for format %s.", argumentCount, parameterCount, format);
        }

    }

    private void init() {
        final Pattern pattern = Pattern.compile(PATTERN);
        final Matcher matcher = pattern.matcher(format);
        int position = 0;
        int i = 0;
        while (i < format.length()) {
            if (matcher.find(i)) {
                if (matcher.start() != i) {
                    formatParts.add(StringPart.of(position++, format.substring(i, matcher.start())));
                }
                final MessageFormatPart messageFormatPart = MessageFormatPart.of(position++, matcher.group());
                formatParts.add(messageFormatPart);
                formats.add(messageFormatPart);
                i = matcher.end();
            } else {
                formatParts.add(StringPart.of(position, format.substring(i)));
                break;
            }
        }
        final Set<Integer> counted = new HashSet<Integer>();
        // Initialize the argument count
        for (MessageFormatPart messageFormatPart : formats) {
            if (messageFormatPart.index() >= 0) {
                if (counted.add(messageFormatPart.index()))
                    argumentCount++;
            }
        }
    }

    @Override
    public String toString() {
        return new StringBuilder(getClass().getSimpleName()).append("[")
                .append("formatParts=")
                .append(formatParts)
                .append(", argumentCount=")
                .append(argumentCount)
                .append("]").toString();
    }

}
