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

public class SourcesMerger extends MergerLogger {

	@Override
	public void mergeResults(GraderResultFolder resultFolder, File mergeFolder) {
		System.out.println("\tMerging Source comments...");

		BulkDownloadFolder bulkDownloadFolder = resultFolder.getBulkDownloadFolder();

		for (StudentFolder studentFolder : bulkDownloadFolder.getStudentFolders()) {
			File feedbackFolder = studentFolder.getFeedbackFolder();
			File sourcesFile = new File(feedbackFolder, "sources.txt");
			File mergedSourcesFile = findLocationInMerge(mergeFolder, resultFolder, sourcesFile);

			// Determine which of the two folders to write into the merged
			// folder
			File toWrite = (File) MergingEnvironment.get().getMergingRules()
					.getMergedVal(ResultTypes.SOURCES_FEEDBACK, sourcesFile, mergedSourcesFile);

			// If we need to overwrite the merged value, do so
			if (toWrite != null && !toWrite.equals(mergedSourcesFile)) {
				try {

					if (!mergedSourcesFile.exists() && !mergedSourcesFile.equals(toWrite)) {
						FileUtils.copyFile(sourcesFile, mergedSourcesFile, true);
					}
				} catch (IOException e) {
					System.out.println("Error merging in " + sourcesFile);
					System.out.println(e);
				}
			}

		}
		System.out.println("\tDone.");

	}

}
