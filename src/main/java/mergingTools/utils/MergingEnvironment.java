package mergingTools.utils;

import java.io.File;

import framework.merging.ProjectMergingRules;
import framework.merging.navigation.MergeableGraderResultFolders;
import framework.merging.navigation.MergeableSakaiGraderResultFolders;

public class MergingEnvironment {

	private String assignmentName;
	ProjectMergingRules mergingRules;
	private MergeableGraderResultFolders mergeableFolders;
	private File outputFolder;

	private MergingEnvironment() {
	}

	public String getAssignmentName() {
		return assignmentName;
	}

	public void setAssignmentName(String assignmentName) {
		this.assignmentName = assignmentName;
	}

	public ProjectMergingRules getMergingRules() {
		return mergingRules;
	}

	public void setMergingRules(ProjectMergingRules mergingRules) {
		this.mergingRules = mergingRules;
	}

	public MergeableGraderResultFolders getMergeableFolders() {
		return mergeableFolders;
	}

	public void setMergeableFolders(String path) {
		this.mergeableFolders = new MergeableSakaiGraderResultFolders(path);
	}

	public File getOutputFolder() {
		return outputFolder;
	}

	public void setOutputFolder(String path) {
		this.outputFolder = new File(path);
	}

	// Singleton methods
	private static MergingEnvironment singleton = null;

	public static MergingEnvironment get() {
		if (singleton == null) {
			singleton = new MergingEnvironment();
		}
		return singleton;
	}
}
