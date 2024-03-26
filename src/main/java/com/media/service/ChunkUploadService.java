package com.media.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.UUID;
import java.util.stream.Stream;

@Slf4j
@Service
public class ChunkUploadService {
    public boolean chunkUpload(MultipartFile file, int chunkNumber, int totalChunks, String key) throws IOException {
    	  //파일 업로드 위치
        String uploadDir = "C:\\workspace\\media_streaming\\src\\main\\resources\\static\\video";
        
        String tempDir = "C:\\workspace\\media_streaming\\src\\main\\resources\\static\\video\\" + key;

        //dir: 인자로 받는 경로를 가진 파일을 나타내는 인스턴스가 된다.
        File dir = new File(tempDir);
        if (!dir.exists()) {
            dir.mkdirs(); //디렉토리가 존재하지 않으면 생성
        }

        //임시 저장 파일 이름
        String filename = file.getOriginalFilename() + ".part" + chunkNumber;

        //tempDir디렉토리에 파일(fliename)경로가 반환된다.
        //"C:\\workspace\\media_streaming\\src\\main\\resources\\static\\video\\파일명.part1";
        Path filePath = Paths.get(tempDir, filename);
        // 경로(filePath)에 데이터(file.getBytes())를 쓴다.
        Files.write(filePath, file.getBytes());

        //마지막 조각이 전송된 경우
        if (chunkNumber == totalChunks-1) {
        	
        	//파일 이름을 '.'을 기준으로 분할해서 저장 (정규표현식)
        	//[0]:파일 이름 , [1]:확장자
            String[] split = file.getOriginalFilename().split("\\.");
            
            //UUID와 확장자를 결합해 새로운 파일 이름을 만든다.
            String outputFilename = UUID.randomUUID() + "." + split[split.length-1];
           
            //uploadDir 디렉토리 내의 outputFilename 파일의 경로를 나타낸다.
            Path outputFile = Paths.get(uploadDir, outputFilename);
            
            //지정된 경로에 새로운 파일을 생성한다. ex) UUID.mp4
            Files.createFile(outputFile);
            
            for (int i = 0; i < totalChunks; i++) {
            	//tempDir경로에서  file.getOriginalFilename() + ".part" + i 파일 위치가 chunkFile에 담긴다.
                Path chunkFile = Paths.get(tempDir, file.getOriginalFilename() + ".part" + i);
                
                //  chunkFile경로의 모든 바이트를 읽어와서 디렉토리 (outputFile)의 새파일 끝에 추가된다.
                Files.write(outputFile, Files.readAllBytes(chunkFile), StandardOpenOption.APPEND);
            }
            
            //tempDir경로의 파일 삭제 
            deleteDirectory(Paths.get(tempDir));
            log.info("File uploaded successfully");
            return true;
        } else {
            return false;
        }
    }

    // 주어진 디렉토리와 그 하위 디렉토리 및 파일 삭제 메서드 
    private void deleteDirectory(Path directory) throws IOException {
    	//Files.walk(): 주어진 디렉토리 아래의 모든 하위 경로 반환하는 스트림 생성 
        try (Stream<Path> walk = Files.walk(directory)){
        	//map():각 경로를 파일 객체로 변환한다.   (메서드 레퍼런스 사용해서 Path인터페이스의 toFile참조)
        	//forEach(): 스트림의 각 요소에접근해 파일을 삭제한다.
            walk.map(Path::toFile).forEach(File::delete);
        }
        //주어진 디렉토리 삭제
        Files.delete(directory);
    }

    //디렉토리 내  파일 갯수를 파악해 마지막 청크번호를 반환 
    public int getLastChunkNumber(String key) {
    	//video디렉토리에 key값과 일치하는 하위 디렉토리 경로가 생성
        Path temp = Paths.get("video", key);
        
        //temp가 가리키는 경로에 있는 파일과 디렉토리의 이름을 문자열 배열로 반환한다.
        String[] list = temp.toFile().list();
        
        //list배열이 null이면 디렉토리에는 파일이 없는걸로 간주해 0반환
        //그렇지않으면 파일의 개수에서 현재디렉토리와 상위 디렉토리를 제외한 값을 반환한다.
        return list == null ? 0 : Math.max(list.length-2, 0);
    }
}