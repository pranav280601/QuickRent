package com.shounoop.carrentalspring.services.customer;

import com.shounoop.carrentalspring.dto.BookACarDto;
import com.shounoop.carrentalspring.dto.CarDto;
import com.shounoop.carrentalspring.entity.BookACar;
import com.shounoop.carrentalspring.entity.Car;
import com.shounoop.carrentalspring.entity.User;
import com.shounoop.carrentalspring.enums.BookCarStatus;
import com.shounoop.carrentalspring.repository.BookACarRepository;
import com.shounoop.carrentalspring.repository.CarRepository;
import com.shounoop.carrentalspring.repository.UserRepository;
import com.shounoop.carrentalspring.utils.PricingUtil;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final BookACarRepository bookACarRepository;
    
    @Autowired
    private com.shounoop.carrentalspring.services.EmailService emailService;


    @Override
    public List<CarDto> getAllCars() {
        return carRepository.findAll().stream().map(Car::getCarDto).collect(Collectors.toList());
    }

    @Override
    public boolean bookACar(BookACarDto bookACarDto) {
        Optional<Car> optionalCar = carRepository.findById(bookACarDto.getCarId());
        Optional<User> optionalUser = userRepository.findById(bookACarDto.getUserId());

        if (optionalCar.isPresent() && optionalUser.isPresent()) {
            Car existingCar = optionalCar.get();

            BookACar bookACar = new BookACar();
            bookACar.setUser(optionalUser.get());
            bookACar.setCar(existingCar);
            bookACar.setBookCarStatus(BookCarStatus.PENDING);

            long diffInMilliSeconds = bookACarDto.getToDate().getTime() - bookACarDto.getFromDate().getTime();
            long days = TimeUnit.MICROSECONDS.toDays(diffInMilliSeconds);

            bookACar.setDays(days);
            bookACar.setPrice(days * existingCar.getPrice());

            bookACarRepository.save(bookACar);
         
            try {
                // Attempt to notify user via email (non-blocking)
                String userEmail = null;
                try {
                    userEmail = bookACar.getUser().getEmail();
                } catch (Exception ignored) { }
            
                if (userEmail != null && !userEmail.isEmpty()) {
                    String subject = "Booking Confirmed - " + bookACar.getId();
                    StringBuilder body = new StringBuilder();
                    body.append("Hi,\n\n");
                    body.append("Your booking (ID: ").append(bookACar.getId()).append(") is confirmed.\n");
                    body.append("Total amount: ").append(bookACar.getPrice()).append("\n");
                    body.append("Start: ").append(bookACar.getFromDate()).append("\n");
                    body.append("End: ").append(bookACar.getToDate()).append("\n\n");
                    body.append("Thank you for using our service.\n");
                    emailService.sendSimpleMessage(userEmail, subject, body.toString());
                }
            } catch (Exception e) {
                System.err.println("Email notification failed: " + e.getMessage());
            }

                        return true;
                    }

        return false;
    }

    @Override
    public CarDto getCarById(Long id) {
        Optional<Car> optionalCar = carRepository.findById(id);
        return optionalCar.map(Car::getCarDto).orElse(null);
    }

    @Override
    public List<BookACarDto> getBookingsByUserId(Long userId) {
        return bookACarRepository.findAllByUserId(userId).stream().map(BookACar::getBookACarDto).collect(Collectors.toList());
    }
    
    public double calculatePrice(double baseRatePerKm, Double startKm, Double endKm, LocalDateTime startTime, LocalDateTime endTime, double basePricePerDay) {
        {
            double price = 0.0;
            if (startKm != null && endKm != null) {
                double distance = Math.abs(endKm - startKm);
                price = baseRatePerKm * distance;
                // long-trip discount for >200km
                if (distance > 200) {
                    price = price * 0.95;
                }
            } else {
                // fallback to day-based pricing
                long days = Math.max(1, ChronoUnit.DAYS.between(startTime.toLocalDate(), endTime.toLocalDate()));
                price = basePricePerDay * days;
            }
            // apply peak-time multiplier based on startTime
            price = PricingUtil.applyPeakMultiplier(price, startTime);
            // round to 2 decimals
            return Math.round(price * 100.0) / 100.0;
        }
    }
}