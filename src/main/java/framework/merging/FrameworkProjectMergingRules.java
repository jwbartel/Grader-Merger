package framework.merging;

import java.util.HashMap;
import java.util.Map;

import framework.merging.rules.MergingRule;
import framework.merging.rules.ResultTypes.ResultType;

public class FrameworkProjectMergingRules implements ProjectMergingRules {

	private MergingRule generalRule = new DefaultMergingRule();
	private Map<ResultType, MergingRule> rules;

	public FrameworkProjectMergingRules() {
		rules = new HashMap<ResultType, MergingRule>();
	}

	@Override
	public void setGeneralRule(MergingRule rule) {
		this.generalRule = rule;
	}

	@Override
	public void add(ResultType resultType, MergingRule rule) {
		rules.put(resultType, rule);
	}

	public Object getMergedVal(ResultType resultType, Object val1, Object val2) {
		MergingRule rule = rules.get(resultType);

		if (rule != null) {
			return rule.mergeResults(val1, val2);
		} else {
			return generalRule.mergeResults(val1, val2);
		}
	}

}
