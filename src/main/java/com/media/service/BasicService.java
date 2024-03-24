package com.media.service;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
/**
 * Path.get():주어진 문자열 경로를 이용해 Path인스턴스를 생성한다. 
 * 				   이 메소드는 팩토리 메서드 패턴을 사용해 Path인터페이스의 구현 클래스를 생성하고 반환
 * 					
 * 
 */
@Service
public class BasicService {

   public void saveFile(MultipartFile file) {
	   if(!file.isEmpty()) {
		
		   //video디렉토리 안에 file.getOriginalFilename()에서 얻은 파일 이름과 일치하는 
		   // 파일의 경로를 Path인스턴스(filePath)를 생성한다.
		   Path filepath = Paths.get("C:\\workspace\\media_streaming\\src\\main\\resources\\static\\video", file.getOriginalFilename());
		   
		   // Files.newOutputStream()으로 OutStream인터페이스를구현한 파일에 시스템에
		   // 쓰기작업을 수행하는 파일 출력 스트림 클래스를 반환해준다. (경로로 지정된 파일에 I/O)
		   try(OutputStream os = Files.newOutputStream(filepath)) {
			   
			   //MultipartFile에 저장된 데이터를 가져와서 해당 데이터를 파일에 쓴다.
			   os.write(file.getBytes());
		} catch (Exception e) {
			throw new RuntimeException(e);
	
		}
	   }
   }
}
