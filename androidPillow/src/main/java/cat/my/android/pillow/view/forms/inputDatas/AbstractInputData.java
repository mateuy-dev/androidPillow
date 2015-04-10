package cat.my.android.pillow.view.forms.inputDatas;

import android.content.Context;
import android.view.View;
import cat.my.android.pillow.view.forms.InputData;
import cat.my.util.exceptions.UnimplementedException;

public abstract class AbstractInputData implements InputData{
	View view;
	Context context;
	
	@Override
	public View getView(Context context){
		this.context = context;
		if(view == null){
			view = createView(context);
		}
		return view;
	}
	
	protected View getView(){
		return view;
	}
	
	public Context getContext() {
		return context;
	}

	protected abstract View createView(Context context);
	
	
	@Override
	public void addOnValueChangedListener(ValueChangedListener listener) {
		throw new UnimplementedException();
	}
}
