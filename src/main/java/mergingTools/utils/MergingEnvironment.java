package mergingTools.utils;

public class MergingEnvironment {

	private String assignmentName;

	private MergingEnvironment() {
	}

	public String getAssignmentName() {
		return assignmentName;
	}

	public void setAssignmentName(String assignmentName) {
		this.assignmentName = assignmentName;
	}

	// Singleton methods
	private static MergingEnvironment singleton = null;

	public static MergingEnvironment get() {
		if (singleton == null)
			singleton = new MergingEnvironment();
		return singleton;
	}
}
