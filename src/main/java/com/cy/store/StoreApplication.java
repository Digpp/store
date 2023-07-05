package com.cy.store;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.util.unit.DataSize;
import org.springframework.util.unit.DataUnit;

import javax.servlet.MultipartConfigElement;


@SpringBootApplication
//MapperScan用于指定当前项目中的Mapper接口路径位置
@MapperScan("com.cy.store.mapper")
public class StoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(StoreApplication.class, args);
	}

	@Bean
	public MultipartConfigElement getMultipartConfigElement(){
		//创建一个配置的工厂对象
		MultipartConfigFactory factory = new MultipartConfigFactory();
		//设置需要创建的对象的相关信息
		factory.setMaxFileSize(DataSize.of(10, DataUnit.MEGABYTES));   //单个文件最大限制
		factory.setMaxRequestSize(DataSize.of(15, DataUnit.MEGABYTES));  //总文件文件最大限制
		//通过工厂类来创建 MultipartConfigElement 对象
		return factory.createMultipartConfig();
	}
}
