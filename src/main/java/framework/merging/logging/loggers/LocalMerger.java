package framework.merging.logging.loggers;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

import mergingTools.utils.MergingEnvironment;

import org.apache.commons.io.FileUtils;

import framework.merging.logging.MergerLogger;
import framework.merging.navigation.GraderResultFolder;
import framework.merging.rules.ResultTypes.ResultType;

public abstract class LocalMerger extends MergerLogger {

	/*
	 * ResultType to be merged
	 */
	protected abstract ResultType getResultType();

	/*
	 * Regular expression that matches the local filenames associated with this
	 * MergerLogger
	 */
	protected abstract String getLocalFileRegex();

	@Override
	public void mergeResults(GraderResultFolder resultFolder, File outputFolder) {

		System.out.println("\tRunning " + this.getClass().getName() + "...");

		Pattern fileNamePattern = Pattern.compile(getLocalFileRegex());

		File[] files = resultFolder.getFolder().listFiles();
		for (File localFile : files) {
			if (!localFile.isDirectory() && fileNamePattern.matcher(localFile.getName()).matches()) {

				File mergedLocalFile = findLocationInMerge(outputFolder, resultFolder, localFile);
				File fileToWrite = (File) MergingEnvironment.get().getMergingRules()
						.getMergedVal(getResultType(), localFile, mergedLocalFile);

				if (fileToWrite != null && !fileToWrite.equals(mergedLocalFile)) {
					try {
						FileUtils.copyFile(fileToWrite, mergedLocalFile, true);
					} catch (IOException e) {
						System.out.println("Error copying file " + fileToWrite);
					}
				}
			}
		}
		System.out.println("\tDone.");

	}
}
