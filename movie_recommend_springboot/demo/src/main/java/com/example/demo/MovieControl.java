package com.example.demo;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.json.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.*;
import org.apache.hc.client5.http.classic.methods.*;
import org.apache.hc.client5.http.entity.*;
import org.apache.hc.client5.http.impl.classic.*;
import org.apache.hc.core5.http.io.entity.*;
import org.apache.hc.core5.http.message.*;
import java.nio.charset.Charset;


@Controller
public class MovieControl {
    @Autowired
    //SQL 쿼리를 실행하는 객체 주입
    private MovieRepository repository;


    //url이 box_office일때 실행
    @RequestMapping(value="/box_office")
    //JSON 문자열을 리턴
    @ResponseBody
    //box office 가장 최근 영화 5개와 각 영화와 가장 가까운 영화를 3개씩 추천
    public String boxOffice() throws Exception{
        //box office 가장 최근 영화 5개를 리턴하는 함수 repository.findBoxOffice() 호출
        List<Movie> boxOfficeList = repository.findBoxOffice();

        //박스 오피스 최신 영화와 추천 영화가 모두 저장될 객체
        List<Map> allMovie = new ArrayList<Map>();

        //boxOfficeList.size() : boxOffice에 저장된 객체의 수 (5개) 만큼 반복
        for (int i=0; i<boxOfficeList.size(); i++) {
            //영화 한편과 해당 영화의 추천 영화가 저장될 객체
            Map oneMovie = new HashMap();
            //boxOffice에 저장된 5편의 영화 중 i번째 영화를 리턴
            Movie movie = boxOfficeList.get(i);
            //영화 정보 저장
            oneMovie.put("movie", movie);

            ArrayList <Movie> recommendMovieList = new ArrayList<Movie>();
            //i번째 영화의 제목을 movieTitle에 대입
            String movieTitle = movie.getTitle();

            System.out.println("movieTitle="+movieTitle);

            //호출 할 Flask의 URL 설정 http://본인EC2아이피:5000/movie_recommend
            HttpPost httpPost = new HttpPost("http://43.201.20.205:5000/movie_recommend");
            
            //Flask로 전송할 영화 제목을 저장 할 객체
            List<BasicNameValuePair> nvps = new ArrayList<>();
            //Flask로 전송할 영화의 제목 설정(파라메터 이름, 파라메터값)
            nvps.add(new BasicNameValuePair("title", movieTitle ));
            //Flask로 전송할 문자 타입 설정 UTF-8
            httpPost.setEntity(
                    new UrlEncodedFormEntity(nvps, Charset.forName("UTF-8")));
            //Flask 에 접속해서 실행 결과를 가져올 객체 생성
            CloseableHttpClient httpclient = HttpClients.createDefault();
            //Flask에 접속해서 실행 결과를 가져옴
            CloseableHttpResponse response2 = httpclient.execute(httpPost);
            //Flask 실행 결과를 가져옴
            String flaskResult =
                    EntityUtils.toString(response2.getEntity(),
                            Charset.forName("UTF-8"));
            System.out.println("flaskResult=" + flaskResult);

            //Flask 서버와 연결 종료
            httpclient.close();

            try{
                //Flask서버에서 가져온 문자열을 JSON 형태 객체로 변환
                JSONArray jsonArray = new JSONArray(flaskResult);
                //jsonArray.length() : JSON 객체에 저장된 데이터수 (추천 영화 제목의 수) 만큼 반복
                for (int j = 0; j < jsonArray.length(); j++) {
                    //jsonArray.getString(j) : j번째 추천 영화 정보 리턴 ["공조2: 인터내셔날","명탐정코난","극주부도"]
                    String recommendTitle = jsonArray.getString(j);
                    
                    //JSONArray recommend = jsonArray.getJSONArray(j);
                    //recommend.get(0);remommend에서 0번째 데이터 영화제목 리턴
                    //String recommendTitle =recommend.getString(0);
                    // //repository.findTitle(recommendTitle) : 추천 영화의 제목의 영화 정보를 데이터베이스에서 조회
                    Movie recommendMovie = repository.findTitle(recommendTitle);
                    // //recommendMovieList 에 추천 영화 정보 추가
                    recommendMovieList.add(recommendMovie);
                }
                //allMovie에 추천 영화 추가
                oneMovie.put("recommend", recommendMovieList);
                allMovie.add(oneMovie);
            }catch(Exception e){
                System.out.println("e="+e);
            }
        }
        System.out.println("allMovie="+allMovie);
        //박스오피스와 추천 영화가 모두 저장된 allMovie를 JSON 문자열로 변환해서 리턴
       return new JSONArray(allMovie).toString();
    }
 
}
