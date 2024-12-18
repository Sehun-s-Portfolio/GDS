// JavaScript to show/hide the navbar on scroll
var prevScrollPos = window.pageYOffset;
var navbar = document.querySelector('.navbar');

window.onscroll = function () {
    var currentScrollPos = window.pageYOffset;
    if (prevScrollPos > currentScrollPos) {
        // Show the navbar
        navbar.style.top = '0';
    } else {
        // Hide the navbar
        navbar.style.top = '-100px'; // Adjust the value as needed
    }
    prevScrollPos = currentScrollPos;

    // Add or remove the bg-white class based on scroll position
    if (currentScrollPos > 100) { // Adjust the scroll position threshold as needed
        navbar.classList.add('bg-white'); // Add bg-white class
    } else {
        navbar.classList.remove('bg-white'); // Remove bg-white class
    }
}

$(document).ready(function () {
    /** 메뉴에 호버 되는 카테고리 리스트 **/
    fetch("/gds/category", {
        method: "GET",
        headers: {
            "Content-Type": "application/json"
        }
    })
        .then(response => response.json())
        .then((data) => {
            categorys = data;

            categorys.forEach(element => {
                let html = '';
                let adminHtml = '';

                html += `
                        <li>
                                <button type="button" onclick="location.href='/gds/product/page/${element.categoryName}'"  class="dropdown-item" aria-expanded="false">
                                    ${element.categoryName}
                                </button>
                        </li>`

                adminHtml += `
                        <li>
                                <button type="button" onclick="location.href='/gds/admin/product/page/${element.categoryName}'"  class="dropdown-item" aria-expanded="false">
                                    ${element.categoryName}
                                </button>
                        </li>`

                if (element.orgCategoryId === 1) {
                    $('.ledHouseLightList').append(html)//화면에 출력
                    $('.adminLedHouseLightList').append(adminHtml)//화면에 출력
                } else if (element.orgCategoryId === 2) {
                    $('.rdPanelList').append(html)//화면에 출력
                    $('.adminRdPanelList').append(adminHtml)//화면에 출력
                } else if (element.orgCategoryId === 3) {
                    $('.dmxSplitterLIst').append(html)//화면에 출력
                    $('.adminDmxSplitterLIst').append(adminHtml)//화면에 출력
                } else if (element.orgCategoryId === 4) {
                    $('.dmxControllerList').append(html)//화면에 출력
                    $('.adminDmxControllerList').append(adminHtml)//화면에 출력
                } else if (element.orgCategoryId === 5) {
                    $('.theatricalLightingList').append(html)//화면에 출력
                    $('.adminTheatricalLightingList').append(adminHtml)//화면에 출력
                }
            });

        });

    // 프로덕트 개수를 세어 마지막에 노출되는 프로덕트를 제외한 나머지 프로덕트들에게 밑줄 스타일 먹이기
    var productLIst = document.getElementsByClassName("productMain");

    for (var i = 0; i < productLIst.length; i++) {
        if (i === productLIst.length - 1) {
            var productIdSection = productLIst[i].id;
            document.getElementById(productLIst[i].id).style.borderBottomStyle = "hidden";
        }
    }

});

function borderSetting(productIdSection) {
    const prd = document.getElementById(productIdSection);

    document.getElementById(productIdSection).style.borderBottomStyle = "solid 2px";
}

// 이미지 추가 하여 수정 시 이벤트 리스너를 동작시키기 위해 따로 확인하여 관리할 변수
var selectProductId = "";

// 업데이트 섹션 노출 / 미노출 전환 확인용 카운트 변수
var buttonCount = 0;

var reAddPicId = '';

