package com.aj.clgportal.dto;

import java.util.List;

import com.aj.enums.ModuleName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeleteRequestDto {
	private ModuleName module;
    private List<Long> ids;
}
