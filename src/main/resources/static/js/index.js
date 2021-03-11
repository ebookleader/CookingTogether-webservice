// 상품 등록
var main = {
    init : function() {
        var _this = this;
        $('#btn-space-save').on('click', function() {
            _this.save();
        });

//        $('#btn-image-save').on('click', function() {
//            _this.uploadImage();
//        });

        $('#btn-space-delete').on('click', function() {
            _this.deleteSpace();
        });

        $('#btn-space-update').on('click', function() {
            _this.updateSpace();
        });

        $('#btn-check-isSeller').on('click', function() {
            _this.checkIsSeller();
        });

        $('#btn-undo-enrollSeller').on('click', function() {
            if(_this.undoEnrollSellerCheck()) {
                _this.undoEnrollSeller();
            }
        });

        $('#btn-enroll-seller').on('click', function() {
            if(_this.enrollSeller()) {
                _this.enrollComplete();
            }
        });

        $('#btn-email-verify').on('click', function() {
            _this.emailVerify();
        });

        $('#btn-email-key').on('click', function() {
            _this.checkEmailKey();
        });

        $('#previewPriceBtn').on('click', function() {
            _this.previewPrice();
        });

        $('#checkReserveBtn').on('click', function() {
            _this.checkReservationIsOk();
        });

        $('#reserve-ongoing-btn').on('click', function() {
            _this.ongoingReservation();
        });

        $("#mainpage-search-button").on('click', function() {
            _this.mainPageSearch();
        });
    },

    save : function() {
        var facilityList = [];
        $('input[name="facility"]').each(function(i) {
            facilityList.push($(this).val());
        });

        var noticeList = [];
        $('input[name="notice"]').each(function(i) {
            noticeList.push($(this).val());
        });

        var policyList = [];
        $('input[name="policy"]').each(function(i) {
            policyList.push($(this).val());
        });

        var optionTitleList = [];
        $('input[name="typeTitle"]').each(function(i) {
            optionTitleList.push($(this).val());
        });

        var startTimeList = [];
        $('input[name="startTime"]').each(function(i) {
            var v = $(this).val();
            var vv;
            if (v.substring(0,1) == 0) {
                vv = v.substring(1,2);
            }
            else {
                vv = v.substring(0,2);
            }
            startTimeList.push(vv);
        });

        var endTimeList = [];
        $('input[name="endTime"]').each(function(i) {
            var v = $(this).val();
            var vv;
            if (v.substring(0,1) == 0) {
                vv = v.substring(1,2);
            }
            else {
                vv = v.substring(0,2);
            }
            endTimeList.push(vv);
        });

        var countList = [];
        $('input[name="count"]').each(function(i) {
            countList.push($(this).val());
        })

        var data = {
            p_name: $('#p_name').val(),
            p_location: $('#p_location').val(),
            p_postcode : $('#sample6_postcode').val(),
            p_address : $('#sample6_address').val(),
            p_detailAddress : $('#sample6_detailAddress').val(),
            p_city: $('#p_city').val(),
            p_category: $('#p_category').val(),
            p_weekdayPrice: $('#p_weekdayPrice').val(),
            p_weekendPrice: $('#p_weekendPrice').val(),
            p_introduce: $('#p_introduce').val(),
            p_maxNum: $('#p_maxNum').val(),
            p_liked: 0,
            p_avgRating: 0.0,
            facility : facilityList,
            notice : noticeList,
            policy : policyList,
            optionTitle : optionTitleList,
            startTime : startTimeList,
            endTime : endTimeList,
            count : countList
        };

        $.ajax({
            type: 'POST',
            url: '/api/v1/products',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function(pid) {
            alert('상품 등록 완료\n상품 사진 등록으로 넘어갑니다.');
            window.location.href = "/products/"+pid+"/imageInsert";
        }).fail(function(error) {
            alert(JSON.stringify(error));
        });
    },

//    uploadImage : function() {
//        var file = $('#img')[0].files[0];
//        var formData = new FormData();
//        formData.append('data', file);
//
//        $.ajax({
//            type: 'POST',
//            url: '/upload',
//            data: formData,
//            processData: false,
//            contentType: false
//        }).done(function (data) {
//            $('#result-image').attr("src", data);
//        }).fail(function (error) {
//            alert(error);
//        });
//    },

    deleteSpace : function() {
        var id = $("#p_id").val();
        $.ajax({
            type: 'DELETE',
            url: '/api/v1/products/delete/'+id,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8'
        }).done(function() {
            alert('해당 게시물이 삭제되었습니다.');
            window.location.href = '/';
        }).fail(function(error){
            alert(JSON.stringify(error));
        });
    },

    updateSpace : function() {

        var facilityList = [];
        $('input[name="facility"]').each(function(i) {
            facilityList.push($(this).val());
        });

        var noticeList = [];
        $('input[name="notice"]').each(function(i) {
            noticeList.push($(this).val());
        });

        var policyList = [];
        $('input[name="policy"]').each(function(i) {
            policyList.push($(this).val());
        });

        var data = {
            p_name: $('#p_name').val(),
            p_location: $('#p_location').val(),
            p_city: $('#p_city').val(),
            p_category: $('#p_category').val(),
            p_weekdayPrice: $('#p_weekdayPrice').val(),
            p_weekendPrice: $('#p_weekendPrice').val(),
            p_introduce: $('#p_introduce').val(),
            p_maxNum: $('#p_maxNum').val(),
            facility : facilityList,
            notice : noticeList,
            policy : policyList
        };

        var id = $('#p_id').val();

        $.ajax({
            type: 'PUT',
            url: '/api/v1/products/'+id,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function() {
            alert('게시글 수정 완료');
            window.location.href = '/';
        }).fail(function() {
            alert(JSON.stringify(error));
        });
    },

    checkIsSeller : function() {
        $.ajax({
            type:'GET',
            url: '/api/user/checkIsSeller',
            success: function(result) {
                if(result==true) {
                    alert('이미 판매자 등록이 완료되었습니다.');
                    return false;
                }
                else {
                    window.location.href = '/seller/enroll';
                }
            }
        });
    },

    undoEnrollSellerCheck : function() {
        if($('#undoSellerCheck1').prop("checked")) {
            if($('#undoSellerCheck2').prop("checked")) {
                return true;
            }
            else {
                alert('두번째 항목이 체크되지 않았습니다.');
            }
        }
        else {
            alert('첫번째 항목이 체크되지 않았습니다.');
        }
        return false;
    },

    undoEnrollSeller : function() {
        $.ajax({
            type: 'PUT',
            url: '/api/user/undo/enrollSeller',
            success: function() {
                alert('판매자 취소가 완료되었습니다. 다시 로그인해주세요.');
                window.location.href = '/logout';
            }
        });
    },

    enrollSeller : function() {
        if($('#sellerCheck1').prop("checked")) {
            if($('#sellerCheck2').prop("checked")) {
                return true;
            }
            else {
                alert('두번째 항목이 체크되지 않았습니다.');
            }
        }
        else {
            alert('첫번째 항목이 체크되지 않았습니다.');
        }
        return false;
    },

    enrollComplete : function() {
        $.ajax({
            type: 'PUT',
            url: '/api/user/enrollSeller',
        }).done(function() {
            alert('판매자 등록이 완료되었습니다.\n다시 로그인해주세요.');
            window.location.href = '/logout';
        }).fail(function() {
            alert(JSON.stringify(error));
        });
    },

    emailVerify : function() {
        var userEmail = $('#userEmail').val();
        $.ajax({
            	type : 'POST',
            	url : '/api/guestUser/email/verify',
        		data : { userEmail:userEmail },
            	dataType :'json'
            }).done(function() {
                alert(userEmail+"로 인증번호가 전송되었습니다.");
                window.location.href = '/myPage/user/checkEmail';
            }).fail(function() {
                alert('fail');
            });
    },

    checkEmailKey : function() {
        var inputKey = $('#verification_code').val();
        var userEmail = $('#userEmail').val();
        if(inputKey=="") {
            alert('인증번호가 입력되지 않았습니다.');
        }
        else {
            $.ajax({
                type: 'PUT',
                url: '/api/guestUser/email/check',
                data : { inputKey:inputKey, userEmail:userEmail },
                dataType :'json',
                success : function(result) {
                    if(result == true) {
                        alert('인증이 완료되었습니다.\n다시 로그인 해주세요.');
                        window.location.href = '/logout';
                    }
                    else {
                        alert('인증번호가 일치하지 않습니다.\n다시 입력해주세요.');
                    }
                }
            });
        }
    },

    previewPrice : function() {
        var p_id = $('#p_id').val();
        var inputDate = $('#reserveDate').val();
        var po_id = $("#optionSelectBox option:selected").val();
        $.ajax({
                type: 'GET',
                url: '/api/v1/products/previewPrice',
                data : { p_id:p_id, inputDate:inputDate, po_id:po_id },
                dataType :'json',
                success : function(result) {
                    var txt = result+"원";
                    $("#previewPrice").val(txt);
                }
         });
    },

    checkReservationIsOk : function() {
        var p_id = $('#p_id').val();
        var inputDate = $('#reserveDate').val();
        var date = inputDate.split("/"); //month, day, year
        var po_id = $("#optionSelectBox option:selected").val();
        var reserveNum = $('#reserveNum').val();
        $.ajax({
                type: 'GET',
                url: '/api/v1/products/checkReservationIsOk',
                data : { p_id:p_id, inputDate:inputDate, po_id:po_id },
                dataType :'json',
                success : function(result) {
                    if (result==true) {
                        alert('예약 가능합니다.');
                        window.location.href = "/space/list/detail/reservationOngoing/"+date[0]+"/"+date[1]+"/"+date[2]+"/"+p_id+"/"+po_id+"/"+reserveNum;
                    }
                    else {
                        alert('해당 날짜의 해당 옵션은 예약이 모두 찼습니다.\n다른 옵션을 선택해주세요.');
                    }
                }
         });
    },

    ongoingReservation : function() {
        var name = $('#reservation-name').val();
        var email = $('#reservation-email').val();
        var phone = $('#reservation-phone').val();
        var chk1 = $('input:checkbox[id="reserveConfirmCheck1"]').is(":checked");
        var chk2 = $('input:checkbox[id="reserveConfirmCheck2"]').is(":checked");
        var isOk = false;
        if(name=="" || name==null) {
            alert('예약자 이름을 입력해주세요');
        }
        else if(email=="" || email==null) {
            alert('예약자 이메일을 입력해주세요');
        }
        else if(phone=="" || phone==null) {
            alert('예약자 핸드폰 번호를 입력해주세요');
        }
        else if(chk1==false || chk2==false) {
            alert('예약 안내사항 체크박스가 체크되지 않았습니다.\n모두 체크해주세요');
        }
        else {
            isOk = true;
        }

        if (isOk) {
            var data = {
                ryear: document.getElementById('ryear').innerHTML,
                rmonth: document.getElementById('rmonth').innerHTML,
                rday: document.getElementById('rday').innerHTML,
                numOfPeople: document.getElementById('reserveNum').innerHTML,
                totalPrice: document.getElementById('totalPrice').innerHTML,
                productId: $('#productId').val(),
                optionId: $('#optionId').val(),
                userReservationName: name,
                userReservationEmail: email,
                userReservationPhone: phone
            };

            $.ajax({
                type: 'POST',
                url: '/api/v1/products/reservation/ongoing',
                dataType: 'json',
                contentType: 'application/json; charset=utf-8',
                data: JSON.stringify(data),
            }).done(function() {
                alert('예약이 완료되었습니다');
                window.location.href = "/";
            }).fail(function(error) {
                alert(JSON.stringify(error));
            });
        }

    },

    mainPageSearch : function() {
        var location = $("#location-dropdown").text();
        var category = $("#category-dropdown").text();
        alert(location);
        alert(category);
//        $.ajax({
//                type: 'GET',
//                url: '/api/v1/products/checkReservationIsOk',
//                data : { p_id:p_id, inputDate:inputDate, po_id:po_id },
//                dataType :'json',
//                success : function(result) {
//                    if (result==true) {
//                        alert('예약 가능합니다.');
//                        window.location.href = "/space/list/detail/reservationOngoing/"+date[0]+"/"+date[1]+"/"+date[2]+"/"+p_id+"/"+po_id+"/"+reserveNum;
//                    }
//                    else {
//                        alert('해당 날짜의 해당 옵션은 예약이 모두 찼습니다.\n다른 옵션을 선택해주세요.');
//                    }
//                }
//         });
    },


};

main.init();