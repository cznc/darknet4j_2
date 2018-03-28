package cn.drknt.lib;

public enum Data_type {
	CLASSIFICATION_DATA("classification_data"),
	 DETECTION_DATA("detection_data"),
	 CAPTCHA_DATA("captcha_data"),
	 REGION_DATA("REGION_DATA"),
	 IMAGE_DATA("image_data"),
	 COMPARE_DATA("compare_data"),
	 WRITING_DATA("writing_data"),
	 SWAG_DATA("swag_data"),
	 TAG_DATA("swag_data"),
	 OLD_CLASSIFICATION_DATA("old_classification_data"),
	 STUDY_DATA("study_data"),
	 DET_DATA("det_data"),
	 SUPER_DATA("super_data"),
	 LETTERBOX_DATA("letterbox_data"),
	 REGRESSION_DATA("regression_data"),
	 SEGMENTATION_DATA("segmentation_data"),
	 INSTANCE_DATA("instance_data");

	private String name; 
	private Data_type(String name) {  
        this.name = name;  
    }
	public String getName() {  
        return name;  
    }  
    public void setName(String name) {  
        this.name = name;  
    }
}
