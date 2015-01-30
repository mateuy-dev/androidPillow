package cat.my.android.restvolley.conf;

import java.util.List;

import cat.my.android.restvolley.RestVolley;
import cat.my.android.restvolley.RestVolleyConfigXml;

import android.content.Context;

public interface IModelConfigurations {
	public List<ModelConfiguration<?>> getModelConfigurators(Context context, RestVolleyConfigXml config);
}
