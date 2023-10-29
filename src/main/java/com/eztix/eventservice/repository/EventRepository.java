package com.eztix.eventservice.repository;

import com.eztix.eventservice.dto.EventDTO;
import com.eztix.eventservice.model.Event;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface EventRepository extends CrudRepository<Event, Long>, PagingAndSortingRepository<Event, Long> {

    List<Event> findAllByIsFeaturedTrueOrderByFeatureSequence();

    List<Event> findAllByCategory(String category);

    List<Event> findAllByCategoryAndNameContainingOrDescriptionContaining(String Category, String name, String description);
    List<Event> findAllByNameContainingOrDescriptionContaining(String name, String description);

    @Query(value = """
                    SELECT
                    e.id as id,
                    e.name as name,
                    e.category as category,
                    e.artist as artist,
                    e.description as description,
                    e.banner_url as bannerURL,
                    e.seat_map_url as seatMapURL,
                    e.is_featured as isFeatured,
                    e.feature_sequence as featuredSequence
                    FROM event e""", nativeQuery = true)
    Iterable<EventDTO> findAllEvent();

    @Query(value = """
                    SELECT
                    e.id as id,
                    e.name as name,
                    e.category as category,
                    e.artist as artist,
                    e.description as description,
                    e.banner_url as bannerURL,
                    e.seat_map_url as seatMapURL,
                    e.is_featured as isFeatured,
                    e.feature_sequence as featuredSequence
                    FROM event e
                    WHERE isFeatured = TRUE
                    ORDER BY featureSequence
                    """, nativeQuery = true)
    Iterable<EventDTO> findAllByIsFeaturedTrue();



}
