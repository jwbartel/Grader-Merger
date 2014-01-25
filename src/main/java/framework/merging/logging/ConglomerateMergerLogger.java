package framework.merging.logging;

import java.util.ArrayList;
import java.util.List;


public class ConglomerateMergerLogger {
	// Static singleton boilerplate code

    private static ConglomerateMergerLogger ourInstance = new ConglomerateMergerLogger();

    public static ConglomerateMergerLogger getInstance() {
        return ourInstance;
    }

    // Actual definition

    private List<MergerLogger> loggers;
    
    private ConglomerateMergerLogger() {
        loggers = new ArrayList<MergerLogger>();
    }

    public void addLogger(MergerLogger logger) {
        loggers.add(logger);
    }
}
