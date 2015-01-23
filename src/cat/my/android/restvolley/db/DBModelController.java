package cat.my.android.restvolley.db;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import cat.my.android.restvolley.IdentificableModel;
import cat.my.android.restvolley.sync.DeletedEntries;
import cat.my.android.util.CursorUtil;
import cat.my.util.StringUtil;



public class DBModelController<T extends IdentificableModel> {
	public static final String COLUMN_NAME_DIRTY = "dirty_row";
	public static final String COLUMN_UPDATED_AT = "updated_at";
	public static final String COLUMN_CREATED_AT = "created_at";
	
	public static final int DIRTY_STATUS_CLEAN = 0;
	public static final int DIRTY_STATUS_UPDATED = 1;
	public static final int DIRTY_STATUS_CREATED = 2;

	public static final String COLUMN_NAME_ID = "id";
	public static final String WHERE_ID_SELECTION = COLUMN_NAME_ID + " == ?";
	
	public static final String COLUMN_TYPE_ID = DBUtil.STRING_TYPE;
	public static final String COMMON_MODEL_ATTRIBUTES = COLUMN_NAME_ID + COLUMN_TYPE_ID + " PRIMARY KEY," + 
			COLUMN_NAME_DIRTY + DBUtil.INT_TYPE + DBUtil.COMMA_SEP +
			COLUMN_UPDATED_AT + DBUtil.TIMESTAMP_TYPE + DBUtil.COMMA_SEP +
			COLUMN_CREATED_AT + DBUtil.TIMESTAMP_TYPE;

	
	SQLiteOpenHelper dbHelper;
	IDbMapping<T> mapper;
	DeletedEntries<T> deletedEntries;
	
	
	
	public DBModelController(SQLiteOpenHelper dbHelpert, IDbMapping<T> mapper, DeletedEntries<T> deletedEntries) {
		this.dbHelper= dbHelpert;
		this.mapper = mapper;
		this.deletedEntries = deletedEntries;
	}

	public int getCount(){
		return getColumnIntegerValue(null, null);
	}
	
	public int getCount(String selection, String[] selectionArgs){
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		String query = "SELECT COUNT(*) FROM "+ getTableName();
		if(!StringUtil.isBlanck(selection)){
			query += " WHERE "+selection;
		}
		Cursor cursor = db.rawQuery(query, selectionArgs);
		cursor.moveToFirst();
		int count= cursor.getInt(0);
		cursor.close();
		db.close();
		return count;
	}
	
	private String getTableName() {
		return mapper.getTableName();
	}

	/**
	 * Returns the model with the given id
	 * @param id
	 * @return
	 */
	public T get(String id) {
		String selection = WHERE_ID_SELECTION;
		String[] selectionArgs = { id };

		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = getCursor(db, selection, selectionArgs, null);

		if (!cursor.moveToFirst()) {
			// not present
			return null;
		}

		T model = createModel(db, cursor, true);
		cursor.close();
		db.close();
		
		return model;
	}
	
	public Cursor getCursorForId(SQLiteDatabase db, String id){
		String selection = WHERE_ID_SELECTION;
		String[] selectionArgs = { id };
		
		Cursor cursor = db.query(getTableName(), // The table to query
				new String[]{"*"}, // The columns to return
				selection, // The columns for the WHERE clause
				selectionArgs, // The values for the WHERE clause
				null,
				null,
				null
				);
		
		return cursor;
	}
	
	public List<T> get(Collection<String> ids){
		List<T> result = new ArrayList<T>();
		for(String id:ids){
			result.add(get(id));
		}
		return result;
	}
	
	/**
	 * @return All the models
	 */
	public List<T> index() {
		return index(null, null, null);
	}
	
	/**
	 * Returns the models that with the given selection and order. All params may be null
	 * @param selection as defined in db.query
	 * @param selectionArgs as defined in db.query
	 * @param order as defined in db.query. If null it will use default order
	 * @return list of models
	 */
	public List<T> index(String selection, String[] selectionArgs, String order){
		order = order!=null ? order : mapper.getDefaultModelOrder();
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = getCursor(db, selection, selectionArgs, order);
		List<T> result = createModels(db, cursor);
		db.close();
		return result;
	}
	

	public List<T> getDirty(int dirtyType) {
		String selection = COLUMN_NAME_DIRTY + " == ?";
		String[] selectionArgs = { dirtyType+"" };
		
		return index(selection, selectionArgs, null);
	}
	
