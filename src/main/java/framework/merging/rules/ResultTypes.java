package framework.merging.rules;

/*
 * A class that contains all types of values that can be merged
 */
public class ResultTypes {

	public static class ResultType {
	}

	public static final ResultType RAW_SCORE = new ResultType();
	public static final ResultType LATE_OR_EARLY_PENALTY = new ResultType();
	public static final ResultType FINAL_SCORE = new ResultType();

	public static final ResultType TEXT_FEEDBACK = new ResultType();
	public static final ResultType JSON_FEEDBACK = new ResultType();

	public static class FeatureResultType extends ResultType {
		String featureName;

		public FeatureResultType(String name) {
			this.featureName = name;
		}

		@Override
		public boolean equals(Object o) {
			if (!(o instanceof FeatureResultType)) {
				return false;
			}

			return featureName.equals(((FeatureResultType) o).featureName);
		}

	}

	public static class RestrictionResultType extends ResultType {
		String restrictionName;

		public RestrictionResultType(String name) {
			this.restrictionName = name;
		}

		@Override
		public boolean equals(Object o) {
			if (!(o instanceof RestrictionResultType)) {
				return false;
			}

			return restrictionName.equals(((RestrictionResultType) o).restrictionName);
		}

	}
}
