package com.jack_wang.service.impl;

import com.jack_wang.dao.ContactRepository;
import com.jack_wang.po.Contact;
import com.jack_wang.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    JavaMailSenderImpl javaMailSender;

    @Override
    public Contact saveContact(Contact contact) {
        return contactRepository.save(contact);
    }

    public void sendEmail(Contact contact) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setSubject("Aboluo留言信息");
        mailMessage.setText("留言人姓名：" + contact.getName() + "\n" + "留言人Email：" + contact.getEmail() + "\n" + "信息：" + contact.getMessage());
        mailMessage.setTo("1318248167@qq.com");
        mailMessage.setFrom("702891393@qq.com");
        javaMailSender.send(mailMessage);
    }

}
