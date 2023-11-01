package com.example.demo;

import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
@Data //setter/getter,toString() 생성자 자동 생성
@Entity(name="movie_tbl") //하이버네이트가 조회할 테이블명
public class Movie {
    @Id //Primary key 속성
    //영화 번호를 저장 할 속성
    private int num;
    //영화 제목을 저장할 속성
    private String title;
    //영화 포스터 url을 저장 할 속성
    private String poster;
    //영화 등급을 저장 할 속성
    private String degree;
    //영화 장르를 저장 할 속성
    private String genre;
    //영화 개봉일을 저장 할 속성
    @Column(name = "open_date")
    private String openDate;
    //국가를 저장할 속성
    private String country;
    //상영 시간을 저장 할 속성
    @Column(name = "movie_time")
    private String movieTime;
    //영화 줄거리를 저장 할 속성
    private String synopsys;
}
