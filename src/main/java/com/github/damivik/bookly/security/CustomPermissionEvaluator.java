package com.github.damivik.bookly.security;

import java.io.Serializable;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.github.damivik.bookly.entity.Bookshelf;
import com.github.damivik.bookly.entity.User;

@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {
	
	@Override
	public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
		User authenticatedUser = (User) authentication.getPrincipal();
		
		if (targetDomainObject instanceof User) {
			User targetUser = (User) targetDomainObject;
			
			return authenticatedUser.getId() == targetUser.getId();
		}
		
		if (targetDomainObject instanceof Bookshelf) {
			Bookshelf bookshelf = (Bookshelf) targetDomainObject;
			
			return authenticatedUser.getId() == bookshelf.getUser().getId();
		}
		return false;
	}

	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType,
			Object permission) {
		return false;
	}

}
