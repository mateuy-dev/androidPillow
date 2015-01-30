package cat.my.android.restvolley.forms.views;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import cat.my.android.restvolley.IdentificableModel;
import cat.my.android.restvolley.RestVolley;
import cat.my.android.restvolley.Listeners.Listener;
import cat.my.android.restvolley.sync.CommonListeners;
import cat.my.android.restvolley.sync.ISynchDataSource;
import cat.my.android.restvolley.utils.BundleUtils;
import cat.my.lib.restvolley.R;
import cat.my.util.StringUtil;

public class ShowModelActivity <T extends IdentificableModel>  extends ActionBarActivity  {
	FormFragment<T> formFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.form_activity);
		if (savedInstanceState == null) {
			formFragment = new FormFragment<T>();
			Bundle bundle = BundleUtils.copyBundle(getIntent().getExtras());
			bundle.putBoolean(FormFragment.EDIT_MODE_PARAM, false);
			formFragment.setArguments(bundle);
			getSupportFragmentManager().beginTransaction().add(R.id.container, formFragment).commit();
		}
	}
}