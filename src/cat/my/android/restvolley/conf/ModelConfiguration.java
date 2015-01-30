package cat.my.android.restvolley.conf;

import java.lang.reflect.Type;

import cat.my.android.restvolley.IDataSource;
import cat.my.android.restvolley.IdentificableModel;
import cat.my.android.restvolley.db.IDbMapping;
import cat.my.android.restvolley.rest.IRestMapping;
import cat.my.android.restvolley.sync.ISynchDataSource;

public interface ModelConfiguration<T extends IdentificableModel> {
	public Class<T> getModelClass();
	
	public Type getCollectionType();

	public IDbMapping<T> getDbMapping();

	public IRestMapping<T> getRestMapping();
	
	public IDataSource<T> getDataSource();
	
	public Class<?> getFormClass();

	
}