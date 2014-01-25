package framework.merging;

import framework.merging.rules.MergingRule;
import framework.merging.rules.ResultTypes.ResultType;

public interface ProjectMergingRules {

	public void setGeneralRule(MergingRule rule);
	
	public void add(ResultType resultType, MergingRule rule);
}
