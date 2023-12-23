package org.gfg.minor1.repository;

import org.gfg.minor1.models.Txn;
import org.gfg.minor1.models.TxnStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TxnRepository extends JpaRepository<Txn,Integer> {
    public Txn findByStudent_ContactAndBook_BookNoAndTxnStatusOrderByCreatedOnDesc(String studentContact,
                                                                                   String bookNo,
                                                                                   TxnStatus status);

    @Transactional
    @Modifying
    @Query(value="update txn set created_on=\" 2023-08-23 00:57:56.342000\" where id=2",nativeQuery=true)
    public void updateTxnCreatedOn();
}
