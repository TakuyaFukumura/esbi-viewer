package com.example.myapplication.repository;

import com.example.myapplication.entity.EsbiData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EsbiDataRepository extends JpaRepository<EsbiData, Long> {
    
    List<EsbiData> findByUserNameOrderByCreatedAtDesc(String userName);
    
    Optional<EsbiData> findTopByUserNameOrderByCreatedAtDesc(String userName);
}
