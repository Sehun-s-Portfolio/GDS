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

var selectCategory = function (value) {
    $("#orgCategory").val(value);
}


// 문의 메일 등록 및 발송 함수
function submitContactInquiry() {

    const emailRegex = /^([a-z0-9_\.-]+)@([\da-z\.-]+)\.([a-z\.]{2,6})$/;
    const phoneRegex = /\d{3}\d{4}\d{4}/;

    if (document.getElementById('contactor').value === '') {
        alert("문의자 이름 정보를 정확하게 기입해주십시오.");
    } else if (document.getElementById('address').value === '') {
        alert("문의자 주소 정보를 정확하게 기입해주십시오.");
    } else if (document.getElementById('email').value === '' ||
    !emailRegex.test(document.getElementById('email').value)) {
        alert("문의자 이메일 정보를 정확하게 기입해주십시오.");
    } else if (document.getElementById('phoneNumber').value === '' ||
        document.getElementById('phoneNumber').value.length !== 11 ||
        !phoneRegex.test(document.getElementById('phoneNumber').value)) {
        alert("전화 번호 정보 입력 양식이 맞지 않습니다.\n전화 번호는 공백일 수 없으며, 특수 기호 없이 번호만 기입하여 주십시오.");
    } else if (document.getElementById('additionalInfo').value === '' ||
        document.getElementById('additionalInfo').value.length <= 10) {
        alert("문의 내용 정보를 정확하게 기입해주십시오.\n문의 내용은 최소 10자 이상 입력하셔야 합니다.");
    } else {
        $.ajax({
            type: 'post',
            url: '/gds/contact/inquiry',
            data: {
                "contactor": document.getElementById('contactor').value,
                "address": document.getElementById('address').value,
                "phoneNumber": document.getElementById('phoneNumber').value,
                "email": document.getElementById('email').value,
                "additionalInfo": document.getElementById('additionalInfo').value
            },
            success: function (data) {
                console.log(data);
                alert("정상적으로 문의가 등록되었습니다.");
                document.location.reload();
            },
            error: function (err) {
                console.log(err);
                alert("문의 등록을 위해서는 모든 정보가 입력되어야 합니다.");
                document.location.reload();
            }
        });
    }

}