package com.example.parking.repository;

import com.example.parking.entity.EntryExitLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntryExitLogRepository extends JpaRepository<EntryExitLog, Long> {
    List<EntryExitLog> findByUserId(Long userId);

    @org.springframework.data.jpa.repository.Query("select l from EntryExitLog l where l.spaceId = :spaceId and l.entryTime <= :now and l.exitTime > :now")
    List<EntryExitLog> findOccupyingNow(@Param("spaceId") Long spaceId,
                                         @Param("now") java.time.LocalDateTime now);

    @org.springframework.data.jpa.repository.Query("select l from EntryExitLog l where l.spaceId = :spaceId and (l.entryTime < :exitTime and l.exitTime > :entryTime)")
    List<EntryExitLog> findOverlapping(@Param("spaceId") Long spaceId,
                                         @Param("entryTime") java.time.LocalDateTime entryTime,
                                         @Param("exitTime") java.time.LocalDateTime exitTime);
}

