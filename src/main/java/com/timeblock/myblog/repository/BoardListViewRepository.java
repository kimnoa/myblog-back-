package com.timeblock.myblog.repository;

import com.timeblock.myblog.entity.BoardListViewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardListViewRepository extends JpaRepository<BoardListViewEntity, Integer> {

}
