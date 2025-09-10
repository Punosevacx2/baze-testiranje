package com.example.demo.DTO;

import java.time.LocalDate;

import com.example.demo.enumeration.TaskStatuss;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponseDTO {

	private String id;

    
    private String title;
    
    private String description; 
    
    private TaskStatuss status;           
    
    private LocalDate deadline;
}
