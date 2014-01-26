package framework.merging;

import java.io.File;

import mergingTools.utils.MergingEnvironment;
import framework.merging.navigation.GraderResultFolder;
import framework.merging.navigation.MergeableGraderResultFolders;
import framework.merging.navigation.NotValidResultFolder;
import framework.navigation.NotValidDownloadFolderException;

public class MergingManager {

	private final String projectName;
	
	public MergingManager(String projectName) {
		this.projectName = projectName;
	}
	
	public void run() {
		File outputFolder = MergingEnvironment.get().getOutputFolder();
		MergeableGraderResultFolders resultFolders = MergingEnvironment.get().getMergeableFolders();
		
		try {
			for (GraderResultFolder resultFolder : resultFolders.getGraderResultFolders()) {
				// TODO: merge results with output folder
			}
		} catch (NotValidResultFolder e) {
			System.out.println("Result folder not valid: "+e.getMessage());
		} catch (NotValidDownloadFolderException e) {
			System.out.println("No valid bulk download folder");
		}
	}
}
