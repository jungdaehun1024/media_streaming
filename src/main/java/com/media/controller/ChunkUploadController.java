package com.media.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.media.service.ChunkUploadService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChunkUploadController {

	//의존성주입
	private final ChunkUploadService chunkUploadService;
	
	@GetMapping("/chunk")
	public String chunkUploadPage() {
		return "chunk";
	}
	
	//클라이언트로부터 받는 HTTP Body에서 이름이 일치하는 값을 추출해서 매핑 
	@ResponseBody
	@PostMapping("/chunk/upload")
	public ResponseEntity<String> chunkUpload(@RequestPart("chunk")MultipartFile file,
																	@RequestParam("chunkNumber")int chunkNumber,
																	@RequestParam("totalChunks")int totalChunks)throws IOException{
		
		//chunkUpload가 T인지 F인지 저장 
		boolean isDone = chunkUploadService.chunkUpload(file, chunkNumber, totalChunks);
		// true이면 ok(200)과 함께 문자열 보내줌 (청크파일이 끝임을 알려준다.)
		return isDone ? ResponseEntity.ok("File uploaded Successfully"):
								//HttpStatus.PARTIAL_CONTENT는 206과 동등한 상태코드를 나타냄 
								// .build()로 최종적으로 ResponseEntity객체를 만들어야 클라이언트에게 실제로 응답이 전송됨
								ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).build();
		
	}
}
