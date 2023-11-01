package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, String> {
    // 실행 할 SQL 구현
    //value="select * from movie_tbl m where m.title=:title limit 1"
    // movie_tbl 테이블에서 title이 일치하는 레코드 조회

    //@Param(value="title") String title : Sql 쿼리에 대입할 파라메터 선언
    //SQL 쿼리에 :title로 대입
    @Query(value="select * from movie_tbl m where m.title=:title limit 1", nativeQuery=true)
    public Movie findTitle(@Param(value="title") String title);

    //실행할 SQL 구현
    //"select * from movie_tbl m order by num limit 5 : 가장 최신 영화 5편을 조회
    @Query(value="select * from movie_tbl m order by num limit 5", nativeQuery=true)
    public List <Movie> findBoxOffice();
}