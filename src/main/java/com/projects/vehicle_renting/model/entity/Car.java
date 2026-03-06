package com.projects.vehicle_renting.model.entity;
import com.projects.vehicle_renting.model.enums.CarStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String vin;
    private String brand;
    private String model;
    private Integer year;
    private String location;
    private BigDecimal pricePerDay;
    private BigDecimal minRentalPrice;
    @ElementCollection
    private List<String> photoUrls;
    @Enumerated(EnumType.STRING)
    private CarStatus status;
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
//    @ManyToOne
//    @JoinColumn(name = "category_id")
//    private Category category;
//    @ManyToOne
//    @JoinColumn(name = "owner_id")
//    private User owner;
}
