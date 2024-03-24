package com.media.controller.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.media.service.BasicService;

import lombok.RequiredArgsConstructor;
/**
 * @RequiredArgsConstructor
 * 해당 클래스에 final로 선언된 필드를 이용해 생성자를 자동으로 생성해준다.(생성자 의존성주입)
 * 
 * @RequestPart 
 *  StandardServletMultipartResolver가 멀티파트 필드에서
 *  파일데이터 추출해 MultipartFile객체에 담아준다.
 *  이때 HTTP요청 멀티파트의 필드 중 이름이  해당 어노테이션으로 주는 문자열이 일치해야한다.
 *  
 *  @RequestParam
 *   HTTP요청ㅇ의 쿼리 매개변수를 받아오는 용도로도 사용되지만 
 *   HTTP POST요청 본문에서도 사용할 수 있다.
 *   이 경우 HTTP body에서 해당 이름을 가진 데이터를 찾아와서 메서드 파라미터에 바인딩
 *   
 *   @ResponseBody 
 *   Http response 본문으로 반환값을 전송한다.
 */
@RequiredArgsConstructor
@RestController
public class BasicApiController {

	private final BasicService basicService;
	
	@ResponseBody 
	@PostMapping("/basic")
	public String saveFile(@RequestPart("file")MultipartFile file,
								 	@RequestParam("desc")String description) {
		
		
		basicService.saveFile(file);
		return "업로드 성공!! - 파일이름:" +file.getOriginalFilename() + ", 파일 설명:" + description;
	}
}
