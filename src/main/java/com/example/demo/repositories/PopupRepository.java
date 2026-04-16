package com.example.demo.repositories;

import com.example.demo.entities.Popup;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PopupRepository extends JpaRepository<Popup, Long> {

    @Query("""
    SELECT p 
    FROM Popup p 
    ORDER BY function('RANDOM')
""")
    List<Popup> findRandomPopups(Pageable pageable);
}
