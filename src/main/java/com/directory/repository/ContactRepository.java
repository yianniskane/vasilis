package com.directory.repository;


import com.directory.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
@Transactional
public interface ContactRepository extends JpaRepository<Contact, Long> {
    @Query("select c from Contact c where c.contact_no = :contact_no")
    Contact findContactByContact_no(@Param("contact_no") Integer contact_no);

    @Modifying
    @Query("delete from Contact c where c.contact_no = :contact_no")
    void deleteContact(@Param("contact_no") Integer contact_no);

    @Query(
            value = "select * from contact c order by c.contact_no desc LIMIT 900",
            nativeQuery = true
    )
    List<Contact> findAllContacts();

    @Query(
            value = "select * from contact c where c.date_recorded >= :ff and c.date_recorded <= :tt order by c.contact_no desc",
            nativeQuery = true
    )
    List<Contact> findContactBetweenDate(@Param("ff") String from, @Param("tt") String until);

    @Modifying
    @Query("update Contact c set c.date_recorded = :date_recorded, c.telephone= :telephone, c.email= :email, c.address.street= :street, c.address.area= :area, c.address.city= :city, c.address.nomos= :nomos, c.address.zip= :zip, c.seller= :seller, c.manager= :manager, c.company= :company, c.afm= :afm, c.comments= :comments, c.completed= :completed, c.followup= :followup, c.ignored= :ignored, c.last_comm_date =:last_comm_date, c.orders= :orders where c.contact_no= :contact_no")
    int updateContact(@Param("date_recorded") String date_recorded, @Param("telephone") String telephone, @Param("email") String email, @Param("street") String street, @Param("area") String area, @Param("city") String city, @Param("nomos") String nomos, @Param("zip") String zip, @Param("seller") String seller, @Param("manager") String manager, @Param("company") String company, @Param("afm") String afm, @Param("comments") String comments, @Param("completed") boolean completed, @Param("followup") String followup, @Param("ignored") boolean ignored, @Param("last_comm_date") String last_comm_date, @Param("orders") String orders, @Param("contact_no") Integer contact_no);

    List<Contact> findByCompanyStartingWithIgnoreCase(String term);

    List<Contact> findByManagerStartingWithIgnoreCase(String term);

    List<Contact> findByTelephoneStartingWithIgnoreCase(String term);

    List<Contact> findByAddress_StreetStartingWithIgnoreCase(String term);

    List<Contact> findByAddress_CityStartingWithIgnoreCase(String term);

    List<Contact> findByAfmStartingWithIgnoreCase(String term);
}
