package org.example.customersupportdb.service;

import org.example.customersupportdb.bean.Customer;

public interface CustomerService {

    Customer findCustomerByNameAndSurname(String name, String surname);
    Customer findCustomerById(Long id);
    Customer save(Customer customer);
}
