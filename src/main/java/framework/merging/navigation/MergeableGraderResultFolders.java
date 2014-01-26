package framework.merging.navigation;

import java.util.List;

import framework.navigation.NotValidDownloadFolderException;

public interface MergeableGraderResultFolders {

	public List<GraderResultFolder> getGraderResultFolders() throws NotValidResultFolder,
			NotValidDownloadFolderException;

}
