package com.mgcss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MgcssTrackGrupo4Application {

	public static void main(String[] args) {
		SpringApplication.run(MgcssTrackGrupo4Application.class, args);

		if (5>2){
			System.out.println("Hola carcola");
		} else if(true == false){
			System.out.println("adios caracol");
		}else{
			System.out.println("Hole caracole");
		}
	}

}
