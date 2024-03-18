package org.example.customersupportdb.dao;

import org.example.customersupportdb.bean.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerDao extends JpaRepository<Customer, Long> {

    Customer findCustomerByNameAndSurname(String name, String surname);
}
