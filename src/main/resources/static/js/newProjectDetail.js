$(function () {
    // datepicker 초기화
    $('#datepicker').datepicker();
    //$('#datepicker').datepicker('show');
});

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

//헤더 네비 끝
//슬라이더 시작
$(document).ready(function () {
    var owl = $(".owl-carousel");

    owl.owlCarousel({
        items: 3, // 한 번에 표시할 아이템 수 (3개의 이미지를 보이게 설정)
        loop: true,
        nav: true,
        navText: ["Previous", "Next"],
        margin: 150, // 이미지 간 간격
        stagePadding: 0, // 이미지 양 옆에 추가로 보이는 공간을 0으로 설정
        center: true, // 이미지를 가운데로 정렬
        autoplay: true,
        autoplayTimeout: 5000,
        autoplayHoverPause: true,
        dots: true, // dots 옵션을 true로 설정
        responsive: {
            0: {
                items: 1
            },
            769: {
                items: 2
            },
            1200: {
                items: 3
            }
        }
    });

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

});


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


// 프로젝트 삭제
function deleteProjectButton(projectId) {

    if (confirm("정말 프로젝트를 삭제하시겠습니까?\n삭제된 내용은 복구 되지않습니다.")) {
        $.ajax({
            type: "delete",
            url: "/gds/project/delete/" + projectId,
            headers: {'Content-Type': 'application/json'},

            success: function (data) {
                console.log(data + " : Delete Success");
                document.location.href = "/gds/project";
            },

            error: function (jqXHR) {
                console.log(jqXHR + " : Delete Failed");
                document.location.href = "/gds/project";
            }
        });

    } else {

    }

}


// 기존에 존재하던 활용 이미지들의 id 리스트
let deleteImgIdList = [];

// 이미지 미리보기 삭제
function deleteImg(projectId, imgId, mediaId) {
    var picSection = 'pic' + imgId;
    const imageList = document.getElementById('imageList');

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

        document.getElementById("deleteImgList" + projectId).value = deleteImgs;
    }
}


// 기존의 활용 이미지들 조회
function callUpdateUseImages(projectId) {

    var descriptionKorText = document.getElementById('descriptionKorText');
    var descriptionEngText = document.getElementById('descriptionEngText');

    let descriptionKorTextList = [];
    descriptionKorTextList = descriptionKorText.getElementsByClassName("eachKorText");

    var korInfoText = "";

    for(var k = 0 ; k < descriptionKorTextList.length ; k++){
        korInfoText += descriptionKorTextList[k].textContent;
    }

    // 수정할 기존 등록된 제품 설명들을 불러오기
    document.getElementById('detailInfo').innerHTML = korInfoText;

    let descriptionEngTextList = [];
    descriptionEngTextList = descriptionEngText.getElementsByClassName("eachEngText");

    var engInfoText = "";

    for(var f = 0 ; f < descriptionEngTextList.length ; f++){
        engInfoText += descriptionEngTextList[f].textContent;
    }

    // 수정할 기존 등록된 제품 설명들을 불러오기
    document.getElementById('detailInfoEng').innerHTML = engInfoText;

    $.ajax({
        type: "get",
        url: "/gds/project/images/" + projectId,
        headers: {'Content-Type': 'application/json'},
        success: function (data) {

            var images = data;
            let html = ''; // 기존 이미지들의 html 구조용

            const imgParent = document.getElementById('imageList');

            // 리스트업 된 이미지들 노출 제외
            while (imgParent.firstChild) {
                imgParent.firstChild.remove();
            }

            html += `
                    <input type="hidden" name="deleteImgList" class="deleteImgList" id="deleteImgList${projectId}">
                `

            for (let i = 0; i < images.length; i++) {
                html += `
                        <li style="position: relative" id="pic${i}" class="useImages">
                             <img src="${images[i].mediaUploadUrl}" style="max-width: 100px; width: 100px; height: 100px; margin-right: 10px; margin-bottom: 10px;">
                             <button type="button" onclick="deleteImg(${projectId}, ${i}, ${images[i].mediaId})" style="position: absolute; right: 13px; top:1px; margin: 0; font-size: 15px; background-color: transparent; color: red; padding: 0; border: none; font-weight: bold;" >
                                <span>
                                   X
                                </span>
                             </button>
                          </li>`
            }

            $('.imageList').append(html)//화면에 출력
        },

        error: function (jqXHR) {
            console.log("프로젝트 이미지들 조회 실패");

        }
    });

}