	/**
	 * Deletes a model from the database
	 * @param model
	 */
	public void delete(T model){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		//get dirty status
		Cursor cursor = getCursorForId(db, model.getId());
		cursor.moveToNext();
		int dirtyStatus = CursorUtil.getInt(cursor, COLUMN_NAME_DIRTY);
		cursor.close();
		
		String selection = WHERE_ID_SELECTION;
		String[] selectionArgs = { model.getId() };
		
		db.delete(getTableName(), selection, selectionArgs);
		if(dirtyStatus!=DIRTY_STATUS_CREATED && deletedEntries!=null){
			//deletedEntries != null so it can be used in no DB entries. This is ungly and may be changed!!!
			//If the status is created it has not been sent to server yet, so we don't need to delete it.
			deletedEntries.setToDelete(db, model);
		}
		db.close();
	}
	
	/**
	 * Creates a list of T, given a cursor. It also closes the cursor.
	 * @param cursor cursor to look into
	 * @return created list of T
	 */
	protected List<T> createModels(SQLiteDatabase db, Cursor cursor){
		List<T> models = new ArrayList<T>();
		while (cursor.moveToNext()) {
			T model = createModel(db, cursor, true);
			models.add(model);
		}
		cursor.close();
		return models;
	}

	/**
	 * Creates an model on the current possition of the cursor
	 * @param cursor
	 * @return new model
	 */
	protected T createModel(SQLiteDatabase db, Cursor cursor, boolean addRelations) {
		String id = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_ID));
		T model =  mapper.createModel(cursor, id);
		if(addRelations)
			mapper.addRelations(model);
		return model;
		
