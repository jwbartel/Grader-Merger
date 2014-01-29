package framework.merging;

import mergingTools.utils.MergingEnvironment;
import net.lingala.zip4j.exception.ZipException;
import framework.merging.logging.ConglomerateMergerLogger;
import framework.merging.navigation.GraderResultFolder;
import framework.merging.navigation.MergeableGraderResultFolders;
import framework.merging.navigation.MergedGradesFolder;
import framework.merging.navigation.MergedSakaiGradesFolder;
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
				System.out.println("Merging results from " + resultFolder.getFolder());
				ConglomerateMergerLogger.getInstance().mergeResults(resultFolder);
			}

			MergedGradesFolder mergedGrades = new MergedSakaiGradesFolder(MergingEnvironment.get()
					.getOutputFolder().getPath());
			if (mergedGrades.getUploadableFolder() != null) {
				System.out.println("Zipping up merged results for upload");
				mergedGrades.zipUploadableFolder();
				System.out.println("Done.");
			}
		} catch (NotValidResultFolderException e) {
			System.out.println("Result folder not valid: " + e.getMessage());
		} catch (NotValidDownloadFolderException e) {
			System.out.println("No valid bulk download folder");
		} catch (ZipException e) {
			System.out.println("Error zipping merged grades.");
			System.out.println(e);
		}
	}
}
