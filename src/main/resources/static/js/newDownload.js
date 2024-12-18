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

$(function () {
    // datepicker 초기화
    $("#datepicker").datepicker();
});


$(document).ready(function () {
    // 취소 버튼 클릭 시
    $(".btn-secondary").click(function () {
        // input file 초기화
        $("#addPic").val(''); // input 파일 선택값 초기화
        $("#imageList").empty(); // 이미지 목록 초기화
    });

    $.ajax({
        url: "/gds/download/catalogs",
        type: 'get',
        data: {},
        success: function (data) {

            var rowCount = 0;
            var rowName = "catalogSection";
            var completeRow = "";

            // data = 카테고리
            // 각 카테고리를 돌리며 데이터 조회
            for (let i = 0; i < data.length; i++) {

                if (data[i].products.length !== 0) {

                    if ((i + 1) % 2 !== 0) {
                        rowCount += 1;
                        completeRow = rowName + rowCount;
                        let html = ``;

                        html += `<div class='col-10 d-flex justify-content-between eachSection rowWrap' id="${completeRow}" style="margin-bottom: 100px;">
                                <div class='col-5 downloadWrap' >
                                    <div class='d-flex align-items-baseline flex-column onebyone'>
                                        <div class='d-flex ldrDiv'>`;

                        html += `<img src="${data[i].medias[0].mediaUploadUrl}" alt="">`;

                        /*
                    for (let j = 0; j < data[i].medias.length; j++) {
                        html += `<img src="${data[i].medias[j].mediaUploadUrl}" alt="">`;
                    }
                    */

                        html += `</div>
                                    <h2>` + data[i].categoryName + `</h2>
                             </div>
                                    <div>`;

                        if(data[i].categoryName === "FIDELITY SERIES"){
                            html += `<div class="accordion" id="accordion">
                                            <div class="accordion-item">
                                                <h2 class="accordion-header" >
                                                    <button class="accordion-button" type="button" data-bs-toggle="collapse"
                                                            data-bs-target="#collapse" aria-expanded="true" aria-controls="collapseOne1">FIDELITY Series 1,2,4,8K Driver</button>
                                                </h2>
                                                <div id="collapse" class="accordion-collapse collapse show" aria-labelledby="headingOne"
                                                     data-bs-parent="#accordion">
                                                    <div class="accordion-body">
                                                        <div class="fileWrap d-flex justify-content-between align-items-center">
                                                            <img src="/img/pdf.png" style="width: 50px">
                                                                <a class='col-10 d-flex justify-content-between align-items-center'
                                                                   href="https://gdskorea.gabia.io/product/file/FIDELITY Series 1,2,4,8K Driver.pdf" download target="_blank">FIDELITY Series 1,2,4,8K Driver.pdf<button class="dtBtn">Download</button>
                                                                </a>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>`;
                        }

                        for (let k = 0; k < data[i].products.length; k++) {
                            html += `<div class="accordion" id="accordion${k}">
                                            <div class="accordion-item">
                                                <h2 class="accordion-header" >
                                                    <button class="accordion-button" type="button" data-bs-toggle="collapse${k}"
                                                            data-bs-target="#collapse${k}" aria-expanded="true" aria-controls="collapseOne1">` + data[i].products[k].productName + `</button>
                                                </h2>
                                                <div id="collapse${k}" class="accordion-collapse collapse show" aria-labelledby="headingOne"
                                                     data-bs-parent="#accordion${k}">
                                                    <div class="accordion-body">
                                                        <div class="fileWrap d-flex justify-content-between align-items-center">
                                                            <img src="/img/pdf.png" style="width: 50px">
                                                                <a class='col-10 d-flex justify-content-between align-items-center'
                                                                   href="${data[i].products[k].productFile}" download target="_blank">` +
                                data[i].products[k].productFileName
                                + `<button class="dtBtn">Download</button>
                                                                </a>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>`;
                        }


                        if ((data.length - 1) === i) {
                            html += `</div>
                                </div>
                            </div>`;

                        } else {
                            html += `</div>
                            </div>`;
                        }

                        $('.projectRowWrap').append(html)//화면에 출력

                    } else if ((i + 1) % 2 === 0) {
                        let html2 = ``;

                        html2 += `<div class='col-5 downloadWrap' >
                                    <div class='d-flex align-items-baseline flex-column onebyone'>
                                        <div class='d-flex ldrDiv'>`;

                        html2 += `<img src="${data[i].medias[0].mediaUploadUrl}" alt="">`;

                        /*
                    for (let j = 0; j < data[i].medias.length; j++) {
                        html += `<img src="${data[i].medias[j].mediaUploadUrl}" alt="">`;
                    }
                    */

                        html2 += `</div>
                                    <h2>` + data[i].categoryName + `</h2>
                             </div>
                                    <div>`;

                        for (let k = 0; k < data[i].products.length; k++) {
                            html2 += `<div class="accordion" id="accordion${k}">
                                            <div class="accordion-item">
                                                <h2 class="accordion-header" >
                                                    <button class="accordion-button" type="button" data-bs-toggle="collapse${k}"
                                                            data-bs-target="#collapse${k}" aria-expanded="true" aria-controls="collapseOne1" value="">` + data[i].products[k].productName + `</button>
                                                </h2>
                                                <div id="collapse${k}" class="accordion-collapse collapse show" aria-labelledby="headingOne"
                                                     data-bs-parent="#accordion${k}">
                                                    <div class="accordion-body">
                                                        <div class="fileWrap d-flex justify-content-between align-items-center">
                                                            <img src="/img/pdf.png" style="width: 50px">
                                                                <a class='col-10 d-flex justify-content-between align-items-center'
                                                                   href="${data[i].products[k].productFile}" download target="_blank">` +
                                data[i].products[k].productFileName
                                + `<button class="dtBtn">Download</button>
                                                                </a>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>`;
                        }

                        html2 += `</div>
                                </div>
                            </div>`;

                        $('.rowWrap').append(html2)//화면에 출력
                        document.getElementById(completeRow).classList.remove('rowWrap');

                    }
                }
            }
        },
        error: function () {
            alert("error");
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