// 새로 추가한 이미지들에 관한 업로드 취소 함수
function deleteNewImg(imgCnt) {
    const imageList = document.getElementById('imageList');

    if (imageList.getElementsByClassName("useImages").length - 1 === 0) {
        alert("이미지는 반드시 1장 이상이 존재해야 합니다.")
    } else {
        var dataTranster = new DataTransfer();
        var fileList = document.getElementById('addPic').files;

        for (var i = 0; i < fileList.length; i++) {
            if (i !== imgCnt) {
                dataTranster.items.add(fileList[i]);
            }
        }

        document.getElementById('addPic').files = dataTranster.files;
        console.log(dataTranster);

        imageList.removeChild(document.getElementById('newPic' + imgCnt));
    }
}

// 업로드 파일들을 유지시키면서 가지고 최종적으로 보낼 파일 리스트를 반영시키기 위한 객체
const updateDataTransfer = new DataTransfer();

// 새로 추가할 이미지들에 대한 업로드
function inputNewImg(input) {
    // 업로드할 파일들
    const files = input.files;

    // id가 addPic인 파일 타입의 input 태그에 업로드 되어있는 상태인 파일들 리스트
    let prevImages = document.getElementById('addPic').files;

    // 만약 기존에 업로드 되어있는 파일들이 존재할 경우
    if(prevImages != null && prevImages.length > 0){
        // 현재 업로드할 파일들을 updateDataTransfer에 추가
        for(var j = 0; j < files.length ; j++){
            updateDataTransfer.items.add(files[j]);
        }

        // id가 addPic인 파일 타입의 input 태그에 업로드 되어있는 상태인 파일들 리스트를
        // 현재 추가하여 넣은 updateDataTransfer의 파일들로 교체(최신화)
        document.getElementById("addPic").files = updateDataTransfer.files;

    // 만약 기존 업로드 파일이 존재하지 않는 초기 상태일 경우
    }else{
        // 현재 업로드할 파일들을 updateDataTransfer에 추가
        for(var k = 0; k < files.length ; k++){
            updateDataTransfer.items.add(files[k]);
        }

        // id가 addPic인 파일 타입의 input 태그에 업로드 되어있는 상태인 파일들 리스트를
        // 현재 추가하여 넣은 updateDataTransfer의 파일들로 교체(최신화)
        document.getElementById("addPic").files = updateDataTransfer.files;
    }

    // 추가한 이미지들을 view 단에서 노출시키기 위한 html 변수
    var html = '';

    // 업로드할 파일들을 조회하면서 형식에 맞게 노출되게끔 html 코드를 구성하여 html 변수에 적재
    for (let i = 0; i < files.length; i++) {
        let src = URL.createObjectURL(files[i]);

        html += `
                        <li style="position: relative" id="newPic${i}" class="useImages">
                             <img src="${src}" style="max-width: 100px; width: 100px; height: 100px; margin-right: 10px; margin-bottom: 10px;">
                             <button type="button" onclick="deleteNewImg(${i})" style="position: absolute; right: 13px; top:1px; margin: 0; font-size: 15px; background-color: transparent; color: red; padding: 0; border: none; font-weight: bold;" >
                                <span>
                                   X
                                </span>
                             </button>
                          </li>`
    }

    // 최종적으로 노출시킬 태그에 append 시킴
    $('.imageList').append(html)//화면에 출력
}


// 수정 시 정합성 검증
function updateInfoCheck() {

    if (document.getElementById('datepicker').value.length !== 10) {
        alert("날짜를 정확하게 명시해야 합니다.");
        return false;
    }

    if (document.getElementById('updateTitleKor').value.length <= 4) {
        alert("한글 타이틀 명이 옳바르게 기입되었는지 확인해주십시오.");
        return false;
    }

    if (document.getElementById('updateLocationKor').value.length <= 2) {
        alert("위치 주소(한글)가 옳바르게 기입되었는지 확인해주십시오.");
        return false;
    }

    if (document.getElementById('updateTitleEng').value.length <= 4) {
        alert("영문 타이틀 명이 옳바르게 기입되었는지 확인해주십시오.");
        return false;
    }

    if (document.getElementById('updateLocationEng').value.length <= 2) {
        alert("위치 주소(영문)가 옳바르게 기입되었는지 확인해주십시오.");
        return false;
    }

    if (document.getElementById('detailInfo').value.length <= 10) {
        alert("상세 정보(한글)는 최소 10자 이상 입력하셔야 합니다.");
        return false;
    }

    if (document.getElementById('detailInfoEng').value.length <= 10) {
        alert("위치 주소(영문)는 최소 10자 이상 입력하셔야 합니다.");
        return false;
    }
}

// 카테고리 생성 시 대분류 카테고리 값을 지정해주기 위한 함수
var selectCategory = function (value) {
    $("#orgCategory").val(value);
}

