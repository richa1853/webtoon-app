package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.business.JwtBusiness;
import com.example.demo.business.UserBusiness;
import com.example.demo.business.WebtoonBusiness;
import com.example.demo.dto.WebtoonDto;
import com.example.demo.entity.AuthRequest;
import com.example.demo.entity.UserInfo;
import com.example.demo.exception.ResourceNotFoundException;

@RestController
@RequestMapping("/webtoons")
public class WebtoonController {

	@Autowired
	private WebtoonBusiness webtoonBusiness;

	@Autowired
	private JwtBusiness jwtService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserBusiness userBusiness;

	@PostMapping("/addUser")
	public String addNewUser(@RequestBody UserInfo userInfo) {
		return userBusiness.addUser(userInfo);
	}

	@PostMapping("/authenticate")
	public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
		if (authentication.isAuthenticated()) {
			return jwtService.generateToken(authRequest.getUsername());
		} else {
			throw new UsernameNotFoundException("invalid user request !");
		}

	}

	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<WebtoonDto> createWebtoon(@RequestBody WebtoonDto webtoonDto) {
		WebtoonDto createdWebtoon = webtoonBusiness.createWebtoon(webtoonDto);
		return new ResponseEntity<>(createdWebtoon, HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<List<WebtoonDto>> getAllWebtoons() {
		List<WebtoonDto> webtoons = webtoonBusiness.getAllWebtoons();
		return new ResponseEntity<>(webtoons, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<WebtoonDto> getWebtoonById(@PathVariable Long id) {
		WebtoonDto webtoon = webtoonBusiness.getWebtoonById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Webtoon not found with id: " + id));
		return new ResponseEntity<>(webtoon, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<Void> deleteWebtoon(@PathVariable Long id) {
		webtoonBusiness.deleteWebtoon(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
