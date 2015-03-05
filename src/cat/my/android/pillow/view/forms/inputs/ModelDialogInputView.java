package cat.my.android.pillow.view.forms.inputs;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.ListView;
import cat.my.android.pillow.IDataSource;
import cat.my.android.pillow.IdentificableModel;
import cat.my.android.pillow.Pillow;
import cat.my.android.pillow.Listeners.ViewListener;
import cat.my.android.pillow.R;
import cat.my.android.pillow.data.sync.CommonListeners;
import cat.my.android.pillow.view.base.IModelListAdapter;
import cat.my.android.pillow.view.forms.inputDatas.EditTextData;
import cat.my.android.pillow.view.list.PillowListAdapter;
import cat.my.util.exceptions.BreakFastException;

public class ModelDialogInputView <T extends IdentificableModel> extends EditText{
	IDataSource<T> dataSource;
	Class<T> selectedClass;
	String selectedId;
	T selected;

	public ModelDialogInputView(Context context, Class<T> selectedClass) {
		super(context);
		this.selectedClass = selectedClass;
		init();
	}
	
	protected void init(){
		dataSource = Pillow.getInstance(getContext()).getDataSource(selectedClass);
		setEms(EditTextData.EMS);
		setFocusable(false);
		setKeyListener(null);
		setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				displaySelectDialog();
			}
		});
	}
	
	private void displaySelectDialog() {
		new SelectDialog(getContext()).show();
	}

	public void setValue(String modelId){
		selectedId = modelId;
		if(modelId==null){
			selected = null;
			setText("");
		} else {
			try {
				T toSearch = selectedClass.newInstance();
				toSearch.setId(modelId);
				dataSource.show(toSearch).setListeners(new ViewListener<T>() {
					@Override
					public void onResponse(T response) {
						selected = response;
						setText(response.toString());
					}
				}, CommonListeners.defaultErrorListener);
			} catch (Exception e) {
				throw new BreakFastException(e);
			}
		}
	}
	
	protected void setValue(T value){
		selectedId = value.getId();
		selected = value;
		setText(value.toString());
	}
	
	public String getValue(){
		return selectedId;
	}
	
	public class SelectDialog extends Dialog{
		IModelListAdapter<T> adapter;
		
		public SelectDialog(Context context) {
			super(context,android.R.style.Theme_Holo_Light_DialogWhenLarge_NoActionBar);
		}
		
		protected void onCreate(Bundle savedInstanceState) {
		    super.onCreate(savedInstanceState);
		    setContentView(R.layout.select_model_dialog);
		    ListView listView = (ListView)findViewById(R.id.list_view);
		    adapter = Pillow.getInstance(getContext()).getViewConfiguration(selectedClass).getListAdapter(getContext());
		    listView.setOnItemClickListener(new OnItemClickListener() {
		    	@Override
		    	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		    		setValue(adapter.getItem(position));
					dismiss();
		    	}
			});
		    
		    listView.setAdapter(adapter);
		    adapter.refreshList();
		    
		    EditText editText = (EditText) findViewById(R.id.search_edit_text);
		    editText.addTextChangedListener(new TextWatcher() {
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					adapter.getFilter().filter(s);
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
				
				@Override
				public void afterTextChanged(Editable s) {}
			});
		    
		}
		
	}
}
