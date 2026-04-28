package com.aj.clgportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aj.clgportal.dto.NoticeReadDto;
import com.aj.clgportal.entity.NoticeRead;


public interface NoticeReadRepository extends JpaRepository<NoticeRead, Long> {
	boolean existsByNoticeIdAndStudentId(Long noticeId, Long studentId);

    boolean existsByNoticeIdAndTeacherId(Long noticeId, Long teacherId);
    
    @Query("""
            SELECT new com.aj.clgportal.dto.NoticeReadDto(
                nr.id,
                n.id,
                s.id,
                t.id,
                nr.readAt
            )
            FROM NoticeRead nr
            JOIN nr.notice n
            LEFT JOIN nr.student s
            LEFT JOIN nr.teacher t
            WHERE n.id = :noticeId
        """)
        List<NoticeReadDto> getNoticeReadDetails(@Param("noticeId") Long noticeId);
}
