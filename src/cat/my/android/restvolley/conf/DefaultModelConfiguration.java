package cat.my.android.restvolley.conf;

import java.lang.reflect.Type;
import java.util.Collection;

import com.google.gson.reflect.TypeToken;

import android.content.Context;

import cat.my.android.restvolley.IDataSource;
import cat.my.android.restvolley.IdentificableModel;
import cat.my.android.restvolley.db.IDbMapping;
import cat.my.android.restvolley.db.ReflectionDbMapping;
import cat.my.android.restvolley.forms.views.FormActivity;
import cat.my.android.restvolley.rest.IRestMapping;
import cat.my.android.restvolley.rest.RailsRestMapping;
import cat.my.android.restvolley.sync.SynchDataSource;
import cat.my.util.UnimplementedException;

public class DefaultModelConfiguration<T extends IdentificableModel> implements ModelConfiguration<T>{
	Context context;
	Class<T> modelClass;
	IDbMapping<T> dbMapping;
	IRestMapping<T> restMapping;
	Type collectionType;
	Class<?> formClass;
	IDataSource<T> dataSource;
	String url;
	
	public DefaultModelConfiguration(Context context, Class<T> modelClass, TypeToken<Collection<T>> collectionTypeToken, String url) {
		super();
		this.modelClass = modelClass;
		this.collectionType = collectionTypeToken.getType();
		this.url = url;
	}

	public void setDbMapping(IDbMapping<T> dbMapping) {
		this.dbMapping = dbMapping;
	}

	public void setRestMapping(IRestMapping<T> restMapping) {
		this.restMapping = restMapping;
	}

	public void setFormClass(Class<?> formClass) {
		this.formClass = formClass;
	}

	public void setDataSource(IDataSource<T> dataSource) {
		this.dataSource = dataSource;
	}

	protected IDataSource<T> createDataSource() {
		return new SynchDataSource<T>(getDbMapping(), getRestMapping(), getContext());
	}

	protected IRestMapping<T> createRestMapping() {
		return new RailsRestMapping<T>(getUrl(), getModelClass(), getCollectionType());
	}

	protected IDbMapping<T> createDbMapping() {
		return new ReflectionDbMapping<T>(getModelClass());
	}

	private String getUrl() {
		return url;
	}
	
	@Override
	public Class<T> getModelClass() {
		return modelClass;
	}
	@Override
	public IDbMapping<T> getDbMapping() {
		if(dbMapping==null)
			dbMapping = createDbMapping();
		return dbMapping;
	}
	@Override
	public IRestMapping<T> getRestMapping() {
		if(restMapping==null){
			restMapping =  createRestMapping();
		}
		return restMapping;
	}
	@Override
	public Type getCollectionType() {
		return collectionType;
	}
	@Override
	public Class<?> getFormClass() {
		return formClass;
	}
	
	@Override
	public IDataSource<T> getDataSource() {
		if(dataSource==null){
			dataSource = createDataSource();
		}
		return dataSource;
	}
	
	private Context getContext() {
		return context;
	}
}