// 업데이트 버튼 클릭 시 동작
function updateButton(productId) {
    selectProductId = productId;
    reAddPicId = 'reAddPic' + productId;
    callUpdateUseImages(productId);

    var prevProductName = "productName" + productId;
    var prevDescript = "descript" + productId;
    var prevSpec = "spec" + productId;
    var prevFileImg = "fileImg" + productId;
    var prevFileName = "fileName" + productId;
    var prevPdfFile = "pdfFile" + productId;
    var prevImageFiles = "imageFiles" + productId;

    var nowProductName = "reUploadProductName" + productId;
    var nowDescript = "reUploadDescript" + productId;
    var nowSpec = "reUploadSpec" + productId;
    var nowFile = "reUploadFile" + productId;
    var nowBtn = "btn" + productId;
    var nowReAddPicBtn = 'reAddPic' + productId;
    var nowImageReuploadBtn = "reUploadImageFile" + productId;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 수정 버튼 클릭 시 초기에 노출될 기존 제품 명 텍스트 호출
    var updateNowProductName = document.getElementById(prevProductName);
    var productNameText = updateNowProductName.getElementsByClassName("realProductName" + productId).item(0).textContent;

    // 수정할 기존 등록된 제품 명 불러오기
    document.getElementById('newUploadProductRealName' + productId).innerHTML = productNameText;
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 수정 버튼 클릭 시 초기에 노출될 기존 설명 텍스트 호출
    var updateNowDescript = document.getElementById(prevDescript);
    let updateNowDescriptList = [];
    updateNowDescriptList = updateNowDescript.getElementsByClassName("realNowDescript" + productId);

    var descriptText = "";

    for(var k = 0 ; k < updateNowDescriptList.length ; k++){
        descriptText += updateNowDescriptList[k].textContent;
    }

    // 수정할 기존 등록된 제품 설명들을 불러오기
    document.getElementById('newUploadDescript' + productId).innerHTML = descriptText;
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 수정 버튼 클릭 시 초기에 노출될 기존 스펙 텍스트 호출
    var updateNowSpec = document.getElementById(prevSpec);
    let updateNowSpecList = [];
    updateNowSpecList = updateNowSpec.getElementsByClassName("realNowSpec" + productId);

    var specText = "";

    for(var i = 0 ; i < updateNowSpecList.length ; i++){
        specText += updateNowSpecList[i].textContent;
    }

    // 수정할 기존 등록된 스펙들을 불러오기
    document.getElementById('reUploadSpec' + productId).innerHTML = specText;
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////

    document.getElementById(prevProductName).classList.add('nowProduct');
    document.getElementById(prevDescript).classList.add('nowProduct');
    document.getElementById(prevSpec).classList.add('nowProduct');
    document.getElementById(prevFileImg).classList.add('nowProduct');
    document.getElementById(prevFileName).classList.add('nowProduct');
    document.getElementById(prevPdfFile).classList.add('nowProduct');
    document.getElementById(prevImageFiles).classList.add('nowProduct');

    document.getElementById(nowProductName).classList.add('nowProduct');
    document.getElementById(nowDescript).classList.add('nowProduct');
    document.getElementById(nowSpec).classList.add('nowProduct');
    document.getElementById(nowFile).classList.add('nowProduct');
    document.getElementById(nowBtn).classList.add('nowProduct');
    document.getElementById(nowReAddPicBtn).classList.add('nowProduct');
    document.getElementById(nowImageReuploadBtn).classList.add('nowProduct');

    if (buttonCount === 0) {
        buttonCount = 0;
        buttonCount += 1;

        var allProduct = document.querySelectorAll(".productMain");

        for (let v = 0; v < allProduct.length; v++) {
            console.log("모든 프로덕트의 id : " + allProduct[v].id)

            if (allProduct[v].id !== productId) {
                document.getElementById("productName" + allProduct[v].id).classList.add('un-use');
                document.getElementById("descript" + allProduct[v].id).classList.add('un-use');
                document.getElementById("spec" + allProduct[v].id).classList.add('un-use');
                document.getElementById("fileImg" + allProduct[v].id).classList.add('un-use');
                document.getElementById("fileName" + allProduct[v].id).classList.add('un-use');
                document.getElementById("pdfFile" + allProduct[v].id).classList.add('un-use');
                document.getElementById("imageFiles" + allProduct[v].id).classList.add('un-use');

                document.getElementById("reUploadProductName" + allProduct[v].id).classList.add('un-use');
                document.getElementById("reUploadDescript" + allProduct[v].id).classList.add('un-use');
                document.getElementById("reUploadSpec" + allProduct[v].id).classList.add('un-use');
                document.getElementById("reUploadFile" + allProduct[v].id).classList.add('un-use');
                document.getElementById("btn" + allProduct[v].id).classList.add('un-use');
                document.getElementById("reAddPic" + allProduct[v].id).classList.add('un-use');
                document.getElementById("reUploadImageFile" + allProduct[v].id).classList.add('un-use');

                $(".productName.un-use").css("display", "block");
                $(".productDescript.un-use").css("display", "block");
                $(".productSpec.un-use").css("display", "block");
                $(".fileImg.un-use").css("display", "block");
                $(".fileName.un-use").css("display", "block");
                $(".pdfFile.un-use").css("display", "flex");
                $(".imageFiles.un-use").css("display", "block");
                $(".imageFiles.un-use .useImageWrapper").css("display", "block");

                $(".reUploadProductName.un-use").css("display", "none");
                $(".reUploadSpec.un-use").css("display", "none");
                $(".reUploadDescript.un-use").css("display", "none");
                $(".confirmBtn.un-use").css("display", "none");
                $(".reUploadFilePart.un-use").css("display", "none");
                $(".reUploadImageFilePart.un-use").css("display", "none");
            }

            document.getElementById("productName" + allProduct[v].id).classList.remove('un-use');
            document.getElementById("descript" + allProduct[v].id).classList.remove('un-use');
            document.getElementById("spec" + allProduct[v].id).classList.remove('un-use');
            document.getElementById("fileImg" + allProduct[v].id).classList.remove('un-use');
            document.getElementById("fileName" + allProduct[v].id).classList.remove('un-use');
            document.getElementById("pdfFile" + allProduct[v].id).classList.remove('un-use');
            document.getElementById("imageFiles" + allProduct[v].id).classList.remove('un-use');

            document.getElementById("reUploadProductName" + allProduct[v].id).classList.remove('un-use');
            document.getElementById("reUploadDescript" + allProduct[v].id).classList.remove('un-use');
            document.getElementById("reUploadSpec" + allProduct[v].id).classList.remove('un-use');
            document.getElementById("reUploadFile" + allProduct[v].id).classList.remove('un-use');
            document.getElementById("btn" + allProduct[v].id).classList.remove('un-use');
            document.getElementById("reAddPic" + allProduct[v].id).classList.remove('un-use');
            document.getElementById("reUploadImageFile" + allProduct[v].id).classList.remove('un-use');
        }

        // 등록된 내용 미노출
        $(".productName.nowProduct").css("display", "none");
        $(".productDescript.nowProduct").css("display", "none");
        $(".productSpec.nowProduct").css("display", "none");
        $(".fileImg.nowProduct").css("display", "none");
        $(".fileName.nowProduct").css("display", "none");
        $(".pdfFile.nowProduct").css("display", "none");
        $(".imageFiles.nowProduct").css("display", "none");
        $(".imageFiles.nowProduct .useImageWrapper").css("display", "none");

        // 수정하고자 하는 프로덕트만 수정 폼 노출 설정
        $(".reUploadProductName.nowProduct").css("display", "block");
        $(".reUploadSpec.nowProduct").css("display", "block");
        $(".reUploadDescript.nowProduct").css("display", "block");
        $(".confirmBtn.nowProduct").css("display", "block");
        $(".reAddPic.nowProduct").css("display", "block");
        $(".reUploadFilePart.nowProduct").css("display", "block");
        $(".reUploadImageFilePart.nowProduct").css("display", "flex");


    } else if (buttonCount !== 0) {
        buttonCount -= 1;

        $(".productName.nowProduct").css("display", "block");
        $(".productDescript.nowProduct").css("display", "block");
        $(".productSpec.nowProduct").css("display", "block");
        $(".fileImg.nowProduct").css("display", "block");
        $(".fileName.nowProduct").css("display", "block");
        $(".pdfFile.nowProduct").css("display", "flex");
        $(".imageFiles.nowProduct").css("display", "block");
        $(".imageFiles.nowProduct .useImageWrapper").css("display", "block");

        $(".reUploadProductName.nowProduct").css("display", "none");
        $(".reUploadSpec.nowProduct").css("display", "none");
        $(".reUploadDescript.nowProduct").css("display", "none");
        $(".confirmBtn.nowProduct").css("display", "none");
        $(".reAddPic.nowProduct").css("display", "none");
        $(".reUploadFilePart.nowProduct").css("display", "none");
        $(".reUploadImageFilePart.nowProduct").css("display", "none");

    }

    document.getElementById(prevProductName).classList.remove('nowProduct');
    document.getElementById(prevDescript).classList.remove('nowProduct');
    document.getElementById(prevSpec).classList.remove('nowProduct');
    document.getElementById(prevFileImg).classList.remove('nowProduct');
    document.getElementById(prevFileName).classList.remove('nowProduct');
    document.getElementById(prevPdfFile).classList.remove('nowProduct');
    document.getElementById(prevImageFiles).classList.remove('nowProduct');

    document.getElementById(nowProductName).classList.remove('nowProduct');
    document.getElementById(nowDescript).classList.remove('nowProduct');
    document.getElementById(nowSpec).classList.remove('nowProduct');
    document.getElementById(nowFile).classList.remove('nowProduct');
    document.getElementById(nowBtn).classList.remove('nowProduct');
    document.getElementById(nowReAddPicBtn).classList.remove('nowProduct');
    document.getElementById(nowImageReuploadBtn).classList.remove('nowProduct');
}

