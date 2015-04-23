package com.mateuyabar.android.pillow.conf;

import java.lang.reflect.Type;

import com.mateuyabar.android.pillow.IDataSource;
import com.mateuyabar.android.pillow.IdentificableModel;
import com.mateuyabar.android.pillow.data.db.IDbMapping;
import com.mateuyabar.android.pillow.data.rest.IRestMapping;
import com.mateuyabar.android.pillow.data.validator.IValidator;

public interface ModelConfiguration<T extends IdentificableModel> {
	public Class<T> getModelClass();
	
	public Type getCollectionType();

	public IDbMapping<T> getDbMapping();

	public IRestMapping<T> getRestMapping();
	
	public IDataSource<T> getDataSource();
	
	public ModelViewConfiguration<T> getViewConfiguration();
	
	public IValidator<T> getValidator();

}