package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Webtoon;

@Repository
public interface WebtoonRepository extends JpaRepository<Webtoon,Long>{

}