let productImg = '';
let source = '';

function getSrc(src) {
    source = src;
}

function getImgId(productImgId) {
    var idValue = "thumbnail";
    productImg = idValue + productImgId;

    document.getElementById(productImg).src = source;
}

// 프로덕트 삭제
function deleteButton(productId) {

    if (confirm("정말 해당 제품을 삭제하시겠습니까?\n삭제된 내용은 복구 되지않습니다.")) {
        $.ajax({
            type: "delete",
            url: "/gds/admin/delete/" + productId,
            headers: {'Content-Type': 'application/json'},

            success: function (data) {
                document.location.reload();
            },

            error: function (jqXHR) {
                document.location.reload();
            }
        });

    } else {

    }

}

// 새로고침
function refreshPage() {
    location.reload();
}

// 첫 번째 드롭다운 토글
$('[data-bs-toggle="dropdown"]').on('click', function (e) {
    e.preventDefault();
    var parentDropdown = $(this).closest('.dropdown');
    parentDropdown.find('.dropdown-menu').first().toggle();

    // 다른 드롭다운 닫기
    $('.dropdown').not(parentDropdown).find('.dropdown-menu').hide();
});

// 관리자 접근 프로덕트 카테고리
$('[data-bs-toggle="adminDropdown1"]').on('click', function (e) {
    e.preventDefault();
    $('#adminDropdown1').toggle();
    // 다른 드롭다운 닫기
    $('#adminDropdown2, #adminDropdown3, #adminDropdown4, #adminDropdown5').hide();
});

