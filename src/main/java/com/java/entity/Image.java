package com.java.entity;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;

import com.java.dto.ImageDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Entity


@NamedNativeQuery(
name = "find_imagedto",
query =
"select image_code AS imageCode, refer_code AS referCode, image_name AS imageName, image_size AS imageSize, image_path AS imagePath from image where refer_code = :reviewCode",
resultSetMapping = "imagedto"
)
@SqlResultSetMapping(
name = "imagedto",
classes = @ConstructorResult(
targetClass = ImageDto.class,
columns = {
@ColumnResult(name = "imageCode", type =  String.class),
@ColumnResult(name = "referCode", type = String.class),
@ColumnResult(name = "imageName", type =  String.class),
@ColumnResult(name = "imageSize", type = Long.class),
@ColumnResult(name = "imagePath", type =  String.class)
}
)
)












@Table(name = "image")
@Getter 
@Setter
@ToString
public class Image {

	@Id
	//@GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "image_code" , nullable = false)
    private String imageCode;

	
    @Column(name="refer_code" , nullable = false)
	private String referCode;
    
    
    @Column(name="image_name" , nullable = false)
	private String imageName;
    
    
    @Column(name="image_size" , nullable = false)
	private long imageSize;
    
    
    @Column(name="image_path" , nullable = false)
	private String imagePath;

    
    
    // 엔티티 클래스 부분.
    public void updateImage(String imageName, long imageSize, String imagePath){
        this.imageName = imageName;
        this.imageSize = imageSize;
        this.imagePath = imagePath;
    }
    
    
    
    }

