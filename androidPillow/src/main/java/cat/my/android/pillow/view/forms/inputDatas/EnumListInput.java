package cat.my.android.pillow.view.forms.inputDatas;

import android.content.Context;
import android.view.View;

import java.util.List;

import cat.my.android.pillow.view.forms.inputs.MultipleEnumSelector;

/**
 * Created by mateuyabar on 25/02/15.
 */
public class EnumListInput extends AbstractInputData {
    Class<?> enumeration;

    public EnumListInput(Class<?> enumeration) {
        this.enumeration = enumeration;
    }

    @Override
    protected View createView(Context context) {
        return new MultipleEnumSelector(context, enumeration);
    }

    @Override
    public Object getValue() {
        return getView().getSelectedEnums();
    }

    @Override
    public void setValue(Object value) {
        getView().setSelectedEnums((List<Enum<?>>) value);
    }

    @Override
    protected MultipleEnumSelector getView() {
        return (MultipleEnumSelector) super.getView();
    }
}
