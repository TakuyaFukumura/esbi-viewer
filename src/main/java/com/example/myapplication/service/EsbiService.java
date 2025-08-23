package com.example.myapplication.service;

import com.example.myapplication.entity.EsbiData;
import com.example.myapplication.repository.EsbiDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class EsbiService {

    private final EsbiDataRepository esbiDataRepository;

    public EsbiService(EsbiDataRepository esbiDataRepository) {
        this.esbiDataRepository = esbiDataRepository;
    }

    public EsbiData saveEsbiData(String userName, BigDecimal employeeIncome, 
                                BigDecimal selfEmployedIncome, BigDecimal businessOwnerIncome, 
                                BigDecimal investorIncome) {
        log.info("Saving ESBI data for user: {}", userName);
        
        EsbiData esbiData = new EsbiData();
        esbiData.setUserName(userName);
        esbiData.setEmployeeIncome(employeeIncome != null ? employeeIncome : BigDecimal.ZERO);
        esbiData.setSelfEmployedIncome(selfEmployedIncome != null ? selfEmployedIncome : BigDecimal.ZERO);
        esbiData.setBusinessOwnerIncome(businessOwnerIncome != null ? businessOwnerIncome : BigDecimal.ZERO);
        esbiData.setInvestorIncome(investorIncome != null ? investorIncome : BigDecimal.ZERO);
        esbiData.setCreatedAt(LocalDateTime.now());
        esbiData.setUpdatedAt(LocalDateTime.now());
        
        return esbiDataRepository.save(esbiData);
    }

    public List<EsbiData> getAllDataByUser(String userName) {
        log.info("Getting all ESBI data for user: {}", userName);
        return esbiDataRepository.findByUserNameOrderByCreatedAtDesc(userName);
    }

    public Optional<EsbiData> getLatestDataByUser(String userName) {
        log.info("Getting latest ESBI data for user: {}", userName);
        return esbiDataRepository.findTopByUserNameOrderByCreatedAtDesc(userName);
    }

    public Optional<EsbiData> getDataById(Long id) {
        log.info("Getting ESBI data by id: {}", id);
        return esbiDataRepository.findById(id);
    }

    public void deleteData(Long id) {
        log.info("Deleting ESBI data with id: {}", id);
        esbiDataRepository.deleteById(id);
    }

    public BigDecimal getTotalIncome(EsbiData esbiData) {
        return esbiData.getEmployeeIncome()
                .add(esbiData.getSelfEmployedIncome())
                .add(esbiData.getBusinessOwnerIncome())
                .add(esbiData.getInvestorIncome());
    }

    public double getPercentage(BigDecimal amount, BigDecimal total) {
        if (total.compareTo(BigDecimal.ZERO) == 0) {
            return 0.0;
        }
        return amount.divide(total, 4, java.math.RoundingMode.HALF_UP)
                     .multiply(BigDecimal.valueOf(100))
                     .doubleValue();
    }
}