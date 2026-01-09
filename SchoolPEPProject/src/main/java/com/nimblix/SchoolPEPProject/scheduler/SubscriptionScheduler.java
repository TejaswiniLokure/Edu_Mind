//package com.nimblix.SchoolPEPProject.scheduler;
//
//import com.nimblix.SchoolPEPProject.Model.School;
//import com.nimblix.SchoolPEPProject.Model.SchoolSubscription;
//import com.nimblix.SchoolPEPProject.Repository.SchoolRepository;
//import com.nimblix.SchoolPEPProject.Repository.SchoolSubscriptionRepository;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@EnableScheduling
//@Component
//@RequiredArgsConstructor
//public class SubscriptionScheduler {
//
//    private final SchoolSubscriptionRepository subscriptionRepository;
//    private final SchoolRepository schoolRepository;
//
//    @Scheduled(cron = "0 0 0 * * ?")
//    @Transactional
//    public void expireSubscriptions() {
//
//        List<SchoolSubscription> expired =
//                subscriptionRepository
//                        .findByPaymentStatus(LocalDateTime.now());
//
//        for (SchoolSubscription sub : expired) {
//
//            sub.setPaymentStatus("EXPIRED");
//
//            School school =
//                    schoolRepository.findById(sub.getSchoolId())
//                            .orElseThrow();
//
//            school.setSubscriptionStatus("EXPIRED");
//            school.setIsActive(false);
//        }
//    }
//}
//
