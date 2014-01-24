package framework.merging;

import java.util.List;

import org.joda.time.DateTime;

import framework.grading.ProjectRequirements;
import framework.grading.testing.CheckResult;
import framework.grading.testing.Feature;
import framework.grading.testing.Restriction;
import framework.grading.testing.TestCase;
import framework.project.Project;

public class MergingGradesRequirements implements ProjectRequirements {

	public void addDueDate(DateTime arg0, double arg1) {
		// TODO Auto-generated method stub

	}

	public void addDueDate(String arg0, double arg1) {
		// TODO Auto-generated method stub

	}

	public void addFeature(Feature arg0) {
		// TODO Auto-generated method stub

	}

	public void addFeature(String arg0, double arg1, List<TestCase> arg2) {
		// TODO Auto-generated method stub

	}

	public void addFeature(String arg0, double arg1, TestCase... arg2) {
		// TODO Auto-generated method stub

	}

	public void addFeature(String arg0, double arg1, boolean arg2, List<TestCase> arg3) {
		// TODO Auto-generated method stub

	}

	public void addFeature(String arg0, double arg1, boolean arg2, TestCase... arg3) {
		// TODO Auto-generated method stub

	}

	public void addRestriction(Restriction arg0) {
		// TODO Auto-generated method stub

	}

	public void addRestriction(String arg0, double arg1, TestCase... arg2) {
		// TODO Auto-generated method stub

	}

	public void addRestriction(String arg0, double arg1, List<TestCase> arg2) {
		// TODO Auto-generated method stub

	}

	public double checkDueDate(DateTime arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	public List<CheckResult> checkFeatures(Project arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<CheckResult> checkRestrictions(Project arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Feature> getFeatures() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Restriction> getRestrictions() {
		// TODO Auto-generated method stub
		return null;
	}

}
