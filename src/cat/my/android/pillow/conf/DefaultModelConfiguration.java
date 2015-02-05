package cat.my.android.pillow.conf;

import java.lang.reflect.Type;
import java.util.Collection;

import android.content.Context;
import cat.my.android.pillow.IDataSource;
import cat.my.android.pillow.IdentificableModel;
import cat.my.android.pillow.data.db.IDbMapping;
import cat.my.android.pillow.data.db.ReflectionDbMapping;
import cat.my.android.pillow.data.rest.IRestMapping;
import cat.my.android.pillow.data.rest.RailsRestMapping;
import cat.my.android.pillow.data.sync.SynchDataSource;

import com.google.gson.reflect.TypeToken;

public class DefaultModelConfiguration<T extends IdentificableModel> implements ModelConfiguration<T>{
	Context context;
	Class<T> modelClass;
	IDbMapping<T> dbMapping;
	IRestMapping<T> restMapping;
	Type collectionType;
	Class<?> formClass;
	IDataSource<T> dataSource;
	ModelViewConfiguration<T> viewConfiguration;
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
	
	public void setViewConfiguration(ModelViewConfiguration<T> viewConfiguration) {
		this.viewConfiguration = viewConfiguration;
	}

	protected IDataSource<T> createDefaultDataSource() {
		return new SynchDataSource<T>(getDbMapping(), getRestMapping(), getContext());
	}

	protected IRestMapping<T> createDefaultRestMapping() {
		return new RailsRestMapping<T>(getUrl(), getModelClass(), getCollectionType());
	}

	protected IDbMapping<T> createDefaultDbMapping() {
		return new ReflectionDbMapping<T>(getModelClass());
	}

	protected ModelViewConfiguration<T> createDefaultViewConfiguration() {
		return new DefaultModelViewConfiguration<T>(modelClass);
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
			dbMapping = createDefaultDbMapping();
		return dbMapping;
	}
	@Override
	public IRestMapping<T> getRestMapping() {
		if(restMapping==null){
			restMapping =  createDefaultRestMapping();
		}
		return restMapping;
	}
	@Override
	public Type getCollectionType() {
		return collectionType;
	}
	
	@Override
	public ModelViewConfiguration<T> getViewConfiguration() {
		if(viewConfiguration==null)
			viewConfiguration = createDefaultViewConfiguration();
		return viewConfiguration;
	}
	

	@Override
	public IDataSource<T> getDataSource() {
		if(dataSource==null){
			dataSource = createDefaultDataSource();
		}
		return dataSource;
	}
	
	private Context getContext() {
		return context;
	}
}
