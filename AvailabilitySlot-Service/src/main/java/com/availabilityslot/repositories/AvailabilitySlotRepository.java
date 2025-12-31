package com.availabilityslot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.availabilityslot.entities.AvailabilitySlot;

public interface AvailabilitySlotRepository extends JpaRepository<AvailabilitySlot, Integer> {

}