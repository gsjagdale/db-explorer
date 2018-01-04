package com.shri.db.explorer.connection;

import java.util.List;
import org.hamcrest.Matchers;
import ch.lambdaj.Lambda;
import com.shri.db.explorer.connection.bo.Entity;

public class LambdaHelper {

	public static final <T> List<String> listNames(List<T> list) {
		return Lambda.extract(list, Lambda.on(Entity.class).getName());
	}

	public static final <T> List<T> selectByName(List<T> list, Object value) {
		return Lambda.select(list, Lambda.having(Lambda.on(Entity.class).getName(), Matchers.equalTo(value)));
	}

	public static final <T> T selectOneByName(List<T> list, Object value) {
		List<T> list2 = selectByName(list, value);
		assert list2 != null;
		assert list2.size() == 1;

		return list2.get(0);
	}
}
