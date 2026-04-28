package com.aj.clgportal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AttachmentDto {
	private Long id;
    private String fileName;
    private String fileUrl;
}