$('[data-bs-toggle="adminDropdown2"]').on('click', function (e) {
    e.preventDefault();
    $('#adminDropdown2').toggle();
    // 다른 드롭다운 닫기
    $('#adminDropdown1, #adminDropdown3, #adminDropdown4, #adminDropdown5').hide();
});

$('[data-bs-toggle="adminDropdown3"]').on('click', function (e) {
    e.preventDefault();
    $('#adminDropdown3').toggle();
    // 다른 드롭다운 닫기
    $('#adminDropdown1, #adminDropdown2, #adminDropdown4, #adminDropdown5').hide();
});

$('[data-bs-toggle="adminDropdown4"]').on('click', function (e) {
    e.preventDefault();
    $('#adminDropdown4').toggle();
    // 다른 드롭다운 닫기
    $('#adminDropdown1, #adminDropdown2, #adminDropdown3, #adminDropdown5').hide();
});

$('[data-bs-toggle="adminDropdown5"]').on('click', function (e) {
    e.preventDefault();
    $('#adminDropdown5').toggle();
    // 다른 드롭다운 닫기
    $('#adminDropdown1, #adminDropdown2, #adminDropdown3, #adminDropdown4').hide();
});


// 일반 고객 접근 프로덕트 카테고리
$('[data-bs-toggle="userDropdown1"]').on('click', function (e) {
    e.preventDefault();
    $('#userDropdown1').toggle();
    // 다른 드롭다운 닫기
    $('#userDropdown2, #userDropdown3, #userDropdown4, #userDropdown5').hide();
});

$('[data-bs-toggle="userDropdown2"]').on('click', function (e) {
    e.preventDefault();
    $('#userDropdown2').toggle();
    // 다른 드롭다운 닫기
    $('#userDropdown1, #userDropdown3, #userDropdown4, #userDropdown5').hide();
});

$('[data-bs-toggle="userDropdown3"]').on('click', function (e) {
    e.preventDefault();
    $('#userDropdown3').toggle();
    // 다른 드롭다운 닫기
    $('#userDropdown1, #userDropdown2, #userDropdown4, #userDropdown5').hide();
});

$('[data-bs-toggle="userDropdown4"]').on('click', function (e) {
    e.preventDefault();
    $('#userDropdown4').toggle();
    // 다른 드롭다운 닫기
    $('#userDropdown1, #userDropdown2, #userDropdown3, #userDropdown5').hide();
});

