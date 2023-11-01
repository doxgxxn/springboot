#클라우드 환경 설정 ubuntu 22.04
FROM ubuntu:22.04
#자바 실행 파일 jar 를 저장할 디렉토리를 도커가 생성하고 실행 파일을 여기에 저장 해 줌
WORKDIR /my_boot

# 작업했던 파일들을 복사해서 my_boot 에 복사
#COPY . (현재 폴더 pom.xml 과 Dockerfile이 있는 폴더 /home/ubuntu/movie_recommend_springboot/demo)
#     . (작업 폴더 /my_boot)
#공백 주의
COPY  .   .

########### 실행 환경 설정 명령 #########
RUN apt update


RUN apt install openjdk-11-jre-headless -y


RUN apt install maven -y
RUN mvn package -Dmaven.test.skip=true

#사용할 포트 설정
EXPOSE 8080

#nohup로 실행 할 명령 설정
ENTRYPOINT ["nohup","java",  "-jar",  "/my_boot/target/demo-0.0.1-SNAPSHOT.jar &"]