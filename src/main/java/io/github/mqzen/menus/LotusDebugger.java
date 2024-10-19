package io.github.mqzen.menus;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class LotusDebugger {
    
    static LotusDebugger EMPTY = new LotusDebugger(null);

    private final Logger logger;
    
    LotusDebugger(Logger logger) {
        this.logger = logger;
    }
    
    public boolean isEmpty() {
        return this == EMPTY || logger == null;
    }
    
    public void debug(String msg, Object... args) {
        if(logger == null) return;
        logger.info(String.format(msg, args));
    }
    
    public void warn(String msg, Object... args) {
        if(logger == null) return;
        logger.warning(String.format(msg, args));
    }
    public void error(String msg, Throwable ex){
        if(logger == null) return;
        logger.log(Level.SEVERE, msg, ex);
    }
    
}
