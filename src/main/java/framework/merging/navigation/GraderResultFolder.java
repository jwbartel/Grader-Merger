package framework.merging.navigation;

import java.io.File;

import framework.navigation.BulkDownloadFolder;

public interface GraderResultFolder {

	public File getFolder();
	
	public File getSpreadsheetFolder();
	
	public BulkDownloadFolder getBulkDownloadFolder();
	
}
