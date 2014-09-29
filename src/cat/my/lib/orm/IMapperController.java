package cat.my.lib.orm;

import cat.my.lib.restvolley.models.IdentificableModel;

public interface IMapperController {
		public <T extends IdentificableModel> ModelMapper<T> getMapper(Class<T> clazz);
	}