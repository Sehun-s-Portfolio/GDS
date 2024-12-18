//헤더 네비 보이기
// JavaScript to show/hide the navbar on scroll
var prevScrollPos = window.pageYOffset;
var navbar = document.querySelector('.navbar');
var navlink = document.querySelector('.nav-item');

window.onscroll = function () {
    var currentScrollPos = window.pageYOffset;

    if (prevScrollPos > currentScrollPos || currentScrollPos < 100) {
        // 네비게이션 바 표시
        navbar.style.top = '0';
        navlink.style.color = "#777";

    } else {
        // 네비게이션 바 숨김
        navbar.style.top = '-100px'; // 필요에 따라 값 조절
        navlink.style.color = "#ffffff";
    }

    // 스크롤 위치에 따라 bg-white 클래스 추가 또는 제거
    if (currentScrollPos > 100) {
        navbar.classList.add('bg-white'); // bg-white 클래스 추가
    } else {
        navbar.classList.remove('bg-white'); // bg-white 클래스 제거
    }

    prevScrollPos = currentScrollPos;
};
//헤더 네비 끝

$(document).ready(function () {
    $(".owl-carousel").owlCarousel({
        items: 3, // 3개의 항목을 보여줍니다.
        loop: true, // 무한 루프
        margin: 10, // 슬라이드 사이의 간격
        autoplay: true, // 자동 재생
        autoplayTimeout: 3000, // 자동 재생 간격(ms)
        autoplayHoverPause: true, // 마우스 호버 시 자동 재생 일시 중지
        dots: true, // 도트 표시 활성화
        responsive:{
            0:{
                items:1
            },
            750:{
                items:2
            },
            1200:{
                items:3
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

    // 인스타 토큰 호출
    fetch("/token/get/insta", {
        method: "GET",
        headers: {
            "Content-Type": "application/json"
        }
    })
        .then(response => response.text())
        .then((data) => {
            instaToken = data;
            console.log(instaToken)

            callInstagramInfo(instaToken);
        });

});

var instaToken = "";

function updateInstagramToken(){
    const newInstaToken = $("#tokenText").val();

    // 인스타 토큰 호출
    fetch("/token/update/insta", {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            token: newInstaToken
        })
    })
        .then(response => response.text())
        .then((data) => {
            instaToken = data;
        });

    console.log("토큰 확인 : " + instaToken);
    $('#instagram-container').remove();
    $('#moreButton').remove();
    callInstagramInfo(instaToken);
}

// 인스타그램 게시글 호출
function callInstagramInfo(inputToken) {
    // 1차 토큰
    //var instaToken = 'IGQWRPYzNWNWE1VERSNFhZAT19iZAzYtZAE1sSTNrVlBCQS1pNzk0ek95dE9xUUhkbzBIWWtNMElCZA2trRnFsa1lDenlGOERCcG9rNFlnSmkwdzFiTWZAKajgzWkJlSFd5MFhfSUtlRTVteEkwcDZAtaVFiRmxCckZADdTgZD';
    // 2차 토큰
    //var instaToken = 'IGQWROZAlRDYTFkTmpMWTZAJNWNQZAWF2b05fY1BoWjFXb0VRbnlVQkpOYmNQYTljN1o2eDBYTUZAUalZA6V3l5RXhVTmxfT0VPZATZABVjQyT3pYb1o3ZA1p2RGZAjNW54U3o0THdpdGZAnWkV1ZAndpemZAOMU9yVEZAfOHYzN3cZD';

    $.ajax({
        url: "https://graph.instagram.com/me/media?fields=id,caption,media_type,media_url,thumbnail_url,is_shared_to_feed,permalink,timestamp,username&access_token=" + inputToken,
        method: 'GET',
        dataType: 'json',
        success: function (response) {
            // API 요청이 성공했을 때 실행될 코드
            var data = response.data;
            let instaHtml = '';
            var rowNum = 0;

            instaHtml += `<div class="col-12 d-flex mx-auto instaContainer" id="instagram-container" style="flex-wrap: wrap">`;

            for (var i = 0; i < 10; i++) {
                rowNum += 1;

                var imgUrl = data[i].media_url;
                var permaLink = data[i].permalink;
                var caption = data[i].caption;

                instaHtml += `
                            <a href=${permaLink} style="width: 20%">
                                <div class="miPost align-items-center">
                                  <div class="overlay">
                                     <img src="https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.gds-korea.com%2F&psig=AOvVaw3i6fCfGPNc3voOduT2lbc8&ust=1698990539570000&source=images&cd=vfe&opi=89978449&ved=0CBEQjRxqFwoTCNjjgIzPpIIDFQAAAAAdAAAAABAE">
                                     <p>GDS Korea</p>
                                  </div>
                                  <img src=${imgUrl} alt="Insta Image ${i}" style="min-height: 270px; margin-bottom: 8px">
                                  <div class="d-flex align-items-center justify-content-around" style="margin-bottom: 8px;">
                                      <img src="/img/instagram.png" alt="Instagram Logo" class="InstaLogo">
                                      <p class="captionText" style="width: 200px;">${caption}</p>
                                  </div>
                                </div>
                            </a>`

                if (10 - 1 === i) {
                    instaHtml += `</div>`;
                }

                // 만약 i 번째 인덱스가 4로 나누었을 때 나머지가 존재하지 않을 경우 instagram-container id 를 추가한 div 태그를 추가한다.
                /**
                 if (i % 5 === 0) {
                 instaHtml += `
                 <div class="col-12 d-flex mx-auto instaContainer" id="instagram-container">
                 <a href="https://www.instagram.com/gdskorea/?hl=ko">
                 <div class="miPost align-items-center">
                 <div class="overlay">
                 <img src="https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.gds-korea.com%2F&psig=AOvVaw3i6fCfGPNc3voOduT2lbc8&ust=1698990539570000&source=images&cd=vfe&opi=89978449&ved=0CBEQjRxqFwoTCNjjgIzPpIIDFQAAAAAdAAAAABAE">
                 <p>GDS Korea</p>
                 </div>
                 <img src=${imgUrl} alt="Insta Image ${i}" style="min-height: 270px; margin-bottom: 8px">
                 <div class="d-flex align-items-center justify-content-around" style="margin-bottom: 8px;">
                 <img src="/img/instagram.png" alt="Instagram Logo" style="width: 30px; height: 30px!important; min-height: 0!important">
                 <p class="captionText" style="width: 200px;">${caption}</p>
                 </div>
                 </div>
                 </a>`

                 if (data.length - 1 === i) {
                 instaHtml += `</div>`;
                 }
                 } else {
                 instaHtml += `
                 <a href="https://www.instagram.com/gdskorea/?hl=ko">
                 <div class="miPost align-items-center ">
                 <div class="overlay">
                 <img src="https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.gds-korea.com%2F&psig=AOvVaw3i6fCfGPNc3voOduT2lbc8&ust=1698990539570000&source=images&cd=vfe&opi=89978449&ved=0CBEQjRxqFwoTCNjjgIzPpIIDFQAAAAAdAAAAABAE">
                 <p>GDS Korea</p>
                 </div>
                 <img src=${imgUrl} alt="Insta Image ${i}" style="min-height: 270px; margin-bottom: 8px">
                 <div class="d-flex align-items-center justify-content-around"  style="margin-bottom: 8px;">
                 <img src="/img/instagram.png" alt="Instagram Logo" style="width: 30px; height: 30px!important; min-height: 0!important;">
                 <p class="captionText" style="width: 200px;">${caption}</p>
                 </div>
                 </div>
                 </a>
                 `

                 if ((i + 1) % 5 === 0 || data.length - 1 === i) {
                 instaHtml += `</div>`
                 }
                 }**/

            }

            instaHtml += `<button type="button" onClick="location.href='https://www.instagram.com/gdskorea/?hl=ko'" id= 'moreButton' class='YellowBtn instaBtn'>MORE</button>`;

            $('#instagramSection').append(instaHtml);
        },

        error: function (xhr, status, error) {
            // API 요청이 실패했을 때 실행될 코드
            console.log("데이터 호출 실패");
            console.log(xhr.responseText);
        }
    });
}

var video = document.getElementById("myVideo");

video.oncanplaythrough = function () {
    video.play();
};
document.addEventListener('DOMContentLoaded', function () {
    AOS.init();
});
const basePath = "/img/insta";


// "Loading" 버튼 숨기기
const loadingButton = document.querySelector('#loading-button');
// loadingButton.style.display = 'none';

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

var selectCategory = function (value) {
    $("#orgCategory").val(value);
}

document.addEventListener('DOMContentLoaded', function () {
    // Get references to the button and the collapsible div
    var hideButton = document.getElementById('hide');
    var navbarCollapse = document.getElementById('navbarCollapse');

    // Add click event listener to the button
    hideButton.addEventListener('click', function () {
        // Remove the 'show' class from the collapsible div
        navbarCollapse.classList.remove('show');
    });
});