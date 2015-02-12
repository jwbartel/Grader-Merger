package framework.merging.navigation;

import java.io.File;

import mergingTools.utils.MergingEnvironment;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;

public class MergedSakaiGradesFolder implements MergedGradesFolder {

	File folder;
	File uploadableFolder;

	public MergedSakaiGradesFolder(String path) throws NotValidResultFolderException {
		folder = new File(path);
		if (!(folder.exists() && folder.isDirectory())) {
			throw new NotValidResultFolderException("Missing assignment folder: " + path);
		}
		uploadableFolder = new File(folder, MergingEnvironment.get().getAssignmentName());
		if (!uploadableFolder.exists()) {
			uploadableFolder = null;
		}
	}

	public File getUploadableFolder() {
		return uploadableFolder;
	}

	public void zipUploadableFolder() throws ZipException {
		File outFile = new File(getUploadableFolder().getPath() + ".zip");
		if (outFile.exists()) {
			outFile.delete();
		}
		ZipFile zipFile = new ZipFile(outFile);
		zipFile.createZipFileFromFolder(getUploadableFolder(), new ZipParameters(), false, 0);
	}

}
