package com.jack_wang.service;

import com.jack_wang.po.Contact;

public interface ContactService {

    Contact saveContact(Contact contact);

    void sendEmail(Contact contact);
}
