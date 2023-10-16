package apiserver.apiserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import apiserver.apiserver.security.AuthorizationService;
import apiserver.apiserver.service.FavoriteService;

@RestController
public class FavoriteController {

	@Autowired
	private FavoriteService favoriteService;
	
	@Autowired
	private AuthorizationService authorizationService;
	
	public FavoriteController(FavoriteService favoriteSerice, AuthorizationService authorizationService) {
		this.favoriteService = favoriteService;
		this.authorizationService = authorizationService;
	}
	
	
}
