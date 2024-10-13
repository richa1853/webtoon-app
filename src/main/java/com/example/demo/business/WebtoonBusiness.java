package com.example.demo.business;

import java.util.List;
import java.util.Optional;

import com.example.demo.dto.WebtoonDto;

public interface WebtoonBusiness {

	List<WebtoonDto> getAllWebtoons();

    Optional<WebtoonDto> getWebtoonById(Long id);

    WebtoonDto createWebtoon(WebtoonDto webtoonDto);

    void deleteWebtoon(Long id);
}
