package framework.merging.navigation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import framework.navigation.NotValidDownloadFolderException;

public class MergeableSakaiGraderResultFolders implements MergeableGraderResultFolders {

	private final File folder;

	public MergeableSakaiGraderResultFolders(String path) {
		folder = new File(path);
	}

	public List<GraderResultFolder> getGraderResultFolders() throws NotValidResultFolderException,
			NotValidDownloadFolderException {
		File[] subfolders = folder.listFiles();

		List<GraderResultFolder> resultFolders = new ArrayList<GraderResultFolder>();
		for (File subfolder : subfolders) {
			System.out.println("Loading results from " + subfolder.getName());
			try {
				resultFolders.add(new SakaiGraderResultFolder(subfolder.getAbsolutePath()));
			} catch (NotValidResultFolderException e) {
				if (e.getMessage().contains("Missing assignment folder:")) {
					System.err.println(e.getMessage());
				} else {
					throw e;
				}
			}
		}

		return resultFolders;
	}

}
