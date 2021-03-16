package com.jeongeun.project.springboot.web;

//import com.webservice.rentalSpace.S3Uploader;

import com.jeongeun.project.springboot.config.auth.LoginUser;
import com.jeongeun.project.springboot.config.auth.dto.SessionUser;
import com.jeongeun.project.springboot.domain.reservation.Reservation;
import com.jeongeun.project.springboot.service.products.ProductsService;
import com.jeongeun.project.springboot.service.user.UserService;
import com.jeongeun.project.springboot.web.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final ProductsService productsService;
    private final UserService userService;
//    private final S3Uploader s3Uploader;

    @GetMapping("/")
    public String index(Model model, @LoginUser SessionUser user) {
        if(user != null) {
            model.addAttribute("user", user);
        }
        return "index";
    }

    @GetMapping("/space/save")
    public String spaceSave(Model model, @LoginUser SessionUser user) {
        if(user != null) {
            model.addAttribute("user", user);
        }
        return "products/space_save";
    }

//    @PostMapping("/upload")
//    @ResponseBody
//    public String upload(@RequestParam("data")MultipartFile multipartFile) throws IOException {
//        return s3Uploader.upload(multipartFile, "static");
//    }

    @RequestMapping("/products/{p_id}/imageInsert")
    public String insertProductsImage(@PathVariable Long p_id, Model model, @LoginUser SessionUser user) {
        if(user != null) {
            model.addAttribute("user", user);
        }
        model.addAttribute("p_id", p_id);
        return "products/space_image_save";
    }


    ////////// space List ////////////

    @GetMapping("/space/list/{pageNum}")
    public String spaceList(Model model, @PathVariable int pageNum, @LoginUser SessionUser user) {
        if(user != null) {
            model.addAttribute("user", user);
        }
        //product list
        model.addAttribute("products", productsService.getProductsList(pageNum));

        // page List
        // 전체 페이지 목록으로, 총 게시글 수로 페이지 목록 수 결정

        model.addAttribute("pageList", productsService.getPageList(pageNum));
        model.addAttribute("lastPageNum", productsService.getLastPageNum());
        addSidebarAttribute(model);
        return "products/space_list";
    }

    @RequestMapping("/space/list/search/{pageNum}")
    public String spaceListSearch(Model model, @RequestParam String spaceNameSearch, @PathVariable int pageNum, @LoginUser SessionUser user) {
        if(user != null) {
            model.addAttribute("user", user);
        }
        model.addAttribute("products", productsService.getProductsListByInput(pageNum, spaceNameSearch));
        model.addAttribute("pageList", productsService.getPageListByInput(pageNum, spaceNameSearch));
        model.addAttribute("lastPageNum", productsService.getLastPageNumByInput(spaceNameSearch));
        model.addAttribute("inputName", spaceNameSearch);
//        model.addAttribute("products", productsService.findAllByInput(spaceNameSearch));
        addSidebarAttribute(model);
        return "products/space_list_name";
    }

    @GetMapping("/space/list/search/{name}/{pageNum}")
    public String spaceListSearchName(Model model, @PathVariable String name, @PathVariable int pageNum, @LoginUser SessionUser user) {
        if(user != null) {
            model.addAttribute("user", user);
        }
        model.addAttribute("products", productsService.getProductsListByInput(pageNum, name));
        model.addAttribute("pageList", productsService.getPageListByInput(pageNum, name));
        model.addAttribute("lastPageNum", productsService.getLastPageNumByInput(name));
        model.addAttribute("inputName", name);
        addSidebarAttribute(model);
        return "products/space_list_name";
    }

    @GetMapping("/space/list/city/{city}/{pageNum}")
    public String spaceListCity(Model model, @PathVariable String city, @PathVariable int pageNum, @LoginUser SessionUser user) {
        if(user != null) {
            model.addAttribute("user", user);
        }
        addSidebarAttribute(model);

        model.addAttribute("products", productsService.getProductsListByCity(pageNum, city));
        model.addAttribute("pageList", productsService.getPageListByCity(pageNum, city));
        model.addAttribute("lastPageNum", productsService.getLastPageNumByCity(city));
        model.addAttribute("inputCity", city);
        return "products/space_list_city";
    }

    @GetMapping("/space/list/price/{priceIndex}/{pageNum}")
    public String spaceListPrice(Model model, @PathVariable int priceIndex, @PathVariable int pageNum, @LoginUser SessionUser user) {
        if(user != null) {
            model.addAttribute("user", user);
        }
        addSidebarAttribute(model);

        Integer[] priceList = {0, 10000, 35000, 70000, 100000};
        int min = priceList[priceIndex];
        int max = priceList[priceIndex+1];
        if(priceIndex==4) {
            model.addAttribute("products", productsService.findAllByPriceLast(min));
            model.addAttribute("pageList", productsService.getPageListByPriceLast(pageNum, min));
            model.addAttribute("lastPageNum", productsService.getLastPageNumByPriceLast(min));
        }
        else {
            model.addAttribute("products", productsService.getProductsListByPrice(pageNum, min, max));
            model.addAttribute("pageList", productsService.getPageListByPrice(pageNum, min, max));
            model.addAttribute("lastPageNum", productsService.getLastPageNumByPrice(min, max));
        }
        model.addAttribute("inputPriceIndex", priceIndex);
        return "products/space_list_price";
    }

    @GetMapping("/space/list/rating/{rating}/{pageNum}")
    public String spaceListRating(Model model, @PathVariable int rating, @PathVariable int pageNum, @LoginUser SessionUser user) {
        if(user != null) {
            model.addAttribute("user", user);
        }
        addSidebarAttribute(model);
        model.addAttribute("products", productsService.getProductsListByRating(pageNum, (double)rating));
        model.addAttribute("pageList", productsService.getPageListByRating(pageNum, (double)rating));
        model.addAttribute("lastPageNum", productsService.getLastPageNumByRating((double)rating));
        model.addAttribute("inputRating", rating);
        return "products/space_list_rating";
    }

    private void addSidebarAttribute(Model model) {
        //city sidebar num
        String[] cityList = {"서울특별시", "인천광역시", "대전광역시", "대구광역시", "부산광역시", "광주광역시", "울산광역시",
                "세종특별자치도", "경기도", "강원도", "충청북도", "충청남도", "전라북도", "전라남도", "경상북도", "경상남도", "제주도"
        };
        for(int i=0;i<17;i++) {
            model.addAttribute("city"+i, productsService.findEachNumByCity(cityList[i]));
        }
        // price sidebar num
        Integer[] priceList = {0, 10000, 35000, 70000, 100000};
        for(int i=0;i<5;i++) {
            if (i==4) {
                model.addAttribute("price"+i, productsService.findEachNumByPriceMax(priceList[i]));
            }
            else {
                model.addAttribute("price"+i, productsService.findEachNumByPrice(priceList[i], priceList[i+1]));
            }
        }
        // rating sidebar num
        for(int i=5;i>=0;i--) {
            model.addAttribute("rating"+i, productsService.findEachNumByRating((double)i));
        }
    }

    @GetMapping("/space/list/detail/{p_id}")
    public String spaceDetail(@PathVariable Long p_id, Model model, @LoginUser SessionUser user) {
        if(user != null) {
            model.addAttribute("user", user);
        }
//        model.addAttribute("thumbnail", productsService.findProductsThumbnailById(p_id));
//        model.addAttribute("files", productsService.findProductsFilesById(p_id));
        model.addAttribute("product", productsService.findProductsDetailById(p_id));
        model.addAttribute("facility", productsService.findProductsFacilityById(p_id));
        model.addAttribute("notice", productsService.findProductsNoticeById(p_id));
        model.addAttribute("policy", productsService.findProductsPolicyById(p_id));
        model.addAttribute("option", productsService.findAllProductsOptionById(p_id));
        model.addAttribute("bookMarked", productsService.isUserBookMarkedProducts(p_id));
        model.addAttribute("reviewList", productsService.findAllProductsReview(p_id));
        return "products/space_detail";
    }

    @GetMapping("/space/update/{p_id}")
    public String spaceUpdate(@PathVariable Long p_id, Model model, @LoginUser SessionUser user) {
        if(user != null) {
            model.addAttribute("user", user);
        }
        model.addAttribute("product", productsService.findById(p_id));
        return "products/space_update";
    }

    @GetMapping("/basicUser/login")
    public String login_user() {
        return "account/login_user";
    }

    @GetMapping("/seller/enroll")
    public String enroll_seller(Model model, @LoginUser SessionUser user) {
        if(user != null) {
            model.addAttribute("user", user);
        }
        return "account/enroll_seller";
    }

    @GetMapping("/isSeller/undo/enroll")
    public String undo_enroll_seller(Model model, @LoginUser SessionUser user)  {
        if(user != null) {
            model.addAttribute("user", user);
        }
        return "account/undo_enroll_seller";
    }


    //////////////////////////////////////
    // my page

    @GetMapping("/myPage/home")
    public String myPage_home(Model model, @LoginUser SessionUser user) {
        if(user != null) {
            model.addAttribute("user", user);
        }
        return "account/mypage_home";
    }

    @GetMapping("/myPage/user/checkEmail")
    public String myPage_checkEmail(Model model, @LoginUser SessionUser user) {
        if(user != null) {
            model.addAttribute("user", user);
        }
        return "account/mypage_check_email";
    }

    @GetMapping("/myPage/user/ongoingReservationList")
    public String mypage_ongoing_reservation_title(Model model, @LoginUser SessionUser user) {
        if(user != null) {
            model.addAttribute("user", user);
        }

        String userEmail = user.getEmail();
        List<ReservationResponseDto> userReservationList = userService.findUserReservation(userEmail);
        model.addAttribute("reservedList", userService.getUserReservationTableList(userReservationList));

        return "account/mypage_ongoing_reservation_list";
    }

    @GetMapping("/myPage/user/ongoingReservation/{rid}")
    public String mypage_ongoing_reservation(Model model, @PathVariable Long rid, @LoginUser SessionUser user) {
        if(user != null) {
            model.addAttribute("user", user);
        }

        model.addAttribute("res", userService.findReservationById(rid));

        model.addAttribute("option", productsService.findProductsOptionByRid(rid));
        return "account/mypage_ongoing_reservation";
    }

    @GetMapping("/myPage/user/previousReservationList")
    public String mypage_previous_reservation_title(Model model, @LoginUser SessionUser user) {
        /* 완료된 예약 현황 테이블 조회 페이지로 이동 */
        if(user != null) {
            model.addAttribute("user", user);
        }

        String userEmail = user.getEmail();
        List<ReservationResponseDto> userPreviousReservationList = userService.findUserPreviousReservation(userEmail);
        model.addAttribute("previousReservedList", userService.getUserPreviousReservationTableList(userPreviousReservationList));
        return "account/mypage_previous_reservation_list";
    }

    @GetMapping("/myPage/user/previousReservation/{rid}")
    public String mypage_previous_reservation(Model model, @PathVariable Long rid, @LoginUser SessionUser user) {
        if(user != null) {
            model.addAttribute("user", user);
        }

        model.addAttribute("res", userService.findPreviousReservationById(rid));
        model.addAttribute("option", productsService.findProductsOptionByRid(rid));
        return "account/mypage_previous_reservation";
    }

    @GetMapping("/myPage/user/bookMarkList")
    public String mypage_bookmarkList(Model model, @LoginUser SessionUser user) {
        if(user != null) {
            model.addAttribute("user", user);
        }
        List<BookMarkResponseDto> bookMarkList = productsService.findAllUserBookMark();
        model.addAttribute("bookMarkList", bookMarkList);
        return "account/mypage_bookmark_list";
    }

    @GetMapping("/mypage/user/writeReview/{rid}/{pid}")
    public String myage_writeReview(Model model, @PathVariable Long rid, @PathVariable Long pid, @LoginUser SessionUser user) {
        /* 리뷰 작성 페이지로 이동 */
        if(user != null) {
            model.addAttribute("user", user);
        }
        model.addAttribute("product", productsService.findById(pid));
        model.addAttribute("rid", rid);
        return "account/mypage_write_review";
    }



    @GetMapping("/space/list/detail/reservationOngoing/{month}/{day}/{year}/{p_id}/{po_id}/{reserveNum}")
    public String reservationOngoing(@PathVariable String month, @PathVariable String day, @PathVariable String year,
                                     @PathVariable Long p_id, @PathVariable Long po_id,
                                     @PathVariable int reserveNum, Model model, @LoginUser SessionUser user) {
        if(user != null) {
            model.addAttribute("user", user);
        }
        String inputDate = month+"/"+day+"/"+year;
        int totalPrice = productsService.calculateProductPrice(p_id, inputDate, po_id);
        int usingTime = productsService.findProductsOptionById(po_id).getUsingTime();
        model.addAttribute("price", totalPrice/usingTime);
        model.addAttribute("product", productsService.findById(p_id));
        model.addAttribute("option", productsService.findProductsOptionById(po_id));
        model.addAttribute("reserveNum", reserveNum);
        model.addAttribute("year", year);
        model.addAttribute("month", month);
        model.addAttribute("day", day);
        model.addAttribute("dayOfWeek", productsService.getDayInKorean(inputDate));
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("weekValue", productsService.findWeekOrWeekend(inputDate));

        return "products/reservation_ongoing";
    }

}

