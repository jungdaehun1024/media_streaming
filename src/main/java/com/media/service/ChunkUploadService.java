package com.media.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ChunkUploadService {

	public boolean chunkUpload(MultipartFile file , int chunkNumber , int totalChunks) throws IOException {
		//파일 업로드 위치
		String uploadDir = "C:\\workspace\\media_streaming\\src\\main\\resources\\static\\video";
		
		//dir:인자로 받는 경로를 가진 파일을 나타내는 인스턴스가된다.
		File dir = new File(uploadDir);
		if(!dir.exists()) {
			dir.mkdirs(); // 디렉토리가 존재하지 않으면 생성 
		}
		
		//임시 저장 파일 이름 
		String filename = file.getOriginalFilename()+".part"+chunkNumber;
		
		//uploadDir디렉토리에 파일(filename)의 경로가 반환된다.
		Path filePath = Paths.get(uploadDir,filename);
		
		//임시저장
		//경로(filePath)에 데이터(file.getBytes())를 쓴다.
		Files.write(filePath,file.getBytes());
		
		
		//마지막 조각이 전송됏을 경우 
		if(chunkNumber == totalChunks-1) {
			
			//파일이름을 '.'를 기준으로 분할해서 저장 (정규표현식)
			// [0]:파일이름 [1]:txt
			String[] split = file.getOriginalFilename().split("\\.");
			
			//UUID와 파일 확장자(.txt)를 결합해 새로운 파일이름을 만든다.
			String outputFilename = UUID.randomUUID()+"."+split[split.length-1];
			
			//디렉토리에 파일(outputFilename)의 경로가 반환된다.
			Path outputFile = Paths.get(uploadDir,outputFilename);
			
			//지정된 경로에 새로운 파일을 생성한다.  (UUID.확장자명)
			Files.createFile(outputFile); 
			
			for(int i = 0; i< totalChunks;i++) {
				//디렉토리에 "file.getOriginalFilename()+".part"+i"경로 생성
				Path chunkFile = Paths.get(uploadDir,file.getOriginalFilename()+".part"+i);
				
				//경로(outputFile)에  chunkFile에서 읽은 바이트 배열을 outputFile에 추가한다.(기존파일에 새로운 내용추가)
				Files.write(outputFile, Files.readAllBytes(chunkFile),StandardOpenOption.APPEND);
				
				//합친 후 삭제
				Files.delete(chunkFile);
			}
			log.info("File uploaded successfully");
			return true;
		}else {
			return false;
		}	
	}
}
