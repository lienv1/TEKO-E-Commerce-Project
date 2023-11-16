package apiserver.apiserver.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import apiserver.apiserver.model.Favorite;
import apiserver.apiserver.security.AuthorizationService;
import apiserver.apiserver.service.FavoriteService;
import apiserver.apiserver.service.UserService;

@RestController
@RequestMapping("/favorite")
public class FavoriteController {

	@Autowired
	private FavoriteService favoriteService;
	
	@Autowired
	private AuthorizationService authorizationService;
	
	@Autowired
	private UserService userService;
	
	public FavoriteController(FavoriteService favoriteService, AuthorizationService authorizationService, UserService userService) {
		this.favoriteService = favoriteService;
		this.authorizationService = authorizationService;
		this.userService = userService;
	}
	
	@PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
	@GetMapping("/product/{productId}/username/{username}")
	public ResponseEntity<Boolean> getFavorite(@PathVariable("productId") Long id, @PathVariable("username") String username, Principal principal){
		boolean isAuthenticated = authorizationService.isAuthenticatedByPrincipal(principal, username);
		if (!isAuthenticated) {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}
		boolean userExist = userService.userExistsByUsername(username);
		if (!userExist) return ResponseEntity.notFound().build();		
		
		boolean isFavorite = favoriteService.isFavorite(username, id);
		return new ResponseEntity<Boolean>(isFavorite,HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
	@PostMapping("/product/{productId}/username/{username}")
	public ResponseEntity<Boolean> addFavorite(@PathVariable("productId") Long id, @PathVariable("username") String username, Principal principal){
		boolean isAuthenticated = authorizationService.isAuthenticatedByPrincipal(principal, username);
		if (!isAuthenticated) {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}
		boolean userExist = userService.userExistsByUsername(username);
		if (!userExist) return ResponseEntity.notFound().build();		
		favoriteService.addFavoriteByUsernameAndProductId(username, id);
		return new ResponseEntity<Boolean>(true,HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
	@DeleteMapping("/product/{productId}/username/{username}")
	public ResponseEntity removeFavorite(@PathVariable("productId") Long id, @PathVariable("username") String username, Principal principal) {
		boolean isAuthenticated = authorizationService.isAuthenticatedByPrincipal(principal, username);
		if (!isAuthenticated) {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}
		boolean userExist = userService.userExistsByUsername(username);
		if (!userExist) return ResponseEntity.notFound().build();		
		try {
			favoriteService.removeFavoriteByUsernameAndProductId(username, id);
			return new ResponseEntity(HttpStatus.OK);
		}
		catch (Exception e) {
			return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
		}
	}
	
}
