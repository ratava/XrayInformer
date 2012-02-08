package me.sourcemaker.XrayInformer;

import uk.co.oliwali.HawkEye.callbacks.BaseCallback;
import uk.co.oliwali.HawkEye.database.SearchQuery.SearchError;

public class HESimpleSearch extends BaseCallback{
	public Integer founds;

	@Override
    public void execute() {
        this.founds = results.size();
    }

	@Override
	public void error(SearchError arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

}
