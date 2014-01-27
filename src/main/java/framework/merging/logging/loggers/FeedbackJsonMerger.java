package framework.merging.logging.loggers;

import java.io.File;
import java.io.IOException;

import mergingTools.utils.MergingEnvironment;

import org.apache.commons.io.FileUtils;

import framework.merging.logging.MergerLogger;
import framework.merging.navigation.GraderResultFolder;
import framework.merging.rules.ResultTypes;
import framework.navigation.BulkDownloadFolder;
import framework.navigation.StudentFolder;

public class FeedbackJsonMerger extends MergerLogger {

	@Override
	public void mergeResults(GraderResultFolder resultFolder, File mergeFolder) {
		System.out.println("\tMerging JSONs...");

		BulkDownloadFolder bulkDownloadFolder = resultFolder.getBulkDownloadFolder();

		for (StudentFolder studentFolder : bulkDownloadFolder.getStudentFolders()) {
			File feedbackFolder = studentFolder.getFeedbackFolder();
			File jsonFeedbackFile = new File(feedbackFolder, "results.json");
			File mergedJsonFeedbackFile = findLocationInMerge(mergeFolder, resultFolder,
					jsonFeedbackFile);

			// Determine which of the two folders to write into the merged
			// folder
			File toMergeIn = (File) MergingEnvironment
					.get()
					.getMergingRules()
					.getMergedVal(ResultTypes.JSON_FEEDBACK, jsonFeedbackFile,
							mergedJsonFeedbackFile);

			// If we need to overwrite the merged value, do so
			if (toMergeIn != null && !toMergeIn.equals(mergedJsonFeedbackFile)) {
				try {

					if (!mergedJsonFeedbackFile.exists()
							&& !mergedJsonFeedbackFile.equals(toMergeIn)) {
						FileUtils.copyFile(jsonFeedbackFile, mergedJsonFeedbackFile, true);
					}
				} catch (IOException e) {
					System.out.println("Error merging in " + jsonFeedbackFile);
					System.out.println(e);
				}
			}

		}
		System.out.println("\tDone.");
	}

}
