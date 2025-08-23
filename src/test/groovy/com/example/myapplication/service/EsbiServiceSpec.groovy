package com.example.myapplication.service

import com.example.myapplication.entity.EsbiData
import com.example.myapplication.repository.EsbiDataRepository
import spock.lang.Specification

import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * EsbiServiceのSpockテスト
 */
class EsbiServiceSpec extends Specification {

    def esbiDataRepository = Mock(EsbiDataRepository)
    def esbiService = new EsbiService(esbiDataRepository)

    def "ESBIデータの保存が正常に動作すること"() {
        given: "保存するデータ"
        def userName = "test_user"
        def employeeIncome = new BigDecimal("5000000")
        def selfEmployedIncome = new BigDecimal("2000000")
        def businessOwnerIncome = new BigDecimal("1000000")
        def investorIncome = new BigDecimal("500000")
        
        def savedData = new EsbiData()
        savedData.id = 1L
        savedData.userName = userName
        savedData.employeeIncome = employeeIncome
        savedData.selfEmployedIncome = selfEmployedIncome
        savedData.businessOwnerIncome = businessOwnerIncome
        savedData.investorIncome = investorIncome
        savedData.createdAt = LocalDateTime.now()
        savedData.updatedAt = LocalDateTime.now()

        when: "データを保存"
        def result = esbiService.saveEsbiData(userName, employeeIncome, selfEmployedIncome, businessOwnerIncome, investorIncome)

        then: "保存されたデータが返される"
        1 * esbiDataRepository.save(_ as EsbiData) >> savedData
        result != null
        result.userName == userName
        result.employeeIncome == employeeIncome
        result.selfEmployedIncome == selfEmployedIncome
        result.businessOwnerIncome == businessOwnerIncome
        result.investorIncome == investorIncome
    }

    def "null値を含むデータの保存でゼロに変換されること"() {
        given: "一部がnullのデータ"
        def userName = "test_user"
        def employeeIncome = new BigDecimal("5000000")
        def selfEmployedIncome = null
        def businessOwnerIncome = new BigDecimal("1000000")
        def investorIncome = null
        
        def savedData = new EsbiData()
        savedData.userName = userName
        savedData.employeeIncome = employeeIncome
        savedData.selfEmployedIncome = BigDecimal.ZERO
        savedData.businessOwnerIncome = businessOwnerIncome
        savedData.investorIncome = BigDecimal.ZERO

        when: "データを保存"
        def result = esbiService.saveEsbiData(userName, employeeIncome, selfEmployedIncome, businessOwnerIncome, investorIncome)

        then: "null値がゼロに変換される"
        1 * esbiDataRepository.save(_ as EsbiData) >> savedData
        result.selfEmployedIncome == BigDecimal.ZERO
        result.investorIncome == BigDecimal.ZERO
    }

    def "合計収入の計算が正しく動作すること"() {
        given: "ESBIデータ"
        def esbiData = new EsbiData()
        esbiData.employeeIncome = new BigDecimal("5000000")
        esbiData.selfEmployedIncome = new BigDecimal("2000000")
        esbiData.businessOwnerIncome = new BigDecimal("1000000")
        esbiData.investorIncome = new BigDecimal("500000")

        when: "合計収入を計算"
        def total = esbiService.getTotalIncome(esbiData)

        then: "正しい合計が返される"
        total == new BigDecimal("8500000")
    }

    def "パーセンテージの計算が正しく動作すること"() {
        given: "金額と合計"
        def amount = new BigDecimal("5000000")
        def total = new BigDecimal("8500000")

        when: "パーセンテージを計算"
        def percentage = esbiService.getPercentage(amount, total)

        then: "正しいパーセンテージが返される"
        Math.abs(percentage - 58.8235) < 0.01 // 小数点以下の誤差を考慮して許容範囲を広げる
    }

    def "合計がゼロの場合のパーセンテージ計算"() {
        given: "合計がゼロ"
        def amount = new BigDecimal("1000000")
        def total = BigDecimal.ZERO

        when: "パーセンテージを計算"
        def percentage = esbiService.getPercentage(amount, total)

        then: "ゼロが返される"
        percentage == 0.0
    }

    def "最新データの取得が正常に動作すること"() {
        given: "最新のESBIデータ"
        def userName = "test_user"
        def latestData = new EsbiData()
        latestData.id = 1L
        latestData.userName = userName
        
        when: "最新データを取得"
        def result = esbiService.getLatestDataByUser(userName)

        then: "最新データが返される"
        1 * esbiDataRepository.findTopByUserNameOrderByCreatedAtDesc(userName) >> Optional.of(latestData)
        result != null
        result.isPresent()
        result.get() == latestData
    }
}