$('[data-bs-toggle="userDropdown5"]').on('click', function (e) {
    e.preventDefault();
    $('#userDropdown5').toggle();
    // 다른 드롭다운 닫기
    $('#userDropdown1, #userDropdown2, #userDropdown3, #userDropdown4').hide();
});

// 문서 클릭 시 드롭다운 닫기
$(document).on('click', function (e) {
    if ($(e.target).closest('.dropdown').length === 0) {
        $('.dropdown-menu').hide();
    }
});


var moreButtons = document.querySelectorAll('button[data-toggle="more-button"]');

function toggleMore(button) {

    //var parent = button.closest('.productMain');
    var parent = document.getElementById(button).closest('.productMain');

    if (parent) {
        var listStyle = parent.querySelector('.listStyle');
        var blur = parent.querySelector('.blur');
        if (listStyle && blur) {
            if (listStyle.style.maxHeight === '500px' || listStyle.style.maxHeight === '500px') {
                listStyle.style.maxHeight = '10000px';
                blur.style.margin = '-20px';
                //   blur.style.display = 'none';
                //button.textContent = '접기'; // 버튼 텍스트 변경
                document.getElementById(button).textContent = '접기';
            } else {
                listStyle.style.maxHeight = '500px';
                blur.style.display = 'flex';
                //button.textContent = '더보기'; // 버튼 텍스트 변경
                document.getElementById(button).textContent = '더보기';
            }
        }
    }
}

moreButtons.forEach(function (button) {
    button.addEventListener('click', function () {
        toggleMore(button);
    });
});

// 이미지 선택 시 모달로 노출되는 이미지
function changeImg(source) {
    document.getElementById('imgModal').src = source;
}

// 카테고리 생성 시 대분류 카테고리 값을 지정해주기 위한 함수
var selectCategory = function (value) {
    $("#orgCategory").val(value);
}

// 카테고리 수정 시 대분류 카테고리 값을 지정해주기 위한 함수
var selectOrgUpdateCategory = function (value) {
    if (value === "0") {
        alert("대분류 카테고리를 설정해주십시오.");
    } else {
        $("#updateOrgCategory").val(value);
    }
}


// 카테고리 삭제
function deleteCategoryButton(categoryId) {
    if (confirm("정말 현재 카테고리를 삭제하시겠습니까?\n삭제된 내용은 복구 되지않으며, 카테고리에 속한 프로덕트들 또한 삭제됩니다.")) {
        $.ajax({
            type: "delete",
            url: "/gds/category/delete/" + categoryId,
            //headers: {'Content-Type': 'application/json'},

            success: function (data) {
                console.log("delete success");
                document.location.href = "/";
            },

            error: function (jqXHR) {
                console.log("delete failed");
                document.location.href = "/";
            }
        });
    } else {

    }
}


// 기존에 존재하던 활용 이미지들의 id 리스트
let deleteImgIdList = [];

// 이미지 미리보기 삭제
function deleteImg(productId, imgId, mediaId) {
    var picSection = 'pic' + productId + '' + imgId;
    const imageList = document.getElementById('imageList' + productId);

    if (imageList.getElementsByClassName("useImages").length - 1 === 0) {
        alert("이미지는 반드시 1장 이상이 존재해야 합니다.")
    } else {
        imageList.removeChild(document.getElementById(picSection));
        deleteImgIdList.push(mediaId);

        var deleteImgs = "";

        for (var i = 0; i < deleteImgIdList.length; i++) {
            deleteImgs += deleteImgIdList.at(i);

            if (i !== deleteImgIdList.length - 1) {
                deleteImgs += "!!";
            }
        }

        document.getElementById("deleteImgList" + productId).value = deleteImgs;
    }
}

