package cat.my.android.pillow.conf;

import java.util.List;

import android.content.Context;
import cat.my.android.pillow.PillowConfigXml;

public interface IModelConfigurations {
	public List<ModelConfiguration<?>> getModelConfigurators(Context context, PillowConfigXml config);
}
