package com.jeongeun.project.springboot.domain.products;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Repository
public interface ProductsQARepository extends JpaRepository<ProductsQA, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE ProductsQA SET originNo=:qaId WHERE qaId=:qaId")
    void updateOriginNo(@PathVariable("qaId") Long qaId);

    @Query("SELECT q FROM ProductsQA q WHERE p_id=:p_id AND groupLayer=:groupLayer ORDER BY qaId DESC")
    List<ProductsQA> findOriginProductsQA(@PathVariable("p_id") Long p_id, @PathVariable("groupLayer") int groupLayer);

    @Query("SELECT q FROM ProductsQA q WHERE p_id=:p_id AND groupLayer=:groupLayer AND originNo=:originNo")
    ProductsQA getReplyProductsQA(@PathVariable("p_id") Long p_id, @PathVariable("groupLayer") int groupLayer, @PathVariable("originNo") Long originNo);

    @Query("SELECT q FROM ProductsQA q WHERE qaId=:qaId")
    ProductsQA findProductsQaById(@PathVariable("qaId") Long qaId);


}
