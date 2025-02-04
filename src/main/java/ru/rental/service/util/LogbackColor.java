package ru.rental.service.util;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.color.ANSIConstants;
import ch.qos.logback.core.pattern.color.ForegroundCompositeConverterBase;

public class LogbackColor extends ForegroundCompositeConverterBase<ILoggingEvent> {

    @Override
    protected String getForegroundColorCode(ILoggingEvent iLoggingEvent) {
        if (iLoggingEvent.getLevel() == Level.INFO) {
            return ANSIConstants.GREEN_FG;
        } else if (iLoggingEvent.getLevel() == Level.WARN) {
            return ANSIConstants.YELLOW_FG;
        } else if (iLoggingEvent.getLevel() == Level.ERROR) {
            return ANSIConstants.RED_FG;
        } else {
            return ANSIConstants.WHITE_FG;
        }
    }
}
