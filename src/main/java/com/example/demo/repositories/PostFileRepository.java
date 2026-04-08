package com.example.demo.repositories;

import com.example.demo.entities.PostFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostFileRepository extends JpaRepository<PostFile, Long> {

}
