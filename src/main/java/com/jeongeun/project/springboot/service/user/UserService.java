package com.jeongeun.project.springboot.service.user;

import com.jeongeun.project.springboot.domain.products.Products;
import com.jeongeun.project.springboot.domain.products.ProductsRepository;
import com.jeongeun.project.springboot.domain.reservation.Reservation;
import com.jeongeun.project.springboot.domain.reservation.ReservationRepository;
import com.jeongeun.project.springboot.domain.user.User;
import com.jeongeun.project.springboot.domain.user.UserRepository;
import com.jeongeun.project.springboot.web.dto.ProductsListResponseDto;
import com.jeongeun.project.springboot.web.dto.ReservationListResponseDto;
import com.jeongeun.project.springboot.web.dto.ReservationResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final JavaMailSender javaMailSender;

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

    @Transactional(readOnly = true)
    public List<ReservationResponseDto> findUserReservation(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("There is no user where email = "+email)
        );
        List<Reservation> userReservationList = reservationRepository.findByUserId(user.getId());

        return reservationRepository.findByUserId(user.getId()).stream()
                .map(ReservationResponseDto::new)
                .collect(Collectors.toList());
    }

    public List<ReservationListResponseDto> getUserReservationTableList(List<ReservationResponseDto> reservationList) {
        List<ReservationListResponseDto> dto = new ArrayList<>();
        for(int i=0;i<reservationList.size();i++) {
            dto.add(new ReservationListResponseDto(reservationList.get(i)));
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
}
