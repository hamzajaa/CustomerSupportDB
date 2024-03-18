package org.example.customersupportdb.service.impl;

import org.example.customersupportdb.bean.Customer;
import org.example.customersupportdb.dao.CustomerDao;
import org.example.customersupportdb.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerDao customerDao;

    @Override
    public Customer findCustomerByNameAndSurname(String name, String surname) {
        return customerDao.findCustomerByNameAndSurname(name, surname);
    }

    @Override
    public Customer findCustomerById(Long id) {
        return customerDao.findById(id).orElse(null);
    }

    @Override
    public Customer save(Customer customer) {
        return customerDao.save(customer);
    }
}
