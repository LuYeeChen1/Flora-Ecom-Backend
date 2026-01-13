package com.backend.flowershop.domain.repository;

import com.backend.flowershop.domain.model.Flower;
import java.util.List;

public interface FlowerRepository {
    List<Flower> findAllPublic();
}