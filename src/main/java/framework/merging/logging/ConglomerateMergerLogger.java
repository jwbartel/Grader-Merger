package framework.merging.logging;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import framework.merging.navigation.GraderResultFolder;

public class ConglomerateMergerLogger {
	// Static singleton boilerplate code

	private static ConglomerateMergerLogger ourInstance = new ConglomerateMergerLogger();

	public static ConglomerateMergerLogger getInstance() {
		return ourInstance;
	}

	// Actual definition

	private List<MergerLogger> loggers;
	private File outputFolder;

	private ConglomerateMergerLogger() {
		loggers = new ArrayList<MergerLogger>();
	}

	public void setOutputFolder(File folder) {
		this.outputFolder = folder;
	}

	public void addLogger(MergerLogger logger) {
		loggers.add(logger);
	}

	public void mergeResults(GraderResultFolder resultFolder) {
		for (MergerLogger logger : loggers) {
			logger.mergeResults(resultFolder, outputFolder);
		}
	}
}
