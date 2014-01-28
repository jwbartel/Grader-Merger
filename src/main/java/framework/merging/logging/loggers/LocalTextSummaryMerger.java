package framework.merging.logging.loggers;

import framework.merging.rules.ResultTypes;
import framework.merging.rules.ResultTypes.ResultType;

public class LocalTextSummaryMerger extends LocalMerger {

	@Override
	protected ResultType getResultType() {
		return ResultTypes.LOCAL_JSON_FEEDBACK;
	}

	@Override
	protected String getLocalFileRegex() {
		return ".+, .+(.+)[.]txt";
	}

}
