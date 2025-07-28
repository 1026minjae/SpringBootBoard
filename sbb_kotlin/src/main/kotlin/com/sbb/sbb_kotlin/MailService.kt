package com.sbb.sbb_kotlin

import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service

@Service
class MailService(
    private val mailSender: JavaMailSender
) {

    fun sendTempPassword(email: String, tempPassword: String) {
        val message = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, false, "UTF-8")

        helper.setTo(email)
        helper.setSubject("[SBB] Here is your temporary password")
        helper.setText("""
            Hello.
            
            Your temporary password is below:
            
            $tempPassword
            
            Your original password is expired, so please change your password after log-in.
        """.trimIndent(), false)

        mailSender.send(message)
    }
}