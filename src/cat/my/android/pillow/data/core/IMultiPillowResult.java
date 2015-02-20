package cat.my.android.pillow.data.core;

public interface IMultiPillowResult<T> extends IPillowResult<T>{
	public void addSubTask(SubTask<T> subtask);
	public IPillowResult<T> getSubResult();
	
	public interface SubTask<T> {
		public IPillowResult<T> execute(T response); 
		
	}

}