//		String projectId = cursor.getString(cursor
//				.getColumnIndex(COLUMN_NAME_PROJECT));
//		return createItem(cursor, id, projectId);
	}
	
	public void update(T model){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		save(db, model, OP_UPDATE);
		db.close();
	}
	
	public void create(T model){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		save(db, model, OP_CREATE);
		db.close();
	}
	
	public void cacheAll(List<T> models) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		for(T model: models)
			save(db, model, OP_CACHE);
		
		//check for models to delete: the ones deleted on the server 
		String whereClause = COLUMN_NAME_DIRTY + " != "+ DIRTY_STATUS_CREATED + " AND "+ COLUMN_NAME_ID + " NOT IN "+getIdList(models);
		String[] whereArgs = {}; 
		//TODO if its dirty_update it is a conflict!
		db.delete(getTableName(), whereClause, whereArgs);
		
		db.close();
	}
	
	/**
	 * Given a list of models it returns its ids in the format ["id1", "id2", ...]
	 * @param models
	 * @return
	 */
	private String getIdList(List<T> models) {
		StringBuilder ids = new StringBuilder();
		ids.append("(");
		boolean first = true;
		for(T model: models){
			if(!first){
				ids.append(",");
			}
			first = false;
			ids.append("\""+model.getId()+"\"");
		}
		ids.append(")");
		return ids.toString();
	}

	private static final int OP_CREATE = 1;
	private static final int OP_UPDATE = 2;
	private static final int OP_CACHE = 3;
	private void save(SQLiteDatabase db, T model, int op){
		ContentValues values = new ContentValues();
		
		
		long milis = System.currentTimeMillis();
		if(op==OP_CREATE){
			//create
			model.setId(createUUID());
			mapper.addModelContentValues(model, values);
			values.put(COLUMN_NAME_DIRTY, DIRTY_STATUS_CREATED);
			values.put(COLUMN_NAME_ID, model.getId());
			values.put(COLUMN_CREATED_AT, milis);
			values.put(COLUMN_UPDATED_AT, milis);
			db.insert(getTableName(), null, values);
		} else if(op == OP_CACHE) {
			Cursor cursor = getCursorForId(db, model.getId());
			if(cursor.moveToNext()){
				//Existing, we need to update
				T existing = createModel(db, cursor, false);
				int dirtyStatus = CursorUtil.getInt(cursor, COLUMN_NAME_DIRTY);
				if(dirtyStatus == DIRTY_STATUS_CLEAN){
					model = merge(model, existing);
					mapper.addModelContentValues(model, values);
					values.put(COLUMN_UPDATED_AT, milis);
					db.update(getTableName(), values, WHERE_ID_SELECTION, new String[]{model.getId()});
				} else {
					//MAYBE CONFLICT, we keep local one that will ovewrite server one
				}
			} else {
				//Not stored
				if(!deletedEntries.isDeleted(model.getId())){
					//If deleted on the local database we don't want to get it back
					mapper.addModelContentValues(model, values);
					values.put(COLUMN_NAME_DIRTY, DIRTY_STATUS_CLEAN);
					values.put(COLUMN_NAME_ID, model.getId());
					values.put(COLUMN_CREATED_AT, milis);
					values.put(COLUMN_UPDATED_AT, milis);
					db.insert(getTableName(), null, values);
				}
			}
			cursor.close();
		} else { //OP_UPDATE
			Cursor cursor = getCursorForId(db, model.getId());
			cursor.moveToNext();
			int dirtyStatus = CursorUtil.getInt(cursor, COLUMN_NAME_DIRTY);
			cursor.close();
			if(dirtyStatus!=DIRTY_STATUS_CREATED)
				values.put(COLUMN_NAME_DIRTY, DIRTY_STATUS_UPDATED);
			mapper.addModelContentValues(model, values);
			values.put(COLUMN_UPDATED_AT, milis);
			db.update(getTableName(), values, WHERE_ID_SELECTION, new String[]{model.getId()});
		}
	}
	
	public void markAsClean(T model){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(COLUMN_NAME_DIRTY, DIRTY_STATUS_CLEAN);
		db.update(getTableName(), values, WHERE_ID_SELECTION, new String[]{model.getId()});
		db.close();
	}
	
	private String createUUID() {
		return DBUtil.createUUID();
	}

	/**
	 * This method is called when an update on the database is performed. newModel is the model that needs to be stored, and existing is the existing one.
	 * The newModel should be updated if need with values from existing.
	 * 
	 * By default does nothing at all, but may be overwritten
	 * 
	 * @param newmodel
	 * @param existing
	 */
	protected T merge(T newmodel, T existing) {
		return newmodel;
	}
	
	
	/**
	 * Gets a database curser with all the attributes and the given selection and selectionArgs
	 * @param selection
	 * @param selectionArgs
	 * @return
	 */
	protected Cursor getCursor(SQLiteDatabase db, String selection, String[] selectionArgs, String orderBy) {
		// Define a projection that specifies which columns from the database
		// you will actually use after this query.
		String[] projection = merge(new String[]{COLUMN_NAME_ID}, mapper.getModelAttributesForProjection());

		Cursor cursor = db.query(getTableName(), // The table to query
				projection, // The columns to return
				selection, // The columns for the WHERE clause
				selectionArgs, // The values for the WHERE clause
				null, // don't group the rows
				null, // don't filter by row groups
				orderBy // The sort order
				);
		return cursor;
	}
	
	
	
	protected static String[] merge(String[] s1, String[] s2){
		List<String> s1List = new ArrayList<String>(Arrays.asList(s1));
		s1List.addAll(Arrays.asList(s2));
		return s1List.toArray(new String[] {});
	}
	
	
	
	
	public String getDropTableQuery(){
		return "DROP TABLE "+getTableName();
	}
	
	protected void setColumnValue(String id, String columnName, Object value){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		if(value instanceof String)
			values.put(columnName, (String)value);
		else if (value instanceof Integer)
			values.put(columnName, (Integer)value);
		else if (value instanceof Double)
			values.put(columnName, (Double)value);
		else //if (value instanceof Boolean)
			values.put(columnName, (Boolean)value);
		
		String selection = COLUMN_NAME_ID + " = ?";
		db.update(getTableName(), values, selection, new String[]{id});
		db.close();
	}
	
	protected int getColumnIntegerValue(String id, String columnName) {
		String selection = WHERE_ID_SELECTION;
		String[] selectionArgs = { id };

		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = getCursor(db, selection, selectionArgs, null);

		if (!cursor.moveToFirst()) {
			// not present
			return 0;
		}
		int value = cursor.getInt(cursor.getColumnIndex(columnName));
		cursor.close();
		db.close();
		
		return value;
	}
	
	public static String creteAttsString(String[][] ATTS){
		StringBuilder builder = new StringBuilder();
		boolean first = true;
		for(String[] att : ATTS){
			if(!first)
				builder.append(DBUtil.COMMA_SEP);
			first = false;
			builder.append(att[0]).append(att[1]);
		}
		return builder.toString();
	}
}
