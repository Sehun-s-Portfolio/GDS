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

// 업로드 파일들을 유지시키면서 가지고 최종적으로 보낼 파일 리스트를 반영시키기 위한 객체
const updateDataTransfer = new DataTransfer();

// 사진 업로드 시 보여질 이미지 미리보기 배열
document.getElementById('addPic').addEventListener('change', function () {
    const imageList = document.getElementById('imageList');
    const files = this.files;

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

    for (let i = 0; i < files.length; i++) {
        const file = files[i];
        const li = document.createElement('li');
        li.id = 'img' + i;
        li.style.position = 'relative';
        li.className = "useImages";

        const img = document.createElement('img');
        img.src = URL.createObjectURL(file); // 파일을 미리보기로 표시
        img.style.maxWidth = '100px'; // 이미지의 최대 너비 설정 img.style.maxHeight='100px' ; // 이미지의 최대 높이 설정

        const btn = document.createElement('button');
        btn.style.position = 'absolute';
        btn.style.right = '13px';
        btn.style.top = '1px';
        btn.style.margin = '0';
        btn.style.fontSize = '15px';
        btn.style.backgroundColor = 'transparent';
        btn.style.color = 'red';
        btn.style.padding = '0';
        btn.style.border = 'none';
        btn.style.fontWeight = 'bold';
        btn.value = 'X';
        btn.innerHTML = `<span>X</span>`;
        btn.onclick = function deleteImg(){
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

                imageList.removeChild(document.getElementById('img' + i));
            }

            //imageList.removeChild(document.getElementById('img' + i));
        }

        li.appendChild(img);
        li.appendChild(btn);
        imageList.appendChild(li);
    }
});

$(function () {
    // datepicker 초기화
    $('#datepicker').datepicker();
    //$('#datepicker').datepicker('show');
});

$(document).ready(function () {
    // 취소 버튼 클릭 시
    $(".btn-secondary").click(function () {
        // input file 초기화
        $("#addPic").val(''); // input 파일 선택값 초기화
        $("#imageList").empty(); // 이미지 목록 초기화
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

                if(element.orgCategoryId === 1){
                    $('.ledHouseLightList').append(html)//화면에 출력
                    $('.adminLedHouseLightList').append(adminHtml)//화면에 출력
                }else if(element.orgCategoryId === 2){
                    $('.rdPanelList').append(html)//화면에 출력
                    $('.adminRdPanelList').append(adminHtml)//화면에 출력
                }else if(element.orgCategoryId === 3){
                    $('.dmxSplitterLIst').append(html)//화면에 출력
                    $('.adminDmxSplitterLIst').append(adminHtml)//화면에 출력
                }else if(element.orgCategoryId === 4){
                    $('.dmxControllerList').append(html)//화면에 출력
                    $('.adminDmxControllerList').append(adminHtml)//화면에 출력
                }else if(element.orgCategoryId === 5){
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

var selectCategory = function(value){
    $("#orgCategory").val(value);
}

// 수정 시 정합성 검증
function projectInfoCheck() {

    const imageList = document.getElementById('imageList');

    if (imageList.getElementsByClassName("useImages").length <= 0){
        alert("업로드 이미지는 최소 하나 이상이여야 합니다.");
        return false;
    }

    if (document.getElementById('datepicker').value.length !== 10) {
        alert("날짜를 정확하게 명시해야 합니다.");
        return false;
    }

    if (document.getElementById('titleKor').value.length <= 2) {
        alert("한글 타이틀 명이 옳바르게 기입되었는지 확인해주십시오.");
        return false;
    }

    if (document.getElementById('locationKor').value.length <= 1) {
        alert("위치 주소(한글)가 옳바르게 기입되었는지 확인해주십시오.");
        return false;
    }

    if (document.getElementById('titleEng').value.length <= 2) {
        alert("영문 타이틀 명이 옳바르게 기입되었는지 확인해주십시오.");
        return false;
    }

    if (document.getElementById('locationEng').value.length <= 1) {
        alert("위치 주소(영문)가 옳바르게 기입되었는지 확인해주십시오.");
        return false;
    }

}