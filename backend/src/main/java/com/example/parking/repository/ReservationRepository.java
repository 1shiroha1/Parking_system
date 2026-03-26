package com.example.parking.repository;

import com.example.parking.entity.Reservation;
import com.example.parking.model.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByUserId(Long userId);

    @Query("select r from Reservation r where r.spaceId = :spaceId and r.status in :statuses")
    List<Reservation> findActiveBySpaceId(@Param("spaceId") Long spaceId,
                                          @Param("statuses") List<ReservationStatus> statuses);

    @Query("select r from Reservation r where r.spaceId = :spaceId and r.status in :statuses and " +
            "(r.startTime < :endTime and r.endTime > :startTime)")
    List<Reservation> findOverlapping(@Param("spaceId") Long spaceId,
                                        @Param("startTime") LocalDateTime startTime,
                                        @Param("endTime") LocalDateTime endTime,
                                        @Param("statuses") List<ReservationStatus> statuses);

    @Query("select r from Reservation r where r.spaceId = :spaceId and r.status = 'CONFIRMED' and r.startTime <= :now and r.endTime > :now")
    List<Reservation> findReservedNow(@Param("spaceId") Long spaceId, @Param("now") LocalDateTime now);
}

