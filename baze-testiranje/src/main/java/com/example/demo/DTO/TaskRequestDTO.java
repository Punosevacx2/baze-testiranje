package com.example.demo.DTO;

import com.example.demo.enumeration.TaskStatuss;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequestDTO {


	private String id;

    private String title;
    private TaskStatuss status;
    
	
	
}
