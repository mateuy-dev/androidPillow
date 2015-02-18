package cat.my.android.pillow.data.db;

import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

public interface IDbMapping<T>{
	/**
	 * This method should be add the model relations (if any) to the given model. In most cases should do nothing
	 * This method is called after a model is obtained from the database -after createModel(cursor)-
	 * @param db 
	 * @param model
	 */
	public void addRelations(T model);
	
	/**
	 * @return order by to be executed on a getAll() method
	 */
	public String getDefaultModelOrder();

	/**
	 * This method should add the specific attributes of each class to values
	 * 
	 * @param values
	 */
	public void addModelContentValues(T model, ContentValues values);
	
	/**
	 * Creates a Model on the current possition of the cursor
	 * @param cursor
	 * @return new model
	 */
	public T createModel(Cursor cursor, String id);
	
	/**
	 * @return the common attributes
	 */
	public String[] getModelAttributesForProjection();
	
	/**
	 * @return the name of the sql table
	 */
	public String getTableName();
	
	public String[][] getAttributes();
	
	public IDBSelection getSelection(T filter);
	
//	Not used. Not using Foreign keys now (Check DBUtil)
//	public List<String> getForeignKeys();
	
	public interface IDBSelection{
		public String getSelection();
		public String[] getArgs();
	}
	
	public class DBSelection implements IDBSelection{
		String selection;
		String[] args;
		public DBSelection(String selection, String[] args) {
			this.selection = selection;
			this.args = args;
		}
		@Override
		public String getSelection() {
			return selection;
		}
		@Override
		public String[] getArgs() {
			return args;
		}
	}

	public List<String> getTriggers();

	
}
