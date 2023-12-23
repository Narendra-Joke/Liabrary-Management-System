package org.gfg.minor1.repository;

import org.gfg.minor1.models.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author,Integer> {
    // native query : is taken care by mysql
    @Query(value = "select * from author where email =:email",nativeQuery = true)
    Author getAuthorWithMailAddress(String email);

    @Query("select a from Author a where a.email= ?1")
    Author getAuthorWithMailAddressWithoutNative(String email);

    Author findByEmail(String email);
}

