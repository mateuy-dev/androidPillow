package cat.my.android.pillow.data.validator;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

import cat.my.util.exceptions.UnimplementedException;

public class GenericComparator implements Comparator<Object>{

		@Override
		public int compare(Object o1, Object o2) {
			if (o1 instanceof Integer){
				Integer i1 = (Integer) o1;
				Integer i2 = (Integer) o2;
				return i1-i2;
			}
			if(o1 instanceof Date){
				Date date1 = (Date) o1;
				Date date2 = (Date) o2;
				return (int) (date1.getTime() - date2.getTime());
			}
			if(o1 instanceof Calendar){
				Calendar cal1 = (Calendar) o1;
				Calendar cal2 = (Calendar) o2;
				return cal1.compareTo(cal2);
			}
			throw new UnimplementedException();
		}
	}