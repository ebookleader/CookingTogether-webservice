$(document).ready(function() {

  // index.html
  // 지역 선택 영역
  $("#seoul,#incheon,#daegu,#daejeon,#busan,#gwangju,#ulsan,#saejong,#gyeonggi,#gangwon,#chungn,#chungs,#junn,#juns,#gyeongn,#gyeongs,#jeju").click(function() {
    var k = $(this).text();
    $("#location-dropdown").text(k);
  })

  // 카테고리 선택 영역
  $("#category-instrument,#category-sing,#category-dance,#category-cooking,#category-musical,#category-study").click(function() {
    var k = $(this).text();
    $("#category-dropdown").text(k);
  })

    // 동적 input(facility) 추가

      var maxField = 10;
      var facility_wrapper = $('.facility_append');
      var cnt = 1;
      var facilityList = new Array(10);

      // 추가
      $('.facility_add_btn').click(function() {
        if(cnt<maxField) {
          cnt++;
          var fieldHtml = '<div class="form-inline"><div class="input-group-prepend"><span class="input-group-text" id="facility-'+cnt+'">'+cnt+'</span></div><input type="text" class="form-control" name="facility" id="p_facility'+cnt+'"></div>'
          var fieldHtml_with_btn = '<div class="form-inline"><div class="input-group-prepend"><span class="input-group-text" id="facility-'+cnt+'">'+cnt+'</span></div><input type="text" class="form-control" name="facility" id="p_facility'+cnt+'"><div id="remove_btn_div'+cnt+'"><a href="#aa" class="facility_remove_btn" id="a_facility_remove_btn"><i class="fa fa-minus ml-3"></i></a></div></div>'
          if(cnt>2) {
            var parent = document.getElementById("remove_btn_div"+(cnt-1));
            parent.removeChild(document.getElementById("a_facility_remove_btn"));
          }
          $('.facility_append').append(fieldHtml_with_btn);
        }
      });

      // 삭제
      $(facility_wrapper).on('click','.facility_remove_btn', function(e) {
        e.preventDefault();
        for(var i=0;i<cnt;i++) {
          facilityList[i] = document.getElementById("p_facility"+(i+1)).value;
        }
        cnt--;
        $('.facility_append').empty();
        for(var i=2;i<=cnt;i++) {
          var fieldHtml = '<div class="form-inline"><div class="input-group-prepend"><span class="input-group-text" id="facility-'+i+'">'+i+'</span></div><input type="text" class="form-control" name="facility" id="p_facility'+i+'"></div>'
          var fieldHtml_with_btn = '<div class="form-inline"><div class="input-group-prepend"><span class="input-group-text" id="facility-'+i+'">'+i+'</span></div><input type="text" class="form-control" name="facility" id="p_facility'+i+'"><div id="remove_btn_div'+i+'"><a href="#aa" class="facility_remove_btn" id="a_facility_remove_btn"><i class="fa fa-minus ml-3"></i></a></div></div>'
          if(i==cnt) {
            $('.facility_append').append(fieldHtml_with_btn);
          }
          else {
            $('.facility_append').append(fieldHtml);
          }
          document.getElementById("p_facility"+i).value = facilityList[i-1];
        }
      });

      // 동적 input(notice) 추가
      var notice_wrapper = $('.notice_append');
      var ncnt = 1;
      var noticeList = new Array(10);

      // 추가
      $('.notice_add_btn').click(function() {
        if(ncnt<maxField) {
          ncnt++;
          var fieldHtml = '<div class="form-inline"><div class="input-group-prepend"><span class="input-group-text" id="notice-'+ncnt+'">'+ncnt+'</span></div><input type="text" class="form-control" name="notice" id="p_notice'+ncnt+'"></div>'
          var fieldHtml_with_btn = '<div class="form-inline"><div class="input-group-prepend"><span class="input-group-text" id="notice-'+ncnt+'">'+ncnt+'</span></div><input type="text" class="form-control" name="notice" id="p_notice'+ncnt+'"><div id="notice_remove_btn_div'+ncnt+'"><a href="#aa" class="notice_remove_btn" id="a_notice_remove_btn"><i class="fa fa-minus ml-3"></i></a></div></div>'
          if(ncnt>2) {
            var parent = document.getElementById("notice_remove_btn_div"+(ncnt-1));
            parent.removeChild(document.getElementById("a_notice_remove_btn"));
          }
          $('.notice_append').append(fieldHtml_with_btn);
        }
      });

      // 삭제
      $(notice_wrapper).on('click','.notice_remove_btn', function(e) {
        e.preventDefault();
        for(var i=0;i<ncnt;i++) {
          noticeList[i] = document.getElementById("p_notice"+(i+1)).value;
        }
        ncnt--;
        $('.notice_append').empty();
        for(var i=2;i<=ncnt;i++) {
          var fieldHtml = '<div class="form-inline"><div class="input-group-prepend"><span class="input-group-text" id="notice-'+i+'">'+i+'</span></div><input type="text" class="form-control" name="notice" id="p_notice'+i+'"></div>'
          var fieldHtml_with_btn = '<div class="form-inline"><div class="input-group-prepend"><span class="input-group-text" id="notice-'+i+'">'+i+'</span></div><input type="text" class="form-control" name="notice" id="p_notice'+i+'"><div id="notice_remove_btn_div'+i+'"><a href="#aa" class="notice_remove_btn" id="a_notice_remove_btn"><i class="fa fa-minus ml-3"></i></a></div></div>'

          if(i==ncnt) {
            $('.notice_append').append(fieldHtml_with_btn);
          }
          else {
            $('.notice_append').append(fieldHtml);
          }
          document.getElementById("p_notice"+i).value = noticeList[i-1];
        }
      });

      // 동적 input(policy) 추가
      var policy_wrapper = $('.policy_append');
      var pcnt = 1;
      var policyList = new Array(10);

      // 추가
      $('.policy_add_btn').click(function() {
        if(pcnt<maxField) {
          pcnt++;
          var fieldHtml = '<div class="form-inline"><div class="input-group-prepend"><span class="input-group-text" id="policy-'+pcnt+'">'+pcnt+'</span></div><input type="text" class="form-control" name="policy" id="p_policy'+pcnt+'"></div>'
          var fieldHtml_with_btn = '<div class="form-inline"><div class="input-group-prepend"><span class="input-group-text" id="policy-'+pcnt+'">'+pcnt+'</span></div><input type="text" class="form-control" name="policy" id="p_policy'+pcnt+'"><div id="policy_remove_btn_div'+pcnt+'"><a href="#aa" class="policy_remove_btn" id="a_policy_remove_btn"><i class="fa fa-minus ml-3"></i></a></div></div>'
          if(pcnt>2) {
            var parent = document.getElementById("policy_remove_btn_div"+(pcnt-1));
            parent.removeChild(document.getElementById("a_policy_remove_btn"));
          }
          $('.policy_append').append(fieldHtml_with_btn);
        }
      });

      // 삭제
      $(policy_wrapper).on('click','.policy_remove_btn', function(e) {
        e.preventDefault();
        for(var i=0;i<pcnt;i++) {
          policyList[i] = document.getElementById("p_policy"+(i+1)).value;
        }
        pcnt--;
        $('.policy_append').empty();
        for(var i=2;i<=pcnt;i++) {
          var fieldHtml = '<div class="form-inline"><div class="input-group-prepend"><span class="input-group-text" id="policy-'+i+'">'+i+'</span></div><input type="text" class="form-control" name="policy" id="p_policy'+i+'"></div>'
          var fieldHtml_with_btn = '<div class="form-inline"><div class="input-group-prepend"><span class="input-group-text" id="policy-'+i+'">'+i+'</span></div><input type="text" class="form-control" name="policy" id="p_policy'+i+'"><div id="policy_remove_btn_div'+i+'"><a href="#aa" class="policy_remove_btn" id="a_policy_remove_btn"><i class="fa fa-minus ml-3"></i></a></div></div>'

          if(i==pcnt) {
            $('.policy_append').append(fieldHtml_with_btn);
          }
          else {
            $('.policy_append').append(fieldHtml);
          }
          document.getElementById("p_policy"+i).value = policyList[i-1];
        }
      });


})


