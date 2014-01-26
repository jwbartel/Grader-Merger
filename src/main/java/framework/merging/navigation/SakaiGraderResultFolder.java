package framework.merging.navigation;

import java.io.File;
import java.io.FileFilter;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import scala.Option;
import tools.DirectoryUtils;

import mergingTools.utils.MergingEnvironment;

import framework.navigation.BulkDownloadFolder;
import framework.navigation.NotValidDownloadFolderException;
import framework.navigation.SakaiBulkDownloadFolder;
import framework.project.StandardProject;

public class SakaiGraderResultFolder implements GraderResultFolder {

	private final File folder;
	private final File spreadsheetFile;
	private final BulkDownloadFolder bulkDownloadFolder;
	
	public SakaiGraderResultFolder(String path) throws NotValidResultFolder, NotValidDownloadFolderException {
		folder = new File(path);
		spreadsheetFile = new File(folder,"grades.xlsx");
		if (!spreadsheetFile.exists()) {
			throw new NotValidResultFolder("Missing grades.xlsx");
		}
		Option<SakaiBulkDownloadFolder> bulkDownloadOption = findBulkDownloadFolder();
		if (bulkDownloadOption.isEmpty()) {
			throw new NotValidResultFolder("Missing a bulk download folder");
		}
		bulkDownloadFolder = bulkDownloadOption.get();
	}
	
	private Option<SakaiBulkDownloadFolder> findBulkDownloadFolder() throws NotValidDownloadFolderException {
		File[] foundFiles = folder.listFiles(new FileFilter() {
			
			public boolean accept(File path) {
				return path.isDirectory();
			}
		});
		if (foundFiles.length > 0) {
			return Option.apply(new SakaiBulkDownloadFolder(foundFiles[0].getAbsolutePath()));
		}
		
		Option<File> zipFile = DirectoryUtils.find(folder, new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(".zip");
            }
        });
        if (zipFile.isEmpty())
            return Option.empty();

        // Extract the zip to look for the folder
        try {
            ZipFile zip = new ZipFile(zipFile.get());
            zip.extractAll(folder.getAbsolutePath());

            // Look for a folder, taking the first one found.
            Option<File> resultsFolder = DirectoryUtils.find(folder, new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return pathname.isDirectory();
                }
            });
            if (resultsFolder.isDefined())
                try {
                    return Option.apply(new SakaiBulkDownloadFolder(resultsFolder.get().getAbsolutePath()));
                } catch (Exception e) {
                    return Option.empty();
                }
            return Option.empty();
        } catch (ZipException e) {
            return Option.empty();
        }
	}
	
	@Override
	public File getFolder() {
		return folder;
	}

	@Override
	public File getSpreadsheetFolder() {
		return spreadsheetFile;
	}

	@Override
	public BulkDownloadFolder getBulkDownloadFolder() {
		return bulkDownloadFolder;
	}

}
