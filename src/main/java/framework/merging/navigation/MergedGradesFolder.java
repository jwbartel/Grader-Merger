package framework.merging.navigation;

import java.io.File;

import net.lingala.zip4j.exception.ZipException;

public interface MergedGradesFolder {

	public File getUploadableFolder();

	public void zipUploadableFolder() throws ZipException;

}
