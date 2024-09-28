package com.directory.repository;


import com.directory.entity.CardInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface CardRepository extends JpaRepository<CardInfo, Long> {
    @Query("update CardInfo c set c.company = :company, c.profession = :profession, c.email = :email, c.telephone = :telephone where c.id = :id")
    @Modifying
    void updateCard(@Param("company") String company, @Param("profession") String profession, @Param("email") String email, @Param("telephone") String telephone, @Param("id") Long id);

    @Query("delete CardInfo c where c.id = :id")
    @Modifying
    void deleteCard(@Param("id") Long id);
}
