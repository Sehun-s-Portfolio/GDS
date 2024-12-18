package com.project.gds.email;

import com.project.gds.contact.request.ContactRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
@Service
public class MailService {

    private final JavaMailSender javaMailSender;

    // 단순 문자 메일 보내기
    public boolean sendSimpleEmail(ContactRequestDto contactRequestDto) {

        MimeMessage message = javaMailSender.createMimeMessage();

        try {

            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");

            // 다중 수신자 이메일
            //String[] sendToList = {"admin@gds-korea.com", "sales@gds-korea.com"};
            //messageHelper.setTo(sendToList);

            messageHelper.setTo("jaemuk.jeong@gds-korea.com"); // 단일 수신자 이메일
            //messageHelper.setTo("wlstpgns51@naver.com");
            messageHelper.setSubject("[문의] " + contactRequestDto.getContactor() + "님의 문의"); // 메일 제목
            messageHelper.setText(
                    " - 문의자 : " + contactRequestDto.getContactor() + "\n" +
                            " - 문의자 이메일 : " + contactRequestDto.getEmail() + "\n" +
                            " - 주소 : " + contactRequestDto.getAddress() + "\n" +
                            " - 전화번호 : " + contactRequestDto.getPhoneNumber() + "\n" +
                            " - 문의 내용 : " + "\n" + contactRequestDto.getAdditionalInfo() + "\n"
            );

            javaMailSender.send(message);
            return true;

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        /**
         try {
         // 단순 문자 메일을 보낼 수 있는 객체 생성
         SimpleMailMessage message = new SimpleMailMessage();

         message.setFrom(contactRequestDto.getEmail()); // 메일을 보낼 송신 이메일
         message.setTo("wlstpgns51@naver.com"); // 메일을 받을 목적지 이메일 (이후 GDS 메일 주소가 들어올 것)
         message.setSubject("[문의] " + contactRequestDto.getContactor() + "님의 문의"); // 메일 제목
         message.setText(
         " - 문의자 : " + contactRequestDto.getContactor() + "\n" +
         " - 주소 : " + contactRequestDto.getAddress() + "\n" +
         " - 전화번호 : " + contactRequestDto.getPhoneNumber() + "\n" +
         " - 문의 내용 : " + "\n" + contactRequestDto.getAdditionalInfo() + "\n"
         ); // 메일 내용

         javaMailSender.send(message);

         return true;
         } catch (MailSendException m){
         m.printStackTrace();

         return false;
         }
         **/
    }

    // HTML 메일 보내기
    public void sendHTMLEmail() {

    }

    // 6자리 랜덤 비밀번호 생성
    public void createRandomPw() {

    }
}
