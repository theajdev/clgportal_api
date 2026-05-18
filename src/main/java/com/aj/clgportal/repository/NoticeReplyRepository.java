package com.aj.clgportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aj.clgportal.entity.NoticeReply;

public interface NoticeReplyRepository extends JpaRepository<NoticeReply, Long> {
	public List<NoticeReply> findByNoticeIdOrderByRepliedOnAsc(Long noticeId);
	
	@Query(value = """
		    SELECT id, message, replied_by, username, profile_pic, designation, replied_on, notice_id, notification_id
FROM (
    SELECT
        nu.id,
        tnr.message,
        tnr.replied_by,
        nu.username,
        tnr.profile_pic,
        tnr.designation,
        tnr.replied_on,
        tnr.notice_id,
        nu.notification_id,

        ROW_NUMBER() OVER (
            PARTITION BY nu.notification_id
            ORDER BY tnr.id DESC
        ) as rn
    FROM notification_user nu
    JOIN tbl_notice_reply tnr
        ON nu.notification_id = tnr.notification_id
    JOIN notification nt
        ON nt.id = nu.notification_id
    WHERE nu.username = :username
        AND nt.department_id = :departmentId
        AND nu.read = false
) sub
WHERE rn = 1
		    """, nativeQuery = true)
List<Object[]> findLatestUnreadByUserAndDepartment(@Param("username") String username,
@Param("departmentId") Long departmentId);
}
