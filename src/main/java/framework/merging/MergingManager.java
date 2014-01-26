package framework.merging;

import mergingTools.utils.MergingEnvironment;
import framework.merging.logging.ConglomerateMergerLogger;
import framework.merging.navigation.GraderResultFolder;
import framework.merging.navigation.MergeableGraderResultFolders;
import framework.merging.navigation.NotValidResultFolderException;
import framework.navigation.NotValidDownloadFolderException;

public class MergingManager {

	private final String projectName;

	public MergingManager(String projectName) {
		this.projectName = projectName;
	}

	public void run() {
		MergeableGraderResultFolders resultFolders = MergingEnvironment.get().getMergeableFolders();

		try {
			for (GraderResultFolder resultFolder : resultFolders.getGraderResultFolders()) {
				ConglomerateMergerLogger.getInstance().mergeResults(resultFolder);
			}
		} catch (NotValidResultFolderException e) {
			System.out.println("Result folder not valid: " + e.getMessage());
		} catch (NotValidDownloadFolderException e) {
			System.out.println("No valid bulk download folder");
		}
	}
}
