package com.example.demo.model.persistence.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<UserOrder, Long> {

	@Query(
		nativeQuery = true,
		value = "select p from user_order where user_id = :userId"
	)
	List<UserOrder> findByUser(@Param("userId") Long userId);
}
