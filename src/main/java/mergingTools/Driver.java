package mergingTools;

import mergingTools.utils.MergingEnvironment;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import framework.merging.MergingManager;
import framework.merging.ProjectMergingRules;
import framework.merging.logging.ConglomerateMergerLogger;
import framework.merging.logging.loggers.CsvMerger;
import framework.merging.logging.loggers.FeedbackJsonMerger;
import framework.merging.logging.loggers.FeedbackTextSummaryMerger;
import framework.merging.logging.loggers.LocalJsonMerger;
import framework.merging.logging.loggers.LocalTextSummaryMerger;
import framework.merging.logging.loggers.SourcesMerger;
import framework.merging.logging.loggers.SpreadsheetMerger;

/**
 * This is the entry class for the grading tools that Maven will reference. Use
 * config.properties to configure what gets run.
 */
public class Driver {

	public static void main(String[] args) {

		try {
			System.out.println("Intializing...");

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
			mergingInput = mergingInput.replace("{projectName}", projectName);
			mergingOutput = mergingOutput.replace("{projectName}", projectName);

			// Setup the environment
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
				if (method.equals("source")) {
					merger.addLogger(new SourcesMerger());
				}
			}
			System.out.println("done.");

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
