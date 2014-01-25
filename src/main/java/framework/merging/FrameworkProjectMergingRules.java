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
    
	public void setGeneralRule(MergingRule rule) {
		this.generalRule = rule;
	}
	
	public void add(ResultType resultType, MergingRule rule) {
		rules.put(resultType, rule);
	}

}
