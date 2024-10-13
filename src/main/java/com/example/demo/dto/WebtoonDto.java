package com.example.demo.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WebtoonDto {
	private Long id;
	private String title;
	private String description;
	private List<CharacterDTO> characters;
}
