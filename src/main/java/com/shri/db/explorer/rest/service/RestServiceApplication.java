package com.shri.db.explorer.rest.service;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

public class RestServiceApplication extends Application{

	private static final Set<Class<?>> SERVICES = new HashSet<>();
	
	@Override
	public Set<Class<?>> getClasses() {
		SERVICES.add(DBService.class);
		return SERVICES;
	}
	
}