// 기존의 활용 이미지들 조회
function callUpdateUseImages(productId) {
    $.ajax({
        type: "get",
        url: "/gds/product/images/" + productId,
        headers: {'Content-Type': 'application/json'},
        success: function (data) {

            var images = data;
            let html = ''; // 기존 이미지들의 html 구조용

            var imageListSection = 'imageList' + images[0].contentId; // 기존 이미지들이 노출될 섹션
            const imgParent = document.getElementById(imageListSection);

            // 리스트업 된 이미지들 노출 제외
            while (imgParent.firstChild) {
                imgParent.firstChild.remove();
            }

            document.getElementById(imageListSection).classList.add('nowProduct');

            html += `
                    <input type="hidden" name="deleteImgList" class="deleteImgList" id="deleteImgList${productId}">
                `

            for (let i = 0; i < images.length; i++) {
                html += `
                        <li style="position: relative" id="pic${images[i].contentId}${i}" class="useImages">
                             <img src="${images[i].mediaUploadUrl}" style="max-width: 100px; width: 100px; height: 100px; margin-right: 10px; margin-bottom: 10px;">
                             <button type="button" onclick="deleteImg(${images[i].contentId}, ${i}, ${images[i].mediaId})" style="position: absolute; right: 13px; top:1px; margin: 0; font-size: 15px; background-color: transparent; color: red; padding: 0; border: none; font-weight: bold;" >
                                <span>
                                   X
                                </span>
                             </button>
                          </li>`
            }

            $('.imageList.nowProduct').append(html)//화면에 출력
            document.getElementById(imageListSection).classList.remove('nowProduct');
        },

        error: function (jqXHR) {
            console.log("프로덕트 이미지들 조회 실패");

        }
    });

}

// 새로 추가한 이미지들에 관한 업로드 취소 함수
function deleteNewImg(imgCnt) {
    const imageList = document.getElementById('imageList' + selectProductId);

    if (imageList.getElementsByClassName("useImages").length - 1 === 0) {
        alert("이미지는 반드시 1장 이상이 존재해야 합니다.")
    }else{
        var dataTranster = new DataTransfer();
        var fileList = document.getElementById('reAddPic' + selectProductId).files;

        for (var i = 0; i < fileList.length; i++) {
            if (i !== imgCnt) {
                dataTranster.items.add(fileList[i]);
            }
        }

        document.getElementById('reAddPic' + selectProductId).files = dataTranster.files;
        console.log(dataTranster);

        imageList.removeChild(document.getElementById('newPic' + selectProductId + '' + imgCnt));
    }
}

// 수정 시 최종적으로 백단으로 보내게 될 잔여 파일들을 가지고 있는 dataTransfer
const updateDataTransfer = new DataTransfer();

// 새로 추가할 이미지들에 대한 업로드
function inputNewImg(input) {
    const files = input.files;

    var reAddPicId = 'reAddPic' + selectProductId;
    let prevImages = document.getElementById(reAddPicId).files;

    if(prevImages != null && prevImages.length > 0){
        for(var j = 0; j < files.length ; j++){
            updateDataTransfer.items.add(files[j]);
        }
        document.getElementById(reAddPicId).files = updateDataTransfer.files;
    }else{
        for(var k = 0; k < files.length ; k++){
            updateDataTransfer.items.add(files[k]);
        }
        document.getElementById(reAddPicId).files = updateDataTransfer.files;
    }

    var imageListSection = 'imageList' + selectProductId; // 기존 이미지들이 노출될 섹션
    document.getElementById(imageListSection).classList.add('nowProduct');

    var html = '';

    for (let i = 0; i < files.length; i++) {
        let src = URL.createObjectURL(files[i]);

        html += `
                        <li style="position: relative" id="newPic${selectProductId}${i}" class="useImages">
                             <img src="${src}" style="max-width: 100px; width: 100px; height: 100px; margin-right: 10px; margin-bottom: 10px;">
                             <button type="button" onclick="deleteNewImg(${i})" style="position: absolute; right: 13px; top:1px; margin: 0; font-size: 15px; background-color: transparent; color: red; padding: 0; border: none; font-weight: bold;" >
                                <span>
                                   X
                                </span>
                             </button>
                          </li>`
    }

    $('.imageList.nowProduct').append(html)//화면에 출력
    document.getElementById(imageListSection).classList.remove('nowProduct');

}

const input = document.getElementById('addPic');
const showImgs = document.getElementById('showImgs');

document.getElementById('addPic').addEventListener('input', (event) => {
    const files = event.target.files;
    showImgs.textContent = Array.from(files).map(file => file.name).join('\n')
});


document.getElementById('uploadKor').addEventListener('input', (event) => {
    const showKorImgs = document.getElementById('showKorFile');
    const files = event.target.files;
    showKorImgs.textContent = Array.from(files).map(file => file.name).join('\n')
});

document.getElementById('uploadEng').addEventListener('input', (event) => {
    const showEngImgs = document.getElementById('showEngFile');
    const files = event.target.files;
    showEngImgs.textContent = Array.from(files).map(file => file.name).join('\n')
});