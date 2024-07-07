package de.functionfactory.sendev_springboot.movies;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends
        JpaRepository<Movie, Integer> {

//    boolean existsCustomerByEmail(String email);
//    boolean existsCustomerById(Integer id);
}
