package framework.merging;

import java.io.File;

import framework.merging.rules.MergingRule;

public class DefaultMergingRule implements MergingRule {

	@Override
	public Object mergeResults(Object version1, Object version2) {
		if (version1 == null) {
			return version2;
		}
		if (version2 == null) {
			return version1;
		}

		if ((version1 instanceof File) && (version2 instanceof File)) {
			return mergeFileResults((File) version1, (File) version2);
		} else if ((version1 instanceof Double) && (version2 instanceof Double)) {
			return mergeDoubleResults((Double) version1, (Double) version2);
		}

		return null;
	}

	public File mergeFileResults(File version1, File version2) {
		if (!version1.exists() && !version2.exists()) {
			return null;
		}
		if (!version1.exists()) {
			return version2;
		}
		if (!version2.exists()) {
			return version1;
		}

		long modified1 = version1.lastModified();
		long modified2 = version2.lastModified();
		if (modified2 > modified1) {
			return version2;
		} else {
			return version1;
		}
	}

	public Double mergeDoubleResults(Double version1, Double version2) {

		if (version2 > version1) {
			return version2;
		} else {
			return version1;
		}
	}

}
