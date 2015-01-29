package cat.my.android.restvolley.conf;

import java.lang.reflect.Type;

import cat.my.android.restvolley.db.IDbMapping;
import cat.my.android.restvolley.rest.IRestMapping;

public class ModelConfiguration<T>{
		Class<T> modelClass;
		IDbMapping<T> dbMapping;
		IRestMapping<T> restMapping;
		Type collectionType;
		Class<?> formClass;
		
		public ModelConfiguration(Class<T> modelClass) {
			super();
			this.modelClass = modelClass;
		}

		public Class<T> getModelClass() {
			return modelClass;
		}

		public IDbMapping<T> getDbMapping() {
			return dbMapping;
		}

		public void setDbMapping(IDbMapping<T> dbMapping) {
			this.dbMapping = dbMapping;
		}

		public IRestMapping<T> getRestMapping() {
			return restMapping;
		}

		public void setRestMapping(IRestMapping<T> restMapping) {
			this.restMapping = restMapping;
		}

		public Type getCollectionType() {
			return collectionType;
		}

		public void setCollectionType(Type collectionType) {
			this.collectionType = collectionType;
		}

		public Class<?> getFormClass() {
			return formClass;
		}

		public void setFormClass(Class<?> formClass) {
			this.formClass = formClass;
		}
	}