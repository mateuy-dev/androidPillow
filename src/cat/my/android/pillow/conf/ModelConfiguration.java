package cat.my.android.pillow.conf;

import java.lang.reflect.Type;

import cat.my.android.pillow.IDataSource;
import cat.my.android.pillow.IdentificableModel;
import cat.my.android.pillow.data.db.IDbMapping;
import cat.my.android.pillow.data.rest.IRestMapping;

public interface ModelConfiguration<T extends IdentificableModel> {
	public Class<T> getModelClass();
	
	public Type getCollectionType();

	public IDbMapping<T> getDbMapping();

	public IRestMapping<T> getRestMapping();
	
	public IDataSource<T> getDataSource();
	
	public Class<?> getFormClass();

	
}