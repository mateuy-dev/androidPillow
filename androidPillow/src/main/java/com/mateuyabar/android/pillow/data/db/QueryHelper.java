package com.mateuyabar.android.pillow.data.db;

import android.content.Context;
import com.mateuyabar.android.pillow.IdentificableModel;
import com.mateuyabar.android.pillow.Pillow;

public class QueryHelper<T extends IdentificableModel> {
	Context context;
	Class<T> clazz;
	IDbMapping<T> dbMapping;
	Pillow pillow;
	public QueryHelper(Context context, Class<T> clazz) {
		this.clazz = clazz;
		this.context = context;
		pillow = Pillow.getInstance(context);
		dbMapping = pillow.getModelConfiguration(clazz).getDbMapping();
	}
	
	public Query getHasManyToManyThrough(IdentificableModel referencedModel, Class<? extends IdentificableModel> through){
		String tableName = pillow.getModelConfiguration(through).getDbMapping().getTableName();
		String referenceModelTableName = pillow.getModelConfiguration(referencedModel.getClass()).getDbMapping().getTableName();
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
