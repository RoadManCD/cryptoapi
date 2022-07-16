package com.crypto.repository;

import com.crypto.entity.PriceList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface PriceListRepository extends JpaRepository<PriceList, String> {


}
