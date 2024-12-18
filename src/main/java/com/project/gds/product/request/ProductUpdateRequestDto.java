package com.project.gds.product.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductUpdateRequestDto {
    private String reUploadProductRealName;
    private String reUploadProductDescript;
    private String reUploadSpec;
    private String deleteImgList; // 기존 이미지들 중 삭제할 이미지들의 id를 합친 값
}
