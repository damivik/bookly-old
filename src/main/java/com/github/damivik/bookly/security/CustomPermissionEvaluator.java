package com.github.damivik.bookly.security;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.github.damivik.bookly.entity.Bookshelf;
import com.github.damivik.bookly.entity.User;
import com.github.damivik.bookly.repository.BookshelfRepository;

@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {

	private BookshelfRepository bookshelfRepository;
	
	@Autowired
	public void setBookshelfRepository(BookshelfRepository bookshelfRepository) {
		this.bookshelfRepository = bookshelfRepository;
	}
	
	@Override
	public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType,
			Object permission) {
		
		User authenticatedUser = (User) authentication.getPrincipal();
		
		if (targetType.equals("User")) {	
			return authenticatedUser.getId() == (int) targetId;
		} else if (targetType.equals("Bookshelf")) {
			Bookshelf shelf = bookshelfRepository.findById((int)targetId).get(); 
			return shelf.getUser().getId() == authenticatedUser.getId();
		}

		return false;
	}

}
