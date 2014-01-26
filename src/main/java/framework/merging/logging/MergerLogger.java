package framework.merging.logging;

import java.io.File;

import framework.merging.navigation.GraderResultFolder;

public abstract class MergerLogger {

	public static File findLocationInOutput(File outputFolder, GraderResultFolder inFolder,
			File input) {

		File inputPath = inFolder.getFolder();
		String subpath = input.getPath().substring(inputPath.getPath().length());

		return new File(outputFolder, subpath);
	}

	public abstract void mergeResults(GraderResultFolder resultFolder, File outputFolder);
}
