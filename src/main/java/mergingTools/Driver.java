package mergingTools;

import mergingTools.utils.MergingEnvironment;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import framework.logging.recorder.ConglomerateRecorder;
import framework.merging.MergingGradesRequirements;

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

			// Get the project name
			String projectName = configuration.getString("project.name");
			MergingEnvironment.get().setAssignmentName(projectName);

			// Logging
			ConglomerateRecorder recorder = ConglomerateRecorder.getInstance();
			recorder.setProjectRequirements(new MergingGradesRequirements());

			String[] loggingMethods = configuration.getString("grader.logger", "csv").split(
					"\\s*\\+\\s*");
			for (String method : loggingMethods) {

//                // Add loggers
//                if (method.equals("local") || method.equals("local-txt"))
//                    recorder.addLogger(new LocalTextSummaryLogger());
//                if (method.equals("local") || method.equals("local-json"))
//                    recorder.addLogger(new LocalJsonLogger());
//                if (method.equals("feedback") || method.equals("feedback-txt"))
//                    recorder.addLogger(new FeedbackTextSummaryLogger());
//                if (method.equals("feedback") || method.equals("feedback-json"))
//                    recorder.addLogger(new FeedbackJsonLogger());
//                if (method.equals("spreadsheet"))
//                    recorder.addLogger(new SpreadsheetLogger(requirements));
//                if (method.equals("csv"))
//                    recorder.addLogger(new CsvLogger());
			}

//            // Run the grading process
//            String controller = configuration.getString("grader.controller", "GradingManager");
//            if (controller.equals("GradingManager")) {
//
//                // Run the GraderManager
//                GradingManager manager = new GradingManager(projectName, requirements);
//                manager.run();
//
//            } else if (controller.equals("SakaiProjectDatabase")) {
//
//                // Start the grading process by, first, getting the settings the running the project database
//                SettingsWindow settingsWindow = SettingsWindow.create();
//                settingsWindow.awaitBegin();
//
//                // Logging/results saving
//                FeatureGradeRecorderSelector.setFactory(new ConglomerateRecorderFactory());
//
//                // Create the database
//                ProjectDatabaseWrapper database = new ProjectDatabaseWrapper();
//                database.addProjectRequirements(requirements);
//
//                // Possibly set the stepper displayer
//                boolean useFrameworkGUI = configuration.getBoolean("grader.controller.useFrameworkGUI", false);
//                if (useFrameworkGUI)
//                    database.setProjectStepperDisplayer(new ProjectStepperDisplayerWrapper());
//
//                // Feedback
////                database.setAutoFeedback(ConglomerateRecorder.getInstance());
//                database.setManualFeedback(ConglomerateRecorder.getInstance());
//
//                database.nonBlockingRunProjectsInteractively();
//            }

		} catch (ConfigurationException e) {
			System.err.println("Error loading config file.");
			System.err.println(e.getMessage());
//		} catch (ClassNotFoundException e) {
//			System.err.println("Could not find project requirements.");
//			System.err.println(e.getMessage());
//		} catch (InstantiationException e) {
//			System.err.println("Could not create project requirements.");
//			System.err.println(e.getMessage());
//		} catch (IllegalAccessException e) {
//			System.err.println("Could not create project requirements.");
//			System.err.println(e.getMessage());
		}
	}
}
