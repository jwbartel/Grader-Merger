package mergingTools;

import mergingTools.utils.CsvMerger;
import mergingTools.utils.FeedbackJsonMerger;
import mergingTools.utils.FeedbackTextSummaryMerger;
import mergingTools.utils.LocalJsonMerger;
import mergingTools.utils.MergingEnvironment;
import mergingTools.utils.SpreadsheetMerger;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import framework.merging.MergingManager;
import framework.merging.ProjectMergingRules;
import framework.merging.logging.ConglomerateMergerLogger;
import framework.merging.logging.loggers.LocalTextSummaryMerger;

/**
 * This is the entry class for the grading tools that Maven will reference. Use
 * config.properties to configure what gets run.
 */
public class Driver {

	public static void main(String[] args) {

		try {
			// Load the config file
			PropertiesConfiguration configuration = new PropertiesConfiguration(
					"./config/config.properties");

			String mergingInput = configuration.getString("merger.input");
			String mergingOutput = configuration.getString("merger.output");

			// Get the project merging rules
			Class<?> _class = Class.forName(configuration.getString("project.merging_rules"));
			ProjectMergingRules projectRules = (ProjectMergingRules) _class.newInstance();

			// Get the project name
			String projectName = configuration.getString("project.name");
			MergingEnvironment.get().setAssignmentName(projectName);
			MergingEnvironment.get().setMergingRules(projectRules);
			MergingEnvironment.get().setMergeableFolders(mergingInput);
			MergingEnvironment.get().setOutputFolder(mergingOutput);

			// Logging
			ConglomerateMergerLogger merger = ConglomerateMergerLogger.getInstance();
			merger.setOutputFolder(MergingEnvironment.get().getOutputFolder());

			String[] loggingMethods = configuration.getString("grader.logger", "csv").split(
					"\\s*\\+\\s*");
			for (String method : loggingMethods) {

				// Add loggers
				if (method.equals("local") || method.equals("local-txt")) {
					merger.addLogger(new LocalTextSummaryMerger());
				}
				if (method.equals("local") || method.equals("local-json")) {
					merger.addLogger(new LocalJsonMerger());
				}
				if (method.equals("feedback") || method.equals("feedback-txt")) {
					merger.addLogger(new FeedbackTextSummaryMerger());
				}
				if (method.equals("feedback") || method.equals("feedback-json")) {
					merger.addLogger(new FeedbackJsonMerger());
				}
				if (method.equals("spreadsheet")) {
					merger.addLogger(new SpreadsheetMerger());
				}
				if (method.equals("csv")) {
					merger.addLogger(new CsvMerger());
				}
			}

			MergingManager manager = new MergingManager(projectName);
			manager.run();

		} catch (ConfigurationException e) {
			System.err.println("Error loading config file.");
			System.err.println(e.getMessage());
		} catch (ClassNotFoundException e) {
			System.err.println("Could not find project merging rules.");
			System.err.println(e.getMessage());
		} catch (InstantiationException e) {
			System.err.println("Could not create project merging rules.");
			System.err.println(e.getMessage());
		} catch (IllegalAccessException e) {
			System.err.println("Could not create project merging rules.");
			System.err.println(e.getMessage());
		}
	}
}
