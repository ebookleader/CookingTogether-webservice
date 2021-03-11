package com.jeongeun.project.springboot.service.products;

import com.jeongeun.project.springboot.config.auth.dto.SessionUser;
import com.jeongeun.project.springboot.domain.products.*;
import com.jeongeun.project.springboot.domain.reservation.Reservation;
import com.jeongeun.project.springboot.domain.reservation.ReservationRepository;
import com.jeongeun.project.springboot.domain.user.User;
import com.jeongeun.project.springboot.domain.user.UserRepository;
import com.jeongeun.project.springboot.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.time.DayOfWeek;
import java.time.LocalDate;
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
    public ProductsResponseDto findById(Long id) {
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
    public List<ProductsListResponseDto> findAllDesc() {
        return productsRepository.findAllDesc().stream()
                .map(ProductsListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductsListResponseDto> findAllByInput(String input) {
        return productsRepository.findAllByInput(input).stream()
                .map(ProductsListResponseDto::new)
                .collect(Collectors.toList());
    }

//    @Transactional(readOnly = true)
//    public List<ProductsListResponseDto> findAllByCategory(String category) {
//        return productsRepository.findAllByCategory(category).stream()
//                .map(ProductsListResponseDto::new)
//                .collect(Collectors.toList());
//    }

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

//    @Transactional(readOnly = true)
//    public int findEachNumByCategory(String p_category) {
//        return productsRepository.findEachNumByCategory(p_category);
//    }

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
            int weekendPrice = products.getP_weekendPrice();
            price = weekendPrice * usingTime;
        } else {
            int weekPrice = products.getP_weekdayPrice();
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


}
