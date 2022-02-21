package com.study.member.entity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
	
	//회원 조회
	Optional<Member> findByEmail(String email);
	
	//회원 여부
	boolean existsByEmail(String email);
}
