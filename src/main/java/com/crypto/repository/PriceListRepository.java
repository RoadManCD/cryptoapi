package com.crypto.repository;

import com.crypto.entity.PriceList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PriceListRepository extends JpaRepository<PriceList, String> {


}
