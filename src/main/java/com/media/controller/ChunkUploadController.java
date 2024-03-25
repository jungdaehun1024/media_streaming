package com.media.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
   @PostMapping("/chunk/upload/{key}")
   public ResponseEntity<String>chunkUpload(@RequestPart("chunk")MultipartFile file,
                                                   @RequestParam("chunkNumber")int chunkNumber,
                                                   @RequestParam("totalChunks")int totalChunks,
                                                   @PathVariable("key") String key)throws IOException{
      boolean isDone = chunkUploadService.chunkUpload(file, chunkNumber, totalChunks,key);
      
      return isDone ?
            ResponseEntity.ok("File uploaded successfully"):
            ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).build();   
      
   }
   
   @ResponseBody
   @GetMapping("/chunk/upload/{key}")
   public ResponseEntity<?>getLastChunkNumber(@PathVariable("key") String key){
      return ResponseEntity.ok(chunkUploadService.getLastChunkNumber(key));
   }
}
