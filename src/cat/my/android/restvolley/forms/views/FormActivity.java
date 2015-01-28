package cat.my.android.restvolley.forms.views;

import cat.my.android.restvolley.IdentificableModel;
import cat.my.android.restvolley.RestVolley;
import cat.my.android.restvolley.Listeners.Listener;
import cat.my.android.restvolley.forms.TFormView;
import cat.my.android.restvolley.sync.CommonListeners;
import cat.my.android.restvolley.sync.ISynchDataSource;
import cat.my.android.restvolley.utils.BundleUtils;
import cat.my.lib.restvolley.R;
import cat.my.util.StringUtil;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class FormActivity<T extends IdentificableModel>  extends ActionBarActivity  {
	FormFragment<T> formFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    addCustomActionBar();
	    setContentView(R.layout.form_activity);
		if (savedInstanceState == null) {
			formFragment = new FormFragment<T>();
			formFragment.setArguments(BundleUtils.copyBundle(getIntent().getExtras()));
			getSupportFragmentManager().beginTransaction().add(R.id.container, formFragment).commit();
		}
	}

	private void addCustomActionBar() {
		// Inflate your custom layout
	    final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(R.layout.form_action_bar, null);
	    // Set up your ActionBar
	    final ActionBar actionBar = getSupportActionBar();
	    actionBar.setDisplayShowHomeEnabled(false);
	    actionBar.setDisplayShowTitleEnabled(false);
	    actionBar.setDisplayShowCustomEnabled(true);
	    actionBar.setCustomView(actionBarLayout);
	   
//	    final int actionBarColor = getResources().getColor(R.color.action_bar);
//	    actionBar.setBackgroundDrawable(new ColorDrawable(actionBarColor));
	    
	    Button saveButton = (Button) findViewById(R.id.action_bar_save);
	    OnClickListener okClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				T model = formFragment.getModel();
				ISynchDataSource<T> dataSource = (ISynchDataSource<T>) RestVolley.getInstance(FormActivity.this).getDataSource(model.getClass());
				if(StringUtil.isBlanck(model.getId())){
					dataSource.create(model, getOnSaveListener(), CommonListeners.dummyErrorListener);
				} else {
					dataSource.update(model, getOnSaveListener(), CommonListeners.dummyErrorListener);
				}
			}
			
		};
		saveButton.setOnClickListener(okClickListener);
	}
	
	public Listener<T> getOnSaveListener(){
		return closeOnSaveListener;
	}
	
	private Listener<T> closeOnSaveListener = new Listener<T>() {
		@Override
		public void onResponse(T response) {
			finish();
		}
	};
	

	
}
