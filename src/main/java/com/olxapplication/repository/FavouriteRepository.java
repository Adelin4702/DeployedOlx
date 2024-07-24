package com.olxapplication.repository;

import com.olxapplication.entity.Favourite;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface FavouriteRepository extends CrudRepository<Favourite, String> {
    Optional<Favourite> findByUserId(String id);
}
