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

	@Override
	public List<GraderResultFolder> getGraderResultFolders() throws NotValidResultFolder,
			NotValidDownloadFolderException {
		File[] subfolders = folder.listFiles();

		List<GraderResultFolder> resultFolders = new ArrayList<GraderResultFolder>();
		for (File subfolder : subfolders) {
			resultFolders.add(new SakaiGraderResultFolder(subfolder.getAbsolutePath()));
		}

		return resultFolders;
	}

}
