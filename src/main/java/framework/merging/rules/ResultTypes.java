package framework.merging.rules;

/*
 * A class that contains all types of values that can be merged
 */
public class ResultTypes {

	public static class ResultType implements Comparable<ResultType> {
		private final Integer code;

		public ResultType(int code) {
			this.code = code;
		}

		public int compareTo(ResultType o) {
			return this.code.compareTo(o.code);
		}

		@Override
		public boolean equals(Object o) {
			if (!(o instanceof ResultType)) {
				return false;
			}
			return this.compareTo((ResultType) o) == 0;
		}
	}

	public static final ResultType RAW_SCORE = new ResultType(1);
	public static final ResultType LATE_OR_EARLY_PENALTY = new ResultType(2);
	public static final ResultType FINAL_SCORE = new ResultType(3);

	public static final ResultType TEXT_FEEDBACK = new ResultType(4);
	public static final ResultType JSON_FEEDBACK = new ResultType(5);

	public static final ResultType LOCAL_TEXT_FEEDBACK = new ResultType(6);
	public static final ResultType LOCAL_JSON_FEEDBACK = new ResultType(7);

	public static final ResultType SOURCES_FEEDBACK = new ResultType(8);

	public static class NamedResultType extends ResultType {
		String name;

		public NamedResultType(String name) {
			super(8);
			this.name = name;
		}

		@Override
		public boolean equals(Object o) {
			if (!(o instanceof NamedResultType)) {
				return false;
			}

			return name.equals(((NamedResultType) o).name);
		}

		@Override
		public int compareTo(ResultType o) {
			if (o instanceof NamedResultType) {
				return name.compareTo(((NamedResultType) o).name);
			} else {
				return super.compareTo(o);
			}
		}

	}
}
