package com.jeongeun.project.springboot.domain.products;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.awt.print.Book;
import java.util.List;

@Repository
public interface BookMarkRepository extends JpaRepository<BookMark, Long> {
    @Query("SELECT COUNT(*) FROM BookMark b WHERE bmuid=:bmuid AND p_id=:p_id")
    Long findUserBookMarkedProducts(@Param("bmuid") Long bmuid, @Param("p_id") Long p_id);

    @Query("SELECT b FROM BookMark b WHERE bmuid=:bmuid AND p_id=:p_id")
    BookMark findBookMark(@Param("bmuid") Long bmuid, @Param("p_id") Long p_id);

    @Query("SELECT b FROM BookMark b WHERE bmuid=:bmuid")
    List<BookMark> findBookMarkByBmuid(@Param("bmuid") Long bmuid);
}
