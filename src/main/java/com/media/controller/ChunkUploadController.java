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
	
	@ResponseBody
	@PostMapping("/chunk/upload")
	public ResponseEntity<String> chunkUpload(@RequestPart("chunk")MultipartFile file,
																	@RequestParam("chunkNumber")int chunkNumber,
																	@RequestParam("totalChunks")int totalChunks)throws IOException{
		
		
		boolean isDone = chunkUploadService.chunkUpload(file, chunkNumber, totalChunks);
		return isDone ? ResponseEntity.ok("File uploaded Successfully"):
								ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).build();
		
	}
}
