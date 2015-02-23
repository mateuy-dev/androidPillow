package cat.my.android.pillow.view.forms.views;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import cat.my.android.pillow.IdentificableModel;
import cat.my.android.pillow.R;
import cat.my.android.pillow.util.BundleUtils;

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