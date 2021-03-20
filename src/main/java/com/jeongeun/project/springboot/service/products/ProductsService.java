package com.jeongeun.project.springboot.service.products;

import com.jeongeun.project.springboot.config.auth.dto.SessionUser;
import com.jeongeun.project.springboot.domain.products.*;
import com.jeongeun.project.springboot.domain.reservation.Reservation;
import com.jeongeun.project.springboot.domain.reservation.ReservationRepository;
import com.jeongeun.project.springboot.domain.reservation.ReservationStatus;
import com.jeongeun.project.springboot.domain.user.User;
import com.jeongeun.project.springboot.domain.user.UserRepository;
import com.jeongeun.project.springboot.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductsService {
    private final HttpSession httpSession;
    private final UserRepository userRepository;
    private final ProductsRepository productsRepository;
    private final ProductsFacilityRepository productsFacilityRepository;
    private final ProductsNoticeRepository productsNoticeRepository;
    private final ProductsPolicyRepository productsPolicyRepository;
    private final ProductsOptionRepository productsOptionRepository;
    private final ReservationRepository reservationRepository;
    private final FilesRepository filesRepository;
    private final BookMarkRepository bookMarkRepository;
    private final ProductsReviewRepository reviewRepository;
    private final ProductsQARepository qaRepository;
    private static final int PAGE_POST_COUNT = 3; // 한 페이지에 존재하는 게시글 수
    private static final int BLOCK_PAGE_NUM = 5; // 한 블럭에 존재하는 페이지 수

    @Transactional
    public Long save(ProductsSaveRequestDto requestDto) {
        String email = ((SessionUser) httpSession.getAttribute("user")).getEmail();
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("There is no user")
        );
        Products products = requestDto.toEntity();
        products.setUser(user);
        productsRepository.save(products);

        List<String> facilities = requestDto.getFacility();
        for (String fac : facilities) {
            if (NotNullAndNotEmpty(fac)) {
                ProductsFacility f = ProductsFacility.builder()
                        .p_facility(fac)
                        .products(products)
                        .build();
                productsFacilityRepository.save(f);
            }
        }

        List<String> notices = requestDto.getNotice();
        for (String pno : notices) {
            if (NotNullAndNotEmpty(pno)) {
                ProductsNotice pn = ProductsNotice.builder()
                        .p_notice(pno)
                        .products(products)
                        .build();
                productsNoticeRepository.save(pn);
            }
        }

        List<String> policies = requestDto.getPolicy();
        for (String pol : policies) {
            if (NotNullAndNotEmpty(pol)) {
                ProductsPolicy p = ProductsPolicy.builder()
                        .p_policy(pol)
                        .products(products)
                        .build();
                productsPolicyRepository.save(p);
            }
        }

        List<String> optionTitle = requestDto.getOptionTitle();
        List<Integer> startTime = requestDto.getStartTime();
        List<Integer> endTime = requestDto.getEndTime();
        List<Integer> count = requestDto.getCount();
        int i = 0;
        for (String title : optionTitle) {
            if (NotNullAndNotEmpty(title)) {
                int stime = startTime.get(i);
                int etime = endTime.get(i);
                int utime;
                if (stime > etime) {
                    utime = (24 - stime) + etime;
                } else {
                    utime = etime - stime;
                }
                int cnt = count.get(i);
                ProductsOption p = ProductsOption.builder()
                        .optionTitle(title)
                        .startTime(stime)
                        .endTime(etime)
                        .usingTime(utime)
                        .availableCount(cnt)
                        .products(products)
                        .build();
                productsOptionRepository.save(p);
                i++;
            }
        }
        return products.getP_id();
    }

    @Transactional(readOnly = true)
    public ProductsListResponseDto findById(Long id) {
        Products entity = productsRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("There is no product which id=" + id)
        );
        return new ProductsListResponseDto(entity);
    }

    @Transactional(readOnly = true)
    public ProductsResponseDto findProductsDetailById(Long id) {
        Products entity = productsRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("There is no product which id=" + id)
        );
        return new ProductsResponseDto(entity);
    }

    @Transactional(readOnly = true)
    public FilesResponseDto findProductsThumbnailById(Long id) {
        Files f = filesRepository.findProductsThumbnail(id);
        return new FilesResponseDto(f);
    }

    @Transactional(readOnly = true)
    public List<FilesResponseDto> findProductsFilesById(Long id) {
        return filesRepository.findProductsFiles(id).stream()
                .map(FilesResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FacilityResponseDto> findProductsFacilityById(Long id) {
        return productsFacilityRepository.findProductsFacility(id).stream()
                .map(FacilityResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductsNoticeResponseDto> findProductsNoticeById(Long id) {
        return productsNoticeRepository.findProductsNotice(id).stream()
                .map(ProductsNoticeResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductsPolicyResponseDto> findProductsPolicyById(Long id) {
        return productsPolicyRepository.findProductsPolicy(id).stream()
                .map(ProductsPolicyResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductsOptionResponseDto> findAllProductsOptionById(Long id) {
        return productsOptionRepository.findAllProductsOption(id).stream()
                .map(ProductsOptionResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductsOptionResponseDto findProductsOptionById(Long id) {
        ProductsOption entity = productsOptionRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("There is no option")
        );
        return new ProductsOptionResponseDto(entity);
    }

    @Transactional(readOnly = true)
    public List<ProductsListResponseDto> getProductsList(int pageNum) {
        return productsRepository.findAll(PageRequest.of(pageNum-1, PAGE_POST_COUNT, Sort.by(Sort.Direction.DESC, "createdDate"))).stream()
                .map(ProductsListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductsListResponseDto> getProductsListByCity(int pageNum, String city) {
        Pageable pageable = PageRequest.of(pageNum-1, PAGE_POST_COUNT, Sort.by(Sort.Direction.DESC, "createdDate"));
        return productsRepository.findByProductsCity(city, pageable).stream()
                .map(ProductsListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductsListResponseDto> getProductsListByPrice(int pageNum, int min, int max) {
        Pageable pageable = PageRequest.of(pageNum-1, PAGE_POST_COUNT, Sort.by(Sort.Direction.DESC, "createdDate"));
        return productsRepository.findByWeekdayPrice(min, max, pageable).stream()
                .map(ProductsListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductsListResponseDto> getProductsListByRating(int pageNum, double rating) {
        Pageable pageable = PageRequest.of(pageNum-1, PAGE_POST_COUNT, Sort.by(Sort.Direction.DESC, "createdDate"));
        return productsRepository.findByProductsAvgRating(rating, rating+1.0, pageable).stream()
                .map(ProductsListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductsListResponseDto> getProductsListByInput(int pageNum, String input) {
        Pageable pageable = PageRequest.of(pageNum-1, PAGE_POST_COUNT, Sort.by(Sort.Direction.DESC, "createdDate"));
        return productsRepository.findByProductsName(input, pageable).stream()
                .map(ProductsListResponseDto::new)
                .collect(Collectors.toList());
    }

    private List<PageListResponseDto> commonGetPageList(int currentPageNum, int lastPageNum) {
        List<PageListResponseDto> pageList = new ArrayList<>();
        int inputCurrentPageNum = currentPageNum;
        int blockLastPageNum = (lastPageNum>currentPageNum+BLOCK_PAGE_NUM) ? currentPageNum+BLOCK_PAGE_NUM : lastPageNum;
        currentPageNum = (currentPageNum<=3) ? 1: currentPageNum-2;

        // 번호 할당
        for(int cur=currentPageNum, i=0;cur<=blockLastPageNum;cur++, i++) {
            if (i==5) {
                break;
            }
            if (cur==inputCurrentPageNum) {
                pageList.add(new PageListResponseDto(cur, true));
            }
            else {
                pageList.add(new PageListResponseDto(cur, false));
            }
        }
        return pageList;
    }

    public List<PageListResponseDto> getPageList(int currentPageNum) {
        int lastPageNum = this.getLastPageNum();
        return this.commonGetPageList(currentPageNum, lastPageNum);
    }

    public List<PageListResponseDto> getPageListByInput(int currentPageNum, String input) {
        int lastPageNum = this.getLastPageNumByInput(input);
        return this.commonGetPageList(currentPageNum, lastPageNum);
    }

    public List<PageListResponseDto> getPageListByCity(int currentPageNum, String city) {
        int lastPageNum = this.getLastPageNumByCity(city);
        return this.commonGetPageList(currentPageNum, lastPageNum);
    }

    public List<PageListResponseDto> getPageListByPrice(int currentPageNum, int min, int max) {
        int lastPageNum = this.getLastPageNumByPrice(min, max);
        return this.commonGetPageList(currentPageNum, lastPageNum);
    }

    public List<PageListResponseDto> getPageListByPriceLast(int currentPageNum, int max) {
        int lastPageNum = this.getLastPageNumByPriceLast(max);
        return this.commonGetPageList(currentPageNum, lastPageNum);
    }

    public List<PageListResponseDto> getPageListByRating(int currentPageNum, double rating) {
        int lastPageNum = this.getLastPageNumByRating(rating);
        return this.commonGetPageList(currentPageNum, lastPageNum);
    }

    public int getLastPageNum() {
        // 총 게시글 수
        double totalPostNum = Double.valueOf(this.getProductsCount());
        // 마지막 페이지 번호
        return (int)(Math.ceil(totalPostNum/PAGE_POST_COUNT));
    }

    public int getLastPageNumByInput(String input) {
        double totalPostNum = Double.valueOf(this.findEachNumByInput(input));
        return (int)(Math.ceil(totalPostNum/PAGE_POST_COUNT));
    }

    public int getLastPageNumByCity(String city) {
        double totalPostNum = Double.valueOf(this.findEachNumByCity(city));
//        double totalPostNum = Double.valueOf(this.getProductsCountByCity(city));
        return (int)(Math.ceil(totalPostNum/PAGE_POST_COUNT));
    }

    public int getLastPageNumByPrice(int min, int max) {
        double totalPostNum = Double.valueOf(this.findEachNumByPrice(min, max));
//        double totalPostNum = Double.valueOf(this.getProductsCountByPrice(min, max));
        return (int)(Math.ceil(totalPostNum/PAGE_POST_COUNT));
    }

    public int getLastPageNumByPriceLast(int max) {
        double totalPostNum = Double.valueOf(this.findEachNumByPriceMax(max));
//        double totalPostNum = Double.valueOf(this.getProductsCountByPriceLast(max));
        return (int)(Math.ceil(totalPostNum/PAGE_POST_COUNT));
    }

    public int getLastPageNumByRating(double rating) {
        double totalPostNum = Double.valueOf(this.findEachNumByRating(rating));
        return (int)(Math.ceil(totalPostNum/PAGE_POST_COUNT));
    }

    @Transactional
    public Long getProductsCount() {
        return productsRepository.count();
    }

    @Transactional(readOnly = true)
    public List<ProductsListResponseDto> findAllByInput(String input) {
        return productsRepository.findAllByInput(input).stream()
                .map(ProductsListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductsListResponseDto> findAllByCity(String city) {
        return productsRepository.findAllByCity(city).stream()
                .map(ProductsListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductsListResponseDto> findAllByPrice(int min, int max) {
        return productsRepository.findAllByPrice(min, max).stream()
                .map(ProductsListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductsListResponseDto> findAllByPriceLast(int max) {
        return productsRepository.findAllByPriceLast(max).stream()
                .map(ProductsListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductsListResponseDto> findAllByRating(double rating) {
        return productsRepository.findAllByRating(rating, rating + 1.0).stream()
                .map(ProductsListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void saveProductsImage(List<MultipartFile> filesList, Long p_id) throws Exception {

        Products products = productsRepository.findById(p_id)
                .orElseThrow(() -> new IllegalArgumentException("There is no product which id=" + p_id));

        String fileUrl = "C:/Users/Jeongeun/IdeaProjects/RentalSpace-webservice/src/main/resources/static/img/";
        int cnt = 0;
        for (MultipartFile files : filesList) {
            String sourceFileName = files.getOriginalFilename();
            if (NotNullAndNotEmpty(sourceFileName)) {
                String sourceFileNameExtension = FilenameUtils.getExtension(sourceFileName).toLowerCase();

                File destinationFile;
                String destinationFileName;

                do {
                    destinationFileName = RandomStringUtils.randomAlphanumeric(32) + "." + sourceFileNameExtension;
                    destinationFile = new File(fileUrl + destinationFileName);
                } while (destinationFile.exists());

                destinationFile.getParentFile().mkdirs();

                files.transferTo(destinationFile);
                Files f;
                if (cnt == 0) {
                    f = Files.builder()
                            .fileName(destinationFileName)
                            .fileOriginalName(sourceFileName)
                            .fileUrl(fileUrl)
                            .isThumbnail(true)
                            .products(products)
                            .build();
                } else {
                    f = Files.builder()
                            .fileName(destinationFileName)
                            .fileOriginalName(sourceFileName)
                            .fileUrl(fileUrl)
                            .isThumbnail(false)
                            .products(products)
                            .build();
                }
                filesRepository.save(f);
                cnt++;
            }

        }

    }

    @Transactional
    public void deleteSpace(Long id) {
        Products products = productsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("There is no product which id=" + id));

        productsRepository.delete(products);
    }

    @Transactional
    public Long update(Long id, ProductsUpdateRequestDto requestDto) {
        Products products = productsRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("There is no products which id=" + id)
        );

        products.update(requestDto.getP_name(), requestDto.getP_postcode(), requestDto.getP_address(), requestDto.getP_detailAddress(),
                requestDto.getP_city(), requestDto.getP_weekdayPrice(), requestDto.getP_weekendPrice(), requestDto.getP_introduce(), requestDto.getP_maxNum());

        productsFacilityRepository.deleteProductsFacility(id);
        productsNoticeRepository.deleteProductsNotice(id);
        productsPolicyRepository.deleteProductsPolicy(id);

        List<String> facilities = requestDto.getFacility();
        for (String f : facilities) {
            ProductsFacility pf = ProductsFacility.builder()
                    .p_facility(f)
                    .products(products)
                    .build();
            productsFacilityRepository.save(pf);
        }

        List<String> notices = requestDto.getNotice();
        for (String pno : notices) {
            ProductsNotice pn = ProductsNotice.builder()
                    .p_notice(pno)
                    .products(products)
                    .build();
            productsNoticeRepository.save(pn);
        }

        List<String> policies = requestDto.getPolicy();
        for (String pol : policies) {
            ProductsPolicy p = ProductsPolicy.builder()
                    .p_policy(pol)
                    .products(products)
                    .build();
            productsPolicyRepository.save(p);
        }
        return id;
    }

    // sidebar 관련
    @Transactional(readOnly = true)
    public int findEachNumByCity(String p_city) {
        return productsRepository.findEachNumByCity(p_city);
    }

    @Transactional(readOnly = true)
    public int findEachNumByPrice(int min, int max) {
        return productsRepository.findEachNumByPrice(min, max);
    }

    @Transactional(readOnly = true)
    public int findEachNumByPriceMax(int max) {
        return productsRepository.findEachNumByPriceLast(max);
    }

    @Transactional(readOnly = true)
    public int findEachNumByRating(double p_avgRating) {
        return productsRepository.findEachNumByRating(p_avgRating, p_avgRating + 1.0);
    }

    @Transactional(readOnly = true)
    public int findEachNumByInput(String input) {
        return productsRepository.findEachNumByInput(input);
    }

    @Transactional(readOnly = true)
    public int calculateProductPrice(Long p_id, String inputDate, Long po_id) {
        Products products = productsRepository.findById(p_id)
                .orElseThrow(() -> new IllegalArgumentException("There is no product"));
        //01/29/2020
//        int year = Integer.parseInt(inputDate.substring(6,10));
//        int month = Integer.parseInt(inputDate.substring(0,2));
//        int day = Integer.parseInt(inputDate.substring(3,5));
//        LocalDate date = LocalDate.of(year, month, day);
//        DayOfWeek week = date.getDayOfWeek();

        int price;
        ProductsOption option = productsOptionRepository.findById(po_id)
                .orElseThrow(() -> new IllegalArgumentException("There is no option"));
        int usingTime = option.getUsingTime();

        if (findWeekOrWeekend(inputDate).equals("주말")) {
            int weekendPrice = products.getWeekendPrice();
            price = weekendPrice * usingTime;
        } else {
            int weekPrice = products.getWeekdayPrice();
            price = weekPrice * usingTime;
        }
        return price;
    }

    @Transactional(readOnly = true)
    public boolean checkReservationIsOk(Long p_id, String inputDate, Long po_id) {
        int maxCount = productsOptionRepository.findOptionAvailableCountByOptionId(po_id);
        int year = splitDate(inputDate)[0];
        int month = splitDate(inputDate)[1];
        int day = splitDate(inputDate)[2];
        int reservedCount = reservationRepository.findReservedCountByOptionId(year, month, day, po_id);
        if (reservedCount < maxCount) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean NotNullAndNotEmpty(String str) {
        if (str != null && !str.isEmpty())
            return true;
        return false;
    }

    public String findWeekOrWeekend(String inputDate) {
        int year = Integer.parseInt(inputDate.substring(6, 10));
        int month = Integer.parseInt(inputDate.substring(0, 2));
        int day = Integer.parseInt(inputDate.substring(3, 5));
        LocalDate date = LocalDate.of(year, month, day);
        DayOfWeek week = date.getDayOfWeek();
        if (week.equals(DayOfWeek.SATURDAY) || week.equals(DayOfWeek.SUNDAY)) {
            return "주말";
        } else {
            return "주중";
        }
    }

    public String getDayInKorean(String inputDate) {
        int year = splitDate(inputDate)[0];
        int month = splitDate(inputDate)[1];
        int day = splitDate(inputDate)[2];
        LocalDate date = LocalDate.of(year, month, day);
        DayOfWeek dow = date.getDayOfWeek();
        String val = "";
        switch (dow) {
            case MONDAY:
                val = "월요일";
                break;
            case TUESDAY:
                val = "화요일";
                break;
            case WEDNESDAY:
                val = "수요일";
                break;
            case THURSDAY:
                val = "목요일";
                break;
            case FRIDAY:
                val = "금요일";
                break;
            case SATURDAY:
                val = "토요일";
                break;
            case SUNDAY:
                val = "일요일";
                break;
        }
        return val;
    }

    private static int[] splitDate(String date) {
        int temp[] = new int[3];
        temp[0] = Integer.parseInt(date.substring(6, 10)); //year
        temp[1] = Integer.parseInt(date.substring(0, 2)); //month
        temp[2] = Integer.parseInt(date.substring(3, 5)); //day
        return temp;
    }

    @Transactional
    public Long saveReservation(ReservationSaveRequestDto requestDto) {
        String email = ((SessionUser) httpSession.getAttribute("user")).getEmail();
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("no user")
        );
        Long userId = user.getId();
        Products products = productsRepository.findById(requestDto.getProductId()).orElseThrow(
                () -> new IllegalArgumentException("no product")
        );
        Long sellerId = products.getSellerId();

        Reservation reservation = requestDto.toEntity(sellerId, userId);
        reservation.setProducts(products);
        reservationRepository.save(reservation);

        return reservation.getRid();
    }

    @Transactional
    public ProductsOptionResponseDto findProductsOptionByRid(Long rid) {
        Reservation r = reservationRepository.findById(rid).orElseThrow(() -> new IllegalArgumentException("There is no reservation where rid="+rid));
        Long po_id = r.getOptionId();
        ProductsOption po = productsOptionRepository.findById(po_id).orElseThrow(() -> new IllegalArgumentException("There is no option where po_id="+po_id));
        return new ProductsOptionResponseDto(po);
    }

    @Transactional
    public boolean cancelReservation(Long rid) {
        boolean result = false;
        Reservation r = reservationRepository.findById(rid).orElseThrow(() -> new IllegalArgumentException("There is no reservation where rid="+rid));
        if (r.getReservationStatus() == ReservationStatus.APPLIED){
            reservationRepository.delete(r);
            result = true;
        }
        return result;
    }

    @Transactional(readOnly = true)
    public boolean isUserBookMarkedProducts(Long pid) {
        String email = ((SessionUser) httpSession.getAttribute("user")).getEmail();
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("no user")
        );
        Long result = bookMarkRepository.findUserBookMarkedProducts(user.getId(), pid);
        if (result>0) {
            return true;
        }
        else {
            return false;
        }
    }

    @Transactional
    public Long saveBookMark(Long pid) {
        String email = ((SessionUser) httpSession.getAttribute("user")).getEmail();
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("There is no user")
        );
        Long id = user.getId();

        Products products = productsRepository.findById(pid).orElseThrow(
                () -> new IllegalArgumentException("no product")
        );

        BookMark bookMark = new BookMarkRequestDto(id).toEntity();

        bookMark.setProducts(products);

        bookMarkRepository.save(bookMark);
        return bookMark.getBmid();
    }

    @Transactional
    public Long deleteBookMark(Long pid) {
        String email = ((SessionUser) httpSession.getAttribute("user")).getEmail();
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("There is no user")
        );

        BookMark bookMark = bookMarkRepository.findBookMark(user.getId(), pid);
        bookMarkRepository.delete(bookMark);
        return pid;
    }

    @Transactional(readOnly = true)
    public List<BookMarkResponseDto> findAllUserBookMark() {
        String email = ((SessionUser) httpSession.getAttribute("user")).getEmail();
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("There is no user")
        );
        List<BookMark> bookMarkList = bookMarkRepository.findBookMarkByBmuid(user.getId());
        List<BookMarkResponseDto> bookMarkResponseDtos = new ArrayList<>();
        for(int i=0;i<bookMarkList.size();i++) {
            BookMarkResponseDto dto = new BookMarkResponseDto((i+1),bookMarkList.get(i), productsRepository.getProductsName(bookMarkList.get(i).getProducts().getP_id()));
            bookMarkResponseDtos.add(dto);
        }

        return bookMarkResponseDtos;
    }


    /* 리뷰 */

    @Transactional(readOnly = true)
    public List<ProductsReviewResponseDto> findAllProductsReview(Long p_id) {

        /* 해당 상품의 리뷰들을 전부 가져옴 */

        List<ProductsReview> productsReviewList = reviewRepository.findProductsReviewByPid(p_id);

        List<ProductsReviewResponseDto> productsReviewResponseDtos = new ArrayList<>();

        for(int i=0;i<productsReviewList.size();i++) {
            ProductsReview productsReview = productsReviewList.get(i);

            String userName = userRepository.findUserNameById(productsReview.getUserId());

            int rating = (int)productsReview.getRating();
            List<Boolean> ratingArray = new ArrayList<>(5);
            for(int j=0;j<5;j++) {
                if(j<rating) {
                    ratingArray.add(true);
                }
                else {
                    ratingArray.add(false);
                }
            }

            productsReviewResponseDtos.add(new ProductsReviewResponseDto(userName, productsReview, ratingArray));
        }

        return productsReviewResponseDtos;
    }

    @Transactional
    public Long saveProductsReview(ProductsReviewSaveRequestDto dto, Long rid) {

        /* my page 지난 예약 리스트에서 예약 finished 건의 상품에대한 리뷰 작성후 저장 */

        Long uid = this.getUserByEmail().getId();
        Products products = productsRepository.findById(dto.getPid()).orElseThrow(
                () -> new IllegalArgumentException("There is no product")
        );
        ProductsReview review =dto.toEntity(uid, rid);
        review.setProducts(products);
        reviewRepository.save(review);
        return review.getReviewId();
    }

    @Transactional
    public Long updateProductsReview(ProductsReviewUpdateRequestDto dto, Long rid) {

        /* 리뷰 수정 페이지에서 수정 완료 버튼 클릭시 update 실행 */

        ProductsReview review = reviewRepository.findProductsReviewByReviewId(rid);
        review.update(dto.getContent(), dto.getRating());

        return rid;
    }

    @Transactional
    public void deleteProductsReview(Long rid) {

        /* 리뷰 삭제 확인 페이지에서 삭제 버튼 클릭시 delete 실행 */

        ProductsReview review = reviewRepository.findProductsReviewByReviewId(rid);
        reviewRepository.delete(review);
    }



    /* Q&A */

    @Transactional
    public Long saveProductsQA(ProductsQASaveRequestDto dto) {

        /* 상품 detail page 에서 해당 상품에 대한 Q&A 작성후(원글에 해당) 저장 */

        Long uid = this.getUserByEmail().getId();
        Products products = productsRepository.findById(dto.getPid()).orElseThrow(
                () -> new IllegalArgumentException("There is no product")
        );

        // 원글일때 설정되는 값
        int originNo;
        int groupOrder = 0;
        int groupLayer = 0;

        ProductsQA productsQA = dto.toEntity(uid, groupOrder, groupLayer);
        productsQA.setProducts(products);

        ProductsQA savedQA = qaRepository.saveAndFlush(productsQA);
        Long qaId = savedQA.getQaId();

        qaRepository.updateOriginNo(qaId);

        return productsQA.getQaId();
    }

    @Transactional(readOnly = true)
    public List<ProductsQAResponseDto> findAllProductsQA(Long pid) {

        /* 해당 상품에 대한 모든 qa 데이터를 가져옴 */
        /* 각 질문에 대한 답변도 같이 가져옴 */

        List<ProductsQA> originProductsQAList = qaRepository.findOriginProductsQA(pid,0);
        List<ProductsQAResponseDto> responseDtoList = new ArrayList<>();

        for(int i=0;i<originProductsQAList.size();i++) {
            ProductsQA qa = originProductsQAList.get(i);

            // 원글 데이터
            String userName = userRepository.findUserNameById(qa.getUserId());
            LocalDateTime dateTime = qa.getModifiedDate();
            int year = dateTime.getYear();
            int month = dateTime.getMonthValue();
            int day = dateTime.getDayOfMonth();
            boolean isSecret = false;
            if(qa.getIsSecret()==1) {
                isSecret = true;
            }

            // 원글에대한 답변 데이터
            Long originNo = qa.getOriginNo();
            boolean hasReply = false;
            String replyContent = "";
            int replyYear = 0;
            int replyMonth = 0;
            int replyDay = 0;

            ProductsQA replyQa = qaRepository.getReplyProductsQA(pid, 1, originNo);
            if (replyQa == null) {
            }
            else {
                hasReply = true;
                replyContent = replyQa.getContent();
                LocalDateTime replyDateTime = replyQa.getModifiedDate();
                replyYear = replyDateTime.getYear();
                replyMonth = replyDateTime.getMonthValue();
                replyDay = replyDateTime.getDayOfMonth();
            }

            responseDtoList.add(new ProductsQAResponseDto(userName, year, month, day, isSecret, qa,
                    hasReply, replyContent, replyYear, replyMonth, replyDay));
        }

        return responseDtoList;
    }

    @Transactional
    public Long saveProductsQAReply(Long qaId, String content) {
        Long uid = this.getUserByEmail().getId();
        ProductsQA qa = qaRepository.findProductsQaById(qaId);

        Long pid = qa.getProducts().getP_id();

        Long originNo = qa.getOriginNo();
        int originGroupOrder = qa.getGroupOrder();
        int originGroupLayer = qa.getGroupLayer();
        int originIsSecret = qa.getIsSecret();

        ProductsQA reply = ProductsQA.builder()
                .userId(uid)
                .content(content)
                .groupOrder(originGroupOrder+1)
                .groupLayer(originGroupLayer+1)
                .isSecret(originIsSecret)
                .build();

        reply.setProducts(qa.getProducts());
        reply.setOriginNo(originNo);

        qaRepository.save(reply);

        return pid;
    }



    @Transactional
    private User getUserByEmail() {
        String email = ((SessionUser) httpSession.getAttribute("user")).getEmail();
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("There is no user")
        );
        return user;
    }

}
