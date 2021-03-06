package com.jeongeun.project.springboot.service.user;

import com.jeongeun.project.springboot.config.auth.dto.SessionUser;
import com.jeongeun.project.springboot.domain.products.*;
import com.jeongeun.project.springboot.domain.reservation.Reservation;
import com.jeongeun.project.springboot.domain.reservation.ReservationRepository;
import com.jeongeun.project.springboot.domain.reservation.ReservationStatus;
import com.jeongeun.project.springboot.domain.user.User;
import com.jeongeun.project.springboot.domain.user.UserRepository;
import com.jeongeun.project.springboot.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final ProductsRepository productsRepository;
    private final ReservationRepository reservationRepository;
    private final ProductsReviewRepository reviewRepository;
    private final ProductsOptionRepository optionRepository;
    private final JavaMailSender javaMailSender;
    private final HttpSession httpSession;

    @Transactional
    public Long updateToSeller(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("There is no user where email = "+email)
        );
        user.updateToSeller();
        return user.getId();
    }

    @Transactional
    public Long undoEnrollSeller(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("There is no user where email = "+email)
        );
        user.undoEnrollSeller();
        return user.getId();
    }

    @Transactional
    public Long sendVerificationMail(String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(
                () -> new IllegalArgumentException("There is no user where email = "+userEmail)
        );
        Random random=new Random();
        String key="";  //????????????
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(userEmail);
        key = makeVerificationCode();
        user.updateVerificationCode(key);
        message.setSubject("space anywhere??? ???????????? ???????????????");
        message.setText("?????? ?????? : "+key);
        javaMailSender.send(message);
        return user.getId();
    }

    public String makeVerificationCode() {
        int codeLength = 8;
        final char[] codeTable =  { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
                'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
                'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
                'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
                'w', 'x', 'y', 'z', '!', '@', '#', '$', '%', '^', '&', '*',
                '1', '2', '3', '4', '5', '6', '7', '8', '9', '0' };

        Random random = new Random(System.currentTimeMillis());
        int tableLength = codeTable.length;
        StringBuffer buf = new StringBuffer();
        for(int i = 0; i < codeLength; i++) {
            buf.append(codeTable[random.nextInt(tableLength)]);
        }
        return buf.toString();
    }

    @Transactional
    public boolean checkEmailCode(String inputKey, String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(
                () -> new IllegalArgumentException("There is no user where email = "+userEmail)
        );
        String userKey = user.getVerificationCode();

        if(userKey.equals(inputKey)) {
            user.updateEmailVerified();
            user.updateToUser();
            return true;
        } else {
            return false;
        }
    }


    /* my page - ?????? ?????? */

    @Transactional(readOnly = true)
    public List<ReservationResponseDto> findUserReservation(String email) {
        // ???????????? ?????? ????????? ?????? ??????
        // reservation status??? finished??? ?????? ?????? ?????? ?????????
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("There is no user where email = "+email)
        );

        return reservationRepository.findByUserId(user.getId(), ReservationStatus.FINISHED.getKey()).stream()
                .map(ReservationResponseDto::new)
                .collect(Collectors.toList());
    }

    public List<ReservationListResponseDto> getUserReservationTableList(List<ReservationResponseDto> reservationList) {
        // ?????? ?????? ????????? ????????? ?????? ????????? ???????????? ????????? response dto ??????
        List<ReservationListResponseDto> dto = new ArrayList<>();
        for(int i=0;i<reservationList.size();i++) {
            // ????????? ?????????????????? hasReview??? ?????? false
            dto.add(new ReservationListResponseDto(reservationList.get(i), false));
        }
        return dto;
    }

    @Transactional(readOnly = true)
    public ReservationResponseDto findReservationById(Long rid) {
        Reservation r = reservationRepository.findById(rid).orElseThrow(
                () -> new IllegalArgumentException("There is no reservation where rid = "+rid)
        );
        return new ReservationResponseDto(r);
    }

    @Transactional(readOnly = true)
    public List<ReservationResponseDto> findUserPreviousReservation(String email) {
        /* ???????????? ?????? ?????? ????????? ?????? */
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("There is no user where email = "+email)
        );

        return reservationRepository.findPreviousByUserId(user.getId(), ReservationStatus.FINISHED.getKey()).stream()
                .map(ReservationResponseDto::new)
                .collect(Collectors.toList());
    }

    public List<ReservationListResponseDto> getUserPreviousReservationTableList(List<ReservationResponseDto> previousReservationList) {
        /* ???????????? ?????? ?????? ???????????? ????????? ????????? ????????? ?????? ?????? */
        List<ReservationListResponseDto> dto = new ArrayList<>();

        // ?????? ?????? ?????? ?????? ?????? ??????
        for(int i=0;i<previousReservationList.size();i++) {
            if (reviewRepository.countByReservationId(previousReservationList.get(i).getRid())>0) {
                // ?????? ????????? ?????? ????????? ?????? ???????????????
                dto.add(new ReservationListResponseDto(previousReservationList.get(i), true));
            }
            else {
                // ?????? ????????? ?????? ????????? ???????????? ?????? ????????? ?????????????????? ?????????
                dto.add(new ReservationListResponseDto(previousReservationList.get(i), false));
            }
        }
        return dto;
    }

    @Transactional(readOnly = true)
    public ReservationResponseDto findPreviousReservationById(Long rid) {
        Reservation r = reservationRepository.findById(rid).orElseThrow(
                () -> new IllegalArgumentException("There is no reservation where rid = "+rid)
        );
        return new ReservationResponseDto(r);
    }

    @Transactional(readOnly = true)
    public List<UserReviewListResponseDto> findAllUserReview() {

        /* ??????????????? ?????????????????? ???????????? ????????? ?????? ??????+????????? ????????? ?????? ?????? ???????????? ?????? ????????? ????????? */

        List<UserReviewListResponseDto> dtos = new ArrayList<>();

        User user = this.getUserByEmail();
        List<ProductsReview> reviewList = reviewRepository.findProductsReviewByUserId(user.getId());

        for(int i=0;i<reviewList.size();i++) {

            ProductsReview productsReview = reviewList.get(i);
            String pname = productsRepository.getProductsName(productsReview.getProducts().getP_id());
            Reservation reservation = reservationRepository.findByRid(productsReview.getReservationId());
            ProductsOption option = optionRepository.findProductsOption(reservation.getOptionId());

            List<Boolean> ratingArray = new ArrayList<>(5);
            int rating = (int)productsReview.getRating();
            for(int j=0;j<5;j++) {
                if(j<rating) {
                    ratingArray.add(true);
                }
                else {
                    ratingArray.add(false);
                }
            }

            dtos.add(new UserReviewListResponseDto(pname, reservation, option, user.getName(), productsReview, ratingArray));
        }

        return dtos;
    }

    @Transactional(readOnly = true)
    public ProductsListResponseDto findProductsByReviewId(Long reviewId) {

        /* reviewId??? product??? ????????? */

        ProductsReview review = reviewRepository.findProductsReviewByReviewId(reviewId);
        Products entity = productsRepository.findById(review.getProducts().getP_id()).orElseThrow(
                () -> new IllegalArgumentException("There is no product")
        );

        return new ProductsListResponseDto(entity);
    }

    @Transactional(readOnly = true)
    public ProductsReviewResponseDto findProductsReviewByReviewId(Long reviewId) {

        /* ?????? ????????? ?????? review id??? review ????????? ????????? */

        User user = this.getUserByEmail();
        ProductsReview review = reviewRepository.findProductsReviewByReviewId(reviewId);

        int rating = (int)review.getRating();
        List<Boolean> ratingArray = new ArrayList<>(5);
        for(int j=0;j<5;j++) {
            if(j<rating) {
                ratingArray.add(true);
            }
            else {
                ratingArray.add(false);
            }
        }
        return new ProductsReviewResponseDto(user.getName(), review, ratingArray);
    }

    @Transactional
    public Long deleteUserAccount() {

        /* ???????????? */

        User user = this.getUserByEmail();
        Long userId = user.getId();
        userRepository.delete(user);

        return userId;
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