$(function () {
  $('#datetimepicker1').datetimepicker({
    format: 'L'
  });
  $('#datetimepicker2').datetimepicker({
    format: 'L',
    useCurrent: false
  });
  $("#datetimepicker1").on("change.datetimepicker", function (e) {
    $('#datetimepicker2').datetimepicker('minDate', e.date);
  });
  $("#datetimepicker2").on("change.datetimepicker", function (e) {
    $('#datetimepicker1').datetimepicker('maxDate', e.date);
  });

  //timepicker
    $("#startTime1, #endTime1, #startTime2, #endTime2, #startTime3, #endTime3, #startTime4, #endTime4, #startTime5, #endTime5" ).timepicker({
        timeFormat: 'HH:mm',
        interval: 60,
        // minTime: '10',
        maxTime: '23:00pm',
        // defaultTime: '11',
        startTime: '00:00am',
        dynamic: false,
        dropdown: true,
        scrollbar: true
    });

});

$('#p_name').keyup(function(e) {
    var p_name = $(this).val();
    $('.title-count').html('('+p_name.length+' / 최대 100자)');
    if(p_name.length > 100) {
        $(this).val(p_name.substring(0,100));
        $('.title-count').html('(100 / 최대 100자)');
    }
});

$('#p_introduce').keyup(function(e) {
    var p_introduce = $(this).val();
    $('.introduce-count').html('('+p_introduce.length+' / 최대 500자)');
    if(p_introduce.length > 500) {
        $(this).val(p_introduce.substring(0,500));
        $('.introduce-count').html('(500 / 최대 500자)');
    }
})


