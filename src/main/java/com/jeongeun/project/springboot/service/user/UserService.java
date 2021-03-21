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
        String key="";  //인증번호
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(userEmail);
        key = makeVerificationCode();
        user.updateVerificationCode(key);
        message.setSubject("space anywhere의 인증번호 메일입니다");
        message.setText("인증 번호 : "+key);
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


    /* my page - 예약 현황 */

    @Transactional(readOnly = true)
    public List<ReservationResponseDto> findUserReservation(String email) {
        // 사용자의 예약 현황을 전체 조회
        // reservation status가 finished가 아닌 모든 행을 가져옴
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("There is no user where email = "+email)
        );

        return reservationRepository.findByUserId(user.getId(), ReservationStatus.FINISHED.getKey()).stream()
                .map(ReservationResponseDto::new)
                .collect(Collectors.toList());
    }

    public List<ReservationListResponseDto> getUserReservationTableList(List<ReservationResponseDto> reservationList) {
        // 예약 현황 테이블 출력을 위한 정보만 가져와서 새로운 response dto 리턴
        List<ReservationListResponseDto> dto = new ArrayList<>();
        for(int i=0;i<reservationList.size();i++) {
            // 예약이 진행중이므로 hasReview는 모두 false
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
        /* 사용자의 지난 예약 현황들 조회 */
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("There is no user where email = "+email)
        );

        return reservationRepository.findPreviousByUserId(user.getId(), ReservationStatus.FINISHED.getKey()).stream()
                .map(ReservationResponseDto::new)
                .collect(Collectors.toList());
    }

    public List<ReservationListResponseDto> getUserPreviousReservationTableList(List<ReservationResponseDto> previousReservationList) {
        /* 사용자의 지난 예약 현황들중 출력에 필요한 정보만 담아 리턴 */
        List<ReservationListResponseDto> dto = new ArrayList<>();

        // 리뷰 작성 버튼 출력 여부 확인
        for(int i=0;i<previousReservationList.size();i++) {
            if (reviewRepository.countByReservationId(previousReservationList.get(i).getRid())>0) {
                // 해당 예약에 대해 리뷰를 이미 작성한경우
                dto.add(new ReservationListResponseDto(previousReservationList.get(i), true));
            }
            else {
                // 해당 예약에 대해 리뷰를 작성하지 않은 경우로 리뷰작성버튼 보여줌
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

        /* 마이페이지 리뷰관리에서 사용자가 작성한 모든 리뷰+리뷰를 작성한 해당 상품 예약건에 대한 정보를 가져옴 */

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

        /* reviewId로 product를 가져옴 */

        ProductsReview review = reviewRepository.findProductsReviewByReviewId(reviewId);
        Products entity = productsRepository.findById(review.getProducts().getP_id()).orElseThrow(
                () -> new IllegalArgumentException("There is no product")
        );

        return new ProductsListResponseDto(entity);
    }

    @Transactional(readOnly = true)
    public ProductsReviewResponseDto findProductsReviewByReviewId(Long reviewId) {

        /* 리뷰 수정을 위해 review id로 review 정보를 가져옴 */

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

        /* 회원탈퇴 */

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
