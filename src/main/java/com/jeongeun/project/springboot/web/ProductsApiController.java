package com.jeongeun.project.springboot.web;

import com.jeongeun.project.springboot.service.products.ProductsService;
import com.jeongeun.project.springboot.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RequiredArgsConstructor
@RestController
public class ProductsApiController {
    private final ProductsService productsService;

    //save
    @PostMapping("/api/v1/products")
    public Long save(@RequestBody ProductsSaveRequestDto requestDto) {

        Long pid = productsService.save(requestDto);
        return pid;
    }

    @RequestMapping("/api/v1/products/{p_id}/imageInsert")
    public RedirectView imageInsert(HttpServletRequest request, @PathVariable Long p_id, @RequestPart List<MultipartFile> files) throws Exception {
        productsService.saveProductsImage(files, p_id);
        return new RedirectView("/");
    }

    //update
    @PutMapping("/api/v1/products/{id}")
    public Long updateSpace(@PathVariable Long id, @RequestBody ProductsUpdateRequestDto requestDto) {
        return productsService.update(id, requestDto);
    }

    @DeleteMapping("/api/v1/products/delete/{id}")
    public Long deleteSpace(@PathVariable Long id) {
        productsService.deleteSpace(id);
        return id;
    }

    @GetMapping("/api/v1/products/previewPrice")
    public String previewProductPrice(Long p_id, String inputDate, Long po_id) {
        int calculatedPrice = productsService.calculateProductPrice(p_id, inputDate, po_id);
        String result = Integer.toString(calculatedPrice);
        return result;
    }

    @GetMapping("/api/v1/products/checkReservationIsOk")
    public boolean checkReservationIsOk(Long p_id, String inputDate, Long po_id) {
        boolean result = productsService.checkReservationIsOk(p_id, inputDate, po_id);
        return result;
    }

    @PostMapping("/api/v1/products/reservation/ongoing")
    public Long saveReservation(@RequestBody ReservationSaveRequestDto requestDto) {
        return productsService.saveReservation(requestDto);
    }

    @DeleteMapping("/api/v1/products/reservation/cancel/{rid}")
    public String cancelReservation(@PathVariable Long rid) {
        String result="";
        if (productsService.cancelReservation(rid)) {
            result = "true";
        }
        else {
            result = "false";
        }
        return result;
    }

    /* 상품 즐겨찾기 */

    @PostMapping("/api/v1/products/saveBookMark/{pid}")
    public Long saveBookMark(@PathVariable Long pid) {
        return productsService.saveBookMark(pid);
    }

    @DeleteMapping("/api/v1/products/deleteBookMark/{pid}")
    public Long deleteBookMark(@PathVariable Long pid) {
        return productsService.deleteBookMark(pid);
    }



    /* 리뷰 */

    @PostMapping("/api/v1/products/saveReview/{rid}")
    public Long saveReview(@RequestBody ProductsReviewSaveRequestDto requestDto, @PathVariable Long rid) {
        /* 리뷰 등록 */
        return productsService.saveProductsReview(requestDto, rid);
    }

    @PutMapping("/api/v1/products/updateReview/{rid}")
    public Long updateReview(@PathVariable Long rid, @RequestBody ProductsReviewUpdateRequestDto dto) {
        /* 리뷰 수정 */
        return productsService.updateProductsReview(dto, rid);
    }

    @DeleteMapping("/api/v1/products/deleteReview/{rid}")
    public Long deleteReview(@PathVariable Long rid) {
        /* 리뷰 삭제 */
        productsService.deleteProductsReview(rid);
        return rid;
    }

    /* Q&A */

    @PostMapping("/api/v1/products/saveQA")
    public Long saveQA(@RequestBody ProductsQASaveRequestDto requestDto) {
        /* 상품 페이지에서 Q&A 등록 */
        return productsService.saveProductsQA(requestDto);
    }
}
