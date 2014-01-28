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

	public static final ResultType LOCAL_TEXT_FEEDBACK = new ResultType();
	public static final ResultType LOCAL_JSON_FEEDBACK = new ResultType();

	public static class NamedResultType extends ResultType {
		String name;

		public NamedResultType(String name) {
			this.name = name;
		}

		@Override
		public boolean equals(Object o) {
			if (!(o instanceof NamedResultType)) {
				return false;
			}

			return name.equals(((NamedResultType) o).name);
		}

	}
}
