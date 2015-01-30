package cat.my.android.restvolley.db;

import android.content.Context;
import cat.my.android.restvolley.IdentificableModel;
import cat.my.android.restvolley.RestVolley;
import cat.my.android.restvolley.sync.ISynchDataSource;

public class QueryHelper<T extends IdentificableModel> {
	Context context;
	Class<T> clazz;
	IDbMapping<T> dbMapping;
	RestVolley restVolley;
	public QueryHelper(Context context, Class<T> clazz) {
		this.clazz = clazz;
		this.context = context;
		restVolley = RestVolley.getInstance(context);
		dbMapping = restVolley.getModelConfiguration(clazz).getDbMapping();
	}
	
	public Query getHasManyToManyThrough(IdentificableModel referencedModel, Class<? extends IdentificableModel> through){
		String tableName = restVolley.getModelConfiguration(through).getDbMapping().getTableName();
		String referenceModelTableName = restVolley.getModelConfiguration(referencedModel.getClass()).getDbMapping().getTableName();
		String idReferencedModelColumn = tableNameToId(referenceModelTableName);
		String currentIdColumn = tableNameToId(dbMapping.getTableName());
		
		String query = "EXISTS (SELECT * FROM "+tableName+" t2 WHERE t2."+idReferencedModelColumn+" = ? AND t2."+currentIdColumn+" = "+dbMapping.getTableName()+".id)";
		return new Query(query, new String[]{referencedModel.getId()});
	}
	
	private String tableNameToId(String string){
		return string + "Id";
	}
	
	/**
	 * @return same string with first letter to lower case
	 */
	private String lowerCaseFirstLetter(String string) {
		char c[] = string.toCharArray();
		c[0] = Character.toLowerCase(c[0]);
		return new String(c);
	}


	public static class Query{
		String selection;
		String[] selectionArgs;
		
		public String getSelection() {
			return selection;
		}
		public String[] getSelectionArgs() {
			return selectionArgs;
		}
		public Query(String selection, String[] selectionArgs) {
			super();
			this.selection = selection;
			this.selectionArgs = selectionArgs;
		}
	}




}
