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

public class FeedbackTextSummaryMerger extends MergerLogger {

	@Override
	public void mergeResults(GraderResultFolder resultFolder, File mergeFolder) {
		System.out.println("\tMerging Text Feedback...");

		BulkDownloadFolder bulkDownloadFolder = resultFolder.getBulkDownloadFolder();

		for (StudentFolder studentFolder : bulkDownloadFolder.getStudentFolders()) {
			File feedbackFolder = studentFolder.getFeedbackFolder();
			File textFeedbackFile = new File(feedbackFolder, "feedback.txt");
			File mergedTextFeedbackFile = findLocationInMerge(mergeFolder, resultFolder,
					textFeedbackFile);
			mergedTextFeedbackFile = new File(mergedTextFeedbackFile.getParent(),
					"resubmission-feedback.txt");

			// Determine which of the two folders to write into the merged
			// folder
			File toWrite = (File) MergingEnvironment
					.get()
					.getMergingRules()
					.getMergedVal(ResultTypes.TEXT_FEEDBACK, textFeedbackFile,
							mergedTextFeedbackFile);

			// If we need to overwrite the merged value, do so
			if (toWrite != null && !toWrite.equals(mergedTextFeedbackFile)) {
				try {

					if (!mergedTextFeedbackFile.exists() && !mergedTextFeedbackFile.equals(toWrite)) {
						FileUtils.copyFile(textFeedbackFile, mergedTextFeedbackFile, true);
					}
				} catch (IOException e) {
					System.out.println("Error merging in " + textFeedbackFile);
					System.out.println(e);
				}
			}

		}
		System.out.println("\tDone.");

	}

}
