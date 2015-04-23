package com.mateuyabar.android.pillow.conf;

import java.util.List;

import android.content.Context;
import com.mateuyabar.android.pillow.PillowConfigXml;

public interface IModelConfigurations {
	public List<ModelConfiguration<?>> getModelConfigurators(Context context, PillowConfigXml config);
}
