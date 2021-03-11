package com.jeongeun.project.springboot.web;

//import com.webservice.rentalSpace.S3Uploader;

import com.jeongeun.project.springboot.config.auth.LoginUser;
import com.jeongeun.project.springboot.config.auth.dto.SessionUser;
import com.jeongeun.project.springboot.service.products.ProductsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final ProductsService productsService;
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



    @RequestMapping("/space/list/search")
    public String spaceListSearch(Model model, @RequestParam String spaceNameSearch, @LoginUser SessionUser user) {
        System.out.println("search >>>>>> "+spaceNameSearch);
        if(user != null) {
            model.addAttribute("user", user);
        }
        model.addAttribute("products", productsService.findAllByInput(spaceNameSearch));
        addSidebarAttribute(model);
        return "products/space_list";
    }

    @GetMapping("/space/list/city/{city}")
    public String spaceListCity(Model model, @PathVariable String city, @LoginUser SessionUser user) {
        if(user != null) {
            model.addAttribute("user", user);
        }
        addSidebarAttribute(model);
        model.addAttribute("products", productsService.findAllByCity(city));
        return "products/space_list";
    }

//    @GetMapping("/space/list/category/{category}")
//    public String spaceListCategory(Model model, @PathVariable String category, @LoginUser SessionUser user) {
//        if(user != null) {
//            model.addAttribute("user", user);
//        }
//        addSidebarAttribute(model);
//        model.addAttribute("products", productsService.findAllByCategory(category));
//        return "products/space_list";
//    }

    @GetMapping("/space/list/price/{priceIndex}")
    public String spaceListPrice(Model model, @PathVariable int priceIndex, @LoginUser SessionUser user) {
        if(user != null) {
            model.addAttribute("user", user);
        }
        addSidebarAttribute(model);

        Integer[] priceList = {0, 10000, 35000, 70000, 100000};
        if(priceIndex==4) {
            model.addAttribute("products", productsService.findAllByPriceLast(priceList[priceIndex]));
        }
        else {
            model.addAttribute("products", productsService.findAllByPrice(priceList[priceIndex], priceList[priceIndex+1]));
        }
        return "products/space_list";
    }

    @GetMapping("/space/list/rating/{rating}")
    public String spaceListRating(Model model, @PathVariable int rating, @LoginUser SessionUser user) {
        if(user != null) {
            model.addAttribute("user", user);
        }
        addSidebarAttribute(model);
        model.addAttribute("products", productsService.findAllByRating((double)rating));
        return "products/space_list";
    }

    private void addSidebarAttribute(Model model) {
        //city sidebar num
        String[] cityList = {"서울특별시", "인천광역시", "대전광역시", "대구광역시", "부산광역시", "광주광역시", "울산광역시",
                "세종특별자치도", "경기도", "강원도", "충청북도", "충청남도", "전라북도", "전라남도", "경상북도", "경상남도", "제주도"
        };
        for(int i=0;i<17;i++) {
            model.addAttribute("city"+i, productsService.findEachNumByCity(cityList[i]));
        }
        //category sidebar num
//        String[] categoryList = {"악기", "녹음", "댄스", "뮤지컬", "쿠킹", "스터디"};
//        for(int i=0;i<6;i++) {
//            model.addAttribute("category"+i, productsService.findEachNumByCategory(categoryList[i]));
//        }
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
        model.addAttribute("product", productsService.findById(p_id));
        model.addAttribute("facility", productsService.findProductsFacilityById(p_id));
        model.addAttribute("notice", productsService.findProductsNoticeById(p_id));
        model.addAttribute("policy", productsService.findProductsPolicyById(p_id));
        model.addAttribute("option", productsService.findAllProductsOptionById(p_id));

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

