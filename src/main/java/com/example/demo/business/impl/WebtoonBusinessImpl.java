package com.example.demo.business.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.business.WebtoonBusiness;
import com.example.demo.dto.CharacterDTO;
import com.example.demo.dto.WebtoonDto;
import com.example.demo.entity.Character;
import com.example.demo.entity.Webtoon;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.CharacterRepository;
import com.example.demo.repository.WebtoonRepository;

@Service
public class WebtoonBusinessImpl implements WebtoonBusiness {
	
	@Autowired
	private WebtoonRepository webtoonRepository;
	@Autowired
    private CharacterRepository characterRepository;

    @Override
    public List<WebtoonDto> getAllWebtoons() {
        List<Webtoon> webtoons = webtoonRepository.findAll();
        return webtoons.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<WebtoonDto> getWebtoonById(Long id) {
        Optional<Webtoon> webtoon = webtoonRepository.findById(id);
        return webtoon.map(this::convertToDTO);
    }

    @Override
    public WebtoonDto createWebtoon(WebtoonDto webtoonCreateDTO) {
        Webtoon webtoon = new Webtoon();
        webtoon.setTitle(webtoonCreateDTO.getTitle());
        webtoon.setDescription(webtoonCreateDTO.getDescription());

        if (webtoonCreateDTO.getCharacters() != null) {
            List<Character> characters = webtoonCreateDTO.getCharacters().stream()
                    .map(this::convertToCharacterEntity)
                    .collect(Collectors.toList());
            webtoon.setCharacters(characters);
            characters.forEach(character -> character.setWebtoon(webtoon));
        }

        Webtoon savedWebtoon = webtoonRepository.save(webtoon);
        return convertToDTO(savedWebtoon);
    }

    @Override
    public void deleteWebtoon(Long id) {
        if (webtoonRepository.existsById(id)) {
            webtoonRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Webtoon with ID " + id + " not found.");
        }
    }

    // Helper method to convert Webtoon entity to WebtoonDTO
    private WebtoonDto convertToDTO(Webtoon webtoon) {
    	WebtoonDto dto = new WebtoonDto();
        dto.setId(webtoon.getId());
        dto.setTitle(webtoon.getTitle());
        dto.setDescription(webtoon.getDescription());
        dto.setCharacters(
                webtoon.getCharacters().stream()
                .map(this::convertToCharacterDTO)
                .collect(Collectors.toList())
        );
        return dto;
    }

    // Helper method to convert Character entity to CharacterDTO
    private CharacterDTO convertToCharacterDTO(Character character) {
        CharacterDTO characterDTO = new CharacterDTO();
        characterDTO.setId(character.getId());
        characterDTO.setName(character.getName());
        characterDTO.setDescription(character.getDescription());
        return characterDTO;
    }

    // Helper method to convert CharacterDTO to Character entity
    private Character convertToCharacterEntity(CharacterDTO characterDTO) {
        Character character = new Character();
        character.setName(characterDTO.getName());
        character.setDescription(characterDTO.getDescription());
        return character;
    }
}