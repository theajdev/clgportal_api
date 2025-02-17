package com.aj.clgportal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aj.clgportal.dto.ApiResponse;
import com.aj.clgportal.dto.YearsDto;
import com.aj.clgportal.service.YearsService;

@RestController
@RequestMapping("/api/years")
public class YearsController {
	
	@Autowired
	YearsService yearsService;
	
	@PostMapping("/")
	public ResponseEntity<YearsDto> newYears(@RequestBody YearsDto yearsDto){
		YearsDto newYear = yearsService.newYear(yearsDto);
		return new ResponseEntity<YearsDto>(newYear,HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<YearsDto> updateYears(@RequestBody YearsDto yearsDto,@PathVariable long id){
		YearsDto updateYear = yearsService.updateYear(yearsDto, id);
		return ResponseEntity.ok(updateYear);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse> deleteYears(@PathVariable long id){
		yearsService.deleteYear(id);
		return new ResponseEntity<ApiResponse>(new ApiResponse("Year deleted successfully.", true),HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<YearsDto> getYearById(@PathVariable long id){
		YearsDto yearById = yearsService.getYearById(id);
		return new ResponseEntity<YearsDto>(yearById,HttpStatus.OK);
	}
	
	@GetMapping("/")
	public ResponseEntity<List<YearsDto>> getAllYears(){
		List<YearsDto> list = yearsService.getAllYears();
		return ResponseEntity.ok(list);
	}
	
	
	
}
